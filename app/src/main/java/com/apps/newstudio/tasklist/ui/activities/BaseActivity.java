package com.apps.newstudio.tasklist.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newstudio.tasklist.R;

public class BaseActivity extends AppCompatActivity {

    public void showToast(String message, int ico, int background, int length) {
        int toastPadding = getResources().getDimensionPixelSize(R.dimen.spacing_smaller_8dp);
        Toast toast = Toast.makeText(this, message, length);
        View viewToast = toast.getView();
        TextView textView = (TextView) ((LinearLayout) viewToast).getChildAt(0);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setGravity(Gravity.CENTER);
        viewToast.setPadding(toastPadding, toastPadding, toastPadding, toastPadding);
        viewToast.setBackground(getResources().getDrawable(background));

        if(ico!=0) {
            ((LinearLayout) viewToast).removeAllViews();
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.small_size_24dp),
                    getResources().getDimensionPixelSize(R.dimen.small_size_24dp));
            imageView.setImageResource(ico);
            layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.spacing_smaller_4dp);
            imageView.setLayoutParams(layoutParams);
            ((LinearLayout) viewToast).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout) viewToast).setGravity(Gravity.CENTER_VERTICAL);
            ((LinearLayout) viewToast).addView(imageView);
            ((LinearLayout) viewToast).addView(textView);
        }
        toast.show();
    }


}
