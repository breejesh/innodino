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
  console.log('getSensorValue called with type:', type);
  if (window.AndroidInputInterface && typeof window.AndroidInputInterface.getSensorValue === 'function') {
    console.log('Calling Android interface for sensor value:', type);
    // Call Android interface
    return window.AndroidInputInterface.getSensorValue(type);
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
    
    
    // After workspace is initialized
    // Add a hidden block far to the left to expand workspace bounds
    var xmlText = '<xml><block type="math_number" x="-1000" y="40" deletable="false" movable="false" collapsed="true" /></xml>';
    var xml;
    if (Blockly.Xml && Blockly.Xml.textToDom) {
      xml = Blockly.Xml.textToDom(xmlText);
    } else if (Blockly.utils && Blockly.utils.xml && Blockly.utils.xml.textToDom) {
      xml = Blockly.utils.xml.textToDom(xmlText);
    } else {
      var parser = new DOMParser();
      xml = parser.parseFromString(xmlText, "text/xml");
    }
    if (Blockly.Xml && Blockly.Xml.domToWorkspace) {
      Blockly.Xml.domToWorkspace(xml, workspace);
    }

    workspace.addChangeListener(function(event) {
      if (event.type === Blockly.Events.BLOCK_CREATE) {
        console.log('Block created:', event.blockId || (event.ids && event.ids[0]));
        // Get the block by ID
        var block = workspace.getBlockById(event.blockId || (event.ids && event.ids[0]));
        console.log('block details:', block);
        if (block) {
          // Move the block to the left (e.g., x = -500)
          console.log('Moving block to the left:', block.id, 'Current x:', block.xy.x);
          block.moveBy(block.xy.x - 300, block.xy.y + 100); // or set to a fixed negative x: block.moveBy(-500, 0);
        console.log('block details:', block);
        }
      }
    });

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
  if (window.AndroidOutputInterface && typeof window.AndroidOutputInterface.sendCommandToArduino === 'function') {
      window.AndroidOutputInterface.sendCommandToArduino(cmd);
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
        var code = Blockly.JavaScript.workspaceToCode(Blockly.getMainWorkspace());
        try {
          eval(code);
        } catch (e) {
          console.error('Error running code: ' + e);
      }
  }
};
