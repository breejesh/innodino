package com.innodino.blocks.ui.codebuilder

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.innodino.blocks.R
import com.innodino.blocks.model.MissionData
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

    fun getAllowedBlocks(freePlay: Boolean, module: String, mission: MissionData?): List<String> {
        val led = listOf("turn_on_led", "turn_off_led", "set_led_brightness", "led_pattern", "clear_led")
        val robot = listOf("move_forward", "move_backward", "turn_left", "turn_right", "stop_robot")
        val sensor = listOf("read_distance")
        val logic = listOf("logic_compare", "logic_operation", "logic_negate", "logic_boolean", "math_number", "math_arithmetic")
        val control = listOf("controls_repeat_ext","controls_if","controls_ifelse", "wait_seconds")
        val variable = listOf("variables_set", "variables_get")
        if(freePlay) {
            if(module == "led") {
                return led + sensor + logic + control + variable
            } else if(module == "robot") {
                return robot + sensor + logic + control + variable
            } else {
                return listOf() // Default empty for unknown modules
            }
        } else {
            if (mission != null) {
                return mission.allowedBlocks
            } else {
                Log.e("MissionCodeBuilder", "Mission is null, returning empty allowed blocks")
                return listOf() // Return empty if no mission data available
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val freePlay = arguments?.getBoolean("FREE_PLAY", false) == true
        val module = arguments?.getString("MISSION_MODULE") ?: "led"
        var allowedBlocks = getAllowedBlocks(freePlay, module, null) // Default empty mission for free play
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
                
                // Set storage context for block persistence
                val storageModule = module.uppercase()
                val storageMissionId = if (freePlay) "freeplay" else (missionId ?: "unknown")
                initializeStorageContext(storageModule, storageMissionId)
                
                // Pass allowed block names to JS
                updateBlocklyToolbox(allowedBlocks)
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
                allowedBlocks = getAllowedBlocks(freePlay, module, mission)
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
                        showMissionCompleteDialog(mission)
                    }
                }.start()
        }

        // Run button
        val runBtn = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.runButton)
        runBtn.setOnClickListener {
            executeBlocklyFunction("runCode")
        }

        // Blockly FABs
        val locateBtn = view.findViewById<View>(R.id.locateButton)
        val clearAllBtn = view.findViewById<View>(R.id.clearAllButton)
        locateBtn.setOnClickListener {
            executeBlocklyFunction("blocklyCenterOnBlocks")
        }
        clearAllBtn.setOnClickListener {
            executeBlocklyFunction("blocklyClearAll")
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

    /**
     * Shows the mission complete congratulations dialog with dino-themed animations.
     * Automatically advances to next mission after 5 seconds or on button click.
     */
    private fun showMissionCompleteDialog(mission: MissionData) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_mission_complete, null)
        val dialog = Dialog(requireContext(), R.style.Theme_InodinoBlocks_Dialog)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)

        // Get views
        val dinoIcon = dialogView.findViewById<ImageView>(R.id.dinoIcon)
        val congratsTitle = dialogView.findViewById<TextView>(R.id.congratsTitle)
        val completeMessage = dialogView.findViewById<TextView>(R.id.completeMessage)
        val progressText = dialogView.findViewById<TextView>(R.id.progressText)
        val nextMissionButton = dialogView.findViewById<Button>(R.id.nextMissionButton)
        val autoAdvanceText = dialogView.findViewById<TextView>(R.id.autoAdvanceText)

        // Set up content
        congratsTitle.text = getString(R.string.congratinos_title)
        completeMessage.text = getString(R.string.mission_complete_message)
        
        // Calculate mission progress
        val allMissions = viewModel.missions.value ?: emptyList()
        val currentIndex = allMissions.indexOfFirst { it.id == mission.id }
        val totalMissions = allMissions.size
        progressText.text = getString(R.string.mission_progress_format, currentIndex + 1, totalMissions)

        // Start dino celebration animation
        val celebrationAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.dino_celebration)
        dinoIcon.startAnimation(celebrationAnim)

        // Set up auto-advance timer
        var timeLeft = 5
        val autoAdvanceHandler = Handler(Looper.getMainLooper())
        val autoAdvanceRunnable = object : Runnable {
            override fun run() {
                if (timeLeft > 0) {
                    autoAdvanceText.text = getString(R.string.auto_advance_format, timeLeft)
                    timeLeft--
                    autoAdvanceHandler.postDelayed(this, 1000)
                } else {
                    // Auto-advance to next mission
                    dialog.dismiss()
                    advanceToNextMission(mission)
                }
            }
        }

        // Handle next mission availability
        if (!mission.nextMissionId.isNullOrBlank()) {
            nextMissionButton.text = getString(R.string.next_mission)
            nextMissionButton.setOnClickListener {
                autoAdvanceHandler.removeCallbacks(autoAdvanceRunnable)
                dialog.dismiss()
                advanceToNextMission(mission)
            }
            autoAdvanceHandler.post(autoAdvanceRunnable)
        } else {
            // Last mission completed
            nextMissionButton.text = "Finish"
            autoAdvanceText.text = getString(R.string.all_missions_complete)
            nextMissionButton.setOnClickListener {
                dialog.dismiss()
                // Navigate back to mission selection or home
                requireActivity().finish()
            }
        }
        dialog.show()
    }

    /**
     * Advances to the next mission in the sequence.
     */
    private fun advanceToNextMission(currentMission: MissionData) {
        // Mark current mission as complete
        viewModel.markMissionDone(currentMission.id)
        
        // Find and navigate to next mission
        if (!currentMission.nextMissionId.isNullOrBlank()) {
            val allMissions = viewModel.missions.value ?: emptyList()
            val nextMission = allMissions.firstOrNull { it.id == currentMission.nextMissionId }
            
            if (nextMission != null) {
                // Set the next mission as current
                viewModel.setCurrentMission(nextMission)

                // Update mission context in storage system
                updateMissionContext(nextMission.id)
                
                // Reset workspace and toolbox for the new mission
                val freePlay = arguments?.getBoolean("FREE_PLAY", false) == true
                val module = arguments?.getString("MISSION_MODULE") ?: "led"
                val newAllowedBlocks = getAllowedBlocks(freePlay, module, nextMission)
                resetWorkspaceForNewMission(newAllowedBlocks)
                
                Log.d("MissionCodeBuilder", "Advanced to mission: ${nextMission.title}, new blocks: ${newAllowedBlocks}")
                
                // Show success toast
                Toast.makeText(requireContext(), "Welcome to ${nextMission.title}!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Helper functions to reduce repetition
    private fun executeBlocklyFunction(functionName: String) {
        val blocklyWebView = view?.findViewById<WebView>(R.id.blocklyWebView)
        blocklyWebView?.evaluateJavascript("if(window.$functionName){window.$functionName();}", null)
    }

    private fun updateBlocklyToolbox(allowedBlocks: List<String>) {
        val allowedBlockNamesJson = Gson().toJson(allowedBlocks)
        Log.d("MissionCodeBuilder", "Updating toolbox with blocks: $allowedBlockNamesJson")
        val blocklyWebView = view?.findViewById<WebView>(R.id.blocklyWebView)
        blocklyWebView?.evaluateJavascript("setAllowedBlocks($allowedBlockNamesJson);", null)
    }

    private fun resetWorkspaceForNewMission(allowedBlocks: List<String>) {
        val allowedBlockNamesJson = Gson().toJson(allowedBlocks)
        Log.d("MissionCodeBuilder", "Resetting workspace for new mission with blocks: $allowedBlockNamesJson")
        val blocklyWebView = view?.findViewById<WebView>(R.id.blocklyWebView)
        blocklyWebView?.evaluateJavascript("resetWorkspaceForNewMission($allowedBlockNamesJson);", null)
    }
    
    /**
     * Initialize storage context for block persistence
     */
    private fun initializeStorageContext(module: String, missionId: String) {
        val blocklyWebView = view?.findViewById<WebView>(R.id.blocklyWebView)
        Log.d("MissionCodeBuilder", "Setting storage context: module=$module, mission=$missionId")
        blocklyWebView?.evaluateJavascript("window.setRexAdventure('$module', '$missionId');", null)
    }

    /**
     * Update storage context when mission changes
     */
    private fun updateMissionContext(missionId: String) {
        val blocklyWebView = view?.findViewById<WebView>(R.id.blocklyWebView)
        Log.d("MissionCodeBuilder", "Updating mission context: $missionId")
        blocklyWebView?.evaluateJavascript("window.setRexMission('$missionId');", null)
    }

}
