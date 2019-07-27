package com.codepoka.sms_detector

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.Toast
import android.support.v4.app.NotificationCompat.getExtras
import android.os.Bundle
import android.content.ContentValues.TAG








class SmsDetectorPlugin(var registrar: Registrar): MethodCallHandler {
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "sms_detector")
      channel.setMethodCallHandler(SmsDetectorPlugin(registrar))
    }
  }

  // private var isListenerAdded = false
  private  var _result:Result? = null
  private val _receiver:SMSReceiver
  init {
    registrar.addRequestPermissionsResultListener { _, _, _ ->
      if (ContextCompat.checkSelfPermission(registrar.activeContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
        if(_result != null){ returnResult()}
      }
      return@addRequestPermissionsResultListener true
    }

    //Sms receiver
    _receiver = SMSReceiver(object: OnSmsReceiveListener {
      override fun onReceive(sms: SmsMessage) {
        _result?.success(sms.displayMessageBody)
      }
    })
  }


  override fun onMethodCall(call: MethodCall, result: Result) {
    _result = result
    if (call.method == "startListeningReceiver") {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(registrar.activeContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
          returnResult()
        }
        else{
          ActivityCompat.requestPermissions(registrar.activity(), arrayOf(Manifest.permission.RECEIVE_SMS),1)
        }
      }
      else{
        returnResult()
      }
    }
    else if (call.method == "stopListeningReceiver") {
      try {
        registrar.activeContext().unregisterReceiver(_receiver)
      }catch (ex:Exception){
        print(ex)
      }
    }
    else {
      result.notImplemented()
    }
  }

  private fun returnResult() {
    try {
      registrar.activeContext().registerReceiver(_receiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }catch (e:Exception){
      print(e)
    }
  }
}


class SMSReceiver(var smsReceiveListener:OnSmsReceiveListener): BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    try {
      val bundle = intent?.extras
      if (bundle != null) {

        val pdusObj = bundle.get("pdus") as Array<*>
        val currentMessage:SmsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          Telephony.Sms.Intents.getMessagesFromIntent(intent)[0]
        }else
        {
          SmsMessage.createFromPdu(pdusObj[0] as ByteArray)
        }

        smsReceiveListener.onReceive(currentMessage)
      } //
    }catch (e:Exception){
      print(e)
    }
  }
}

interface OnSmsReceiveListener {
  fun onReceive(sms: SmsMessage)
}