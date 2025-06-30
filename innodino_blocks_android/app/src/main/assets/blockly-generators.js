'use strict';

// Ensure Blockly.JavaScript is defined.
goog.require('Blockly.JavaScript');

// --- LED Blocks ---

Blockly.JavaScript['turn_on_led'] = function(block) {
  var value_x = Blockly.JavaScript.valueToCode(block, 'X', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  var value_y = Blockly.JavaScript.valueToCode(block, 'Y', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  return `led_on(${value_x}, ${value_y});\n`;
};

Blockly.JavaScript['turn_off_led'] = function(block) {
  var value_x = Blockly.JavaScript.valueToCode(block, 'X', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  var value_y = Blockly.JavaScript.valueToCode(block, 'Y', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  return `led_off(${value_x}, ${value_y});\n`;
};

Blockly.JavaScript['set_led_brightness'] = function(block) {
  var value_brightness = Blockly.JavaScript.valueToCode(block, 'BRIGHTNESS', Blockly.JavaScript.ORDER_ATOMIC) || '50';
  return `led_brightness(${value_brightness});\n`;
};

Blockly.JavaScript['led_pattern'] = function(block) {
  return 'led_pattern();\n';
};


// --- Robot Blocks ---

Blockly.JavaScript['move_forward'] = function(block) {
  var value_steps = Blockly.JavaScript.valueToCode(block, 'STEPS', Blockly.JavaScript.ORDER_ATOMIC) || '1';
  return `move_forward(${value_steps});\n`;
};

Blockly.JavaScript['turn_left'] = function(block) {
  var value_degrees = Blockly.JavaScript.valueToCode(block, 'DEGREES', Blockly.JavaScript.ORDER_ATOMIC) || '90';
  return `turn_left(${value_degrees});\n`;
};

Blockly.JavaScript['turn_right'] = function(block) {
  var value_degrees = Blockly.JavaScript.valueToCode(block, 'DEGREES', Blockly.JavaScript.ORDER_ATOMIC) || '90';
  return `turn_right(${value_degrees});\n`;
};

Blockly.JavaScript['stop_robot'] = function(block) {
  return 'stop_robot();\n';
};


// --- Time Blocks ---

Blockly.JavaScript['wait_seconds'] = function(block) {
  var value_seconds = Blockly.JavaScript.valueToCode(block, 'SECONDS', Blockly.JavaScript.ORDER_ATOMIC) || '1';
  return `wait(${value_seconds});\n`;
};


// --- Variable/Message Blocks ---

Blockly.JavaScript['display_message'] = function(block) {
  var value_message = Blockly.JavaScript.valueToCode(block, 'MESSAGE', Blockly.JavaScript.ORDER_ATOMIC) || "''";
  return `display_message(${value_message});\n`;
};

// --- Custom Repeat Block ---
// Note: The standard 'controls_repeat_ext' is usually preferred.
Blockly.JavaScript['repeat'] = function(block) {
  var value_times = Blockly.JavaScript.valueToCode(block, 'TIMES', Blockly.JavaScript.ORDER_ATOMIC) || '0';
  var statements_do = Blockly.JavaScript.statementToCode(block, 'DO');
  var loopVar = Blockly.JavaScript.variableDB_.getDistinctName('count', Blockly.Variables.NAME_TYPE);
  return `for (var ${loopVar} = 0; ${loopVar} < ${value_times}; ${loopVar}++) {\n${statements_do}}\n`;
};

// --- Utility Code Generators ---

Blockly.JavaScript['wait_seconds'] = function (block) {
  var seconds = Blockly.JavaScript.valueToCode(block, 'SECONDS', Blockly.JavaScript.ORDER_ATOMIC);
  var code = `await wait(${seconds} * 1000);\n`;
  return code;
};

Blockly.JavaScript['map_function'] = function (block) {
  var value = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_ATOMIC);
  var fromMin = Blockly.JavaScript.valueToCode(block, 'FROM_MIN', Blockly.JavaScript.ORDER_ATOMIC);
  var fromMax = Blockly.JavaScript.valueToCode(block, 'FROM_MAX', Blockly.JavaScript.ORDER_ATOMIC);
  var toMin = Blockly.JavaScript.valueToCode(block, 'TO_MIN', Blockly.JavaScript.ORDER_ATOMIC);
  var toMax = Blockly.JavaScript.valueToCode(block, 'TO_MAX', Blockly.JavaScript.ORDER_ATOMIC);
  var code = `map(${value}, ${fromMin}, ${fromMax}, ${toMin}, ${toMax})`;
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

// Sensor Blocks
Blockly.JavaScript['read_distance'] = function(block) {
  var code = 'readDistance()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.JavaScript['read_light'] = function(block) {
  var code = 'readLight()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.JavaScript['read_temperature'] = function(block) {
  var code = 'readTemperature()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

// Define simulation functions
defineSimulationFunctions();

// Simulation functions for the generated code (these would connect to actual hardware)
function defineSimulationFunctions() {
  window.map = function (value, fromMin, fromMax, toMin, toMax) {
    return (value - fromMin) * (toMax - toMin) / (fromMax - fromMin) + toMin;
  };

  window.turnOnLed = function (x, y) {
    console.log(`🟢 Turning ON LED at position (${x}, ${y})`);
    // TODO: Send command to actual hardware
  };

  window.turnOffLed = function (x, y) {
    console.log(`🔴 Turning OFF LED at position (${x}, ${y})`);
    // TODO: Send command to actual hardware
  };

  window.setLED = function (ledNum, color) {
    console.log(`🔥 Setting LED ${ledNum} to color ${color}`);
    // TODO: Send command to actual hardware
  };

  window.setLEDBrightness = function (brightness) {
    console.log(`🔆 Setting LED brightness to ${brightness}`);
  };

  window.playLEDPattern = function (pattern) {
    console.log(`✨ Playing LED pattern: ${pattern}`);
  };

  window.readDistance = function () {
    var distance = Math.floor(Math.random() * 100) + 10; // Simulate 10-110 cm
    console.log(`📏 Distance sensor reading: ${distance} cm`);
    return distance;
  };

  window.readLight = function () {
    var light = Math.floor(Math.random() * 100); // Simulate 0-100%
    console.log(`☀️ Light sensor reading: ${light}%`);
    return light;
  };

  window.readTemperature = function () {
    var temp = Math.floor(Math.random() * 30) + 15; // Simulate 15-45°C
    console.log(`🌡️ Temperature sensor reading: ${temp}°C`);
    return temp;
  };

  window.moveForward = function (steps) {
    console.log(`🦕 Moving DinoBot forward ${steps} steps`);
  };

  window.turnLeft = function (degrees) {
    console.log(`↺ Turning DinoBot left ${degrees} degrees`);
  };

  window.turnRight = function (degrees) {
    console.log(`↻ Turning DinoBot right ${degrees} degrees`);
  };

  window.stopRobot = function () {
    console.log(`🛑 Stopping DinoBot`);
  };

  window.playSound = function (sound) {
    console.log(`🔊 Playing sound: ${sound}`);
  };

  window.displayMessage = function (message) {
    console.log(`📱 Displaying message: ${message}`);
    alert(`DinoBot says: ${message}`);
  };

  window.wait = function (ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  };
}
  window.turnRight = function (degrees) {
    console.log(`↻ Turning DinoBot right ${degrees} degrees`);
  };

  window.stopRobot = function () {
    console.log(`🛑 Stopping DinoBot`);
  };

  window.playSound = function (sound) {
    console.log(`🔊 Playing sound: ${sound}`);
  };

  window.displayMessage = function (message) {
    console.log(`📱 Displaying message: ${message}`);
    alert(`DinoBot says: ${message}`);
  };

  window.wait = function (ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  };
