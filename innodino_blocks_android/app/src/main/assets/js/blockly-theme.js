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
