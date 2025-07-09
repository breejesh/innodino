// 🦖 Simple Rex's Adventure Block Storage
let currentModule = "LED"; // Default to LED module
let currentMission = "freeplay"; // Default to freeplay mode
let autoSaveInterval = null;

// Save blocks for current module and mission
window.saveBlocks = () => {
	if (!workspace) return;

	try {
		const xml = Blockly.Xml.workspaceToDom(workspace);
		const xmlText = Blockly.utils.xml.domToText(xml);
		const storageKey = `rex_${currentModule}_${currentMission}`;
		localStorage.setItem(storageKey, xmlText);
		console.log(`💾 Rex saved ${currentModule} blocks for ${currentMission}`);

		// Log to Android for debugging
		if (window.AndroidOutputInterface && window.AndroidOutputInterface.logStorageInfo) {
			window.AndroidOutputInterface.logStorageInfo(currentModule, currentMission, "SAVE");
		}
	} catch (e) {
		console.error("🚨 Rex failed to save blocks:", e);
	}
};

// Load blocks for current module and mission
window.loadBlocks = () => {
	if (!workspace) return;

	try {
		const storageKey = `rex_${currentModule}_${currentMission}`;
		const saved = localStorage.getItem(storageKey);
		if (saved) {
			workspace.clear();
			const xml = Blockly.utils.xml.textToDom(saved);
			Blockly.Xml.domToWorkspace(xml, workspace);
			console.log(`🦕 Rex loaded ${currentModule} blocks for ${currentMission}`);

			// Log to Android for debugging
			if (window.AndroidOutputInterface && window.AndroidOutputInterface.logStorageInfo) {
				window.AndroidOutputInterface.logStorageInfo(currentModule, currentMission, "LOAD");
			}
		} else {
			console.log(`🌟 Rex starts fresh ${currentModule} adventure in ${currentMission}`);
		}
	} catch (e) {
		console.error("🚨 Rex failed to load blocks:", e);
	}
};

// Start auto-save system
window.startAutoSave = () => {
	if (autoSaveInterval) clearInterval(autoSaveInterval);

	autoSaveInterval = setInterval(() => {
		saveBlocks();
	}, 10000); // Save every 10 seconds

	console.log("⏰ Rex auto-save activated every 10 seconds");
};

// Stop auto-save system
window.stopAutoSave = () => {
	if (autoSaveInterval) {
		clearInterval(autoSaveInterval);
		autoSaveInterval = null;
	}
};

// Set module type (called from Android)
window.setRexModule = function (moduleType) {
	// Switch to new module
	currentModule = moduleType;
	loadBlocks();
};

// Set mission ID (called from Android)
window.setRexMission = function (missionId) {
	// Switch to new mission
	currentMission = missionId;
	loadBlocks();
};

// Set both module and mission (called from Android)
window.setRexAdventure = function (moduleType, missionId) {
	// Switch to new adventure
	currentModule = moduleType;
	currentMission = missionId;
	loadBlocks();
};

// Manual save (for Android)
window.saveRexAdventure = function () {
	saveBlocks();
	return true;
};
