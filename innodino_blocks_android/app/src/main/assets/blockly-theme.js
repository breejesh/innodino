// InnoDino Blockly Theme and Visual Effects

// Add a Material gradient to blocks after Blockly loads
function addMaterialGradient() {
  var svg = document.querySelector('svg');
  if (svg && !svg.querySelector('#blocklyMaterialGradient')) {
    var defs = document.createElementNS('http://www.w3.org/2000/svg', 'defs');
    var grad = document.createElementNS('http://www.w3.org/2000/svg', 'linearGradient');
    grad.setAttribute('id', 'blocklyMaterialGradient');
    grad.setAttribute('x1', '0%');
    grad.setAttribute('y1', '0%');
    grad.setAttribute('x2', '0%');
    grad.setAttribute('y2', '100%');
    var stop1 = document.createElementNS('http://www.w3.org/2000/svg', 'stop');
    stop1.setAttribute('offset', '0%');
    stop1.setAttribute('stop-color', '#FFFFFF');
    stop1.setAttribute('stop-opacity', '0.25');
    var stop2 = document.createElementNS('http://www.w3.org/2000/svg', 'stop');
    stop2.setAttribute('offset', '100%');
    stop2.setAttribute('stop-color', '#000000');
    stop2.setAttribute('stop-opacity', '0.07');
    grad.appendChild(stop1);
    grad.appendChild(stop2);
    defs.appendChild(grad);
    svg.insertBefore(defs, svg.firstChild);
  }
  // Set all blocks to use the gradient
  var blocks = document.querySelectorAll('.blocklyBlockBackground');
  blocks.forEach(function (b) {
    b.setAttribute('fill', 'url(#blocklyMaterialGradient)');
  });
}

// InnoDino color palette
const INNODINO_COLORS = {
  '#5C81A6': '#2D9CDB', // Logic: Tech Teal
  '#5CA65C': '#6FCF97', // Loops: Dino Green
  '#5C68A6': '#2D9CDB', // Math: Tech Teal
  '#5CA68D': '#4F4F4F', // Text: Slate Gray
  '#745CA6': '#2D9CDB', // Lists: Tech Teal
  '#A6745C': '#FFCE55', // Color: Sun Yellow
  '#A65C81': '#6FCF97', // Variables: Dino Green
  '#9A5CA6': '#2D9CDB', // Functions: Tech Teal
  '#FF6680': '#EB5757', // Alert: Soft Coral
  '#E53935': '#EB5757', // Alert: Soft Coral
  '#FFAB19': '#FFCE55', // Math/Yellow: Sun Yellow
  '#FFCF00': '#FFCE55', // Math/Yellow: Sun Yellow
  '#FFC107': '#FFCE55', // Math/Yellow: Sun Yellow
};

function applyInnoDinoBlockColors() {
  document.querySelectorAll('.blocklyBlockBackground').forEach(function (block) {
    const orig = block.getAttribute('fill');
    if (INNODINO_COLORS[orig]) {
      block.setAttribute('fill', INNODINO_COLORS[orig]);
    }
  });
}

function setupColorListener(workspace) {
  workspace.addChangeListener(function () {
    setTimeout(applyInnoDinoBlockColors, 100);
  });
}

// Patch Blockly inject to always set up color listener
if (!window.originalBlocklyInject) {
  window.originalBlocklyInject = Blockly.inject;
  Blockly.inject = function () {
    const ws = window.originalBlocklyInject.apply(this, arguments);
    setupColorListener(ws);
    setTimeout(applyInnoDinoBlockColors, 500);
    return ws;
  };
}

// Initialize theme effects on page load
window.addEventListener('load', function () {
  setTimeout(addMaterialGradient, 500);
  setTimeout(applyInnoDinoBlockColors, 500);
});

// InnoDino Blockly theme configuration

// Set default Blockly theme colors to match InnoDino palette
Blockly.Themes.InnoDino = Blockly.Theme.defineTheme('innodino', {
  'base': Blockly.Themes.Classic,
  'componentStyles': {
    'workspaceBackgroundColour': '#FAFAFA',
    'toolboxBackgroundColour': '#4F4F4F',
    'toolboxForegroundColour': '#FFFFFF',
    'flyoutBackgroundColour': '#FAFAFA',
    'flyoutForegroundColour': '#4F4F4F',
    'flyoutOpacity': 0.9,
    'scrollbarColour': '#6FCF97',
    'insertionMarkerColour': '#6FCF97',
    'insertionMarkerOpacity': 0.3,
    'markerColour': '#2D9CDB',
    'cursorColour': '#2D9CDB'
  }
});
