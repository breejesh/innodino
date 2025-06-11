import 'package:flutter/material.dart';

class RobotMissionsScreen extends StatefulWidget {
  const RobotMissionsScreen({super.key});

  @override
  State<RobotMissionsScreen> createState() => _RobotMissionsScreenState();
}

class _RobotMissionsScreenState extends State<RobotMissionsScreen> {
  List<RobotCommand> commands = [];
  bool isExecuting = false;
  int currentCommandIndex = -1;
  int currentMissionIndex = 0;
  List<Mission> missions = [];

  @override
  void initState() {
    super.initState();
    _initializeMissions();
  }

  void _initializeMissions() {
    missions = [
      // LEVEL 1: Basic Commands (Sequential Programming)
      Mission(
        id: 1,
        title: "First Steps",
        description: "Learn basic robot movements",
        objective: "Make the robot move forward 3 times",
        concept: "Sequential Commands",
        explanation: "Commands run one after another in order. This is called 'sequence' in programming!",
        requiredCommands: ['forward', 'forward', 'forward'],
        maxCommands: 5,
        difficulty: 1,
      ),
      Mission(
        id: 2,
        title: "Square Dance",
        description: "Create a square path",
        objective: "Move forward, turn right, repeat to make a square",
        concept: "Patterns & Planning",
        explanation: "Breaking big tasks into smaller steps is key to programming!",
        requiredCommands: ['forward', 'turnRight', 'forward', 'turnRight', 'forward', 'turnRight', 'forward', 'turnRight'],
        maxCommands: 10,
        difficulty: 1,
      ),

      // LEVEL 2: Loops (Repetition)
      Mission(
        id: 3,
        title: "Loop Master",
        description: "Use loops to save time",
        objective: "Make the robot spin 4 times using a repeat loop",
        concept: "Loops (Repetition)",
        explanation: "Loops let us repeat actions without writing the same command over and over!",
        requiredCommands: ['repeat4', 'turnRight'],
        maxCommands: 3,
        difficulty: 2,
      ),
      Mission(
        id: 4,
        title: "Efficient Explorer",
        description: "Explore with loops",
        objective: "Move forward 3 times, then turn around using loops",
        concept: "Nested Actions",
        explanation: "We can put loops inside other commands to create complex behaviors!",
        requiredCommands: ['repeat3', 'forward', 'repeat2', 'turnRight'],
        maxCommands: 6,
        difficulty: 2,
      ),

      // LEVEL 3: Conditionals (Logic)
      Mission(
        id: 5,
        title: "Smart Navigator",
        description: "Make decisions with IF statements",
        objective: "Check for obstacles and only move if path is clear",
        concept: "Conditional Logic (IF)",
        explanation: "IF statements let robots make decisions based on what they sense!",
        requiredCommands: ['checkDistance', 'ifClear', 'forward'],
        maxCommands: 5,
        difficulty: 3,
      ),
      Mission(
        id: 6,
        title: "Obstacle Avoider",
        description: "Navigate around obstacles",
        objective: "If blocked, turn right and try again",
        concept: "IF-ELSE Logic",
        explanation: "ELSE lets us do something different when the IF condition is false!",
        requiredCommands: ['checkDistance', 'ifBlocked', 'turnRight', 'else', 'forward'],
        maxCommands: 8,
        difficulty: 3,
      ),

      // LEVEL 4: Combined Concepts
      Mission(
        id: 7,
        title: "Patrol Bot",
        description: "Combine loops and conditions",
        objective: "Patrol in a loop, checking for obstacles each time",
        concept: "Loops + Conditions",
        explanation: "Real programs combine loops and IF statements to create smart behavior!",
        requiredCommands: ['repeat5', 'checkDistance', 'ifClear', 'forward', 'else', 'turnRight'],
        maxCommands: 10,
        difficulty: 4,
      ),
      Mission(
        id: 8,
        title: "Maze Solver",
        description: "Navigate a complex maze",
        objective: "Use nested loops and conditions to find the exit",
        concept: "Complex Logic",
        explanation: "Advanced programs use nested loops and multiple conditions to solve complex problems!",
        requiredCommands: ['repeat10', 'checkDistance', 'ifClear', 'forward', 'else', 'turnRight', 'checkDistance', 'ifClear', 'forward'],
        maxCommands: 15,
        difficulty: 5,
      ),
    ];
  }

  Mission get currentMission => missions[currentMissionIndex];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF0F8FF),
      appBar: AppBar(
        title: Text('🎯 Mission ${currentMission.id}: ${currentMission.title}'),
        backgroundColor: _getDifficultyColor(currentMission.difficulty),
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: const Icon(Icons.help_outline),
            onPressed: _showMissionHelp,
            tooltip: 'Mission Help',
          ),
          IconButton(
            icon: const Icon(Icons.play_arrow),
            onPressed: commands.isNotEmpty ? _executeCommands : null,
            tooltip: 'Test Solution',
          ),
        ],
      ),
      body: Column(
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
                      '${commands.length}/${currentMission.maxCommands} commands',
                      style: TextStyle(color: Colors.grey[600]),
                    ),
                    const Spacer(),
                    if (_isMissionComplete())
                      const Row(
                        children: [
                          Icon(Icons.check_circle, color: Colors.green, size: 16),
                          SizedBox(width: 4),
                          Text('Complete!', style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold)),
                        ],
                      ),
                  ],
                ),
              ],
            ),
          ),

          // Command List
          Expanded(
            flex: 2,
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
                        const Icon(Icons.list, color: Colors.blue),
                        const SizedBox(width: 8),
                        const Text(
                          'Your Solution',
                          style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                        ),
                        const Spacer(),
                        if (_isMissionComplete())
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
                  ),
                  Expanded(
                    child: commands.isEmpty
                        ? const Center(
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Icon(Icons.psychology, size: 64, color: Colors.grey),
                                SizedBox(height: 16),
                                Text(
                                  'Start building your solution!\nUse the programming blocks below.',
                                  textAlign: TextAlign.center,
                                  style: TextStyle(fontSize: 16, color: Colors.grey),
                                ),
                              ],
                            ),
                          )
                        : ReorderableListView.builder(
                            padding: const EdgeInsets.all(8),
                            itemCount: commands.length,
                            onReorder: _reorderCommands,
                            itemBuilder: (context, index) {
                              final command = commands[index];
                              final isActive = currentCommandIndex == index && isExecuting;
                              return _buildCommandTile(command, index, isActive);
                            },
                          ),
                  ),
                ],
              ),
            ),
          ),

          // Programming Blocks based on mission level
          Expanded(
            flex: 2,
            child: Container(
              margin: const EdgeInsets.all(16),
              child: Column(
                children: [
                  Text(
                    'Programming Blocks',
                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 8),
                  Expanded(
                    child: GridView.count(
                      crossAxisCount: 3,
                      crossAxisSpacing: 8,
                      mainAxisSpacing: 8,
                      children: _buildAvailableCommands(),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  List<Widget> _buildAvailableCommands() {
    List<Widget> commands = [];
    
    // Basic movement commands (always available)
    commands.addAll([
      _buildCommandButton('⬆️', 'Forward', Colors.green, () => _addCommand(RobotCommand.forward())),
      _buildCommandButton('⬇️', 'Backward', Colors.orange, () => _addCommand(RobotCommand.backward())),
      _buildCommandButton('⬅️', 'Turn Left', Colors.blue, () => _addCommand(RobotCommand.turnLeft())),
      _buildCommandButton('➡️', 'Turn Right', Colors.purple, () => _addCommand(RobotCommand.turnRight())),
    ]);

    // Level 2+: Loops
    if (currentMission.difficulty >= 2) {
      commands.addAll([
        _buildCommandButton('🔄', 'Repeat 3x', Colors.indigo, () => _addCommand(RobotCommand.repeat3())),
        _buildCommandButton('🔁', 'Repeat 4x', Colors.indigo, () => _addCommand(RobotCommand.repeat4())),
        _buildCommandButton('↩️', 'Repeat 5x', Colors.indigo, () => _addCommand(RobotCommand.repeat5())),
      ]);
    }

    // Level 3+: Conditionals
    if (currentMission.difficulty >= 3) {
      commands.addAll([
        _buildCommandButton('📡', 'Check Distance', Colors.teal, () => _addCommand(RobotCommand.checkDistance())),
        _buildCommandButton('✅', 'If Clear', Colors.green[600]!, () => _addCommand(RobotCommand.ifClear())),
        _buildCommandButton('🚫', 'If Blocked', Colors.red[600]!, () => _addCommand(RobotCommand.ifBlocked())),
      ]);
    }

    // Level 4+: Advanced
    if (currentMission.difficulty >= 4) {
      commands.addAll([
        _buildCommandButton('↔️', 'Else', Colors.amber, () => _addCommand(RobotCommand.elseCommand())),
        _buildCommandButton('🔟', 'Repeat 10x', Colors.indigo, () => _addCommand(RobotCommand.repeat10())),
      ]);
    }

    // Utility commands
    commands.addAll([
      _buildCommandButton('⏸️', 'Wait', Colors.grey, () => _addCommand(RobotCommand.wait())),
      _buildCommandButton('🗑️', 'Clear All', Colors.red[300]!, _clearCommands),
    ]);

    return commands;
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
    if (commands.length > currentMission.maxCommands) return false;
    
    // Simple check - in a real implementation, you'd validate the logic
    for (String required in currentMission.requiredCommands) {
      bool found = commands.any((cmd) => cmd.type == required);
      if (!found) return false;
    }
    return true;
  }

  void _nextMission() {
    if (currentMissionIndex < missions.length - 1) {
      setState(() {
        currentMissionIndex++;
        commands.clear();
        currentCommandIndex = -1;
        isExecuting = false;
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
        content: const Text('You\'ve completed all missions and learned the fundamentals of programming:\n\n• Sequential Commands\n• Loops (Repetition)\n• Conditional Logic\n• Complex Problem Solving\n\nYou\'re ready for more advanced programming!'),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.of(context).pop();
              setState(() {
                currentMissionIndex = 0;
                commands.clear();
              });
            },
            child: const Text('Start Over'),
          ),
        ],
      ),
    );
  }

  Widget _buildCommandButton(String emoji, String label, Color color, VoidCallback onTap) {
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

  Widget _buildCommandTile(RobotCommand command, int index, bool isActive) {
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
        subtitle: command.description != null ? Text(command.description!) : null,
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            if (isActive) const SizedBox(
              width: 20,
              height: 20,
              child: CircularProgressIndicator(strokeWidth: 2),
            ),
            IconButton(
              icon: const Icon(Icons.delete, color: Colors.red),
              onPressed: () => _removeCommand(index),
            ),
          ],
        ),
      ),
    );
  }

  void _addCommand(RobotCommand command) {
    if (commands.length >= currentMission.maxCommands) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Maximum ${currentMission.maxCommands} commands for this mission!')),
      );
      return;
    }
    setState(() {
      commands.add(command);
    });
  }

  void _removeCommand(int index) {
    setState(() {
      commands.removeAt(index);
    });
  }

  void _clearCommands() {
    setState(() {
      commands.clear();
      currentCommandIndex = -1;
      isExecuting = false;
    });
  }

  void _reorderCommands(int oldIndex, int newIndex) {
    setState(() {
      if (newIndex > oldIndex) newIndex--;
      final command = commands.removeAt(oldIndex);
      commands.insert(newIndex, command);
    });
  }

  void _executeCommands() async {
    if (commands.isEmpty) return;
    
    setState(() {
      isExecuting = true;
      currentCommandIndex = 0;
    });

    for (int i = 0; i < commands.length; i++) {
      if (!isExecuting) break;
      
      setState(() {
        currentCommandIndex = i;
      });

      await Future.delayed(Duration(milliseconds: commands[i].duration));
    }

    setState(() {
      isExecuting = false;
      currentCommandIndex = -1;
    });

    if (_isMissionComplete()) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('🎉 Mission Complete! Great job learning programming!'),
          backgroundColor: Colors.green,
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Good try! Check the mission requirements and try again.'),
          backgroundColor: Colors.orange,
        ),
      );
    }
  }
}

class Mission {
  final int id;
  final String title;
  final String description;
  final String objective;
  final String concept;
  final String explanation;
  final List<String> requiredCommands;
  final int maxCommands;
  final int difficulty;

  Mission({
    required this.id,
    required this.title,
    required this.description,
    required this.objective,
    required this.concept,
    required this.explanation,
    required this.requiredCommands,
    required this.maxCommands,
    required this.difficulty,
  });
}

class RobotCommand {
  final String id;
  final String type;
  final String emoji;
  final String name;
  final String? description;
  final Color color;
  final int duration;

  RobotCommand({
    required this.id,
    required this.type,
    required this.emoji,
    required this.name,
    this.description,
    required this.color,
    this.duration = 1000,
  });

  // Basic movement commands
  factory RobotCommand.forward() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'forward',
    emoji: '⬆️',
    name: 'Move Forward',
    description: 'Go straight ahead',
    color: Colors.green,
    duration: 2000,
  );

  factory RobotCommand.backward() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'backward',
    emoji: '⬇️',
    name: 'Move Backward',
    description: 'Go in reverse',
    color: Colors.orange,
    duration: 2000,
  );

  factory RobotCommand.turnLeft() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'turnLeft',
    emoji: '⬅️',
    name: 'Turn Left',
    description: 'Rotate left 90°',
    color: Colors.blue,
    duration: 1500,
  );

  factory RobotCommand.turnRight() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'turnRight',
    emoji: '➡️',
    name: 'Turn Right',
    description: 'Rotate right 90°',
    color: Colors.purple,
    duration: 1500,
  );

  // Loop commands
  factory RobotCommand.repeat3() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'repeat3',
    emoji: '🔄',
    name: 'Repeat 3 Times',
    description: 'Repeat next command 3 times',
    color: Colors.indigo,
    duration: 500,
  );

  factory RobotCommand.repeat4() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'repeat4',
    emoji: '🔁',
    name: 'Repeat 4 Times',
    description: 'Repeat next command 4 times',
    color: Colors.indigo,
    duration: 500,
  );

  factory RobotCommand.repeat5() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'repeat5',
    emoji: '↩️',
    name: 'Repeat 5 Times',
    description: 'Repeat next command 5 times',
    color: Colors.indigo,
    duration: 500,
  );

  factory RobotCommand.repeat10() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'repeat10',
    emoji: '🔟',
    name: 'Repeat 10 Times',
    description: 'Repeat next command 10 times',
    color: Colors.indigo,
    duration: 500,
  );

  // Conditional commands
  factory RobotCommand.checkDistance() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'checkDistance',
    emoji: '📡',
    name: 'Check Distance',
    description: 'Look for obstacles ahead',
    color: Colors.teal,
    duration: 1000,
  );

  factory RobotCommand.ifClear() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'ifClear',
    emoji: '✅',
    name: 'If Path Clear',
    description: 'Only do next command if no obstacle',
    color: Colors.green[600]!,
    duration: 500,
  );

  factory RobotCommand.ifBlocked() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'ifBlocked',
    emoji: '🚫',
    name: 'If Path Blocked',
    description: 'Only do next command if obstacle detected',
    color: Colors.red[600]!,
    duration: 500,
  );

  factory RobotCommand.elseCommand() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'else',
    emoji: '↔️',
    name: 'Else',
    description: 'Do this if IF condition was false',
    color: Colors.amber,
    duration: 500,
  );

  // Utility commands
  factory RobotCommand.wait() => RobotCommand(
    id: DateTime.now().millisecondsSinceEpoch.toString(),
    type: 'wait',
    emoji: '⏸️',
    name: 'Wait',
    description: 'Pause for 2 seconds',
    color: Colors.grey,
    duration: 2000,
  );
}