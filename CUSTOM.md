
# 修正紀錄
## Android VoIP通知
### 實作於 FirebasePluginMessagingService.java
- 藉由通知參數 notification_android_voip_action 與一般通知分開
- android_voip_callback_timestamp用來判斷幾秒內不重複接收VoIP通知
- 拒接透過 HttpURLConnectionPost() 依照參數 android_voip_session_id 與 android_voip_callback_reject_url
### Intent
- fullScreenIntent - 顯示滿版通知
- cancelIntent - 通知Action按鈕.拒接
- confirmIntent - 通知Action按鈕.接聽
### 通知Action
- 按鈕事件透過 OnNotificationOpenReceiver.java 接收

----
## Android 滿版畫面通知
### **包含檔案**
- FullscreenActivity.java - 滿版通知
- activity_fullscreen.xml - 滿版通知
- drawable/cordova_plugin_firebase_btn_cancel.xml - 拒接按鈕底圖
- drawable/cordova_plugin_firebase_btn_submit.xml - 接聽按鈕底圖
- drawable-anydpi/ic_action_cordova_plugin_firebase_close.xml - 拒接icon
- drawable-anydpi/ic_action_cordova_plugin_firebase_check.xml - 接聽icon
### **Plugin.xml**
寫入權限設定
````xml
<config-file target="AndroidManifest.xml" parent="/*">
    <!-- ... -->
    <uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT" />
</config-file>
````
寫入Activity設定
````xml
<config-file target="AndroidManifest.xml" parent="/manifest/application">
    <activity
        android:name="org.apache.cordova.firebase.FullscreenActivity"
        android:label="@string/title_activity_fullscreen"
        android:theme="@style/Theme.AppCompat.NoActionBar"
        android:showOnLockScreen="true"
        android:parentActivityName=".MainActivity"></activity>
</config-file>
````
放置檔案
````xml
<!-- FullscreenActivity -->
<source-file src="src/android/FullscreenActivity.java" target-dir="src/org/apache/cordova/firebase"/>
<source-file src="src/android/activity_fullscreen.xml" target-dir="res/layout"/>
<source-file src="src/android/drawable/cordova_plugin_firebase_btn_cancel.xml" target-dir="res/drawable"/>
<source-file src="src/android/drawable/cordova_plugin_firebase_btn_submit.xml" target-dir="res/drawable"/>
<source-file src="src/android/drawable-anydpi/ic_action_cordova_plugin_firebase_close.xml" target-dir="res/drawable-anydpi"/>
<source-file src="src/android/drawable-anydpi/ic_action_cordova_plugin_firebase_check.xml" target-dir="res/drawable-anydpi"/>
````
### 實作於 FirebasePluginMessagingService.java:404
````
.setFullScreenIntent(fullScreenPendingIntent, true);
````
### 在build.gradle打開ViewBinding開關
````
android {
    // ...
    buildFeatures {
        viewBinding true
    }
}
````
----