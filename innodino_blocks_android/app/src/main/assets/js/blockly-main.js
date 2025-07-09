// InnoDino Blockly JavaScript

let workspace = null;

// Wait for Blockly to be loaded before running any Blockly code
window.onBlocklyReady = (callback) => {
	const setupBlockly = () => {
		callback();
	};

	if (window.Blockly) {
		setupBlockly();
	} else {
		const check = setInterval(function () {
			if (window.Blockly) {
				clearInterval(check);
				setupBlockly();
			}
		}, 50);
		// Timeout after 3 seconds
		setTimeout(function () {
			clearInterval(check);
			if (!window.Blockly) {
				document.body.innerHTML += '<div style="color:#EB5757;font-size:20px;text-align:center;margin-top:40px;">Blockly library failed to load.<br>Check your internet connection or asset path.</div>';
				console.error("Blockly library failed to load.");
			}
		}, 3000);
	}
};

window.loadBlockly = () => {
	try {
		if (workspace) {
			workspace.dispose();
		}

		workspace = Blockly.inject("blocklyDiv", {
			toolbox: document.getElementById("toolbox"),
			scrollbars: true,
			horizontalLayout: true,
			toolboxPosition: "top",
			zoom: { controls: false, wheel: true, pinch: true, startScale: 0.8, maxScale: 1, minScale: 0.5 },
			trashcan: false, // Hide the trashcan icon
			renderer: "zelos",
			grid: { spacing: 32, length: 3, colour: "#E0E0E0", snap: true },
			move: { scrollbars: false, drag: true, wheel: true },
			sounds: false,
			media: "",
			oneBasedIndex: false,
			plugins: {
				flyoutsHorizontalToolbox: HybridFlyout,
			},
			theme: {
				blockStyles: {
					// Logic blocks -> Tech Teal
					logic_blocks: {
						colourPrimary: "#6FCF97",
						colourSecondary: "#85D4A6",
						colourTertiary: "#B8E6C1",
					},
					// Loop blocks -> Dino Green
					loop_blocks: {
						colourPrimary: "#6FCF97",
						colourSecondary: "#85D4A6",
						colourTertiary: "#B8E6C1",
					},
					// Math blocks -> Sun Yellow
					math_blocks: {
						colourPrimary: "#4F4F4F",
						colourSecondary: "#7F7F7F",
						colourTertiary: "#BFBFBF",
					},
					// Variable blocks -> Dino Green
					variable_blocks: {
						colourPrimary: "#4F4F4F",
						colourSecondary: "#7F7F7F",
						colourTertiary: "#BFBFBF",
					},
					// Text blocks -> Slate Gray
					text_blocks: {
						colourPrimary: "#4F4F4F",
						colourSecondary: "#7F7F7F",
						colourTertiary: "#BFBFBF",
					},
					// Procedure blocks -> Tech Teal
					procedure_blocks: {
						colourPrimary: "#2D9CDB",
						colourSecondary: "#5DADE2",
						colourTertiary: "#AED6F1",
					},
				},
				componentStyles: {
					workspaceBackgroundColour: "#FAFAFA",
					toolboxBackgroundColour: "#FFFFFF",
					toolboxForegroundColour: "#4F4F4F",
					flyoutBackgroundColour: "#FFFFFF",
					flyoutForegroundColour: "#4F4F4F",
					flyoutOpacity: 0.9,
					scrollbarColour: "#6FCF97",
					insertionMarkerColour: "#6FCF97",
					insertionMarkerOpacity: 0.3,
					markerColour: "#2D9CDB",
					cursorColour: "#2D9CDB",
				},
			},
			flyout: {
				autoClose: false,
				scrollbars: true,
			},
		});

		const toolbox = workspace.getToolbox();
		workspace.getComponentManager().removeCapability(toolbox.id, Blockly.ComponentManager.Capability.DELETE_AREA);
	} catch (e) {
		document.body.innerHTML += '<div style="color:#EB5757;font-size:20px;text-align:center;margin-top:40px;">Blockly failed to load toolbox.<br>' + e + "</div>";
		console.error("Blockly load error", e);
	}
};

window.sleep = (seconds) => {
	var e = new Date().getTime() + seconds * 1000;
	while (new Date().getTime() <= e) {}
};

// Show execution modal
window.showExecutionModal = function () {
	console.log("Showing execution modal...");
	const modal = document.getElementById("executionModal");
	if (modal) {
		console.log("modal found", modal);
		modal.style.display = "flex";
	}
};

window.hideExecutionModal = function () {
	console.log("Hiding execution modal...");
	const modal = document.getElementById("executionModal");
	if (modal) {
		modal.style.display = "none";
	}
};

// Initialize on page load
window.onload = function () {
	// Suppress media-related errors
	window.addEventListener("error", function (e) {
		if (e.message && e.message.includes("NotSupportedError")) {
			console.log("Suppressed media error:", e.message);
			e.preventDefault();
			return false;
		}
	});

	// Wait for Blockly to be fully loaded, then define custom blocks, then initialize workspace
	onBlocklyReady(function () {
		try {
			// Setup custom flyout before defining blocks
			defineCustomBlocks();
			loadBlockly();
			if (window.Blockly && typeof Blockly.getMainWorkspace === "function") {
				window.workspace = Blockly.getMainWorkspace();
			}
		} catch (e) {
			console.error("Error during initialization:", e);
			loadBlockly(); // Try to load anyway
		}
	});
};

// Register the custom flyout
Blockly.registry.register(Blockly.registry.Type.FLYOUTS_HORIZONTAL_TOOLBOX, "hybridFlyout", HybridFlyout);
