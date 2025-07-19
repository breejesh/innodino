# 🦖 DinoSerial Protocol v1.0

This protocol enables playful, robust communication between the InnoDino Blocks Android app and your Arduino-powered DinoBot and LED Matrix modules.

---

## 📦 Message Structure

```
@<MODULE>|<ACTION>|<PARAMS>;
```

- `@` — Start of message
- `<MODULE>` — `LED` or `BOT`
- `<ACTION>` — See below for supported actions
- `<PARAMS>` — Comma-separated parameters (see examples)
- `;` — End of message

---

## 🦕 Supported Modules & Actions

### **LED Module**

- `TURN_ON` — Turn on a specific LED  
  **Params:** `<index>`
  - Example: `@LED|TURN_ON|3;`
- `TURN_OFF` — Turn off a specific LED  
  **Params:** `<index>`
  - Example: `@LED|TURN_OFF|3;`
- `SET_BRIGHTNESS` — Set brightness for a specific LED  
  **Params:** `<index>,<brightness>` (brightness: 0-255)
  - Example: `@LED|SET_BRIGHTNESS|3,128;`

### **BOT Module**

- `MOVE_FORWARD` — Move DinoBot forward  
  **Params:** `<duration_ms>`
  - Example: `@BOT|MOVE_FORWARD|1000;`
- `MOVE_BACKWARDS` — Move DinoBot backwards  
  **Params:** `<duration_ms>`
  - Example: `@BOT|MOVE_BACKWARDS|1000;`
- `TURN_RIGHT` — Turn DinoBot right  
  **Params:** `<duration_ms>`
  - Example: `@BOT|TURN_RIGHT|500;`
- `TURN_LEFT` — Turn DinoBot left  
  **Params:** `<duration_ms>`
  - Example: `@BOT|TURN_LEFT|500;`

### **SENSOR Module**

- `READ|DISTANCE` — Read distance from ultrasonic sensor  
  **Params:** None
  - Example: `@SENSOR|READ|DISTANCE;`
- `READ|TEMPERATURE` — Read temperature from temperature sensor  
  **Params:** None
  - Example: `@SENSOR|READ|TEMPERATURE;`
- `READ|LIGHT` — Read light level from light sensor  
  **Params:** None
  - Example: `@SENSOR|READ|LIGHT;`

---

## 🔄 Responses

- **Command Acknowledgment:**  
  `@ACK|OK;`
- **Error:**  
  `@ERR|<errorType>;`
- **Sensor Data Response:**  
  `@SENSOR_DATA|<sensorType>|<value(s)>;`

### **Sensor Response Examples**

- **Ultrasonic Distance:** `@SENSOR_DATA|DISTANCE|25.4;` (distance in cm)
- **Temperature:** `@SENSOR_DATA|TEMPERATURE|23.5;` (temperature in °C)
- **Light Level:** `@SENSOR_DATA|LIGHT|512;` (0-1023 scale)

---

## 🧩 Example Messages

| Purpose                    | Message Example                     | Description                              |
| -------------------------- | ----------------------------------- | ---------------------------------------- |
| Turn on LED #3             | `@LED\|TURN_ON\|3;`                 | Light up LED 3                           |
| Set LED #3 brightness      | `@LED\|SET_BRIGHTNESS\|3,200;`      | Set LED 3 to medium brightness           |
| Move DinoBot forward       | `@BOT\|MOVE_FORWARD\|1500;`         | Move forward for 1.5 seconds             |
| Turn DinoBot left          | `@BOT\|TURN_LEFT\|600;`             | Turn left for 0.6 seconds                |
| Read distance sensor       | `@SENSOR\|READ\|ULTRASONIC;`       | Request ultrasonic distance reading      |
| Read temperature           | `@SENSOR\|READ\|TEMPERATURE;`      | Request temperature reading              |
| Acknowledge success        | `@ACK\|OK;`                         | Arduino confirms successful command      |
| Return distance data       | `@SENSOR_DATA\|DISTANCE\|25.4;`   | Arduino sends distance: 25.4 cm         |
| Return temperature data    | `@SENSOR_DATA\|TEMPERATURE\|23.5;`  | Arduino sends temperature: 23.5°C       |
| Report error               | `@ERR\|INVALID_PARAM;`              | Arduino reports an invalid param         |

---

## 🛠️ Parsing Example (Arduino)

```cpp
// ...existing code...
if (Serial.available()) {
    String msg = Serial.readStringUntil(';');
    // Parse message: @MODULE|ACTION|PARAMS
    // Example: @BOT|MOVE_FORWARD|1000
    // Example: @SENSOR|READ_ULTRASONIC|
    
    if (msg.startsWith("@SENSOR|READ_ULTRASONIC")) {
        float distance = readUltrasonicSensor();
        Serial.print("@SENSOR_DATA|ULTRASONIC|");
        Serial.print(distance);
        Serial.println(";");
    }
    else if (msg.startsWith("@SENSOR|READ_ALL")) {
        float distance = readUltrasonicSensor();
        float temp = readTemperatureSensor();
        int light = readLightSensor();
        float accelX, accelY, accelZ;
        readAccelerometer(&accelX, &accelY, &accelZ);
        
        Serial.print("@SENSOR_DATA|ALL|");
        Serial.print(distance); Serial.print(",");
        Serial.print(temp); Serial.print(",");
        Serial.print(light); Serial.print(",");
        Serial.print(accelX); Serial.print(",");
        Serial.print(accelY); Serial.print(",");
        Serial.print(accelZ);
        Serial.println(";");
    }
    // ...parse and execute other commands...
}
// ...existing code...
```

---

## 🌈 Tips for Extension

- Add new actions as your adventures grow!
- Keep parameter formats simple and consistent for easy parsing.

---
