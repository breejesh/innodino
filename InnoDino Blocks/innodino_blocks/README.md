# InnoDino Blocks

A Flutter-based block programming interface for Arduino robot control. This application allows kids to program their Arduino robot using an intuitive block-based interface.

## Features

- Intuitive block-based programming interface
- Support for robot movement commands
- Ultrasonic sensor integration
- 8x8 LED matrix support
- Real-time Arduino code generation
- Direct upload to Arduino

## Prerequisites

- Flutter SDK (latest version)
- Android Studio
- Arduino IDE
- Arduino board (compatible with the robot)
- Required Arduino libraries:
  - Servo.h
  - Wire.h
  - Adafruit_GFX.h
  - Adafruit_SSD1306.h

## Setup

1. Install Flutter:
   ```bash
   # Download Flutter SDK from https://flutter.dev/docs/get-started/install
   # Add Flutter to your PATH
   ```

2. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/innodino_blocks.git
   cd innodino_blocks
   ```

3. Install dependencies:
   ```bash
   flutter pub get
   ```

4. Run the app:
   ```bash
   flutter run
   ```

## Hardware Setup

1. Connect the ultrasonic sensor:
   - TRIG_PIN -> Arduino pin 7
   - ECHO_PIN -> Arduino pin 8
   - VCC -> 5V
   - GND -> GND

2. Connect the motor driver:
   - LEFT_MOTOR_PIN1 -> Arduino pin 5
   - LEFT_MOTOR_PIN2 -> Arduino pin 6
   - RIGHT_MOTOR_PIN1 -> Arduino pin 9
   - RIGHT_MOTOR_PIN2 -> Arduino pin 10

3. Connect the LED matrix:
   - SDA -> Arduino A4
   - SCL -> Arduino A5
   - VCC -> 5V
   - GND -> GND

## Usage

1. Launch the app
2. Click "Start Programming" to open the block editor
3. Drag and drop blocks to create your program
4. Click "Connect Arduino" to connect your device
5. Click the upload button to send the program to your Arduino

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
