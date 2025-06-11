@JS('window.serial')
library serial;

import 'dart:async';
import 'dart:html' as html;
import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:js/js.dart';

@JS('SerialPort')
class SerialPort {
  external SerialPort();
  external Future<void> open(dynamic options);
  external Future<void> close();
  external dynamic getInfo();
}

@JS('Serial')
class Serial {
  external Serial();
  external Future<List<SerialPort>> getPorts();
  external Future<SerialPort> requestPort([dynamic options]);
}

extension SerialExtension on html.Navigator {
  Serial get serial => Serial();
}

class ArduinoService {
  StreamController<String>? _dataController;
  StreamSubscription? _subscription;
  SerialPort? _webSerialPort;

  Future<List<String>> getAvailablePorts() async {
    if (kIsWeb) {
      try {
        // Request port access using Web Serial API
        final port = await html.window.navigator.serial.requestPort();
        _webSerialPort = port;
        return ['Web Serial Port'];
      } catch (e) {
        print('Error accessing Web Serial API: $e');
      }
      return [];
    } else {
      // For desktop/mobile platforms
      return ['COM3', 'COM4', 'COM5', 'COM6'];
    }
  }

  Future<bool> connect(String portName) async {
    try {
      _dataController = StreamController<String>.broadcast();
      
      if (kIsWeb) {
        if (_webSerialPort != null) {
          await _webSerialPort!.open({'baudRate': 9600});
          return true;
        }
        return false;
      } else {
        // Existing desktop/mobile implementation
        await Future.delayed(const Duration(seconds: 1));
        return true;
      }
    } catch (e) {
      print('Error connecting to Arduino: $e');
      return false;
    }
  }

  void disconnect() {
    if (kIsWeb && _webSerialPort != null) {
      _webSerialPort!.close();
      _webSerialPort = null;
    }
    _subscription?.cancel();
    _dataController?.close();
    _dataController = null;
    _subscription = null;
  }

  String generateArduinoCode(List<Map<String, dynamic>> blocks) {
    final buffer = StringBuffer();
    
    // Add necessary includes
    buffer.writeln('#include <Servo.h>');
    buffer.writeln('#include <Wire.h>');
    buffer.writeln('#include <Adafruit_GFX.h>');
    buffer.writeln('#include <Adafruit_SSD1306.h>');
    buffer.writeln();

    // Define pins
    buffer.writeln('// Pin definitions');
    buffer.writeln('#define TRIG_PIN 7');
    buffer.writeln('#define ECHO_PIN 8');
    buffer.writeln('#define LEFT_MOTOR_PIN1 5');
    buffer.writeln('#define LEFT_MOTOR_PIN2 6');
    buffer.writeln('#define RIGHT_MOTOR_PIN1 9');
    buffer.writeln('#define RIGHT_MOTOR_PIN2 10');
    buffer.writeln();

    // Setup function
    buffer.writeln('void setup() {');
    buffer.writeln('  Serial.begin(9600);');
    buffer.writeln('  pinMode(TRIG_PIN, OUTPUT);');
    buffer.writeln('  pinMode(ECHO_PIN, INPUT);');
    buffer.writeln('  pinMode(LEFT_MOTOR_PIN1, OUTPUT);');
    buffer.writeln('  pinMode(LEFT_MOTOR_PIN2, OUTPUT);');
    buffer.writeln('  pinMode(RIGHT_MOTOR_PIN1, OUTPUT);');
    buffer.writeln('  pinMode(RIGHT_MOTOR_PIN2, OUTPUT);');
    buffer.writeln('}');
    buffer.writeln();

    // Helper functions
    buffer.writeln('// Helper functions');
    buffer.writeln('void moveForward() {');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN1, HIGH);');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN2, LOW);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN1, HIGH);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN2, LOW);');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('void moveBackward() {');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN1, LOW);');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN2, HIGH);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN1, LOW);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN2, HIGH);');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('void turnLeft() {');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN1, LOW);');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN2, HIGH);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN1, HIGH);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN2, LOW);');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('void turnRight() {');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN1, HIGH);');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN2, LOW);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN1, LOW);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN2, HIGH);');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('void stopMotors() {');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN1, LOW);');
    buffer.writeln('  digitalWrite(LEFT_MOTOR_PIN2, LOW);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN1, LOW);');
    buffer.writeln('  digitalWrite(RIGHT_MOTOR_PIN2, LOW);');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('float getDistance() {');
    buffer.writeln('  digitalWrite(TRIG_PIN, LOW);');
    buffer.writeln('  delayMicroseconds(2);');
    buffer.writeln('  digitalWrite(TRIG_PIN, HIGH);');
    buffer.writeln('  delayMicroseconds(10);');
    buffer.writeln('  digitalWrite(TRIG_PIN, LOW);');
    buffer.writeln('  long duration = pulseIn(ECHO_PIN, HIGH);');
    buffer.writeln('  return duration * 0.034 / 2;');
    buffer.writeln('}');
    buffer.writeln();

    // Main loop
    buffer.writeln('void loop() {');
    
    // Generate code for each block
    for (var block in blocks) {
      switch (block['type']) {
        case 'forward':
          buffer.writeln('  moveForward();');
          buffer.writeln('  delay(1000);');
          buffer.writeln('  stopMotors();');
          break;
        case 'backward':
          buffer.writeln('  moveBackward();');
          buffer.writeln('  delay(1000);');
          buffer.writeln('  stopMotors();');
          break;
        case 'turn_left':
          buffer.writeln('  turnLeft();');
          buffer.writeln('  delay(500);');
          buffer.writeln('  stopMotors();');
          break;
        case 'turn_right':
          buffer.writeln('  turnRight();');
          buffer.writeln('  delay(500);');
          buffer.writeln('  stopMotors();');
          break;
        case 'ultrasonic':
          buffer.writeln('  float distance = getDistance();');
          buffer.writeln('  if (distance < 20) {');
          buffer.writeln('    stopMotors();');
          buffer.writeln('    delay(1000);');
          buffer.writeln('  }');
          buffer.writeln('  delay(100);'); // Add a small delay to avoid spamming
          break;
        case 'led_matrix':
          // TODO: Implement LED matrix code generation
          break;
        case 'repeat':
          final repeatCount = block['repeatCount'] ?? 3;
          buffer.writeln('  for(int i = 0; i < $repeatCount; i++) {');
          if (block['children'] != null) {
            for (var child in block['children']) {
              // Recursively generate code for nested blocks
              generateBlockCode(buffer, [child]);
            }
          }
          buffer.writeln('  }');
          break;
        case 'if_distance':
          final threshold = block['threshold'] ?? 20.0;
          buffer.writeln('  if (getDistance() < $threshold) {');
          if (block['children'] != null) {
            for (var child in block['children']) {
              // Recursively generate code for nested blocks
              generateBlockCode(buffer, [child]);
            }
          }
          buffer.writeln('  }');
          break;
      }
    }
    
    buffer.writeln('}');

    return buffer.toString();
  }

  void generateBlockCode(StringBuffer buffer, List<Map<String, dynamic>> blocks) {
    for (var block in blocks) {
      switch (block['type']) {
        case 'forward':
          buffer.writeln('  moveForward();');
          buffer.writeln('  delay(1000);');
          buffer.writeln('  stopMotors();');
          break;
        case 'backward':
          buffer.writeln('  moveBackward();');
          buffer.writeln('  delay(1000);');
          buffer.writeln('  stopMotors();');
          break;
        case 'turn_left':
          buffer.writeln('  turnLeft();');
          buffer.writeln('  delay(500);');
          buffer.writeln('  stopMotors();');
          break;
        case 'turn_right':
          buffer.writeln('  turnRight();');
          buffer.writeln('  delay(500);');
          buffer.writeln('  stopMotors();');
          break;
        case 'ultrasonic':
          buffer.writeln('  float distance = getDistance();');
          buffer.writeln('  if (distance < 20) {');
          buffer.writeln('    stopMotors();');
          buffer.writeln('    delay(1000);');
          buffer.writeln('  }');
          break;
        case 'led_matrix':
          // TODO: Implement LED matrix code generation
          break;
        case 'repeat':
          final repeatCount = block['repeatCount'] ?? 3;
          buffer.writeln('  for(int i = 0; i < $repeatCount; i++) {');
          if (block['children'] != null) {
            for (var child in block['children']) {
              // Recursively generate code for nested blocks
              generateBlockCode(buffer, [child]);
            }
          }
          buffer.writeln('  }');
          break;
        case 'if_distance':
          final threshold = block['threshold'] ?? 20.0;
          buffer.writeln('  if (getDistance() < $threshold) {');
          if (block['children'] != null) {
            for (var child in block['children']) {
              // Recursively generate code for nested blocks
              generateBlockCode(buffer, [child]);
            }
          }
          buffer.writeln('  }');
          break;
      }
    }
  }

  Future<bool> uploadCode(String code) async {
    try {
      // In a real implementation, this would use avrdude or similar to upload the code
      // For now, we'll just simulate a successful upload
      await Future.delayed(const Duration(seconds: 2));
      return true;
    } catch (e) {
      print('Error uploading code: $e');
      return false;
    }
  }

  String generateBlocklyCode(String jsCode) {
    StringBuffer buffer = StringBuffer();

    // Setup code
    buffer.writeln('#define TRIG_PIN 12');
    buffer.writeln('#define ECHO_PIN 11');
    buffer.writeln('#define MOTOR_A1 2');
    buffer.writeln('#define MOTOR_A2 3');
    buffer.writeln('#define MOTOR_B1 4');
    buffer.writeln('#define MOTOR_B2 5');
    buffer.writeln();

    // Setup function
    buffer.writeln('void setup() {');
    buffer.writeln('  Serial.begin(9600);');
    buffer.writeln('  pinMode(TRIG_PIN, OUTPUT);');
    buffer.writeln('  pinMode(ECHO_PIN, INPUT);');
    buffer.writeln('  pinMode(MOTOR_A1, OUTPUT);');
    buffer.writeln('  pinMode(MOTOR_A2, OUTPUT);');
    buffer.writeln('  pinMode(MOTOR_B1, OUTPUT);');
    buffer.writeln('  pinMode(MOTOR_B2, OUTPUT);');
    buffer.writeln('}');
    buffer.writeln();

    // Helper functions
    buffer.writeln('void moveForward(int duration = 1000) {');
    buffer.writeln('  digitalWrite(MOTOR_A1, HIGH);');
    buffer.writeln('  digitalWrite(MOTOR_A2, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_B1, HIGH);');
    buffer.writeln('  digitalWrite(MOTOR_B2, LOW);');
    buffer.writeln('  delay(duration);');
    buffer.writeln('  stopMotors();');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('void moveBackward(int duration = 1000) {');
    buffer.writeln('  digitalWrite(MOTOR_A1, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_A2, HIGH);');
    buffer.writeln('  digitalWrite(MOTOR_B1, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_B2, HIGH);');
    buffer.writeln('  delay(duration);');
    buffer.writeln('  stopMotors();');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('void turnLeft() {');
    buffer.writeln('  digitalWrite(MOTOR_A1, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_A2, HIGH);');
    buffer.writeln('  digitalWrite(MOTOR_B1, HIGH);');
    buffer.writeln('  digitalWrite(MOTOR_B2, LOW);');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('void turnRight() {');
    buffer.writeln('  digitalWrite(MOTOR_A1, HIGH);');
    buffer.writeln('  digitalWrite(MOTOR_A2, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_B1, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_B2, HIGH);');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('void stopMotors() {');
    buffer.writeln('  digitalWrite(MOTOR_A1, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_A2, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_B1, LOW);');
    buffer.writeln('  digitalWrite(MOTOR_B2, LOW);');
    buffer.writeln('}');
    buffer.writeln();

    buffer.writeln('float getDistance() {');
    buffer.writeln('  digitalWrite(TRIG_PIN, LOW);');
    buffer.writeln('  delayMicroseconds(2);');
    buffer.writeln('  digitalWrite(TRIG_PIN, HIGH);');
    buffer.writeln('  delayMicroseconds(10);');
    buffer.writeln('  digitalWrite(TRIG_PIN, LOW);');
    buffer.writeln('  long duration = pulseIn(ECHO_PIN, HIGH);');
    buffer.writeln('  return duration * 0.034 / 2;');
    buffer.writeln('}');
    buffer.writeln();

    // Main loop with generated code
    buffer.writeln('void loop() {');
    buffer.write(_convertJsToArduino(jsCode));
    buffer.writeln('}');

    return buffer.toString();
  }

  String _convertJsToArduino(String jsCode) {
    StringBuffer buffer = StringBuffer();
    
    // Add variable declarations
    if (jsCode.contains('=')) {
      buffer.writeln('  // Variable declarations');
      Set<String> variables = _extractVariables(jsCode);
      for (String variable in variables) {
        buffer.writeln('  float $variable = 0;');
      }
      buffer.writeln();
    }

    // Replace JavaScript patterns with Arduino code
    String arduinoCode = jsCode
      .replaceAll('moveForward(', '  moveForward(')
      .replaceAll('moveBackward(', '  moveBackward(')
      .replaceAll('turn_left();', '  turnLeft();\n  delay(500);\n  stopMotors();')
      .replaceAll('turn_right();', '  turnRight();\n  delay(500);\n  stopMotors();')
      .replaceAll('check_distance()', 'getDistance()')
      .replaceAll('for (var count', '  for (int count')
      .replaceAll('while (!', '  while (!')
      .replaceAllMapped(RegExp(r'moveForward\((\d+)\);'), (match) => 
        '  moveForward();\n  delay(${match.group(1)});\n  stopMotors();')
      .replaceAllMapped(RegExp(r'moveBackward\((\d+)\);'), (match) => 
        '  moveBackward();\n  delay(${match.group(1)});\n  stopMotors();')
      .replaceAll('if (', '  if (');

    buffer.write(arduinoCode);
    return buffer.toString();
  }

  Set<String> _extractVariables(String code) {
    Set<String> variables = {};
    RegExp varRegex = RegExp(r'(\w+)\s*=\s*[-\d.]+;');
    for (Match match in varRegex.allMatches(code)) {
      if (match.group(1) != null) {
        variables.add(match.group(1)!);
      }
    }
    return variables;
  }
}