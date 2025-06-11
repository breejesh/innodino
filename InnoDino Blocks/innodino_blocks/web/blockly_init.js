// Define custom blocks
Blockly.defineBlocksWithJsonArray([
  {
    "type": "move_forward",
    "message0": "Move Forward %1 seconds",
    "args0": [
      {
        "type": "field_number",
        "name": "DURATION",
        "value": 1,
        "min": 0.1,
        "max": 10,
        "precision": 0.1
      }
    ],
    "previousStatement": null,
    "nextStatement": null,
    "colour": 230,
    "tooltip": "Move the robot forward"
  },
  {
    "type": "move_backward",
    "message0": "Move Backward %1 seconds",
    "args0": [
      {
        "type": "field_number",
        "name": "DURATION",
        "value": 1,
        "min": 0.1,
        "max": 10,
        "precision": 0.1
      }
    ],
    "previousStatement": null,
    "nextStatement": null,
    "colour": 230,
    "tooltip": "Move the robot backward"
  },
  {
    "type": "turn_left",
    "message0": "Turn Left",
    "previousStatement": null,
    "nextStatement": null,
    "colour": 230,
    "tooltip": "Turn the robot left"
  },
  {
    "type": "turn_right",
    "message0": "Turn Right",
    "previousStatement": null,
    "nextStatement": null,
    "colour": 230,
    "tooltip": "Turn the robot right"
  },
  {
    "type": "check_distance",
    "message0": "Get Distance",
    "output": "Number",
    "colour": 120,
    "tooltip": "Get the distance from the ultrasonic sensor"
  },
  {
    "type": "distance_compare",
    "message0": "Distance %1 %2",
    "args0": [
      {
        "type": "field_dropdown",
        "name": "OP",
        "options": [
          ["<", "LT"],
          [">", "GT"],
          ["=", "EQ"]
        ]
      },
      {
        "type": "input_value",
        "name": "THRESHOLD",
        "check": "Number"
      }
    ],
    "output": "Boolean",
    "colour": 210,
    "tooltip": "Compare the distance with a value"
  }
]);

// Code generators
Blockly.JavaScript['move_forward'] = function(block) {
  const duration = block.getFieldValue('DURATION') * 1000;
  return 'moveForward(' + duration + ');\n';
};

Blockly.JavaScript['move_backward'] = function(block) {
  const duration = block.getFieldValue('DURATION') * 1000;
  return 'moveBackward(' + duration + ');\n';
};

Blockly.JavaScript['turn_left'] = function(block) {
  return 'turnLeft();\n';
};

Blockly.JavaScript['turn_right'] = function(block) {
  return 'turnRight();\n';
};

Blockly.JavaScript['check_distance'] = function(block) {
  return ['getDistance()', Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.JavaScript['distance_compare'] = function(block) {
  const threshold = Blockly.JavaScript.valueToCode(block, 'THRESHOLD',
      Blockly.JavaScript.ORDER_RELATIONAL) || '0';
  const operator = block.getFieldValue('OP');
  const operators = {
    'LT': '<',
    'GT': '>',
    'EQ': '=='
  };
  return ['getDistance() ' + operators[operator] + ' ' + threshold,
          Blockly.JavaScript.ORDER_RELATIONAL];
};

// Toolbox configuration
const toolbox = {
  kind: 'categoryToolbox',
  contents: [
    {
      kind: 'category',
      name: 'Movement',
      colour: '230',
      contents: [
        { kind: 'block', type: 'move_forward' },
        { kind: 'block', type: 'move_backward' },
        { kind: 'block', type: 'turn_left' },
        { kind: 'block', type: 'turn_right' }
      ]
    },
    {
      kind: 'category',
      name: 'Sensors',
      colour: '120',
      contents: [
        { kind: 'block', type: 'check_distance' },
        { kind: 'block', type: 'distance_compare' }
      ]
    },
    {
      kind: 'category',
      name: 'Logic',
      colour: '210',
      contents: [
        { kind: 'block', type: 'controls_if' },
        { kind: 'block', type: 'logic_compare' },
        { kind: 'block', type: 'logic_operation' },
        { kind: 'block', type: 'logic_negate' }
      ]
    },
    {
      kind: 'category',
      name: 'Loops',
      colour: '120',
      contents: [
        { kind: 'block', type: 'controls_repeat_ext' },
        { kind: 'block', type: 'controls_whileUntil' },
        { kind: 'block', type: 'controls_for' }
      ]
    },
    {
      kind: 'category',
      name: 'Math',
      colour: '230',
      contents: [
        { kind: 'block', type: 'math_number' },
        { kind: 'block', type: 'math_arithmetic' },
        { kind: 'block', type: 'math_single' }
      ]
    },
    {
      kind: 'category',
      name: 'Variables',
      custom: 'VARIABLE',
      colour: '330'
    }
  ]
};

// Initialize Blockly workspace when the page loads
document.addEventListener('DOMContentLoaded', function() {
  window.workspace = Blockly.inject('blocklyDiv', {
    toolbox: toolbox,
    scrollbars: true,
    move: {
      scrollbars: true,
      drag: true,
      wheel: true
    },
    zoom: {
      controls: true,
      wheel: true,
      startScale: 1.0,
      maxScale: 3,
      minScale: 0.3,
      scaleSpeed: 1.2
    },
    grid: {
      spacing: 25,
      length: 3,
      colour: '#ccc',
      snap: true
    },
    theme: Blockly.Theme.defineTheme('custom', {
      'base': Blockly.Themes.Classic,
      'componentStyles': {
        'workspaceBackgroundColour': '#f8f9fa',
        'toolboxBackgroundColour': '#ffffff',
        'toolboxForegroundColour': '#212529',
        'flyoutBackgroundColour': '#ffffff',
        'flyoutForegroundColour': '#212529',
        'flyoutOpacity': 0.9,
        'scrollbarColour': '#cccccc',
        'insertionMarkerColour': '#0d6efd',
        'insertionMarkerOpacity': 0.3,
        'scrollbarOpacity': 0.4,
        'cursorColour': '#0d6efd'
      }
    })
  });
});