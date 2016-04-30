package bdonotifier.studiau.com.bdonotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Daniel Au on 4/24/2016.
 *
 * We pop a notification into the status bar for the user to click on.
 * When the user clicks the notification, a new activity is opened.
 */
public class NotifyService extends Service {

    // Class for clients to access.
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 795;
    // Name of an intent extra we can use to identify if this service was started
    // to create a notification.
    public static final String INTENT_NOTIFY = "bdonotifier.studiau.com.bdonotifier.INTENT_NOTIFY";
    // The system notification manager.
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "received start is " + startId + ": " + intent);

        // If this service was started by our AlarmTask intent then we want to show the notification.
        if (intent.getBooleanExtra(INTENT_NOTIFY, false)) {
            showNotification();
        }

        // We don't care if this service is stopped as we have already delivered our notification.
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new ServiceBinder();

    // Creates a notification and shows it in the OS drag-down status bar.
    private void showNotification() {
        // This is the title of the notification.
        CharSequence title = "Alarm!!";
        // This is the icon to use on the notification.
        //int icon = R.drawable.ic_dialog_alert;
        // This is the scrolling text of the notification.
        CharSequence text = "Your notification time is upon us.";
        Toast.makeText(this, title,Toast.LENGTH_LONG).show();
        // What time to show on the notification.
        long time = System.currentTimeMillis();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.android_white)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setLights(Color.GREEN, 500, 500)
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app.
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensure that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(NOTIFICATION, mBuilder.build());

        // Stop the service when we are finished.
        stopSelf();
    }
}
