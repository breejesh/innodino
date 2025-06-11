import 'package:flutter/material.dart';

class Block {
  final String type;
  final String label;
  final IconData icon;
  final Color color;

  const Block({
    required this.type,
    required this.label,
    required this.icon,
    required this.color,
  });

  Map<String, dynamic> toJson() {
    return {
      'type': type,
      'label': label,
    };
  }

  factory Block.fromJson(Map<String, dynamic> json) {
    IconData icon;
    Color color;

    switch (json['type']) {
      case 'forward':
        icon = Icons.arrow_upward;
        color = Colors.blue;
        break;
      case 'backward':
        icon = Icons.arrow_downward;
        color = Colors.blue;
        break;
      case 'turn_left':
        icon = Icons.rotate_left;
        color = Colors.blue;
        break;
      case 'turn_right':
        icon = Icons.rotate_right;
        color = Colors.blue;
        break;
      case 'ultrasonic':
        icon = Icons.sensors;
        color = Colors.green;
        break;
      case 'led_matrix':
        icon = Icons.grid_on;
        color = Colors.purple;
        break;
      case 'repeat':
        icon = Icons.repeat;
        color = Colors.orange;
        break;
      case 'if_distance':
        icon = Icons.call_split;
        color = Colors.purple;
        break;
      default:
        icon = Icons.help_outline;
        color = Colors.grey;
    }

    return Block(
      type: json['type'],
      label: json['label'],
      icon: icon,
      color: color,
    );
  }
}