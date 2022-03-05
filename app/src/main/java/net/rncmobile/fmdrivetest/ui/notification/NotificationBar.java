package net.rncmobile.fmdrivetest.ui.notification;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.ui.main.MainActivity;

import java.util.Objects;

/**
 * Created by cedric_f25 on 16.06.2017.
 */

public class NotificationBar {
    private static final String TAG = "NotificationBar";

    private static volatile NotificationBar instance = null;

    private NotificationManager mNotificationManager;
    private Notification.Builder notificationBuilder;
    public Notification notification;

    private Context context;

    public static NotificationBar getInstance() {
        synchronized (NotificationBar.class) {
            if (NotificationBar.instance == null) {
                NotificationBar.instance = new NotificationBar();
            }
        }
        return NotificationBar.instance;
    }

    private static final int notifyID = 1;

    public void run(Context context) {
        try {
            this.context = context;
            // Retrieve MainActivity intent
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent mainActivityIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            String channelId = "default";
            String title = context.getString(R.string.app_name);
            String message = "toto";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(message);
                channel.enableLights(false);
                channel.setVibrationPattern(new long[]{0});
                channel.enableVibration(true);
                channel.setShowBadge(true);
                mNotificationManager.createNotificationChannel(channel);

                notificationBuilder = new Notification.Builder(context, channelId);
                notificationBuilder.setStyle(new Notification.MediaStyle());
            } else
                notificationBuilder = new Notification.Builder(context);

            notification = notificationBuilder.build();

            mNotificationManager.notify(notifyID, notification);
        } catch (Exception e) {
            Log.d(TAG, "Erreur notification " + e.toString());
        }
    }

    public void refresh(Context context, IMyCell cell) {
        if (notificationBuilder != null && notification != null && mNotificationManager != null) {
            if (cell != null && cell.getLcid() > 0) {
                int icNotification = R.drawable.ic_iti;

                if (cell.isRightMaskFreeMobile())
                    icNotification = R.drawable.ic_rp;

                String contentTitle = "-";
                String contentText = "-";

                if (cell.getTech() == -1)
                    contentTitle = context.getString(R.string.planeMode);
                else if (cell.getTech() == 2) {
                    contentTitle = context.getString(R.string.notifContentTitle2G,
                            cell.getCid(), cell.getXac(), cell.getMainSignal());
                } else {
                    if(cell.getFreq() != null && cell.isRightMaskFreeMobile()) {
                        // Icon by Freq
                        switch (cell.getFreq()) {
                            case "900":
                                icNotification = R.drawable.ic_rp_900;
                                break;
                            case "2100":
                                icNotification = R.drawable.ic_rp_2100;
                                break;
                            case "700":
                                icNotification = R.drawable.ic_rp_700;
                                break;
                            case "800":
                                icNotification = R.drawable.ic_rp_800;
                                break;
                            case "1800":
                                icNotification = R.drawable.ic_rp_1800;
                                break;
                            case "2600":
                                icNotification = R.drawable.ic_rp_2600;
                                break;
                        }
/*
                        if (!cell.isAuthorizedNetwork())
                            icNotification = R.drawable.ic_iti;
*/
                        if (cell.getTech() == 2)
                            contentTitle = context.getString(R.string.notifContentTitle2G,
                                    cell.getCid(), cell.getXac(), cell.getMainSignal());

                        if (cell.getTech() == 3)
                            contentTitle = context.getString(R.string.notifContentTitle3G,
                                    cell.getRnc(), cell.getCid(), cell.getMainSignal());

                        if (cell.getTech() == 4)
                            contentTitle = context.getString(R.string.notifContentTitle4G,
                                    cell.getRnc(), cell.getCid(),
                                    cell.getMainSignal(),
                                    cell.getSnr());

                        if (cell.getTechDetect() == 45)
                            contentTitle = context.getString(R.string.notifContentTitle45G,
                                    cell.getRnc(), cell.getCid(),
                                    cell.getMainSignal(),
                                    cell.getSnr());
                    } else contentTitle = "-";

                }

                notificationBuilder.setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setSmallIcon(icNotification);
            } else {
                notificationBuilder.setContentTitle(context.getText(R.string.noConnection))
                        .setContentText(context.getText(R.string.noConnection));
            }

            notification = notificationBuilder.build();
            mNotificationManager.notify(notifyID, notification);
        }
    }

    public void stop() {
        try {
            mNotificationManager.cancel(notifyID);
            notification = null;
            ((NotificationManager) Objects.requireNonNull(context
                    .getSystemService(NOTIFICATION_SERVICE)))
                    .cancelAll();
        } catch (Exception e)  {
            Log.d(TAG, "Erreur notification: " + e.toString());
        }
    }
}
