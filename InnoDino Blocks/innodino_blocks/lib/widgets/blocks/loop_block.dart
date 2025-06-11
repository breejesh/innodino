import 'package:flutter/material.dart';

class LoopBlock extends StatelessWidget {
  final String type;
  final String label;
  final IconData icon;
  final int repeatCount;

  const LoopBlock({
    super.key,
    required this.type,
    required this.label,
    required this.icon,
    this.repeatCount = 3,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.orange,
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
                  '$label x$repeatCount',
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