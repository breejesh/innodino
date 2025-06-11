import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart';
import 'dart:html' as html;
import 'dart:ui_web' as ui_web;

class BlocklyWorkspace extends StatefulWidget {
  final Function(String) onCodeGenerated;

  const BlocklyWorkspace({
    Key? key,
    required this.onCodeGenerated,
  }) : super(key: key);

  @override
  State<BlocklyWorkspace> createState() => _BlocklyWorkspaceState();
}

class _BlocklyWorkspaceState extends State<BlocklyWorkspace> {
  late html.IFrameElement _iframeElement;
  final String _viewType = 'blockly-iframe';

  @override
  void initState() {
    super.initState();
    if (kIsWeb) {
      _initWebView();
      _setupMessageListener();
    }
  }

  void _initWebView() {
    // Create an iframe element
    _iframeElement = html.IFrameElement()
      ..src = '/blockly.html'
      ..style.border = 'none'
      ..style.width = '100%'
      ..style.height = '100%';

    // Register the iframe as a platform view
    ui_web.platformViewRegistry.registerViewFactory(
      _viewType,
      (int viewId) => _iframeElement,
    );
  }

  void _setupMessageListener() {
    html.window.onMessage.listen((html.MessageEvent event) {
      if (event.data is Map && event.data['type'] == 'blocklyCode') {
        widget.onCodeGenerated(event.data['code']);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    if (kIsWeb) {
      return HtmlElementView(viewType: _viewType);
    } else {
      // For mobile platforms, you would use WebView here
      return const Center(
        child: Text('Blockly workspace is only available on web platform'),
      );
    }
  }
}