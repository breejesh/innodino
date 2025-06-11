import 'package:flutter/material.dart';
import '../widgets/blockly_workspace.dart';
import '../services/arduino_service.dart';

class BlockEditorScreen extends StatefulWidget {
  const BlockEditorScreen({super.key});

  @override
  State<BlockEditorScreen> createState() => _BlockEditorScreenState();
}

class _BlockEditorScreenState extends State<BlockEditorScreen> {
  final ArduinoService _arduinoService = ArduinoService();
  String _arduinoCode = '';

  void _handleCodeGenerated(String code) {
    setState(() {
      _arduinoCode = _arduinoService.generateBlocklyCode(code);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Block Editor'),
        actions: [
          IconButton(
            icon: const Icon(Icons.upload),
            tooltip: 'Upload to Arduino',
            onPressed: () async {
              if (_arduinoCode.isEmpty) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Please create a program first'),
                  ),
                );
                return;
              }

              final success = await _arduinoService.uploadCode(_arduinoCode);
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(
                    success ? 'Code uploaded successfully!' : 'Failed to upload code',
                  ),
                  backgroundColor: success ? Colors.green : Colors.red,
                ),
              );
            },
          ),
        ],
      ),
      body: Row(
        children: [
          // Blockly workspace
          Expanded(
            flex: 2,
            child: BlocklyWorkspace(
              onCodeGenerated: _handleCodeGenerated,
            ),
          ),
          // Code preview panel
          if (_arduinoCode.isNotEmpty)
            Expanded(
              flex: 1,
              child: Container(
                decoration: BoxDecoration(
                  border: Border(
                    left: BorderSide(
                      color: Theme.of(context).dividerColor,
                    ),
                  ),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        'Generated Arduino Code',
                        style: Theme.of(context).textTheme.titleMedium,
                      ),
                    ),
                    Expanded(
                      child: SingleChildScrollView(
                        padding: const EdgeInsets.all(8.0),
                        child: SelectableText(
                          _arduinoCode,
                          style: const TextStyle(
                            fontFamily: 'monospace',
                            fontSize: 12,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          showDialog(
            context: context,
            builder: (context) => AlertDialog(
              title: const Text('Help'),
              content: const SingleChildScrollView(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text('How to use the Block Editor:'),
                    SizedBox(height: 8),
                    Text('1. Drag blocks from the left panel'),
                    Text('2. Connect blocks to create a program'),
                    Text('3. Use variables to store values'),
                    Text('4. Add control blocks for loops and conditions'),
                    Text('5. Click Upload to send to Arduino'),
                    SizedBox(height: 16),
                    Text('Tips:'),
                    Text('• Blocks snap together automatically'),
                    Text('• Use math blocks for calculations'),
                    Text('• Check the generated code on the right'),
                  ],
                ),
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.of(context).pop(),
                  child: const Text('Got it'),
                ),
              ],
            ),
          );
        },
        tooltip: 'Show help',
        child: const Icon(Icons.help_outline),
      ),
    );
  }
}