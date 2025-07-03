package com.innodino.blocks.ui.codebuilder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.core.content.ContextCompat
import android.util.Log
import android.widget.Toast
import android.webkit.WebChromeClient
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import com.innodino.blocks.R
import com.innodino.blocks.ui.execution.CodeExecutionActivity
import com.innodino.blocks.util.SensorProvider
import com.innodino.blocks.viewmodel.MissionCodeBuilderViewModel

/**
 * Fragment for the mission code builder screen.
 * Loads mission data, configures Blockly workspace, and handles progress.
 */
class MissionCodeBuilderFragment : Fragment() {
    private val viewModel: MissionCodeBuilderViewModel by viewModels()
    private var webViewLoaded = false
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
        val freePlay = arguments?.getBoolean("FREE_PLAY", false) == true
        val module = arguments?.getString("MISSION_MODULE") ?: "led"
        var allowedBlocks = if (freePlay) {
            // Free Play: Only LED, sensor, logic, repeat, variable blocks
            val led = listOf("turn_on_led", "turn_off_led", "set_led_brightness", "led_pattern")
            val sensor = listOf("read_distance")
            val logic = listOf("logic_compare", "logic_operation", "logic_negate", "logic_boolean", "math_number", "math_arithmetic")
            val repeat = listOf("repeat", "controls_repeat_ext", "controls_if", "wait_seconds")
            val variable = listOf("variables_set", "variables_get", "text", "display_message")
            (led + sensor + logic + repeat + variable)
        } else listOf()
        val missionId = arguments?.getString("MISSION_ID")
        val assetFile = if (module == "robot") "missions/missions_robot.json" else "missions/missions_led.json"
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
                // Pass allowed block names to JS instead of toolbox XML
                val allowedBlockNamesJson = com.google.gson.Gson().toJson(allowedBlocks)
                Log.d("MissionCodeBuilder", "Allowed blocks JSON: $allowedBlockNamesJson")
                blocklyWebView.evaluateJavascript("setAllowedBlocks($allowedBlockNamesJson);", null)
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

        timeoutHandler = Handler(Looper.getMainLooper())
        timeoutHandler?.postDelayed({
            if (!missionLoaded && !freePlay) {
                Log.e("MissionCodeBuilder", "Mission data load timeout")
                descView.text = "Mission failed to load. Please check your mission data or try again."
                blocklyWebView.visibility = View.GONE
                markDoneBtn.visibility = View.GONE
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Mission data failed to load!", Toast.LENGTH_LONG).show()
            }
        }, 5000)

        if (!freePlay) {
            viewModel.loadMissions(context, assetFile, forceMissionId = missionId)
            viewModel.currentMission.observe(viewLifecycleOwner) { mission ->
                allowedBlocks = mission?.allowedBlocks ?: allowedBlocks
                val num = mission?.id?.substringAfterLast('_')?.toIntOrNull()?.let { "#$it" } ?: ""
                missionNumberView.text = num
                descView.text = mission?.description ?: ""
                Log.d("MissionCodeBuilder", "Mission observer triggered: $mission")
                if (mission == null) {
                    Log.e("MissionCodeBuilder", "Mission is null! Using Free Play allowedBlocks.")
                    descView.text = "Please return and try again."
                    blocklyWebView.visibility = View.GONE
                    markDoneBtn.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    return@observe
                }
                missionLoaded = true
                Log.d("MissionCodeBuilder", "Mission loaded: ${mission.title}, allowedBlocks: ${mission.allowedBlocks}")
                descView.text = mission.description
            }
        } else {
            // Free Play: hide mission UI
            view.findViewById<View>(R.id.missionHeader).visibility = View.GONE
            view.findViewById<View>(R.id.markDoneButton).visibility = View.GONE
        }

        // Set mission header background color based on category/module
        val missionHeader = view.findViewById<View>(R.id.missionHeader)
        val headerColor = when (module) {
            "led" -> ContextCompat.getColor(context, R.color.soft_coral) // #6FCF97
            "robot" -> ContextCompat.getColor(context, R.color.tech_teal) // #2D9CDB
            else -> ContextCompat.getColor(context, R.color.dino_green)
        }
        missionHeader.setBackgroundColor(headerColor)

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
            // Call the JS function to generate and send code to Android
            blocklyWebView.evaluateJavascript("runCode();", null)
        }

        // Blockly FABs (updated)
        val locateBtn = view.findViewById<View>(R.id.locateButton)
        val clearAllBtn = view.findViewById<View>(R.id.clearAllButton)
        locateBtn.setOnClickListener {
            blocklyWebView.evaluateJavascript("if(window.blocklyCenterOnBlocks){window.blocklyCenterOnBlocks();}", null)
        }
        clearAllBtn.setOnClickListener {
            blocklyWebView.evaluateJavascript("if(window.workspace){window.workspace.clear();}else if(window.Blockly && Blockly.getMainWorkspace){Blockly.getMainWorkspace().clear();}", null)
        }

        // Demo button
        val demoButton = view.findViewById<View>(R.id.missionDemoButton)
        demoButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_demo, null)
            val dialog = android.app.Dialog(requireContext())
            dialog.setContentView(dialogView)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(true)
            val okBtn = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.okButton)
            okBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
        blocklyWebView.addJavascriptInterface(BlocklyJsInputBridge(), "AndroidInputInterface")
        blocklyWebView.addJavascriptInterface(BlocklyJsOutputBridge(), "AndroidOutputInterface")
    }
}
