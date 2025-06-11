import 'package:flutter/material.dart';

class ArduinoConnectionScreen extends StatefulWidget {
  const ArduinoConnectionScreen({super.key});

  @override
  State<ArduinoConnectionScreen> createState() => _ArduinoConnectionScreenState();
}

class _ArduinoConnectionScreenState extends State<ArduinoConnectionScreen>
    with TickerProviderStateMixin {
  bool _isConnected = false;
  bool _isSearching = false;
  String _selectedPort = '';
  late AnimationController _pulseController;
  late Animation<double> _pulseAnimation;

  @override
  void initState() {
    super.initState();
    _pulseController = AnimationController(
      duration: const Duration(seconds: 1),
      vsync: this,
    );
    _pulseAnimation = Tween<double>(
      begin: 0.8,
      end: 1.2,
    ).animate(CurvedAnimation(
      parent: _pulseController,
      curve: Curves.easeInOut,
    ));
  }

  @override
  void dispose() {
    _pulseController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF0F8FF),
      appBar: AppBar(
        title: const Text('🔧 Connect Your InnoDino'),
        backgroundColor: Colors.green,
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          children: [
            // Connection Status Card
            Card(
              elevation: 8,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
              child: Container(
                width: double.infinity,
                padding: const EdgeInsets.all(24),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(20),
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: _isConnected 
                        ? [Colors.green[300]!, Colors.green[600]!]
                        : [Colors.grey[300]!, Colors.grey[500]!],
                  ),
                ),
                child: Column(
                  children: [
                    AnimatedBuilder(
                      animation: _pulseAnimation,
                      builder: (context, child) {
                        return Transform.scale(
                          scale: _isConnected ? _pulseAnimation.value : 1.0,
                          child: Icon(
                            _isConnected ? Icons.wifi : Icons.wifi_off,
                            size: 64,
                            color: Colors.white,
                          ),
                        );
                      },
                    ),
                    const SizedBox(height: 16),
                    Text(
                      _isConnected ? '🎉 Connected!' : '🔍 Looking for InnoDino...',
                      style: const TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      _isConnected 
                          ? 'Ready to send commands!' 
                          : 'Make sure your robot is plugged in',
                      style: const TextStyle(
                        fontSize: 16,
                        color: Colors.white70,
                      ),
                    ),
                  ],
                ),
              ),
            ),

            const SizedBox(height: 24),

            // Robot Selection
            Card(
              elevation: 4,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        const Icon(Icons.smart_toy, color: Colors.blue, size: 28),
                        const SizedBox(width: 12),
                        const Text(
                          'Choose Your Robot',
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 16),
                    _buildRobotOption('🦕 InnoDino Walker', 'The walking robot with sensors', 'COM3'),
                    const SizedBox(height: 12),
                    _buildRobotOption('🌈 LED Matrix', 'The 8x8 light display', 'COM4'),
                    const SizedBox(height: 12),
                    _buildRobotOption('🔍 Auto-detect', 'Let me find it automatically', 'AUTO'),
                  ],
                ),
              ),
            ),

            const SizedBox(height: 24),

            // Connection Button
            SizedBox(
              width: double.infinity,
              height: 60,
              child: ElevatedButton(
                onPressed: _isSearching ? null : _handleConnection,
                style: ElevatedButton.styleFrom(
                  backgroundColor: _isConnected ? Colors.red : Colors.green,
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(16),
                  ),
                  elevation: 8,
                ),
                child: _isSearching
                    ? const Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 24,
                            height: 24,
                            child: CircularProgressIndicator(
                              strokeWidth: 2,
                              valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
                            ),
                          ),
                          SizedBox(width: 12),
                          Text('Searching...', style: TextStyle(fontSize: 18)),
                        ],
                      )
                    : Text(
                        _isConnected ? 'Disconnect' : 'Connect to InnoDino',
                        style: const TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
              ),
            ),

            const SizedBox(height: 24),

            // Help Section
            if (!_isConnected) ...[
              Card(
                color: Colors.blue[50],
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      Row(
                        children: [
                          Icon(Icons.help_outline, color: Colors.blue[700]),
                          const SizedBox(width: 8),
                          Text(
                            'Need Help?',
                            style: TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                              color: Colors.blue[700],
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 12),
                      const Text(
                        '1. Plug your InnoDino into the computer\n'
                        '2. Wait for the green light to blink\n'
                        '3. Tap "Connect to InnoDino"\n'
                        '4. Start creating awesome programs!',
                        style: TextStyle(fontSize: 14),
                      ),
                    ],
                  ),
                ),
              ),
            ],

            if (_isConnected) ...[
              const Spacer(),
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.green[100],
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Row(
                  children: [
                    Icon(Icons.check_circle, color: Colors.green[700], size: 32),
                    const SizedBox(width: 12),
                    const Expanded(
                      child: Text(
                        'Your InnoDino is ready!\nGo back and start programming!',
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildRobotOption(String name, String description, String port) {
    final isSelected = _selectedPort == port;
    return InkWell(
      onTap: () => setState(() => _selectedPort = port),
      borderRadius: BorderRadius.circular(12),
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          border: Border.all(
            color: isSelected ? Colors.blue : Colors.grey[300]!,
            width: 2,
          ),
          borderRadius: BorderRadius.circular(12),
          color: isSelected ? Colors.blue[50] : null,
        ),
        child: Row(
          children: [
            Radio<String>(
              value: port,
              groupValue: _selectedPort,
              onChanged: (value) => setState(() => _selectedPort = value!),
              activeColor: Colors.blue,
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    name,
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    description,
                    style: TextStyle(
                      fontSize: 14,
                      color: Colors.grey[600],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _handleConnection() async {
    if (_isConnected) {
      // Disconnect
      setState(() {
        _isConnected = false;
        _pulseController.stop();
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Disconnected from InnoDino'),
          backgroundColor: Colors.orange,
        ),
      );
    } else {
      // Connect
      if (_selectedPort.isEmpty) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Please choose a robot first!'),
            backgroundColor: Colors.red,
          ),
        );
        return;
      }

      setState(() => _isSearching = true);

      // Simulate connection process
      await Future.delayed(const Duration(seconds: 2));

      setState(() {
        _isSearching = false;
        _isConnected = true;
      });

      _pulseController.repeat(reverse: true);

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('🎉 Connected to InnoDino! Ready to program!'),
          backgroundColor: Colors.green,
        ),
      );
    }
  }
}