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
      horizontalLayout: true,
      toolboxPosition: 'top',
      zoom: { controls: false, wheel: true, pinch: true, startScale: 0.8, maxScale: 1, minScale: 0.5 },
      trashcan: false, // Hide the trashcan icon
      renderer: 'zelos',
      grid: { spacing: 32, length: 3, colour: '#E0E0E0', snap: true },
      move: { scrollbars: false, drag: true, wheel: true },
      sounds: false,
      media: '',
      oneBasedIndex: false,
      plugins: {
        'flyoutsHorizontalToolbox': HybridFlyout
      },
      theme: {
        "blockStyles": {
          // Logic blocks -> Tech Teal
          "logic_blocks": {
            "colourPrimary": "#6FCF97",
            "colourSecondary": "#85D4A6",
            "colourTertiary": "#B8E6C1"
          },
          // Loop blocks -> Dino Green
          "loop_blocks": {
            "colourPrimary": "#6FCF97",
            "colourSecondary": "#85D4A6",
            "colourTertiary": "#B8E6C1"
          },
          // Math blocks -> Sun Yellow
          "math_blocks": {
            "colourPrimary": "#FFCE55",
            "colourSecondary": "#FFD773",
            "colourTertiary": "#FFE699"
          },
          // Variable blocks -> Dino Green
          "variable_blocks": {
            "colourPrimary": "#4F4F4F",
            "colourSecondary": "#7F7F7F",
            "colourTertiary": "#BFBFBF"
          },
          // Text blocks -> Slate Gray
          "text_blocks": {
            "colourPrimary": "#4F4F4F",
            "colourSecondary": "#7F7F7F",
            "colourTertiary": "#BFBFBF"
          },
          // Procedure blocks -> Tech Teal
          "procedure_blocks": {
            "colourPrimary": "#2D9CDB",
            "colourSecondary": "#5DADE2",
            "colourTertiary": "#AED6F1"
          }
        },
        "categoryStyles": {
          "logic_category": {
            "colour": "#6FCF97"
          },
          "loop_category": {
            "colour": "#6FCF97"
          },
          "math_category": {
            "colour": "#FFCE55"
          },
          "variable_category": {
            "colour": "#4F4F4F"
          },
          "text_category": {
            "colour": "#4F4F4F"
          },
          "procedure_category": {
            "colour": "#2D9CDB"
          }
        },
        "componentStyles": {
          "workspaceBackgroundColour": "#FAFAFA",
          "toolboxBackgroundColour": "#FFFFFF",
          "toolboxForegroundColour": "#4F4F4F",
          "flyoutBackgroundColour": "#FFFFFF",
          "flyoutForegroundColour": "#4F4F4F",
          "flyoutOpacity": 0.9,
          "scrollbarColour": "#6FCF97",
          "insertionMarkerColour": "#6FCF97",
          "insertionMarkerOpacity": 0.3,
          "markerColour": "#2D9CDB",
          "cursorColour": "#2D9CDB"
        }
      },
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
    
    // Setup vertical flyout layout
    setTimeout(() => {
      forceVerticalFlyoutLayout();
    }, 200);

  } catch (e) {
    document.body.innerHTML += '<div style="color:#EB5757;font-size:20px;text-align:center;margin-top:40px;">Blockly failed to load toolbox.<br>' + e + '</div>';
    console.error('Blockly load error', e);
  }
}

function addHiddenBlock(x, y) {
    // After workspace is initialized
    // Add a hidden block far to the left to expand workspace bounds
    var xmlText = '<xml><block type="math_number" x="' + x + '" y="' + y + '" deletable="false" movable="false" collapsed="true" /></xml>';
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

// Custom Hybrid Flyout - Horizontal position, Vertical layout
class HybridFlyout extends Blockly.HorizontalFlyout {
  constructor(workspaceOptions) {
    super(workspaceOptions);
    // Override to use vertical layout behavior
    this.horizontalLayout = false; // This controls block arrangement
  }

  // Override layout to arrange blocks vertically like VerticalFlyout
  layout_(contents) {
    this.workspace_.scale = this.targetWorkspace.scale;
    const margin = this.MARGIN;
    const cursorX = this.RTL ? margin : margin + this.tabWidth_;
    let cursorY = margin;

    for (const item of contents) {
      item.getElement().moveBy(cursorX, cursorY);
      cursorY += item.getElement().getBoundingRectangle().getHeight() + margin;
    }
  }

  // Override reflow to calculate height like VerticalFlyout
  reflowInternal_() {
    this.workspace_.scale = this.getFlyoutScale();
    let flyoutHeight = this.getContents().reduce((maxHeightSoFar, item) => {
      return maxHeightSoFar + item.getElement().getBoundingRectangle().getHeight();
    }, 0);
    flyoutHeight += this.MARGIN * (this.getContents().length + 1);
    flyoutHeight *= this.workspace_.scale;
    flyoutHeight += Blockly.Scrollbar.scrollbarThickness;

    // Set a reasonable width for vertical layout
    const flyoutWidth = 200; // Fixed width for vertical block layout

    if (this.getHeight() !== flyoutHeight || this.getWidth() !== flyoutWidth) {
      this.height_ = flyoutHeight;
      this.width_ = flyoutWidth;
      this.position();
      this.targetWorkspace.resizeContents();
      this.targetWorkspace.recordDragTargets();
    }
  }

  // Override wheel scrolling for vertical behavior
  wheel_(e) {
    const scrollDelta = Blockly.browserEvents.getScrollDeltaPixels(e);
    
    if (scrollDelta.y) {
      const metricsManager = this.workspace_.getMetricsManager();
      const scrollMetrics = metricsManager.getScrollMetrics();
      const viewMetrics = metricsManager.getViewMetrics();
      const pos = viewMetrics.top - scrollMetrics.top + scrollDelta.y;

      this.workspace_.scrollbar?.setY(pos);
      Blockly.WidgetDiv.hideIfOwnerIsInWorkspace(this.workspace_);
      Blockly.dropDownDiv.hideWithoutAnimation();
    }
    e.preventDefault();
    e.stopPropagation();
  }

  // Keep horizontal positioning but allow vertical scrolling
  isDragTowardWorkspace(currentDragDeltaXY) {
    const dx = currentDragDeltaXY.x;
    const dy = currentDragDeltaXY.y;
    const dragDirection = (Math.atan2(dy, dx) / Math.PI) * 180;
    const range = this.dragAngleRange_;
    
    // Allow both horizontal and vertical drag directions
    return (
      (dragDirection < 90 + range && dragDirection > 90 - range) ||
      (dragDirection > -90 - range && dragDirection < -90 + range) ||
      (dragDirection < range && dragDirection > -range) ||
      dragDirection < -180 + range ||
      dragDirection > 180 - range
    );
  }
}

// Register the custom flyout
Blockly.registry.register(
  Blockly.registry.Type.FLYOUTS_HORIZONTAL_TOOLBOX,
  'hybridFlyout',
  HybridFlyout
);


// DOM Manipulation Approach (Most Reliable)
function forceVerticalFlyoutLayout() {
  const flyout = workspace?.getFlyout?.();
  if (!flyout || !flyout.isVisible()) return;
  
  const flyoutSvg = flyout.svgGroup_;
  if (!flyoutSvg) return;
  
  // Find all block groups in the flyout
  const blockGroups = flyoutSvg.querySelectorAll('.blocklyBlock');
  let currentY = 20; // Starting Y position
  const blockSpacing = 16; // Space between blocks
  const leftMargin = 20; // Left margin for all blocks
  
  blockGroups.forEach((blockGroup, index) => {
    if (blockGroup && blockGroup.transform && blockGroup.transform.baseVal) {
      // Get current transform or create new one
      let transform = blockGroup.transform.baseVal.getItem(0);
      if (!transform) {
        transform = flyoutSvg.ownerSVGElement.createSVGTransform();
        blockGroup.transform.baseVal.appendItem(transform);
      }
      
      // Set new vertical position
      transform.setTranslate(leftMargin, currentY);
      
      // Calculate next Y position based on block height
      const bbox = blockGroup.getBBox();
      currentY += bbox.height + blockSpacing;
    }
  });
  
  // Update flyout scrolling area
  if (flyout.workspace_ && flyout.workspace_.scrollbar) {
    flyout.workspace_.scrollbar.setContainerVisible(true);
  }
}

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
      // Setup custom flyout before defining blocks
      defineCustomBlocks();
      loadBlockly();
      setGlobalWorkspaceReference();
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

function setGlobalWorkspaceReference() {
  if (window.Blockly && typeof Blockly.getMainWorkspace === 'function') {
    window.workspace = Blockly.getMainWorkspace();
  }
}
