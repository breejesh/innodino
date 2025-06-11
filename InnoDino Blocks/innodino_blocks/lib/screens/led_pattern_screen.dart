import 'package:flutter/material.dart';

class LEDPatternScreen extends StatefulWidget {
  const LEDPatternScreen({super.key});

  @override
  State<LEDPatternScreen> createState() => _LEDPatternScreenState();
}

class _LEDPatternScreenState extends State<LEDPatternScreen> {
  List<List<bool>> ledMatrix = List.generate(8, (_) => List.filled(8, false));
  List<LEDCommand> codeCommands = [];
  bool isExecuting = false;
  int currentCommandIndex = -1;
  int currentMissionIndex = 0;
  List<LEDMission> missions = [];
  bool showInteractiveMode = false;

  @override
  void initState() {
    super.initState();
    _initializeMissions();
  }

  void _initializeMissions() {
    missions = [
      // LEVEL 1: Basic Commands (Sequential Programming)
      LEDMission(
        id: 1,
        title: "Light It Up",
        description: "Learn basic LED control",
        objective: "Turn on 3 LEDs in a row",
        concept: "Sequential Commands",
        explanation: "Commands execute one after another. Each setLED command controls one LED at a time!",
        requiredCommands: ['setLED', 'setLED', 'setLED'],
        maxCommands: 5,
        difficulty: 1,
        expectedPattern: _createRowPattern(),
      ),
      LEDMission(
        id: 2,
        title: "Pattern Builder",
        description: "Create your first pattern",
        objective: "Make a cross pattern using setLED commands",
        concept: "Coordinate System",
        explanation: "LEDs have positions (row, column). Programming means being precise with coordinates!",
        requiredCommands: ['setLED', 'setLED', 'setLED', 'setLED', 'setLED'],
        maxCommands: 8,
        difficulty: 1,
        expectedPattern: _createCrossPattern(),
      ),

      // LEVEL 2: Loops (Repetition)
      LEDMission(
        id: 3,
        title: "Loop the Light",
        description: "Use loops to save time",
        objective: "Light up an entire row using a for loop",
        concept: "For Loops",
        explanation: "Loops repeat actions! Instead of 8 setLED commands, use 1 loop that runs 8 times!",
        requiredCommands: ['forLoop', 'setLED'],
        maxCommands: 4,
        difficulty: 2,
        expectedPattern: _createFullRowPattern(),
      ),
      LEDMission(
        id: 4,
        title: "Grid Master",
        description: "Master nested loops",
        objective: "Fill the entire LED matrix using nested loops",
        concept: "Nested Loops",
        explanation: "Loops inside loops! One loop for rows, another for columns. This is how we fill grids!",
        requiredCommands: ['forLoop', 'forLoop', 'setLED'],
        maxCommands: 6,
        difficulty: 2,
        expectedPattern: _createFullGridPattern(),
      ),

      // LEVEL 3: Conditionals (Logic)
      LEDMission(
        id: 5,
        title: "Smart Patterns",
        description: "Add logic to your code",
        objective: "Create a checkerboard using IF conditions",
        concept: "Conditional Logic",
        explanation: "IF statements let us make decisions! Light LEDs only when certain conditions are true!",
        requiredCommands: ['forLoop', 'forLoop', 'ifCondition', 'setLED'],
        maxCommands: 8,
        difficulty: 3,
        expectedPattern: _createCheckerboardPattern(),
      ),
      LEDMission(
        id: 6,
        title: "Border Guard",
        description: "Conditional boundaries",
        objective: "Light only the border LEDs using IF conditions",
        concept: "Boundary Conditions",
        explanation: "IF statements can check if we're at edges! Perfect for creating borders and frames!",
        requiredCommands: ['forLoop', 'forLoop', 'ifCondition', 'setLED'],
        maxCommands: 10,
        difficulty: 3,
        expectedPattern: _createBorderPattern(),
      ),

      // LEVEL 4: Functions and Advanced Concepts
      LEDMission(
        id: 7,
        title: "Function Factory",
        description: "Create reusable code",
        objective: "Make a function to draw squares, then use it 4 times",
        concept: "Functions",
        explanation: "Functions are like mini-programs! Write code once, use it many times!",
        requiredCommands: ['function', 'forLoop', 'setLED', 'callFunction'],
        maxCommands: 12,
        difficulty: 4,
        expectedPattern: _createFourSquaresPattern(),
      ),
      LEDMission(
        id: 8,
        title: "Animation Master",
        description: "Bring patterns to life",
        objective: "Create an animated pattern using loops and delays",
        concept: "Animation & Timing",
        explanation: "Real programs use timing! Combine loops, conditions, and delays to create animations!",
        requiredCommands: ['whileLoop', 'forLoop', 'ifCondition', 'setLED', 'delay', 'clearLED'],
        maxCommands: 15,
        difficulty: 5,
        expectedPattern: _createAnimationPattern(),
      ),
    ];
  }

  LEDMission get currentMission => missions[currentMissionIndex];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF0F8FF),
      appBar: AppBar(
        title: Text(showInteractiveMode 
          ? '🎨 LED Interactive Mode' 
          : '🌈 Mission ${currentMission.id}: ${currentMission.title}'),
        backgroundColor: showInteractiveMode 
          ? Colors.purple 
          : _getDifficultyColor(currentMission.difficulty),
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: Icon(showInteractiveMode ? Icons.code : Icons.palette),
            onPressed: () => setState(() => showInteractiveMode = !showInteractiveMode),
            tooltip: showInteractiveMode ? 'Mission Mode' : 'Interactive Mode',
          ),
          if (!showInteractiveMode) ...[
            IconButton(
              icon: const Icon(Icons.help_outline),
              onPressed: _showMissionHelp,
              tooltip: 'Mission Help',
            ),
            IconButton(
              icon: const Icon(Icons.play_arrow),
              onPressed: codeCommands.isNotEmpty ? _executeCode : null,
              tooltip: 'Run Code',
            ),
          ] else ...[
            IconButton(
              icon: const Icon(Icons.upload),
              onPressed: _uploadToArduino,
              tooltip: 'Send to Robot',
            ),
          ],
        ],
      ),
      body: showInteractiveMode ? _buildInteractiveMode() : _buildMissionMode(),
    );
  }

  Widget _buildMissionMode() {
    return Column(
      children: [
        // Mission Info Card
        Container(
          margin: const EdgeInsets.all(16),
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(12),
            boxShadow: [BoxShadow(color: Colors.blue.withOpacity(0.1), blurRadius: 8)],
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                    decoration: BoxDecoration(
                      color: _getDifficultyColor(currentMission.difficulty),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Text(
                      currentMission.concept,
                      style: const TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold),
                    ),
                  ),
                  const Spacer(),
                  Text(
                    'Level ${currentMission.difficulty}',
                    style: TextStyle(
                      color: _getDifficultyColor(currentMission.difficulty),
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 8),
              Text(
                currentMission.objective,
                style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 4),
              Text(
                currentMission.explanation,
                style: TextStyle(color: Colors.grey[600]),
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Icon(Icons.code, size: 16, color: Colors.grey[600]),
                  const SizedBox(width: 4),
                  Text(
                    '${codeCommands.length}/${currentMission.maxCommands} commands',
                    style: TextStyle(color: Colors.grey[600]),
                  ),
                  const Spacer(),
                  if (_isMissionComplete())
                    Row(
                      children: [
                        const Icon(Icons.check_circle, color: Colors.green, size: 16),
                        const SizedBox(width: 4),
                        const Text('Complete!', style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold)),
                        const SizedBox(width: 16),
                        ElevatedButton.icon(
                          onPressed: _nextMission,
                          icon: const Icon(Icons.arrow_forward, size: 16),
                          label: const Text('Next Mission'),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.green,
                            foregroundColor: Colors.white,
                          ),
                        ),
                      ],
                    ),
                ],
              ),
            ],
          ),
        ),

        // Code Editor (Full Width)
        Expanded(
          flex: 3,
          child: Container(
            margin: const EdgeInsets.symmetric(horizontal: 16),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(12),
              boxShadow: [BoxShadow(color: Colors.grey.withOpacity(0.1), blurRadius: 8)],
            ),
            child: Column(
              children: [
                Container(
                  padding: const EdgeInsets.all(16),
                  decoration: const BoxDecoration(
                    color: Color(0xFFF5F5F5),
                    borderRadius: BorderRadius.vertical(top: Radius.circular(12)),
                  ),
                  child: Row(
                    children: [
                      const Icon(Icons.code, color: Colors.blue),
                      const SizedBox(width: 8),
                      const Text(
                        'Your Code - Output will show on real LED matrix',
                        style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                      const Spacer(),
                      ElevatedButton.icon(
                        onPressed: codeCommands.isNotEmpty ? _executeCode : null,
                        icon: const Icon(Icons.play_arrow, size: 16),
                        label: const Text('Run on Robot'),
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.green,
                          foregroundColor: Colors.white,
                        ),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: codeCommands.isEmpty
                      ? const Center(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(Icons.code_outlined, size: 64, color: Colors.grey),
                              SizedBox(height: 16),
                              Text(
                                'Build your LED code!\nUse programming blocks below.',
                                textAlign: TextAlign.center,
                                style: TextStyle(fontSize: 16, color: Colors.grey),
                              ),
                              SizedBox(height: 8),
                              Text(
                                'The pattern will display on your robot\'s LED matrix',
                                textAlign: TextAlign.center,
                                style: TextStyle(fontSize: 14, color: Colors.grey),
                              ),
                            ],
                          ),
                        )
                      : ReorderableListView.builder(
                          padding: const EdgeInsets.all(8),
                          itemCount: codeCommands.length,
                          onReorder: _reorderCommands,
                          itemBuilder: (context, index) {
                            final command = codeCommands[index];
                            final isActive = currentCommandIndex == index && isExecuting;
                            return _buildCodeTile(command, index, isActive);
                          },
                        ),
                ),
              ],
            ),
          ),
        ),

        // Code Blocks based on mission level
        Expanded(
          flex: 2,
          child: Container(
            margin: const EdgeInsets.all(16),
            child: Column(
              children: [
                const Text(
                  'Programming Blocks',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 8),
                Expanded(
                  child: GridView.count(
                    crossAxisCount: 4,
                    crossAxisSpacing: 8,
                    mainAxisSpacing: 8,
                    children: _buildAvailableCodeBlocks(),
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildInteractiveMode() {
    // ...existing interactive mode code...
    return Column(
      children: [
        // LED Matrix Grid
        Expanded(
          flex: 3,
          child: Container(
            margin: const EdgeInsets.all(20),
            padding: const EdgeInsets.all(20),
            decoration: BoxDecoration(
              color: Colors.black87,
              borderRadius: BorderRadius.circular(20),
              boxShadow: [
                BoxShadow(
                  color: Colors.purple.withOpacity(0.3),
                  blurRadius: 10,
                  spreadRadius: 2,
                ),
              ],
            ),
            child: AspectRatio(
              aspectRatio: 1,
              child: GridView.builder(
                physics: const NeverScrollableScrollPhysics(),
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 8,
                  crossAxisSpacing: 4,
                  mainAxisSpacing: 4,
                ),
                itemCount: 64,
                itemBuilder: (context, index) {
                  int row = index ~/ 8;
                  int col = index % 8;
                  return GestureDetector(
                    onTap: () => _toggleLED(row, col),
                    child: Container(
                      decoration: BoxDecoration(
                        color: ledMatrix[row][col] 
                            ? Colors.yellow 
                            : Colors.grey[800],
                        borderRadius: BorderRadius.circular(4),
                        boxShadow: ledMatrix[row][col] 
                            ? [BoxShadow(
                                color: Colors.yellow.withOpacity(0.6),
                                blurRadius: 8,
                                spreadRadius: 1,
                              )]
                            : null,
                      ),
                    ),
                  );
                },
              ),
            ),
          ),
        ),
        
        // Quick Pattern Buttons
        Container(
          height: 120,
          padding: const EdgeInsets.symmetric(horizontal: 20),
          child: Column(
            children: [
              const Text(
                'Quick Patterns',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  _buildQuickPatternButton('😊', _createSmileyPattern, Colors.orange),
                  _buildQuickPatternButton('❤️', _createHeartPattern, Colors.red),
                  _buildQuickPatternButton('⭐', _createStarPattern, Colors.yellow[700]!),
                  _buildQuickPatternButton('🔄', _createArrowPattern, Colors.blue),
                  _buildQuickPatternButton('🧹', _clearPattern, Colors.grey),
                ],
              ),
            ],
          ),
        ),
        
        // Animation Controls
        Container(
          padding: const EdgeInsets.all(20),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              ElevatedButton.icon(
                onPressed: () => setState(() => showInteractiveMode = false),
                icon: const Icon(Icons.school),
                label: const Text('Back to Missions'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.blue,
                  foregroundColor: Colors.white,
                ),
              ),
              ElevatedButton.icon(
                onPressed: _uploadToArduino,
                icon: const Icon(Icons.upload),
                label: const Text('Send to Robot'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.green,
                  foregroundColor: Colors.white,
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  List<Widget> _buildAvailableCodeBlocks() {
    List<Widget> blocks = [];
    
    // Basic LED commands (always available)
    blocks.addAll([
      _buildCodeBlockButton('💡', 'Set LED', Colors.yellow[700]!, () => _showSetLEDDialog()),
      _buildCodeBlockButton('⚫', 'Clear LED', Colors.grey, () => _showClearLEDDialog()),
      _buildCodeBlockButton('⏸️', 'Delay', Colors.blue[300]!, () => _showDelayDialog()),
      _buildCodeBlockButton('🧹', 'Clear All', Colors.red[300]!, _clearCode),
    ]);

    // Level 2+: Loops
    if (currentMission.difficulty >= 2) {
      blocks.addAll([
        _buildCodeBlockButton('🔄', 'For Loop', Colors.indigo, () => _showForLoopDialog()),
        _buildCodeBlockButton('🔁', 'While Loop', Colors.indigo, () => _addCodeCommand(LEDCommand.whileLoop())),
      ]);
    }

    // Level 3+: Conditionals
    if (currentMission.difficulty >= 3) {
      blocks.addAll([
        _buildCodeBlockButton('❓', 'If Condition', Colors.orange, () => _showIfConditionDialog()),
        _buildCodeBlockButton('↔️', 'Else', Colors.amber, () => _addCodeCommand(LEDCommand.elseCommand())),
      ]);
    }

    // Level 4+: Functions
    if (currentMission.difficulty >= 4) {
      blocks.addAll([
        _buildCodeBlockButton('📦', 'Function', Colors.purple, () => _showFunctionDialog()),
        _buildCodeBlockButton('📞', 'Call Function', Colors.purple[300]!, () => _showCallFunctionDialog()),
      ]);
    }

    return blocks;
  }

  // Input dialogs for commands that need parameters
  void _showSetLEDDialog({int? editIndex}) {
    int row = 3, col = 3;
    if (editIndex != null) {
      final command = codeCommands[editIndex];
      row = command.parameters?['row'] ?? 3;
      col = command.parameters?['col'] ?? 3;
    }
    
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('💡 Set LED Position'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Choose LED coordinates (0-7):'),
              const SizedBox(height: 16),
              Row(
                children: [
                  const Text('Row: '),
                  Expanded(
                    child: Slider(
                      value: row.toDouble(),
                      min: 0,
                      max: 7,
                      divisions: 7,
                      label: row.toString(),
                      onChanged: (value) {
                        setDialogState(() => row = value.round());
                      },
                    ),
                  ),
                  SizedBox(width: 40, child: Text(row.toString())),
                ],
              ),
              Row(
                children: [
                  const Text('Col: '),
                  Expanded(
                    child: Slider(
                      value: col.toDouble(),
                      min: 0,
                      max: 7,
                      divisions: 7,
                      label: col.toString(),
                      onChanged: (value) {
                        setDialogState(() => col = value.round());
                      },
                    ),
                  ),
                  SizedBox(width: 40, child: Text(col.toString())),
                ],
              ),
              const SizedBox(height: 16),
              Text('LED will be at position ($row, $col)'),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                if (editIndex != null) {
                  _updateCodeCommand(editIndex, LEDCommand.setLEDAt(row, col));
                } else {
                  _addCodeCommand(LEDCommand.setLEDAt(row, col));
                }
              },
              child: const Text('Add Command'),
            ),
          ],
        ),
      ),
    );
  }

  void _showClearLEDDialog({int? editIndex}) {
    int row = 3, col = 3;
    if (editIndex != null) {
      final command = codeCommands[editIndex];
      row = command.parameters?['row'] ?? 3;
      col = command.parameters?['col'] ?? 3;
    }
    
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('⚫ Clear LED Position'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Choose LED coordinates to clear (0-7):'),
              const SizedBox(height: 16),
              Row(
                children: [
                  const Text('Row: '),
                  Expanded(
                    child: Slider(
                      value: row.toDouble(),
                      min: 0,
                      max: 7,
                      divisions: 7,
                      label: row.toString(),
                      onChanged: (value) {
                        setDialogState(() => row = value.round());
                      },
                    ),
                  ),
                  SizedBox(width: 40, child: Text(row.toString())),
                ],
              ),
              Row(
                children: [
                  const Text('Col: '),
                  Expanded(
                    child: Slider(
                      value: col.toDouble(),
                      min: 0,
                      max: 7,
                      divisions: 7,
                      label: col.toString(),
                      onChanged: (value) {
                        setDialogState(() => col = value.round());
                      },
                    ),
                  ),
                  SizedBox(width: 40, child: Text(col.toString())),
                ],
              ),
              const SizedBox(height: 16),
              Text('Clear LED at position ($row, $col)'),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                if (editIndex != null) {
                  _updateCodeCommand(editIndex, LEDCommand.clearLEDAt(row, col));
                } else {
                  _addCodeCommand(LEDCommand.clearLEDAt(row, col));
                }
              },
              child: const Text('Add Command'),
            ),
          ],
        ),
      ),
    );
  }

  void _showDelayDialog({int? editIndex}) {
    int milliseconds = 500;
    if (editIndex != null) {
      final command = codeCommands[editIndex];
      milliseconds = command.parameters?['milliseconds'] ?? 500;
    }
    
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('⏸️ Set Delay Time'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Choose delay duration:'),
              const SizedBox(height: 16),
              Row(
                children: [
                  const Text('Time: '),
                  Expanded(
                    child: Slider(
                      value: milliseconds.toDouble(),
                      min: 100,
                      max: 3000,
                      divisions: 29,
                      label: '${milliseconds}ms',
                      onChanged: (value) {
                        setDialogState(() => milliseconds = value.round());
                      },
                    ),
                  ),
                  SizedBox(width: 60, child: Text('${milliseconds}ms')),
                ],
              ),
              const SizedBox(height: 16),
              Text('Wait for ${milliseconds} milliseconds'),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                if (editIndex != null) {
                  _updateCodeCommand(editIndex, LEDCommand.delayFor(milliseconds));
                } else {
                  _addCodeCommand(LEDCommand.delayFor(milliseconds));
                }
              },
              child: const Text('Add Command'),
            ),
          ],
        ),
      ),
    );
  }

  void _showForLoopDialog({int? editIndex}) {
    int start = 0, end = 8;
    String variable = 'i';
    if (editIndex != null) {
      final command = codeCommands[editIndex];
      start = command.parameters?['start'] ?? 0;
      end = command.parameters?['end'] ?? 8;
      variable = command.parameters?['variable'] ?? 'i';
    }
    
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('🔄 For Loop Parameters'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Configure your loop:'),
              const SizedBox(height: 16),
              Row(
                children: [
                  const Text('Start: '),
                  Expanded(
                    child: Slider(
                      value: start.toDouble(),
                      min: 0,
                      max: 7,
                      divisions: 7,
                      label: start.toString(),
                      onChanged: (value) {
                        setDialogState(() => start = value.round());
                      },
                    ),
                  ),
                  SizedBox(width: 40, child: Text(start.toString())),
                ],
              ),
              Row(
                children: [
                  const Text('End: '),
                  Expanded(
                    child: Slider(
                      value: end.toDouble(),
                      min: 1,
                      max: 8,
                      divisions: 7,
                      label: end.toString(),
                      onChanged: (value) {
                        setDialogState(() => end = value.round());
                      },
                    ),
                  ),
                  SizedBox(width: 40, child: Text(end.toString())),
                ],
              ),
              const SizedBox(height: 16),
              Text('for ($variable = $start; $variable < $end; $variable++)'),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                if (editIndex != null) {
                  _updateCodeCommand(editIndex, LEDCommand.forLoopRange(start, end, variable));
                } else {
                  _addCodeCommand(LEDCommand.forLoopRange(start, end, variable));
                }
              },
              child: const Text('Add Loop'),
            ),
          ],
        ),
      ),
    );
  }

  void _showIfConditionDialog({int? editIndex}) {
    String condition = '(row + col) % 2 == 0';
    List<String> presetConditions = [
      '(row + col) % 2 == 0',
      'row == 0 || row == 7',
      'col == 0 || col == 7',
      'row == col',
      'row + col == 7',
    ];
    if (editIndex != null) {
      final command = codeCommands[editIndex];
      condition = command.parameters?['condition'] ?? condition;
    }
    
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('❓ If Condition'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Choose a condition:'),
              const SizedBox(height: 16),
              ...presetConditions.map((preset) => RadioListTile<String>(
                title: Text(preset),
                value: preset,
                groupValue: condition,
                onChanged: (value) {
                  setDialogState(() => condition = value!);
                },
              )),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                if (editIndex != null) {
                  _updateCodeCommand(editIndex, LEDCommand.ifConditionWith(condition));
                } else {
                  _addCodeCommand(LEDCommand.ifConditionWith(condition));
                }
              },
              child: const Text('Add Condition'),
            ),
          ],
        ),
      ),
    );
  }

  void _showFunctionDialog({int? editIndex}) {
    String functionName = 'drawSquare';
    List<String> presetFunctions = [
      'drawSquare',
      'drawLine',
      'drawBorder',
      'fillArea',
    ];
    if (editIndex != null) {
      final command = codeCommands[editIndex];
      functionName = command.parameters?['name'] ?? functionName;
    }
    
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('📦 Define Function'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Function name:'),
              const SizedBox(height: 16),
              ...presetFunctions.map((preset) => RadioListTile<String>(
                title: Text(preset),
                value: preset,
                groupValue: functionName,
                onChanged: (value) {
                  setDialogState(() => functionName = value!);
                },
              )),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                if (editIndex != null) {
                  _updateCodeCommand(editIndex, LEDCommand.functionNamed(functionName));
                } else {
                  _addCodeCommand(LEDCommand.functionNamed(functionName));
                }
              },
              child: const Text('Define Function'),
            ),
          ],
        ),
      ),
    );
  }

  void _showCallFunctionDialog({int? editIndex}) {
    String functionName = 'drawSquare';
    List<String> availableFunctions = [
      'drawSquare',
      'drawLine',
      'drawBorder',
      'fillArea',
    ];
    if (editIndex != null) {
      final command = codeCommands[editIndex];
      functionName = command.parameters?['name'] ?? functionName;
    }
    
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('📞 Call Function'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Which function to call:'),
              const SizedBox(height: 16),
              ...availableFunctions.map((preset) => RadioListTile<String>(
                title: Text(preset),
                value: preset,
                groupValue: functionName,
                onChanged: (value) {
                  setDialogState(() => functionName = value!);
                },
              )),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                if (editIndex != null) {
                  _updateCodeCommand(editIndex, LEDCommand.callFunctionNamed(functionName));
                } else {
                  _addCodeCommand(LEDCommand.callFunctionNamed(functionName));
                }
              },
              child: const Text('Call Function'),
            ),
          ],
        ),
      ),
    );
  }

  List<List<bool>> _createRowPattern() {
    var pattern = List.generate(8, (_) => List.filled(8, false));
    pattern[3][2] = pattern[3][3] = pattern[3][4] = true;
    return pattern;
  }

  List<List<bool>> _createCrossPattern() {
    var pattern = List.generate(8, (_) => List.filled(8, false));
    pattern[3][3] = pattern[2][3] = pattern[4][3] = pattern[3][2] = pattern[3][4] = true;
    return pattern;
  }

  List<List<bool>> _createFullRowPattern() {
    var pattern = List.generate(8, (_) => List.filled(8, false));
    for (int i = 0; i < 8; i++) pattern[3][i] = true;
    return pattern;
  }

  List<List<bool>> _createFullGridPattern() {
    return List.generate(8, (_) => List.filled(8, true));
  }

  List<List<bool>> _createCheckerboardPattern() {
    var pattern = List.generate(8, (_) => List.filled(8, false));
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if ((i + j) % 2 == 0) pattern[i][j] = true;
      }
    }
    return pattern;
  }

  List<List<bool>> _createBorderPattern() {
    var pattern = List.generate(8, (_) => List.filled(8, false));
    for (int i = 0; i < 8; i++) {
      pattern[0][i] = pattern[7][i] = pattern[i][0] = pattern[i][7] = true;
    }
    return pattern;
  }

  List<List<bool>> _createFourSquaresPattern() {
    var pattern = List.generate(8, (_) => List.filled(8, false));
    // Four 2x2 squares in corners
    pattern[1][1] = pattern[1][2] = pattern[2][1] = pattern[2][2] = true;
    pattern[1][5] = pattern[1][6] = pattern[2][5] = pattern[2][6] = true;
    pattern[5][1] = pattern[5][2] = pattern[6][1] = pattern[6][2] = true;
    pattern[5][5] = pattern[5][6] = pattern[6][5] = pattern[6][6] = true;
    return pattern;
  }

  List<List<bool>> _createAnimationPattern() {
    var pattern = List.generate(8, (_) => List.filled(8, false));
    pattern[3][3] = pattern[3][4] = pattern[4][3] = pattern[4][4] = true;
    return pattern;
  }

  Color _getDifficultyColor(int difficulty) {
    switch (difficulty) {
      case 1: return Colors.green;
      case 2: return Colors.blue;
      case 3: return Colors.orange;
      case 4: return Colors.purple;
      case 5: return Colors.red;
      default: return Colors.grey;
    }
  }

  bool _isMissionComplete() {
    if (codeCommands.length > currentMission.maxCommands) return false;
    
    // Check if required commands are present
    for (String required in currentMission.requiredCommands) {
      bool found = codeCommands.any((cmd) => cmd.type == required);
      if (!found) return false;
    }
    return true;
  }

  void _nextMission() {
    if (currentMissionIndex < missions.length - 1) {
      setState(() {
        currentMissionIndex++;
        codeCommands.clear();
        currentCommandIndex = -1;
        isExecuting = false;
        _clearPattern();
      });
      
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('🎉 New concept unlocked: ${currentMission.concept}!'),
          backgroundColor: Colors.green,
        ),
      );
    } else {
      _showCongratulations();
    }
  }

  void _showMissionHelp() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('💡 ${currentMission.concept}'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(currentMission.explanation),
            const SizedBox(height: 16),
            Text('Goal: ${currentMission.objective}', 
                 style: const TextStyle(fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Text('Max commands: ${currentMission.maxCommands}'),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Got it!'),
          ),
        ],
      ),
    );
  }

  void _showCongratulations() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('🎊 Congratulations!'),
        content: const Text('You\'ve mastered LED programming! You learned:\n\n• Sequential Commands\n• Loops & Iteration\n• Conditional Logic\n• Functions & Reusability\n• Animation & Timing\n\nYou\'re ready for advanced programming!'),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.of(context).pop();
              setState(() {
                currentMissionIndex = 0;
                codeCommands.clear();
                showInteractiveMode = true;
              });
            },
            child: const Text('Explore More'),
          ),
        ],
      ),
    );
  }

  Widget _buildCodeBlockButton(String emoji, String label, Color color, VoidCallback onTap) {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(8),
        child: Container(
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(8),
            gradient: LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: [color.withOpacity(0.8), color],
            ),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(emoji, style: const TextStyle(fontSize: 20)),
              const SizedBox(height: 2),
              Text(
                label,
                style: const TextStyle(
                  color: Colors.white,
                  fontWeight: FontWeight.bold,
                  fontSize: 10,
                ),
                textAlign: TextAlign.center,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildCodeTile(LEDCommand command, int index, bool isActive) {
    String parameterText = '';
    if (command.parameters != null) {
      switch (command.type) {
        case 'setLED':
        case 'clearLED':
          final row = command.parameters!['row'];
          final col = command.parameters!['col'];
          parameterText = 'at ($row, $col)';
          break;
        case 'forLoop':
          final start = command.parameters!['start'];
          final end = command.parameters!['end'];
          final variable = command.parameters!['variable'];
          parameterText = 'for ($variable = $start; $variable < $end; $variable++)';
          break;
        case 'delay':
          final ms = command.parameters!['milliseconds'];
          parameterText = 'wait ${ms}ms';
          break;
        case 'ifCondition':
          final condition = command.parameters!['condition'];
          parameterText = 'if ($condition)';
          break;
        case 'function':
        case 'callFunction':
          final name = command.parameters!['name'];
          parameterText = name;
          break;
      }
    }

    return Card(
      key: ValueKey(command.id),
      margin: const EdgeInsets.symmetric(vertical: 2, horizontal: 8),
      color: isActive ? Colors.green[100] : null,
      child: ListTile(
        leading: CircleAvatar(
          backgroundColor: command.color,
          child: Text(command.emoji, style: const TextStyle(fontSize: 16)),
        ),
        title: Text(command.name),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (command.description != null) Text(command.description!),
            if (parameterText.isNotEmpty) 
              Text(
                parameterText,
                style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.blue),
              ),
          ],
        ),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            if (isActive) const SizedBox(
              width: 20,
              height: 20,
              child: CircularProgressIndicator(strokeWidth: 2),
            ),
            IconButton(
              icon: const Icon(Icons.edit, color: Colors.blue),
              onPressed: () => _editCommand(index),
            ),
            IconButton(
              icon: const Icon(Icons.delete, color: Colors.red),
              onPressed: () => _removeCodeCommand(index),
            ),
          ],
        ),
      ),
    );
  }

  void _editCommand(int index) {
    final command = codeCommands[index];
    switch (command.type) {
      case 'setLED':
        _showSetLEDDialog(editIndex: index);
        break;
      case 'clearLED':
        _showClearLEDDialog(editIndex: index);
        break;
      case 'delay':
        _showDelayDialog(editIndex: index);
        break;
      case 'forLoop':
        _showForLoopDialog(editIndex: index);
        break;
      case 'ifCondition':
        _showIfConditionDialog(editIndex: index);
        break;
      case 'function':
        _showFunctionDialog(editIndex: index);
        break;
      case 'callFunction':
        _showCallFunctionDialog(editIndex: index);
        break;
      default:
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('This command cannot be edited')),
        );
    }
  }
  
  void _addCodeCommand(LEDCommand command) {
    if (codeCommands.length >= currentMission.maxCommands) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Maximum ${currentMission.maxCommands} commands for this mission!')),
      );
      return;
    }
    setState(() {
      codeCommands.add(command);
    });
  }

  void _removeCodeCommand(int index) {
    setState(() {
      codeCommands.removeAt(index);
    });
  }

  void _clearCode() {
    setState(() {
      codeCommands.clear();
      currentCommandIndex = -1;
      isExecuting = false;
    });
  }

  void _reorderCommands(int oldIndex, int newIndex) {
    setState(() {
      if (newIndex > oldIndex) newIndex--;
      final command = codeCommands.removeAt(oldIndex);
      codeCommands.insert(newIndex, command);
    });
  }

  void _executeCode() async {
    if (codeCommands.isEmpty) return;
    
    setState(() {
      isExecuting = true;
      currentCommandIndex = 0;
      _clearPattern();
    });

    for (int i = 0; i < codeCommands.length; i++) {
      if (!isExecuting) break;
      
      setState(() {
        currentCommandIndex = i;
      });

      // Simulate command execution
      await _executeCommand(codeCommands[i]);
      await Future.delayed(const Duration(milliseconds: 500));
    }

    setState(() {
      isExecuting = false;
      currentCommandIndex = -1;
    });

    if (_isMissionComplete()) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('🎉 Mission Complete! Great programming!'),
          backgroundColor: Colors.green,
        ),
      );
    }
  }

  Future<void> _executeCommand(LEDCommand command) async {
    switch (command.type) {
      case 'setLED':
        final row = command.parameters?['row'] ?? 3;
        final col = command.parameters?['col'] ?? 3;
        setState(() {
          ledMatrix[row][col] = true;
        });
        break;
      case 'clearLED':
        final row = command.parameters?['row'] ?? 3;
        final col = command.parameters?['col'] ?? 3;
        setState(() {
          ledMatrix[row][col] = false;
        });
        break;
      case 'forLoop':
        final start = command.parameters?['start'] ?? 0;
        final end = command.parameters?['end'] ?? 8;
        // Simulate loop execution
        for (int i = start; i < end; i++) {
          setState(() {
            ledMatrix[3][i] = true; // Simple visualization
          });
          await Future.delayed(const Duration(milliseconds: 100));
        }
        break;
      case 'delay':
        final milliseconds = command.parameters?['milliseconds'] ?? 500;
        await Future.delayed(Duration(milliseconds: milliseconds));
        break;
      case 'ifCondition':
        // Simulate conditional execution - for demo purposes
        final condition = command.parameters?['condition'] ?? '';
        if (condition.contains('checkerboard') || condition.contains('% 2 == 0')) {
          // Example: create checkerboard pattern
          for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
              if ((i + j) % 2 == 0) {
                setState(() {
                  ledMatrix[i][j] = true;
                });
                await Future.delayed(const Duration(milliseconds: 50));
              }
            }
          }
        }
        break;
      // Add more command executions as needed
    }
  }

  // ...existing interactive mode methods...
  Widget _buildQuickPatternButton(String emoji, VoidCallback onPressed, Color color) {
    return ElevatedButton(
      onPressed: onPressed,
      style: ElevatedButton.styleFrom(
        backgroundColor: color,
        shape: const CircleBorder(),
        padding: const EdgeInsets.all(15),
      ),
      child: Text(
        emoji,
        style: const TextStyle(fontSize: 20),
      ),
    );
  }

  void _toggleLED(int row, int col) {
    setState(() {
      ledMatrix[row][col] = !ledMatrix[row][col];
    });
  }

  void _createSmileyPattern() {
    setState(() {
      ledMatrix = List.generate(8, (_) => List.filled(8, false));
      // Eyes
      ledMatrix[2][2] = true;
      ledMatrix[2][5] = true;
      // Smile
      ledMatrix[5][2] = true;
      ledMatrix[6][3] = true;
      ledMatrix[6][4] = true;
      ledMatrix[5][5] = true;
    });
  }

  void _createHeartPattern() {
    setState(() {
      ledMatrix = List.generate(8, (_) => List.filled(8, false));
      // Heart pattern
      ledMatrix[1][2] = ledMatrix[1][3] = ledMatrix[1][4] = ledMatrix[1][5] = true;
      ledMatrix[2][1] = ledMatrix[2][6] = true;
      ledMatrix[3][0] = ledMatrix[3][7] = true;
      ledMatrix[4][1] = ledMatrix[4][6] = true;
      ledMatrix[5][2] = ledMatrix[5][5] = true;
      ledMatrix[6][3] = ledMatrix[6][4] = true;
    });
  }

  void _createStarPattern() {
    setState(() {
      ledMatrix = List.generate(8, (_) => List.filled(8, false));
      // Star pattern
      ledMatrix[1][3] = ledMatrix[1][4] = true;
      ledMatrix[2][2] = ledMatrix[2][3] = ledMatrix[2][4] = ledMatrix[2][5] = true;
      ledMatrix[3][1] = ledMatrix[3][6] = true;
      ledMatrix[4][2] = ledMatrix[4][5] = true;
      ledMatrix[5][3] = ledMatrix[5][4] = true;
    });
  }

  void _createArrowPattern() {
    setState(() {
      ledMatrix = List.generate(8, (_) => List.filled(8, false));
      // Arrow pointing right
      ledMatrix[3][1] = ledMatrix[3][2] = ledMatrix[3][3] = ledMatrix[3][4] = true;
      ledMatrix[1][4] = ledMatrix[2][4] = ledMatrix[4][4] = ledMatrix[5][4] = true;
      ledMatrix[0][5] = ledMatrix[6][5] = true;
    });
  }

  void _clearPattern() {
    setState(() {
      ledMatrix = List.generate(8, (_) => List.filled(8, false));
    });
  }

  void _uploadToArduino() {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text('Sending pattern to your InnoDino! 🦕'),
        backgroundColor: Colors.green,
      ),
    );
  }

  void _updateCodeCommand(int index, LEDCommand newCommand) {
    setState(() {
      codeCommands[index] = newCommand;
    });
  }
}

class LEDMission {
  final int id;
  final String title;
  final String description;
  final String objective;
  final String concept;
  final String explanation;
  final List<String> requiredCommands;
  final int maxCommands;
  final int difficulty;
  final List<List<bool>> expectedPattern;

  LEDMission({
    required this.id,
    required this.title,
    required this.description,
    required this.objective,
    required this.concept,
    required this.explanation,
    required this.requiredCommands,
    required this.maxCommands,
    required this.difficulty,
    required this.expectedPattern,
  });
}

class LEDCommand {
  final String id;
  final String type;
  final String emoji;
  final String name;
  final String? description;
  final Color color;
  final Map<String, dynamic>? parameters;

  LEDCommand({
    required this.id,
    required this.type,
    required this.emoji,
    required this.name,
    this.description,
    required this.color,
    this.parameters,
  });

  factory LEDCommand.setLED() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'setLED',
    emoji: '💡',
    name: 'Set LED On',
    description: 'Turn on LED at position (row, col)',
    color: Colors.yellow[700]!,
    parameters: {'row': 3, 'col': 3},
  );

  factory LEDCommand.clearLED() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'clearLED',
    emoji: '⚫',
    name: 'Clear LED',
    description: 'Turn off LED at position (row, col)',
    color: Colors.grey,
    parameters: {'row': 3, 'col': 3},
  );

  factory LEDCommand.forLoop() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'forLoop',
    emoji: '🔄',
    name: 'For Loop',
    description: 'Repeat commands for each value',
    color: Colors.indigo,
    parameters: {'start': 0, 'end': 8, 'variable': 'i'},
  );

  factory LEDCommand.whileLoop() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'whileLoop',
    emoji: '🔁',
    name: 'While Loop',
    description: 'Repeat while condition is true',
    color: Colors.indigo,
    parameters: {'condition': 'true'},
  );

  factory LEDCommand.ifCondition() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'ifCondition',
    emoji: '❓',
    name: 'If Condition',
    description: 'Execute only if condition is true',
    color: Colors.orange,
    parameters: {'condition': '(row + col) % 2 == 0'},
  );

  factory LEDCommand.elseCommand() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'else',
    emoji: '↔️',
    name: 'Else',
    description: 'Execute when IF condition is false',
    color: Colors.amber,
  );

  factory LEDCommand.function() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'function',
    emoji: '📦',
    name: 'Define Function',
    description: 'Create a reusable function',
    color: Colors.purple,
    parameters: {'name': 'drawSquare'},
  );

  factory LEDCommand.callFunction() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'callFunction',
    emoji: '📞',
    name: 'Call Function',
    description: 'Use a defined function',
    color: Colors.purple[300]!,
    parameters: {'name': 'drawSquare'},
  );

  factory LEDCommand.delay() => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'delay',
    emoji: '⏸️',
    name: 'Delay',
    description: 'Wait for specified time',
    color: Colors.blue[300]!,
    parameters: {'milliseconds': 500},
  );

  // Command factories with parameters
  factory LEDCommand.setLEDAt(int row, int col) => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'setLED',
    emoji: '💡',
    name: 'Set LED On',
    description: 'Turn on LED at position (row, col)',
    color: Colors.yellow[700]!,
    parameters: {'row': row, 'col': col},
  );

  factory LEDCommand.clearLEDAt(int row, int col) => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'clearLED',
    emoji: '⚫',
    name: 'Clear LED',
    description: 'Turn off LED at position (row, col)',
    color: Colors.grey,
    parameters: {'row': row, 'col': col},
  );

  factory LEDCommand.forLoopRange(int start, int end, String variable) => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'forLoop',
    emoji: '🔄',
    name: 'For Loop',
    description: 'Repeat commands from $start to $end',
    color: Colors.indigo,
    parameters: {'start': start, 'end': end, 'variable': variable},
  );

  factory LEDCommand.ifConditionWith(String condition) => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'ifCondition',
    emoji: '❓',
    name: 'If Condition',
    description: 'Execute if $condition',
    color: Colors.orange,
    parameters: {'condition': condition},
  );

  factory LEDCommand.functionNamed(String name) => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'function',
    emoji: '📦',
    name: 'Define Function',
    description: 'Create a function named $name',
    color: Colors.purple,
    parameters: {'name': name},
  );

  factory LEDCommand.callFunctionNamed(String name) => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'callFunction',
    emoji: '📞',
    name: 'Call Function',
    description: 'Call the function $name',
    color: Colors.purple[300]!,
    parameters: {'name': name},
  );

  factory LEDCommand.delayFor(int milliseconds) => LEDCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'delay',
    emoji: '⏸️',
    name: 'Delay',
    description: 'Wait for $milliseconds milliseconds',
    color: Colors.blue[300]!,
    parameters: {'milliseconds': milliseconds},
  );
}