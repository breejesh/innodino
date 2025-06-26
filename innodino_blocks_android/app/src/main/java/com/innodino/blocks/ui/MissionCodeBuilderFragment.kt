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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.innodino.blocks.R
import com.innodino.blocks.viewmodel.MissionCodeBuilderViewModel

/**
 * Fragment for the mission code builder screen.
 * Loads mission data, configures Blockly workspace, and handles progress.
 */
class MissionCodeBuilderFragment : Fragment() {
    private val viewModel: MissionCodeBuilderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mission_code_builder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val missionId = arguments?.getString("MISSION_ID")
        val module = arguments?.getString("MISSION_MODULE") ?: "led"
        val assetFile = if (module == "robot") "missions_robot.json" else "missions_led.json"
        viewModel.loadMissions(context, assetFile)
        // If a missionId is provided, set it as the current mission
        missionId?.let { id ->
            viewModel.missions.observe(viewLifecycleOwner) { missions ->
                missions?.find { it.id == id }?.let { mission ->
                    viewModel.setCurrentMission(mission)
                } ?: run {
                    // Mission not found, show error or fallback
                    view.findViewById<TextView>(R.id.missionTitle)?.text = "Mission not found"
                    view.findViewById<TextView>(R.id.missionDescription)?.text = "Please return and try again."
                }
            }
        }

        val titleView = view.findViewById<TextView>(R.id.missionTitle)
        val descView = view.findViewById<TextView>(R.id.missionDescription)
        val markDoneBtn = view.findViewById<Button>(R.id.markDoneButton)
        val blocklyWebView = view.findViewById<WebView>(R.id.blocklyWebView)
        blocklyWebView.settings.javaScriptEnabled = true
        blocklyWebView.webViewClient = WebViewClient()
        blocklyWebView.addJavascriptInterface(object {
            @JavascriptInterface
            fun onMissionDone(blocklyXml: String) {
                // Save progress and move to next mission
                activity?.runOnUiThread {
                    viewModel.currentMission.value?.let { mission ->
                        viewModel.markMissionDone(mission.id)
                    }
                }
            }
        }, "Android")

        viewModel.currentMission.observe(viewLifecycleOwner, Observer { mission ->
            if (mission != null) {
                titleView.text = mission.title
                descView.text = mission.description
                // Load Blockly WebView for this mission
                blocklyWebView.loadUrl("file:///android_asset/blockly_mission.html")
                blocklyWebView.post {
                    val toolboxXml = loadToolboxXml(context, mission.id)
                    blocklyWebView.evaluateJavascript("loadBlockly(`$toolboxXml`, null);") { }
                }
                markDoneBtn.isEnabled = true
                markDoneBtn.setOnClickListener {
                    blocklyWebView.evaluateJavascript("window.markMissionDone();") { }
                }
            } else {
                titleView.text = "All Missions Complete!"
                descView.text = "Great job, Dino Coder!"
                markDoneBtn.isEnabled = false
            }
        })
    }

    private fun loadToolboxXml(context: android.content.Context, missionId: String): String {
        val assetName = when (missionId) {
            "led_1" -> "blockly/toolbox_led_1.xml"
            "led_2" -> "blockly/toolbox_led_2.xml"
            "bot_1" -> "blockly/toolbox_bot_1.xml"
            else -> "blockly/toolbox_all.xml"
        }
        return context.assets.open(assetName).bufferedReader().use { it.readText().replace("\n", "") }
    }
}
