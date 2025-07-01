'use strict';

// --- LED Blocks ---

Blockly.JavaScript['turn_on_led'] = function(block) {
  var value_x = Blockly.JavaScript.valueToCode(block, 'X', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  var value_y = Blockly.JavaScript.valueToCode(block, 'Y', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  return `sendSerialCommand("@LED|TURN_ON|${value_x},${value_y};");\n`;
};

Blockly.JavaScript['turn_off_led'] = function(block) {
  var value_x = Blockly.JavaScript.valueToCode(block, 'X', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  var value_y = Blockly.JavaScript.valueToCode(block, 'Y', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  return `sendSerialCommand("@LED|TURN_OFF|${value_x},${value_y};");\n`;
};

Blockly.JavaScript['set_led_brightness'] = function(block) {
  var value_brightness = Blockly.JavaScript.valueToCode(block, 'BRIGHTNESS', Blockly.JavaScript.ORDER_ATOMIC) || '50';
  return `sendSerialCommand("@LED|SET_BRIGHTNESS|${value_brightness};");\n`;
};

Blockly.JavaScript['led_pattern'] = function(block) {
  var value_pattern = Blockly.JavaScript.valueToCode(block, 'PATTERN', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  return `sendSerialCommand("@LED|PATTERN|${value_pattern};");\n`;
};


// --- Robot Blocks ---

Blockly.JavaScript['move_forward'] = function(block) {
  var value_steps = Blockly.JavaScript.valueToCode(block, 'STEPS', Blockly.JavaScript.ORDER_ATOMIC) || '1';
  return `sendSerialCommand("@ROBOT|MOVE_FORWARD|${value_steps};");\n`;
};

Blockly.JavaScript['turn_left'] = function(block) {
  var value_degrees = Blockly.JavaScript.valueToCode(block, 'DEGREES', Blockly.JavaScript.ORDER_ATOMIC) || '90';
  return `sendSerialCommand("@ROBOT|TURN_LEFT|${value_degrees};");\n`;
};

Blockly.JavaScript['turn_right'] = function(block) {
  var value_degrees = Blockly.JavaScript.valueToCode(block, 'DEGREES', Blockly.JavaScript.ORDER_ATOMIC) || '90';
  return `sendSerialCommand("@ROBOT|TURN_RIGHT|${value_degrees};");\n`;
};

Blockly.JavaScript['stop_robot'] = function(block) {
  return 'sendSerialCommand("@ROBOT|STOP;");\n';
};


// --- Time Blocks ---
Blockly.JavaScript['wait_seconds'] = function(block) {
  var value_seconds = Blockly.JavaScript.valueToCode(block, 'SECONDS', Blockly.JavaScript.ORDER_ATOMIC) || '1';
  return `@TIME|WAIT|${value_seconds};\n`;
};

// --- Sensor Blocks ---
Blockly.JavaScript['read_distance'] = function(block) {
  return 'sendSerialCommand("@SENSOR|READ|DISTANCE;");\n';
};
Blockly.JavaScript['read_light'] = function(block) {
  return 'sendSerialCommand("@SENSOR|READ|LIGHT;");\n';
};
Blockly.JavaScript['read_temperature'] = function(block) {
  return 'sendSerialCommand("@SENSOR|READ|TEMPERATURE;");\n';
};

// --- Variable Blocks ---

Blockly.JavaScript['variables_set'] = function(block) {
  var varName = Blockly.JavaScript.nameDB_.getName(block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  var value = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_ASSIGNMENT) || '0';
  return varName + ' = ' + value + ';\n';
};

Blockly.JavaScript['variables_get'] = function(block) {
  var varName = Blockly.JavaScript.nameDB_.getName(block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  return [varName, Blockly.JavaScript.ORDER_ATOMIC];
};
