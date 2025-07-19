"use strict";

// --- LED Blocks ---

Blockly.JavaScript.forBlock["turn_on_led"] = function (block) {
	var value_x = Blockly.JavaScript.valueToCode(block, "X", Blockly.JavaScript.ORDER_ATOMIC) || "0";
	var value_y = Blockly.JavaScript.valueToCode(block, "Y", Blockly.JavaScript.ORDER_ATOMIC) || "0";
	return 'sendSerialCommand("@LED|TURN_ON|" + ' + value_x + ' + "," + ' + value_y + ' + ";");\n';
};

Blockly.JavaScript.forBlock["turn_off_led"] = function (block) {
	var value_x = Blockly.JavaScript.valueToCode(block, "X", Blockly.JavaScript.ORDER_ATOMIC) || "0";
	var value_y = Blockly.JavaScript.valueToCode(block, "Y", Blockly.JavaScript.ORDER_ATOMIC) || "0";
	return 'sendSerialCommand("@LED|TURN_OFF|" + ' + value_x + ' + "," + ' + value_y + ' + ";");\n';
};

Blockly.JavaScript.forBlock["set_led_brightness"] = function (block) {
	var value_brightness = block.getFieldValue("BRIGHTNESS") || "15";
	return 'sendSerialCommand("@LED|SET_BRIGHTNESS|" + ' + value_brightness + ' + ";");\n';
};

Blockly.JavaScript.forBlock["led_pattern"] = function (block) {
	var dropdown_pattern = block.getFieldValue("PATTERN") || "ERROR";
	return 'sendSerialCommand("@LED|PATTERN|" + "' + dropdown_pattern + '" + ";");\n';
};

Blockly.JavaScript.forBlock["clear_led"] = function (block) {
	return 'sendSerialCommand("@LED|CLEAR|-;");\n';
};

// --- Robot Blocks ---

Blockly.JavaScript.forBlock["move_forward"] = function (block) {
	var value_time = Blockly.JavaScript.valueToCode(block, "SECONDS", Blockly.JavaScript.ORDER_ATOMIC) || "1";
	return 'sendSerialCommand("@ROBOT|MOVE_FORWARD|" + ' + value_time + ' + ";");\n';
};

Blockly.JavaScript.forBlock["move_backward"] = function (block) {
	var value_time = Blockly.JavaScript.valueToCode(block, "SECONDS", Blockly.JavaScript.ORDER_ATOMIC) || "1";
	return 'sendSerialCommand("@ROBOT|MOVE_BACKWARD|" + ' + value_time + ' + ";");\n';
};

Blockly.JavaScript.forBlock["turn_left"] = function (block) {
	var value_degrees = Blockly.JavaScript.valueToCode(block, "SECONDS", Blockly.JavaScript.ORDER_ATOMIC) || "90";
	return 'sendSerialCommand("@ROBOT|TURN_LEFT|" + ' + value_degrees + ' + ";");\n';
};

Blockly.JavaScript.forBlock["turn_right"] = function (block) {
	var value_degrees = Blockly.JavaScript.valueToCode(block, "SECONDS", Blockly.JavaScript.ORDER_ATOMIC) || "90";
	return 'sendSerialCommand("@ROBOT|TURN_RIGHT|" + ' + value_degrees + ' + ";");\n';
};

Blockly.JavaScript.forBlock["stop_robot"] = function (block) {
	return 'sendSerialCommand("@ROBOT|STOP|;");\n';
};

// --- Time Blocks ---
Blockly.JavaScript.forBlock["wait_seconds"] = function (block) {
	var value_seconds = Blockly.JavaScript.valueToCode(block, "SECONDS", Blockly.JavaScript.ORDER_ATOMIC) || "1";
	return 'sendSerialCommand("@TIME|WAIT|" + ' + value_seconds + ' + ";");\n';
};

// --- Sensor Blocks ---
Blockly.JavaScript.forBlock["read_distance"] = function (block) {
	return ['getSensorValue("@SENSOR|READ|DISTANCE;")', Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

// --- Variable Blocks ---
Blockly.JavaScript.forBlock["variables_set"] = function (block) {
	var varName = Blockly.JavaScript.nameDB_.getName(block.getFieldValue("VAR"), Blockly.Variables.NAME_TYPE);
	var value = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_ASSIGNMENT) || "0";
	return varName + " = " + value + ";\n";
};

Blockly.JavaScript.forBlock["variables_get"] = function (block) {
	var varName = Blockly.JavaScript.nameDB_.getName(block.getFieldValue("VAR"), Blockly.Variables.NAME_TYPE);
	return [varName, Blockly.JavaScript.ORDER_ATOMIC];
};
