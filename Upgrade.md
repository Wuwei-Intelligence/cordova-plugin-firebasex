

plugin.xml
> 相關FullscreenActivity的設定

<uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT" />

<activity android:name="org.apache.cordova.firebase.FullscreenActivity" android:label="@string/app_name" android:theme="@style/Theme.AppCompat.NoActionBar" android:showOnLockScreen="true" android:parentActivityName=".MainActivity"></activity>

<source-file src="src/android/FullscreenActivity.java" target-dir="src/org/apache/cordova/firebase"/>
<source-file src="src/android/activity_fullscreen.xml" target-dir="res/layout"/>
<source-file src="src/android/drawable/cordova_plugin_firebase_btn_cancel.xml" target-dir="res/drawable"/>
<source-file src="src/android/drawable/cordova_plugin_firebase_btn_submit.xml" target-dir="res/drawable"/>
<source-file src="src/android/drawable-anydpi/ic_action_cordova_plugin_firebase_close.xml" target-dir="res/drawable-anydpi"/>
<source-file src="src/android/drawable-anydpi/ic_action_cordova_plugin_firebase_check.xml" target-dir="res/drawable-anydpi"/>
