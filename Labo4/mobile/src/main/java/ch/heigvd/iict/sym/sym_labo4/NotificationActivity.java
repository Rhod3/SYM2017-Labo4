package ch.heigvd.iict.sym.sym_labo4;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import ch.heigvd.iict.sym.wearcommon.Constants;

public class NotificationActivity extends AppCompatActivity {

    // Button to send a pending intent notification
    private Button pendingIntent;

    // Button to send notification with action buttons
    private Button action;

    // Button to send a notification with wearable only features
    private Button wearableOnly;

    //Notification builder used to build the notification
    private NotificationCompat.Builder notificationBuilder;

    // Notification manager used to send the notification
    NotificationManagerCompat notificationManager;

    /**
     * Called at the creation of the activity.
     * In this phase we initialise the buttons of the activity and bind
     * a OnClickListener to each one of them.
     *
     * @param savedInstanceState the previous saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (getIntent() != null)
            onNewIntent(getIntent());

        // Initialisation of the buttons
        pendingIntent = findViewById(R.id.notif_btn_pending);
        action = findViewById(R.id.notif_btn_action);
        wearableOnly = findViewById(R.id.notif_btn_wearable_only);

        // Initialisation of the notification manager
        notificationManager = NotificationManagerCompat.from(this);

        // Binding the click listeners to the buttons
        pendingIntent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyPendingIntent();
            }
        });

        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyActionButton();
            }
        });

        wearableOnly.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyWearableOnly();
            }
        });
    }

    /**
     * Creating and sending a pending intent notification.
     */
    private void notifyPendingIntent() {
        // Building the pending intent notification
        notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.pending_id))
                .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                .setContentTitle(getString(R.string.pending_title))
                .setContentText(getString(R.string.pending_text))
                .setContentIntent(createPendingIntent(0, getString(R.string.toast_phone)));

        // Sending the notification
        notificationManager.notify(1, notificationBuilder.build());
    }

    /**
     * Creating and sending a notification with an action button.
     */
    private void notifyActionButton() {
        // Building the notification with an action button
        notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.action_id))
                .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                .setContentTitle(getString(R.string.action_title))
                .setContentText(getString(R.string.action_text))
                .addAction(R.drawable.ic_lightbulb_on_white_18dp, getString(R.string.action_button_text),
                        createPendingIntent(0, getString(R.string.toast_phone)));

        // Sending the notification
        notificationManager.notify(1, notificationBuilder.build());
    }

    /**
     * Creating and sending a notification with wearable only features.
     */
    private void notifyWearableOnly() {
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender().setHintHideIcon(true);

        // Building the notification with wearable only features
        notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.wearable_only_id))
                .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                .setContentTitle(getString(R.string.wearable_only_title))
                .setContentText(getString(R.string.wearable_only_text))
                .extend(wearableExtender)
                .addAction(R.drawable.ic_lightbulb_on_white_18dp, getString(R.string.action_button_text),
                        createPendingIntent(0, getString(R.string.toast_phone)));

        // Sending the notification
        notificationManager.notify(1, notificationBuilder.build());
    }

    /*
    --------------------------
    Fourni dans le template
    --------------------------
     */

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
