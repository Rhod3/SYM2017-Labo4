package ch.heigvd.iict.sym.sym_labo4;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.heigvd.iict.sym.wearcommon.Constants;

public class NotificationActivity extends AppCompatActivity {

    private Button pendingIntent;
    private Button action;
    private Button wearableOnly;

    private NotificationCompat.Builder notificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (getIntent() != null)
            onNewIntent(getIntent());

        pendingIntent = findViewById(R.id.notif_btn_pending);
        action = findViewById(R.id.notif_btn_action);
        wearableOnly = findViewById(R.id.notif_btn_wearable_only);

        pendingIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingIntentNotification();
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionNotification();
            }
        });

        wearableOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wearableOnlyNotification();
            }
        });
    }

    private void pendingIntentNotification() {
        notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.pending_id))
                .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                .setContentTitle(getString(R.string.pending_title))
                .setContentText(getString(R.string.pending_text))
                .setContentIntent(createPendingIntent(0, getString(R.string.toast_phone)));

        notify(notificationBuilder.build());
    }

    private void actionNotification() {
        notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.action_id))
                .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                .setContentTitle(getString(R.string.action_title))
                .setContentText(getString(R.string.action_text))
                .addAction(R.drawable.ic_lightbulb_on_white_18dp, getString(R.string.action_button_text),
                        createPendingIntent(0, getString(R.string.toast_phone)));

        notify(notificationBuilder.build());
    }

    private void wearableOnlyNotification() {
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender().setHintHideIcon(true);

        notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.wearable_only_id))
                .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                .setContentTitle(getString(R.string.wearable_only_title))
                .setContentText(getString(R.string.wearable_only_text))
                .extend(wearableExtender)
                .addAction(R.drawable.ic_lightbulb_on_white_18dp, getString(R.string.action_button_text),
                        createPendingIntent(0, getString(R.string.toast_phone)));

        notify(notificationBuilder.build());
    }

    private void notify(Notification notification) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);
    }

    /*
     *  Code fourni pour les PendingIntent
     */

    /*
     *  Method called by system when a new Intent is received
     *  Display a toast with a message if the Intent is generated by
     *  createPendingIntent method.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) return;
        if (Constants.MY_PENDING_INTENT_ACTION.equals(intent.getAction()))
            Toast.makeText(this, "" + intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
    }

    /**
     * Method used to create a PendingIntent with the specified message
     * The intent will start a new activity Instance or bring to front an existing one.
     * See parentActivityName and launchMode options in Manifest
     * See https://developer.android.com/training/notify-user/navigation.html for TaskStackBuilder
     *
     * @param requestCode The request code
     * @param message     The message
     * @return The pending Intent
     */
    private PendingIntent createPendingIntent(int requestCode, String message) {
        Intent myIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
        myIntent.setAction(Constants.MY_PENDING_INTENT_ACTION);
        myIntent.putExtra("msg", message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(myIntent);

        return stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
