import 'dart:async';

import 'package:flutter/services.dart';

class SmsDetector {
  static const MethodChannel _channel = const MethodChannel('sms_detector');

  static Future<String> waitForSms() async {
    Completer<String> _completer = Completer();
    //return await _channel.invokeMethod('startListeningReceiver');
    _channel.invokeMethod('startListeningReceiver').then((x){
      if(x == "permissionDenied"){
        _completer.completeError("Permission denied");
      }else if(x == "error")
        {
          _completer.completeError("Failed to detect SMS");
        }
      else
        _completer.complete(x);
    });

    return _completer.future;
  }

  static stopSmsReceiver() async {
    return await _channel.invokeMethod('stopListeningReceiver');
  }
}
