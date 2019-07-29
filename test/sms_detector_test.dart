import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:sms_detector/sms_detector.dart';

void main() {
  const MethodChannel channel = MethodChannel('sms_detector');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(SmsDetector.waitForSms, '42');
  });
}
