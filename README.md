# Flutter SMS Detector

 ##### A flutter plugin for receiving incoming sms in android. Currently supports on only Android.

### Usage

 ###### To use this plugin, add sms_detector as a [dependency in your pubspec.yaml file.](https://flutter.dev/docs/development/packages-and-plugins/using-packages)


### Example

```dart
@override
  void initState() {
    super.initState();
    retrieveNewSms();
  }

  // call this method to start sms receiver
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
```