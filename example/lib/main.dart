import 'package:flutter/material.dart';
import 'package:sms_detector/sms_detector.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _smsBody = 'Unknown';

  @override
  void initState() {
    super.initState();
    retrieveNewSms();
  }

  retrieveNewSms() async {
    _smsBody = await SmsDetector.waitForSmsReceive();
    setState(() {});
  }


  @override
  void dispose() {
    //stop sms receiver
    SmsDetector.stopSmsReceiver();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              Center(
                child: Text('SMS Body : $_smsBody'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
