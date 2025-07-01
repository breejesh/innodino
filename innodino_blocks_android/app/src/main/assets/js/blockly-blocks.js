// InnoDino Custom Block Definitions

function defineCustomBlocks() {
  console.log('Defining custom InnoDino blocks...');

  // LED Control Blocks - All with jigsaw connections
  Blockly.defineBlocksWithJsonArray([
    {
      "type": "turn_on_led",
      "message0": "🔴 Turn ON LED x: %1 y: %2",
      "args0": [
        {
          "type": "input_value",
          "name": "X",
          "check": "Number"
        },
        {
          "type": "input_value",
          "name": "Y",
          "check": "Number"
        }
      ],
      "inputsInline": true,
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#6FCF97",
      "tooltip": "Turn on LED at position (x,y)",
      "helpUrl": ""
    },
    {
      "type": "turn_off_led",
      "message0": "⚪ Turn OFF LED x: %1 y: %2",
      "args0": [
        {
          "type": "input_value",
          "name": "X",
          "check": "Number"
        },
        {
          "type": "input_value",
          "name": "Y",
          "check": "Number"
        }
      ],
      "inputsInline": true,
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#6FCF97",
      "tooltip": "Turn off LED at position (x,y)",
      "helpUrl": ""
    },
    {
      "type": "set_led_brightness",
      "message0": "🔆 Set LED brightness to %1",
      "args0": [
        {
          "type": "field_dropdown",
          "name": "BRIGHTNESS",
          "options": [
            ["0", "0"],
            ["1", "1"],
            ["2", "2"],
            ["3", "3"],
            ["4", "4"],
            ["5", "5"]
          ]
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#6FCF97",
      "tooltip": "Set LED brightness (0-5)",
      "helpUrl": ""
    },
    {
      "type": "led_pattern",
      "message0": "✨ Create LED pattern %1",
      "args0": [
        {
          "type": "field_dropdown",
          "name": "PATTERN",
          "options": [
            ["Rainbow", "rainbow"],
            ["Blink", "blink"],
            ["Wave", "wave"],
            ["Sparkle", "sparkle"],
            ["Fade", "fade"]
          ]
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#6FCF97",
      "tooltip": "Create animated LED patterns",
      "helpUrl": ""
    }
  ]);

  // Sensor Blocks - Output only (no jigsaw connections)
  Blockly.defineBlocksWithJsonArray([
    {
      "type": "read_distance",
      "message0": "📏 Distance sensor reading",
      "output": "Number",
      "colour": "#2D9CDB",
      "tooltip": "Get distance reading from ultrasonic sensor (cm)",
      "helpUrl": ""
    },
    {
      "type": "read_light",
      "message0": "☀️ Light sensor reading",
      "output": "Number",
      "colour": "#2D9CDB",
      "tooltip": "Get light level reading (0-100%)",
      "helpUrl": ""
    },
    {
      "type": "read_temperature",
      "message0": "🌡️ Temperature sensor reading",
      "output": "Number",
      "colour": "#2D9CDB",
      "tooltip": "Get temperature reading (°C)",
      "helpUrl": ""
    }
  ]);

  // Robot Movement Blocks - All with jigsaw connections
  Blockly.defineBlocksWithJsonArray([
    {
      "type": "move_forward",
      "message0": "🦕 Move forward %1 steps",
      "args0": [
        {
          "type": "input_value",
          "name": "STEPS",
          "check": "Number"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#FFCE55",
      "tooltip": "Move the DinoBot forward",
      "helpUrl": ""
    },
    {
      "type": "turn_left",
      "message0": "↺ Turn left %1 degrees",
      "args0": [
        {
          "type": "input_value",
          "name": "DEGREES",
          "check": "Number"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#FFCE55",
      "tooltip": "Turn the DinoBot left",
      "helpUrl": ""
    },
    {
      "type": "turn_right",
      "message0": "↻ Turn right %1 degrees",
      "args0": [
        {
          "type": "input_value",
          "name": "DEGREES",
          "check": "Number"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#FFCE55",
      "tooltip": "Turn the DinoBot right",
      "helpUrl": ""
    },
    {
      "type": "stop_robot",
      "message0": "🛑 Stop DinoBot",
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#EB5757",
      "tooltip": "Stop all robot movement",
      "helpUrl": ""
    }
  ]);

  // Control and Logic Blocks - With proper jigsaw connections
  Blockly.defineBlocksWithJsonArray([
    {
      "type": "variables_set",
      "message0": "📦 Set %1 to %2",
      "args0": [
        {
          "type": "field_variable",
          "name": "VAR",
          "variable": "item"
        },
        {
          "type": "input_value",
          "name": "VALUE"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#6FCF97",
      "tooltip": "Set a variable to a value",
      "helpUrl": ""
    },
    {
      "type": "repeat",
      "message0": "🔄 Repeat %1 times %2 %3",
      "args0": [
        {
          "type": "input_value",
          "name": "TIMES",
          "check": "Number"
        },
        {
          "type": "input_dummy"
        },
        {
          "type": "input_statement",
          "name": "DO"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#6FCF97",
      "tooltip": "Repeat the enclosed blocks",
      "helpUrl": ""
    },
    {
      "type": "wait_seconds",
      "message0": "⏰ Wait %1 seconds",
      "args0": [
        {
          "type": "input_value",
          "name": "SECONDS",
          "check": "Number"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#2D9CDB",
      "tooltip": "Wait for a specific number of seconds",
      "helpUrl": ""
    },
    {
      "type": "display_message",
      "message0": "📱 Display message %1",
      "args0": [
        {
          "type": "input_value",
          "name": "MESSAGE",
          "check": "String"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#6FCF97",
      "tooltip": "Display a message on the screen",
      "helpUrl": ""
    }
  ]);
}
