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
  } else {
    sendError("UNKNOWN_MODULE");
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
    // Params: row,col,brightness
    int firstComma = params.indexOf(',');
    int secondComma = params.indexOf(',', firstComma + 1);
    if (firstComma == -1 || secondComma == -1) {
      sendError("BAD_PARAM");
      return;
    }
    int row = params.substring(0, firstComma).toInt();
    int col = params.substring(firstComma + 1, secondComma).toInt();
    int brightness = params.substring(secondComma + 1).toInt();
    setLed(row, col);
    setIntensity(brightness);
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