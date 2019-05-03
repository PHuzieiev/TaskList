package com.apps.newstudio.tasklist.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;

public class DialogTwoButtons {

    private Context mContext;
    private String mTitle, mText;
    private String[] mButtonsTitles;
    private OnButtonClick mOnButtonClickOne, mOnButtonClickTwo;

    private Dialog mDialog;

    /**
     * Constructor for DialogTwoButtons object
     *
     * @param context          Context object
     * @param title            title for Dialog object
     * @param text             text for Dialog object
     * @param buttonsTitles    titles for Buttons object
     * @param onButtonClickOne action for first Button object
     * @param onButtonClickTwo action for second Button object
     */
    public DialogTwoButtons(Context context, String title, String text, String[] buttonsTitles,
                            OnButtonClick onButtonClickOne, OnButtonClick onButtonClickTwo) {
        mContext = context;
        mTitle = title;
        mText = text;
        mButtonsTitles = buttonsTitles;
        mOnButtonClickOne = onButtonClickOne;
        mOnButtonClickTwo = onButtonClickTwo;
        createDialog();
    }

    /**
     * Creates Dialog object and sets main components for this object
     */
    private void createDialog() {
        mDialog = new Dialog(mContext);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
        mDialog.getWindow().setBackgroundDrawable(drawable);
        mDialog.setContentView(R.layout.dialog_two_button);
        Button buttonOne = mDialog.getWindow().findViewById(R.id.dialog_button_one);
        Button buttonTwo = mDialog.getWindow().findViewById(R.id.dialog_button_two);
        buttonOne.setText(mButtonsTitles[0]);
        buttonTwo.setText(mButtonsTitles[1]);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                mOnButtonClickOne.function();
            }
        });
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                mOnButtonClickTwo.function();
            }
        });
        ((TextView) mDialog.getWindow().findViewById(R.id.dialog_title)).setText(mTitle);
        ((TextView) mDialog.getWindow().findViewById(R.id.dialog_text)).setText(mText);
        mDialog.show();

    }

    /**
     * Interface to perform action for Buttons objects
     */
    public interface OnButtonClick {
        void function();
    }

}
