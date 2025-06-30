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

---

## 🔄 Responses

- **Success:**  
  `@ACK|OK;`
- **Error:**  
  `@ERR|<errorType>;`

---

## 🧩 Example Messages

| Purpose               | Message Example                | Description                         |
| --------------------- | ------------------------------ | ----------------------------------- |
| Turn on LED #3        | `@LED\|TURN_ON\|3;`            | Light up LED 3                      |
| Set LED #3 brightness | `@LED\|SET_BRIGHTNESS\|3,200;` | Set LED 3 to medium brightness      |
| Move DinoBot forward  | `@BOT\|MOVE_FORWARD\|1500;`    | Move forward for 1.5 seconds        |
| Turn DinoBot left     | `@BOT\|TURN_LEFT\|600;`        | Turn left for 0.6 seconds           |
| Acknowledge success   | `@ACK\|OK;`                    | Arduino confirms successful command |
| Report error          | `@ERR\|INVALID_PARAM;`         | Arduino reports an invalid param    |

---

## 🛠️ Parsing Example (Arduino)

```cpp
// ...existing code...
if (Serial.available()) {
    String msg = Serial.readStringUntil(';');
    // Parse message: @MODULE|ACTION|PARAMS
    // Example: @BOT|MOVE_FORWARD|1000
    // ...parse and execute...
}
// ...existing code...
```

---

## 🌈 Tips for Extension

- Add new actions as your adventures grow!
- Keep parameter formats simple and consistent for easy parsing.

---
