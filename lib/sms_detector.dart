import 'dart:async';

import 'package:flutter/services.dart';

class SmsDetector {
  static const MethodChannel _channel = const MethodChannel('sms_detector');

  static Future<String>  waitForSmsReceive() async {
    return await _channel.invokeMethod('startListeningReceiver');
  }

  static stopSmsReceiver() async {
    return await _channel.invokeMethod('stopListeningReceiver');
  }
}
