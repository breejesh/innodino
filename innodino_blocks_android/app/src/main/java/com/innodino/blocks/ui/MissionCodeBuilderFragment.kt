package com.innodino.blocks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.core.content.ContextCompat
import android.webkit.WebResourceRequest
import android.util.Log
import android.widget.Toast
import android.webkit.WebChromeClient
import android.widget.LinearLayout
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import com.innodino.blocks.R
import com.innodino.blocks.viewmodel.MissionCodeBuilderViewModel

/**
 * Fragment for the mission code builder screen.
 * Loads mission data, configures Blockly workspace, and handles progress.
 */
class MissionCodeBuilderFragment : Fragment() {
    private val viewModel: MissionCodeBuilderViewModel by viewModels()
    private var webViewLoaded = false
    private var pendingToolboxXml: String? = null
    private var missionLoaded = false
    private var timeoutHandler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mission_code_builder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        Log.d("MissionCodeBuilder", "Fragment created, arguments: $arguments")
        val missionId = arguments?.getString("MISSION_ID")
        val module = arguments?.getString("MISSION_MODULE") ?: "led"
        val assetFile = if (module == "robot") "missions_robot.json" else "missions_led.json"
        Log.d("MissionCodeBuilder", "Loading missions from asset: $assetFile, missionId: $missionId")
        viewModel.loadMissions(context, assetFile, forceMissionId = missionId)

        val missionNumberView = view.findViewById<TextView>(R.id.missionNumber)
        val descView = view.findViewById<TextView>(R.id.missionDescription)
        val markDoneBtn = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.markDoneButton)
        val blocklyWebView = view.findViewById<WebView>(R.id.blocklyWebView)
        val progressBar = ProgressBar(context).apply {
            isIndeterminate = true
            visibility = View.VISIBLE
        }
        (view as ViewGroup).addView(progressBar)
        blocklyWebView.settings.javaScriptEnabled = true
        blocklyWebView.settings.domStorageEnabled = true
        blocklyWebView.webChromeClient = WebChromeClient()
        blocklyWebView.setBackgroundColor(ContextCompat.getColor(context, R.color.cloud_white))
        blocklyWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("MissionCodeBuilder", "WebView loaded: $url")
                webViewLoaded = true
                pendingToolboxXml?.let {
                    Log.d("MissionCodeBuilder", "Injecting toolbox after WebView load: $it")
                    blocklyWebView.evaluateJavascript("setToolbox(`$it`);", null)
                    pendingToolboxXml = null
                }
                progressBar.visibility = View.GONE
            }
            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                Log.e("MissionCodeBuilder", "WebView error: $description")
                descView.text = "Error: $description"
                blocklyWebView.visibility = View.GONE
                markDoneBtn.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }
        blocklyWebView.loadUrl("file:///android_asset/blockly_mobile.html")
        Log.d("MissionCodeBuilder", "WebView loading blockly_mobile.html")

        // Timeout if mission not loaded in 3 seconds
        timeoutHandler = Handler(Looper.getMainLooper())
        timeoutHandler?.postDelayed({
            if (!missionLoaded) {
                Log.e("MissionCodeBuilder", "Mission data load timeout")
                descView.text = "Mission failed to load. Please check your mission data or try again."
                blocklyWebView.visibility = View.GONE
                markDoneBtn.visibility = View.GONE
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Mission data failed to load!", Toast.LENGTH_LONG).show()
            }
        }, 5000) // Increase timeout to 5 seconds for slow devices

        // Observe mission and update UI/toolbox
        viewModel.currentMission.observe(viewLifecycleOwner) { mission ->
            val num = mission?.id?.substringAfterLast('_')?.toIntOrNull()?.let { "#$it" } ?: ""
            missionNumberView.text = num
            descView.text = mission?.description ?: ""
            Log.d("MissionCodeBuilder", "Mission observer triggered: $mission")
            if (mission == null) {
                Log.e("MissionCodeBuilder", "Mission is null!")
                descView.text = "Please return and try again."
                blocklyWebView.visibility = View.GONE
                markDoneBtn.visibility = View.GONE
                progressBar.visibility = View.GONE
                return@observe
            }
            missionLoaded = true
            Log.d("MissionCodeBuilder", "Mission loaded: ${mission.title}, allowedBlocks: ${mission.allowedBlocks}")
            // RESTORED: Use mission-specific toolbox
            val toolboxXml = buildToolboxXml(mission.allowedBlocks)
            Log.d("MissionCodeBuilder", "Toolbox XML: $toolboxXml")
            if (toolboxXml.isBlank()) {
                Log.e("MissionCodeBuilder", "Toolbox XML is blank! Mission: ${mission.title}, allowedBlocks: ${mission.allowedBlocks}")
                descView.text = "No blocks available for this mission!"
                blocklyWebView.visibility = View.GONE
                markDoneBtn.visibility = View.GONE
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "No blocks available for this mission!", Toast.LENGTH_LONG).show()
                return@observe
            }
            Log.d("MissionCodeBuilder", "webViewLoaded: $webViewLoaded, pendingToolboxXml: $pendingToolboxXml")
            if (webViewLoaded) {
                Log.d("MissionCodeBuilder", "Injecting toolbox immediately: $toolboxXml")
                blocklyWebView.evaluateJavascript("setToolbox(`$toolboxXml`);", null)
            } else {
                Log.d("MissionCodeBuilder", "Toolbox injection pending until WebView loads.")
                pendingToolboxXml = toolboxXml
            }
            // Extra debug: check WebView visibility and URL
            Log.d("MissionCodeBuilder", "blocklyWebView.visibility: ${blocklyWebView.visibility}, URL: ${blocklyWebView.url}")
            blocklyWebView.postDelayed({
                Log.d("MissionCodeBuilder", "(Delayed) blocklyWebView.visibility: ${blocklyWebView.visibility}, URL: ${blocklyWebView.url}")
            }, 1000)

            // Set mission title and description
            // titleView.text = mission.title
            descView.text = mission.description
        }

        // Set header background color based on module
        val headerView = view.findViewById<View>(R.id.missionHeader)
        val headerColor = when (module) {
            "robot" -> ContextCompat.getColor(requireContext(), R.color.tech_teal) // #2D9CDB
            else -> ContextCompat.getColor(requireContext(), R.color.soft_coral) // #EB5757 (red for LED)
        }
        headerView.setBackgroundColor(headerColor)

        // Mark as Done button
        markDoneBtn.setOnClickListener {
            markDoneBtn.animate()
                .rotationBy(360f)
                .scaleX(1.3f).scaleY(1.3f)
                .setDuration(250)
                .withEndAction {
                    markDoneBtn.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                    viewModel.currentMission.value?.let { mission ->
                        viewModel.markMissionDone(mission.id)
                        // Move to next mission if available
                        if (!mission.nextMissionId.isNullOrBlank()) {
                            // You may want to trigger navigation here
                            Toast.makeText(requireContext(), "Moving to next mission...", Toast.LENGTH_SHORT).show()
                            // TODO: Replace with real navigation logic
                        } else {
                            Toast.makeText(requireContext(), "All missions complete!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
        }

        // Run button
        val runBtn = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.runButton)
        runBtn.setOnClickListener {
            blocklyWebView.evaluateJavascript("window.getCode();") { code ->
                // For now, just show the code in a Toast (or handle as needed)
                Toast.makeText(requireContext(), "Generated code:\n" + code, Toast.LENGTH_LONG).show()
                Log.d("MissionCodeBuilder", "Run button code: $code")
                // TODO: Send code to robot/LED/etc.
            }
        }

        // Blockly FABs (updated)
        val locateBtn = view.findViewById<View>(R.id.locateButton)
        val clearAllBtn = view.findViewById<View>(R.id.clearAllButton)
        locateBtn.setOnClickListener {
            blocklyWebView.evaluateJavascript("window.blocklyCenterOnBlocks();", null)
        }
        clearAllBtn.setOnClickListener {
            blocklyWebView.evaluateJavascript("if(window.workspace){window.workspace.clear();}", null)
        }
    }

    // Helper to build Blockly toolbox XML for allowed blocks
    private fun buildToolboxXml(allowedBlocks: List<String>): String {
        if (allowedBlocks.isEmpty()) return "<xml><block type=\"text\"></block></xml>"
        
        // Create categories based on allowed blocks
        val sb = StringBuilder("<xml>")
        
        // LED blocks
        val ledBlocks = allowedBlocks.filter { it.startsWith("set_led") || it == "led_pattern" || it == "colour_picker" }
        if (ledBlocks.isNotEmpty()) {
            sb.append("<category name=\"💡 LED\" colour=\"#6FCF97\">")
            ledBlocks.forEach { block ->
                when (block) {
                    "set_led" -> sb.append("<block type=\"set_led\"><value name=\"COLOR\"><shadow type=\"colour_picker\"><field name=\"COLOUR\">#ff0000</field></shadow></value></block>")
                    "set_led_brightness" -> sb.append("<block type=\"set_led_brightness\"><value name=\"BRIGHTNESS\"><shadow type=\"math_number\"><field name=\"NUM\">50</field></shadow></value></block>")
                    "led_pattern" -> sb.append("<block type=\"led_pattern\"></block>")
                    "colour_picker" -> sb.append("<block type=\"colour_picker\"></block>")
                }
            }
            sb.append("</category>")
        }
        
        // Sensor blocks
        val sensorBlocks = allowedBlocks.filter { it.startsWith("read_") }
        if (sensorBlocks.isNotEmpty()) {
            sb.append("<category name=\"📏 Sensor\" colour=\"#2D9CDB\">")
            sensorBlocks.forEach { block ->
                sb.append("<block type=\"$block\"></block>")
            }
            sb.append("</category>")
        }
        
        // Robot blocks
        val robotBlocks = allowedBlocks.filter { it.startsWith("move_") || it.startsWith("turn_") || it == "stop_robot" }
        if (robotBlocks.isNotEmpty()) {
            sb.append("<category name=\"🦕 Robot\" colour=\"#FFCE55\">")
            robotBlocks.forEach { block ->
                when (block) {
                    "move_forward" -> sb.append("<block type=\"move_forward\"><value name=\"STEPS\"><shadow type=\"math_number\"><field name=\"NUM\">5</field></shadow></value></block>")
                    "turn_left" -> sb.append("<block type=\"turn_left\"><value name=\"DEGREES\"><shadow type=\"math_number\"><field name=\"NUM\">90</field></shadow></value></block>")
                    "turn_right" -> sb.append("<block type=\"turn_right\"><value name=\"DEGREES\"><shadow type=\"math_number\"><field name=\"NUM\">90</field></shadow></value></block>")
                    "stop_robot" -> sb.append("<block type=\"stop_robot\"></block>")
                }
            }
            sb.append("</category>")
        }
        
        // Control/Repeat blocks
        val controlBlocks = allowedBlocks.filter { it == "repeat" || it.startsWith("controls_") || it == "wait_seconds" }
        if (controlBlocks.isNotEmpty()) {
            sb.append("<category name=\"🔄 Repeat\" colour=\"#6FCF97\">")
            controlBlocks.forEach { block ->
                when (block) {
                    "repeat" -> sb.append("<block type=\"repeat\"><value name=\"TIMES\"><shadow type=\"math_number\"><field name=\"NUM\">5</field></shadow></value></block>")
                    "controls_repeat_ext" -> sb.append("<block type=\"controls_repeat_ext\"><value name=\"TIMES\"><shadow type=\"math_number\"><field name=\"NUM\">10</field></shadow></value></block>")
                    "controls_if" -> sb.append("<block type=\"controls_if\"></block>")
                    "wait_seconds" -> sb.append("<block type=\"wait_seconds\"><value name=\"SECONDS\"><shadow type=\"math_number\"><field name=\"NUM\">1</field></shadow></value></block>")
                }
            }
            sb.append("</category>")
        }
        
        // Logic blocks
        val logicBlocks = allowedBlocks.filter { it.startsWith("logic_") || it.startsWith("math_") }
        if (logicBlocks.isNotEmpty()) {
            sb.append("<category name=\"🧠 Logic\" colour=\"#2D9CDB\">")
            logicBlocks.forEach { block ->
                sb.append("<block type=\"$block\"></block>")
            }
            sb.append("</category>")
        }
        
        // Variable blocks
        val variableBlocks = allowedBlocks.filter { it.startsWith("variables_") || it == "text" || it == "display_message" }
        if (variableBlocks.isNotEmpty()) {
            sb.append("<category name=\"📦 Variable\" colour=\"#6FCF97\">")
            variableBlocks.forEach { block ->
                when (block) {
                    "variables_set" -> sb.append("<block type=\"variables_set\"><value name=\"VALUE\"><shadow type=\"math_number\"><field name=\"NUM\">0</field></shadow></value></block>")
                    "variables_get" -> sb.append("<block type=\"variables_get\"><field name=\"VAR\">item</field></block>")
                    "text" -> sb.append("<block type=\"text\"></block>")
                    "display_message" -> sb.append("<block type=\"display_message\"><value name=\"MESSAGE\"><shadow type=\"text\"><field name=\"TEXT\">Hello Dino!</field></shadow></value></block>")
                }
            }
            sb.append("</category>")
        }
        
        // Add any remaining blocks in a general category
        val remainingBlocks = allowedBlocks.filter { block ->
            !ledBlocks.contains(block) && !sensorBlocks.contains(block) && 
            !robotBlocks.contains(block) && !controlBlocks.contains(block) && 
            !logicBlocks.contains(block) && !variableBlocks.contains(block)
        }
        if (remainingBlocks.isNotEmpty()) {
            sb.append("<category name=\"Other\" colour=\"#4F4F4F\">")
            remainingBlocks.forEach { block ->
                sb.append("<block type=\"$block\"></block>")
            }
            sb.append("</category>")
        }
        
        sb.append("</xml>")
        return sb.toString()
    }
}
