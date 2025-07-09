// Runnig block code
window.runCode = function () {
	console.log("Running code...");
	showExecutionModal();
	var code = Blockly.JavaScript.workspaceToCode(Blockly.getMainWorkspace());
	try {
		setTimeout(() => {
			console.log("eval start");
			eval(code);
			hideExecutionModal();
			console.log("eval end");
		}, 50);
	} catch (e) {
		console.error("Error running code: " + e);
		hideExecutionModal();
	}
};

// Send serial command to Android
window.sendSerialCommand = function (cmd) {
	if (window.AndroidOutputInterface && typeof window.AndroidOutputInterface.sendCommandToArduino === "function") {
		window.AndroidOutputInterface.sendCommandToArduino(cmd);
	} else {
		console.error("BlocklyJsBridge is not available.");
		sleep(1);
		console.log("Simulated sendSerialCommand:", cmd);
	}
};

// Get sensor value from Android
window.getSensorValue = (type) => {
	console.log("getSensorValue called with type:", type);
	if (window.AndroidInputInterface && typeof window.AndroidInputInterface.getSensorValue === "function") {
		console.log("Calling Android interface for sensor value:", type);
		return window.AndroidInputInterface.getSensorValue(type);
	} else {
		// Fallback: simulate in browser
		if (type === "DISTANCE") {
			return Math.floor(Math.random() * 100);
		} else {
			return 0;
		}
	}
};

// Clear workspace
window.blocklyClearAll = function () {
	if (window.workspace) {
		window.workspace.clear();
	}
};

// Function to set custom toolbox
window.setToolbox = function (toolboxXml) {
	console.log("Setting custom toolbox:", toolboxXml);
	if (!workspace) {
		console.error("Workspace not initialized");
		return;
	}
	try {
		// Parse the XML string and update toolbox
		const parser = new DOMParser();
		const toolboxDoc = parser.parseFromString(toolboxXml, "text/xml");
		const toolboxElement = toolboxDoc.documentElement;

		// Update the workspace toolbox
		workspace.updateToolbox(toolboxElement);
		console.log("Toolbox updated successfully");
	} catch (e) {
		console.error("Error setting toolbox:", e);
	}
};

// Center the workspace on blocks
window.blocklyCenterOnBlocks = function () {
	if (window.workspace) {
		window.workspace.scrollCenter();
	}
};

// Comprehensive reset function for mission transitions
window.resetWorkspaceForNewMission = function (allowedBlockNames) {
	console.log("Resetting workspace for new mission with blocks:", allowedBlockNames);

	// Clear the workspace first
	if (window.workspace) {
		window.workspace.clear();
	}

	// Update the toolbox with new allowed blocks
	setAllowedBlocks(allowedBlockNames);

	// Center the workspace view
	if (window.workspace) {
		window.workspace.scrollCenter();
	}

	console.log("Workspace reset complete");
};
