//We always have to include the library
#include "LedControl.h"
#include <HCSR04.h>

#define MAX_MSG_LEN 64

String incomingMsg = "";
char startChar = '@';
char endChar = ';';

LedControl lc = LedControl(12,11,10,1);
byte triggerPin = 2;
byte echoPin = 4;


/* we always wait a bit between updates of the display */
unsigned long delaytime = 10;

void setup() {
  lc.shutdown(0,false);
  /* Set the brightness to a medium values */
  lc.setIntensity(0,1);
  /* and clear the display */
  lc.clearDisplay(0);
  HCSR04.begin(triggerPin, echoPin);
  clearAll();
  Serial.begin(9600);
}

void setIntensity(int val) {
  if(val < 0) {
    val = 0;
  }
  if(val > 15) {
    val = 15;
  }
  lc.setIntensity(0, val);
}

void clearAll() {
  lc.clearDisplay(0);
}

void setLed(int row, int col) {
  lc.setLed(0, row, col, true);
}

void clearLed(int row, int col) {
  lc.setLed(0, row, col, false);
}

void pattern() {
  for(int row=0;row<8;row++) {
    for(int col=0;col<8;col++) {
      delay(delaytime);
      setLed(row, col);
      delay(delaytime);
    }
  }
}

int getDistance() {
    double* distances = HCSR04.measureDistanceCm();
    return distances[0];
}

void loop() { 
   while (Serial.available()) {
    char c = Serial.read();
    if (c == startChar) {
      incomingMsg = ""; // Start new message
    }
    incomingMsg += c;
    if (c == endChar) {
      processMessage(incomingMsg);
      incomingMsg = "";
    }
  }
}

// Parse and execute DinoSerial protocol message for LED module
void processMessage(String msg) {
  // Remove start/end chars
  if (msg[0] == startChar) msg = msg.substring(1);
  if (msg[msg.length() - 1] == endChar) msg = msg.substring(0, msg.length() - 1);

  // Split by '|'
  int firstDelim = msg.indexOf('|');
  int secondDelim = msg.indexOf('|', firstDelim + 1);

  if (firstDelim == -1 || secondDelim == -1) {
    sendError("BAD_FORMAT");
    return;
  }

  String module = msg.substring(0, firstDelim);
  String action = msg.substring(firstDelim + 1, secondDelim);
  String params = msg.substring(secondDelim + 1);

  if (module == "LED") {
    handleLED(action, params);
  } else if(module == "TIME") {
    handleTime(action, params);
  } else {
    sendError("UNKNOWN_MODULE");
  }
}

void handleTime(String action, String params) {
  if(action == "WAIT") {
    int waitTimeInSeconds = params.toInt();
    delay(waitTimeInSeconds * 1000);
    sendAck();
  } else {
    sendError("BAD_PARAM");
  }
}

void handleLED(String action, String params) {
  if (action == "TURN_ON" || action == "TURN_OFF") {
    int commaIdx = params.indexOf(',');
    if (commaIdx == -1) {
      sendError("BAD_PARAM");
      return;
    }
    int row = params.substring(0, commaIdx).toInt();
    int col = params.substring(commaIdx + 1).toInt();
    if (action == "TURN_ON") {
      setLed(row, col);
    } else if(action == "TURN_OFF") {
      clearLed(row, col);
    } else {
      sendError("UNKNOWN_ACTION");
    }
    sendAck();
  } else if (action == "SET_BRIGHTNESS") {
    int brightness = params.toInt();
    setIntensity(brightness);
    sendAck();
  } else if (action == "PATTERN") {
    // Params: pattern_name
    createLedPattern(params);
    sendAck();
  } else if (action == "CLEAR") {
    // Clear all LEDs
    clearAll();
    sendAck();
  } else {
    sendError("UNKNOWN_ACTION");
  }
}

void sendAck() {
  Serial.println("@ACK|OK;");
}

void sendError(String error) {
  Serial.print("@ERR|");
  Serial.print(error);
  Serial.println(";");
}

// Helper function to rotate coordinates 90 degrees clockwise
// For 8x8 matrix: (row, col) -> (col, 7-row)
void setLedRotated90(int row, int col) {
  setLed(col, 7 - row);
}

void createLedPattern(String pattern) {
  clearAll(); // Clear display before creating new pattern
  
  if (pattern == "SMILEY") {
    // Smiley face pattern (rotated 90 degrees right)
    // Eyes positioned correctly for upright smiley after rotation
    setLedRotated90(1, 1); setLedRotated90(1, 6); // Eyes
    setLedRotated90(4, 2); setLedRotated90(5, 3); setLedRotated90(5, 4); setLedRotated90(4, 5); // Smile
  }
  else if (pattern == "OUTLINE") {
    // Border outline (rotated 90 degrees right)
    for(int i = 0; i < 8; i++) {
      setLedRotated90(0, i); // Top row
      setLedRotated90(7, i); // Bottom row
      setLedRotated90(i, 0); // Left column
      setLedRotated90(i, 7); // Right column
    }
  }
  else if (pattern == "FULL") {
    // All LEDs on (rotation doesn't matter - same pattern)
    for(int row = 0; row < 8; row++) {
      for(int col = 0; col < 8; col++) {
        setLed(row, col);
      }
    }
  }
  else if (pattern == "PLUS") {
    // Cross pattern (rotated 90 degrees right)
    for(int i = 0; i < 8; i++) {
      setLedRotated90(i, 3); // Vertical line
      setLedRotated90(i, 4); // Vertical line
      setLedRotated90(3, i); // Horizontal line
      setLedRotated90(4, i); // Horizontal line
    }
  }
  else if (pattern == "DIAMOND") {
    // Diamond shape (rotated 90 degrees right)
    setLedRotated90(0, 3); setLedRotated90(0, 4);
    setLedRotated90(1, 2); setLedRotated90(1, 5);
    setLedRotated90(2, 1); setLedRotated90(2, 6);
    setLedRotated90(3, 0); setLedRotated90(3, 7);
    setLedRotated90(4, 0); setLedRotated90(4, 7);
    setLedRotated90(5, 1); setLedRotated90(5, 6);
    setLedRotated90(6, 2); setLedRotated90(6, 5);
    setLedRotated90(7, 3); setLedRotated90(7, 4);
  }
  else if (pattern == "HEART") {
    // Heart shape (rotated 90 degrees right)
    setLedRotated90(1, 1); setLedRotated90(1, 2); setLedRotated90(1, 5); setLedRotated90(1, 6);
    setLedRotated90(2, 0); setLedRotated90(2, 3); setLedRotated90(2, 4); setLedRotated90(2, 7);
    setLedRotated90(3, 0); setLedRotated90(3, 7);
    setLedRotated90(4, 1); setLedRotated90(4, 6);
    setLedRotated90(5, 2); setLedRotated90(5, 5);
    setLedRotated90(6, 3); setLedRotated90(6, 4);
  }
  else if (pattern == "ARROW") {
    // Arrow pointing up (rotated 90 degrees right - now points right)
    setLedRotated90(0, 3); setLedRotated90(0, 4);
    setLedRotated90(1, 2); setLedRotated90(1, 5);
    setLedRotated90(2, 1); setLedRotated90(2, 6);
    setLedRotated90(3, 0); setLedRotated90(3, 7);
    setLedRotated90(4, 3); setLedRotated90(4, 4);
    setLedRotated90(5, 3); setLedRotated90(5, 4);
    setLedRotated90(6, 3); setLedRotated90(6, 4);
    setLedRotated90(7, 3); setLedRotated90(7, 4);
  }
  else if (pattern == "CHESSBOARD") {
    // Checkerboard pattern (rotated 90 degrees right)
    for(int row = 0; row < 8; row++) {
      for(int col = 0; col < 8; col++) {
        if((row + col) % 2 == 0) {
          setLedRotated90(row, col);
        }
      }
    }
  }
  else if (pattern == "CROSS") {
    // X shape (rotated 90 degrees right)
    for(int i = 0; i < 8; i++) {
      setLedRotated90(i, i);     // Main diagonal
      setLedRotated90(i, 7-i);   // Anti-diagonal
    }
  }
  else {
    // Unknown pattern - show error pattern (blinking corners, rotated)
    setLedRotated90(0, 0); setLedRotated90(0, 7); setLedRotated90(7, 0); setLedRotated90(7, 7);
  }
}