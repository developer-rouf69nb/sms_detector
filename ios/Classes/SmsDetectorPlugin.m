#import "SmsDetectorPlugin.h"
#import <sms_detector/sms_detector-Swift.h>

@implementation SmsDetectorPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSmsDetectorPlugin registerWithRegistrar:registrar];
}
@end
