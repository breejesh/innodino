// InnoDino Blockly JavaScript

let workspace = null;

// Wait for Blockly to be loaded before running any Blockly code
function onBlocklyReady(callback) {
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
        console.error('Blockly library failed to load.');
      }
    }, 3000);
  }
}

function getSensorValue(type) {
  if (window.Android && typeof window.Android.getSensorValue === 'function') {
    // Call Android interface
    return window.Android.getSensorValue(type);
  }
  // Fallback: simulate in browser
  if (type === "DISTANCE") return Math.floor(Math.random() * 100);
  if (type === "LIGHT") return Math.floor(Math.random() * 1024);
  if (type === "TEMPERATURE") return (20 + Math.random() * 10).toFixed(1);
  return 0;
}

function loadBlockly() {
  try {
    if (workspace) {
      workspace.dispose();
    }

    workspace = Blockly.inject('blocklyDiv', {
      toolbox: document.getElementById('toolbox'),
      scrollbars: true,
      zoom: { controls: false, wheel: true, pinch: true, startScale: 0.8, maxScale: 1, minScale: 0.5 },
      trashcan: false, // Hide the trashcan icon
      renderer: 'zelos',
      grid: { spacing: 32, length: 3, colour: '#E0E0E0', snap: true },
      move: { scrollbars: false, drag: true, wheel: true },
      sounds: false,
      media: '',
      oneBasedIndex: false,
      theme: Blockly.Themes.Classic,
      flyout: {
        autoClose: false,
        scrollbars: true
      }
    });

    const toolbox = workspace.getToolbox();
    workspace.getComponentManager().removeCapability(
    toolbox.id,
    Blockly.ComponentManager.Capability.DELETE_AREA
    );


    // Re-apply block style after every workspace update
    workspace.addChangeListener(function () {
      setTimeout(addMaterialGradient, 100);
    });
    setTimeout(addMaterialGradient, 500);

  } catch (e) {
    document.body.innerHTML += '<div style="color:#EB5757;font-size:20px;text-align:center;margin-top:40px;">Blockly failed to load toolbox.<br>' + e + '</div>';
    console.error('Blockly load error', e);
  }
}

// Function to set custom toolbox (called from Android)
window.setToolbox = function (toolboxXml) {
  console.log('Setting custom toolbox:', toolboxXml);
  if (!workspace) {
    console.error('Workspace not initialized');
    return;
  }
  try {
    // Parse the XML string and update toolbox
    const parser = new DOMParser();
    const toolboxDoc = parser.parseFromString(toolboxXml, 'text/xml');
    const toolboxElement = toolboxDoc.documentElement;

    // Update the workspace toolbox
    workspace.updateToolbox(toolboxElement);
    console.log('Toolbox updated successfully');
  } catch (e) {
    console.error('Error setting toolbox:', e);
  }
};

// Expose utility functions
window.getCode = function () {
  if (!workspace) return '';
  return Blockly.JavaScript.workspaceToCode(workspace);
};

window.getXml = function () {
  if (!workspace) return '';
  return Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(workspace));
};

window.blocklyCenterOnBlocks = function () {
  if (window.workspace) {
    window.workspace.scrollCenter();
  }
};

window.blocklyClearAll = function () {
  if (window.workspace) {
    window.workspace.clear();
  }
};

// Simulate sendSerialCommand in browser
window.sendSerialCommand = function(cmd) {
  if (window.AndroidInterface && typeof window.AndroidInterface.sendCommandToArduino === 'function') {
      window.AndroidInterface.sendCommandToArduino(cmd);
  } else {
      console.error("BlocklyJsBridge is not available.");
  }
  console.log('Simulated sendSerialCommand:', cmd);
};

// Initialize on page load
window.onload = function () {
  // Suppress media-related errors
  window.addEventListener('error', function (e) {
    if (e.message && e.message.includes('NotSupportedError')) {
      console.log('Suppressed media error:', e.message);
      e.preventDefault();
      return false;
    }
  });

  // Wait for Blockly to be fully loaded, then define custom blocks, then initialize workspace
  onBlocklyReady(function () {
    try {
      defineCustomBlocks();
      loadBlockly();
    } catch (e) {
      console.error('Error during initialization:', e);
      loadBlockly(); // Try to load anyway
    }
    // Browser: Run generated code directly
    var runBtn = document.getElementById('runCodeBtn');
    if (runBtn) {
      runBtn.onclick = function () {
        runCode();
      };
    }
  });

  window.runCode = function () {
     console.log('turn_on_led generator:', Blockly.JavaScript['turn_on_led']);
        var code = Blockly.JavaScript.workspaceToCode(Blockly.getMainWorkspace());
        try {
          eval(code);
        } catch (e) {
          console.error('Error running code: ' + e);
      }
  }
};
