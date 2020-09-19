package org.apache.cordova.firebase;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.dmarc.cordovacall.HttpURLConnectionPost;

public class OnNotificationOpenReceiver extends BroadcastReceiver {

    // Called on tapping foreground notification
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage(context.getPackageName());
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle data = intent.getExtras();

            if (!data.containsKey("android_voip_messageType")) {
                if(!data.containsKey("messageType")) data.putString("messageType", "notification");
                data.putString("tap", FirebasePlugin.inBackground() ? "background" : "foreground");

                Log.d(FirebasePlugin.TAG, "OnNotificationOpenReceiver.onReceive(): "+data.toString());

                FirebasePlugin.sendMessage(data, context);

                launchIntent.putExtras(data);
                context.startActivity(launchIntent);
            } else {
                // Check Action
                String _action = intent.getAction();
                if (_action.equals("Confirm")) {
                    Bundle send_data = new Bundle();
                    send_data.putString("messageType", data.getString("android_voip_messageType"));
                    send_data.putString("tap", "background");
                    send_data.putString("action", data.getString("android_voip_action"));
                    send_data.putString("title", data.getString("android_voip_title"));
                    send_data.putString("session_id", data.getString("android_voip_session_id"));
                    send_data.putString("token", data.getString("android_voip_token"));
                    send_data.putString("pickup_url", data.getString("android_voip_callback_pickup_url"));
                    send_data.putString("hangup_url", data.getString("android_voip_callback_hangup_url"));
                    send_data.putString("reject_url", data.getString("android_voip_callback_reject_url"));
                    FirebasePlugin.sendMessage(send_data, context);
                    context.startActivity(launchIntent);
                } else {
                    String sessionid = data.getString("android_voip_session_id");
                    String reject_url = data.getString("android_voip_callback_reject_url");
                    new HttpURLConnectionPost().execute(reject_url, sessionid);
                }

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(data.getInt("notify_hashCode_id"));
            }
        }catch (Exception e){
            FirebasePlugin.handleExceptionWithoutContext(e);
        }
    }
}
