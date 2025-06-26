//We always have to include the library
#include "LedControl.h"
#include <HCSR04.h>

LedControl lc = LedControl(12,11,10,1);
byte triggerPin = 2;
byte echoPin = 4;


/* we always wait a bit between updates of the display */
unsigned long delaytime = 10;

void setup() {
  /*
   The MAX72XX is in power-saving mode on startup,
   we have to do a wakeup call
   */
  lc.shutdown(0,false);
  /* Set the brightness to a medium values */
  lc.setIntensity(0,1);
  /* and clear the display */
  lc.clearDisplay(0);
  HCSR04.begin(triggerPin, echoPin);
  pattern();
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
  setIntensity(getDistance() * 10 / 200);
  delay(delaytime);
}
