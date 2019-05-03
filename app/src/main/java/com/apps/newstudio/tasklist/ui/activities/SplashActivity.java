package com.apps.newstudio.tasklist.ui.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfDialogList.OnItemClickListener;
import com.apps.newstudio.tasklist.data.adapters.DataForDialogListItem;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.ui.dialogs.DialogList;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.MessageBroadcastReceiver;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private String mDialogListTitle;
    private DialogList mDialogList;
    private List<DataForDialogListItem> mDataForDialogList;

    /**
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState Bundle object of saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setMessageManager();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!ConstantsManager.LANGUAGE_CHOSEN.equals(DataManager.getInstance().getPreferenceManager()
                        .loadString(ConstantsManager.LANGUAGE_CHOSEN_KEY, null))) {
                    DataManager.getInstance().getPreferenceManager().
                            saveString(ConstantsManager.LANGUAGE_CHOSEN_KEY, ConstantsManager.LANGUAGE_CHOSEN);
                    setLang();
                } else {
                    startNewActivity();
                }
            }
        }, 1500);
    }

    /**
     * Opens special dialog to chose language
     */
    private void setLang() {
        getDataForDialogList();
        mDialogList = new DialogList(this, mDialogListTitle, mDataForDialogList, new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        DataManager.getInstance().getPreferenceManager().
                                saveString(ConstantsManager.LANGUAGE_KEY, ConstantsManager.LANGUAGE_ENG);
                        break;
                    case 1:
                        DataManager.getInstance().getPreferenceManager().
                                saveString(ConstantsManager.LANGUAGE_KEY, ConstantsManager.LANGUAGE_UKR);
                        break;
                    case 2:
                        DataManager.getInstance().getPreferenceManager().
                                saveString(ConstantsManager.LANGUAGE_KEY, ConstantsManager.LANGUAGE_RUS);
                        break;
                }
                mDialogList.getDialog().dismiss();
            }
        }, 0, new Integer[]{R.drawable.ic_radio_button_checked, R.drawable.ic_radio_button_unchecked}
                , ConstantsManager.DIALOG_LIST_TYPE_ONE);
        mDialogList.getDialog().findViewById(R.id.dialog_list_main_layout)
                .setLayoutParams(new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.huge_size_300dp),
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        mDialogList.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startNewActivity();
            }
        });
    }

    /**
     * Opens MainActivity
     */
    private void startNewActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Creates alarm manager object
     */
    public void setMessageManager() {
        try {
            Context context = TaskListApplication.getContext();
            Intent alarm_intent = new Intent(context, MessageBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarm_intent, 0);
            AlarmManager alarmManager_not = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager_not.cancel(pendingIntent);
            alarmManager_not.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);

            NotificationManager notificationManager = (NotificationManager) TaskListApplication.getContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (Exception ignored) {
        }
    }

    /**
     * Performs data forDialogList object where user will chose language
     */
    private void getDataForDialogList() {
        mDataForDialogList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mDataForDialogList.add(new DataForDialogListItem(i,
                    getResources().getStringArray(R.array.language_options)[i], false));
        }
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mDialogListTitle = getString(R.string.language_eng);
                mDataForDialogList.get(0).setChosen(true);
            }

            @Override
            public void ukrLanguage() {
                mDialogListTitle = getString(R.string.language_ukr);
                mDataForDialogList.get(1).setChosen(true);
            }

            @Override
            public void rusLanguage() {
                mDialogListTitle = getString(R.string.language_rus);
                mDataForDialogList.get(2).setChosen(true);
            }
        };
    }
}
