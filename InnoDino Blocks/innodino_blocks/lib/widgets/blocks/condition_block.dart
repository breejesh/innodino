import 'package:flutter/material.dart';

class ConditionBlock extends StatelessWidget {
  final String type;
  final String label;
  final IconData icon;
  final double threshold;

  const ConditionBlock({
    super.key,
    required this.type,
    required this.label,
    required this.icon,
    this.threshold = 20.0,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.purple,
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Icon(icon, color: Colors.white),
                Text(
                  '$label < ${threshold}cm',
                  style: const TextStyle(color: Colors.white),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}