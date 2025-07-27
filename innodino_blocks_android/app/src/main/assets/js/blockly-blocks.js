// InnoDino Custom Block Definitions

function defineCustomBlocks() {
  console.log('Defining custom InnoDino blocks...');

  // LED Control Blocks - All with jigsaw connections
  Blockly.defineBlocksWithJsonArray([
    {
      "type": "turn_on_led",
      "message0": "💡 Turn ON LED x: %1 y: %2",
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
      "colour": "#EB5757",
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
      "colour": "#EB5757",
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
      "colour": "#EB5757",
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
            ["SMILEY", "SMILEY"],
            ["OUTLINE", "OUTLINE"],
            ["FULL", "FULL"],
            ["PLUS", "PLUS"],
            ["DIAMOND", "DIAMOND"],
            ["HEART", "HEART"],
            ["ARROW", "ARROW"],
            ["CHESSBOARD", "CHESSBOARD"],
            ["CROSS", "CROSS"]
          ]
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#EB5757",
      "tooltip": "Create animated LED patterns",
      "helpUrl": ""
    }, 
    {
      "type": "clear_led",
      "message0": "Clear Entire LED",
      "args0": null,
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#EB5757",
      "tooltip": "Clear Entire LED",
      "helpUrl": ""
    }
  ]);

  // Sensor Blocks - Output only (no jigsaw connections)
  Blockly.defineBlocksWithJsonArray([
    {
      "type": "read_distance",
      "message0": "📏 Distance sensor reading",
      "output": "Number",
      "colour": "#FFCE55",
      "tooltip": "Get distance reading from ultrasonic sensor (cm)",
      "helpUrl": ""
    }
  ]);

  // Robot Movement Blocks - All with jigsaw connections
  Blockly.defineBlocksWithJsonArray([
    {
      "type": "move_forward",
      "message0": "🢁 Move forward %1 seconds",
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
      "tooltip": "Move the DinoBot forward",
      "helpUrl": ""
    },
    {
      "type": "move_backward",
      "message0": "🡻 Move backward %1 seconds",
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
      "tooltip": "Move the DinoBot backward",
      "helpUrl": ""
    },
    {
      "type": "turn_left",
      "message0": "↺ Turn left %1 seconds",
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
      "tooltip": "Turn the DinoBot left",
      "helpUrl": ""
    },
    {
      "type": "turn_right",
      "message0": "↻ Turn right %1 seconds",
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
      "tooltip": "Turn the DinoBot right",
      "helpUrl": ""
    },
    {
      "type": "stop_robot",
      "message0": "🛑 Stop DinoBot",
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#2D9CDB",
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
          "variable": "variable"
        },
        {
          "type": "input_value",
          "name": "VALUE"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": "#4F4F4F",
      "tooltip": "Set a variable to a value",
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
      "colour": "#4F4F4F",
      "tooltip": "Wait for a specific number of seconds",
      "helpUrl": ""
    }
  ]);
}
