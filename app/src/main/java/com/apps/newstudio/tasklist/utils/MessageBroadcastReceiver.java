package com.apps.newstudio.tasklist.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.storage.models.TaskEntity;
import com.apps.newstudio.tasklist.ui.activities.SplashActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageBroadcastReceiver extends BroadcastReceiver {

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.
     *
     * @param context Context object
     * @param intent  Intent object
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        List<TaskEntity> taskList = DataManager.getInstance().getDatabaseManager()
                .getTasksUsingDateAndTime(day, month, year, hour, minute);
        if (taskList.size() > 0) {
            for (int i = 0; i < taskList.size(); i++) {
                TaskEntity task = taskList.get(i);
                if (DataManager.getInstance().getPreferenceManager()
                        .loadInt(ConstantsManager.TOTAL_ALARM_STATE_FLAG, ConstantsManager.TOTAL_ALARM_STATE_ON)
                        == ConstantsManager.TOTAL_ALARM_STATE_ON
                        && task.getAlarmState() == ConstantsManager.ALARM_FLAG_ON) {

                    Intent intent_act = new Intent(TaskListApplication.getContext(), SplashActivity.class);
                    PendingIntent pIntent_act = PendingIntent.getActivity(TaskListApplication.getContext(), 1, intent_act, 0);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(TaskListApplication.getContext(),
                                    ConstantsManager.NOTIFICATION_CHANNEL_ID);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBuilder.setSmallIcon(R.drawable.ico_small);
                        mBuilder.setColor(TaskListApplication.getContext().getResources().getColor(R.color.colorPrimary));
                    } else {
                        mBuilder.setSmallIcon(R.drawable.ico_circle);
                    }

                    String contentText = "";
                    if (task.getHourBeginning() != -1) {
                        contentText = AuxiliaryFunctions.convertToTimeFormat(task.getHourBeginning(), task.getMinuteBeginning());
                    } else {
                        switch (DataManager.getInstance().getPreferenceManager().getLanguage()) {
                            case ConstantsManager.LANGUAGE_ENG:
                                contentText = TaskListApplication.getContext().getResources()
                                        .getStringArray(R.array.categories_eng)[task.getCategoryId()];
                                break;
                            case ConstantsManager.LANGUAGE_RUS:
                                contentText = TaskListApplication.getContext().getResources()
                                        .getStringArray(R.array.categories_rus)[task.getCategoryId()];
                                break;
                            case ConstantsManager.LANGUAGE_UKR:
                                contentText = TaskListApplication.getContext().getResources()
                                        .getStringArray(R.array.categories_ukr)[task.getCategoryId()];
                                break;
                        }
                    }
                    if (task.getHourEnd() != -1) {
                        contentText = contentText + " - " + AuxiliaryFunctions.convertToTimeFormat(task.getHourEnd(), task.getMinuteEnd());
                    }

                    mBuilder.setContentTitle(task.getTitle())
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setAutoCancel(true)
                            .setTicker(TaskListApplication.getContext().getString(R.string.app_name))
                            .setContentIntent(pIntent_act)
                            .setContentText(contentText);

                    NotificationManager notificationManager = (NotificationManager) TaskListApplication.getContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(Integer.parseInt("" + task.getId()), mBuilder.build());
                }
            }
        }
    }
}
