package org.apache.cordova.firebase;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

import city.waffle.user.dev.databinding.ActivityFullscreenBinding;

import com.dmarc.cordovacall.HttpURLConnectionPost;

public class FullscreenActivity extends AppCompatActivity {

    private ActivityFullscreenBinding binding;

    // 接收廣播
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle _msg = intent.getExtras();
            if (_msg != null && _msg.getString("action") != null && _msg.getString("action").equals("CutOffCall")) {
                finishAndRemoveTask();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // 監聽廣播
        LocalBroadcastManager.getInstance(this).registerReceiver(mConnReceiver, new IntentFilter("city.waffle.intercom.action.notification"));

        //
        Bundle _notificationSendBundle = getIntent().getExtras();
        String _title = _notificationSendBundle.getString("android_voip_title");
        if (_title != null) {
            binding.callTitle.setText(_title);
        }
    }

    public void onPickUp(View view) {
        //
        Bundle _notificationSendBundle = getIntent().getExtras();
        Bundle send_data = new Bundle();
        send_data.putString("messageType", _notificationSendBundle.getString("android_voip_messageType"));
        send_data.putString("tap", "background");
        send_data.putString("action", _notificationSendBundle.getString("android_voip_action"));
        send_data.putString("title", _notificationSendBundle.getString("android_voip_title"));
        send_data.putString("session_id", _notificationSendBundle.getString("android_voip_session_id"));
        send_data.putString("token", _notificationSendBundle.getString("android_voip_token"));
        send_data.putString("pickup_url", _notificationSendBundle.getString("android_voip_callback_pickup_url"));
        send_data.putString("hangup_url", _notificationSendBundle.getString("android_voip_callback_hangup_url"));
        send_data.putString("reject_url", _notificationSendBundle.getString("android_voip_callback_reject_url"));
        FirebasePlugin.sendMessage(send_data, null);

        this.cancelNotification();

        // 透過 ActivityManager - 檢查 city.waffle.user.dev.MainActivity 是不是存在
        ActivityManager _activityManager = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> taskList = _activityManager.getRunningTasks(10);
        ActivityManager.RunningTaskInfo task = taskList.get(0);
        if (!(
            task != null &&
                task.baseActivity.getClassName().equals("city.waffle.user.dev.MainActivity") &&
                task.topActivity.getClassName().equals(this.getClass().getName())
        )) {
            Intent _mainActivity = new Intent(this, city.waffle.user.dev.MainActivity.class);
            startActivity(_mainActivity);
        }
        finish();
    }

    public void onCutOffCall(View view) {
        Bundle _notificationSendBundle = getIntent().getExtras();
        String sessionid = _notificationSendBundle.getString("android_voip_session_id");
        String reject_url = _notificationSendBundle.getString("android_voip_callback_reject_url");
        new HttpURLConnectionPost().execute(reject_url, sessionid);

        this.cancelNotification();
    }

    private void cancelNotification() {
        // 取消通知
        Bundle _notificationSendBundle = getIntent().getExtras();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel("WaffleIntercomIncommingCall", _notificationSendBundle.getInt("notify_hashCode_id"));
    }
}
