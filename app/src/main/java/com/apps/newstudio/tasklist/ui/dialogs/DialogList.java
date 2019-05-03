package com.apps.newstudio.tasklist.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfDialogList;
import com.apps.newstudio.tasklist.data.adapters.DataForDialogListItem;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.List;

public class DialogList {

    private Context mContext;
    private String mTitle;
    private List<DataForDialogListItem> mData;
    private AdapterOfDialogList.OnItemClickListener mClick;
    private Integer[] mDrawable;

    private Dialog mDialog;
    private AdapterOfDialogList mAdapter;
    private int mType;

    /**
     * Constructor for DialogList object
     * @param context Context object
     * @param title title for Dialog object
     * @param data main data for DialogList object
     * @param click action for items of list
     * @param position position of chosen item
     * @param drawable image for items
     * @param type type of DialogList object
     */
    public DialogList(Context context, String title,
                      List<DataForDialogListItem> data, AdapterOfDialogList.OnItemClickListener click,
                      int position, Integer[] drawable, int type) {
        mContext = context;
        mTitle = title;
        mData = data;
        mClick = click;
        mDrawable = drawable;
        mType = type;
        createDialog(position);
    }

    /**
     * Creates Dialog object and sets all components of Dialog object
     * @param position position of chosen item of list
     */
    private void createDialog(int position) {
        mDialog = new Dialog(mContext);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
        mDialog.getWindow().setBackgroundDrawable(drawable);
        mDialog.setContentView(R.layout.dialog_list);
        ((TextView) mDialog.getWindow().findViewById(R.id.dialog_list_title)).setText(mTitle);
        RecyclerView list = mDialog.getWindow().findViewById(R.id.dialog_list_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskListApplication.getContext());
        list.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterOfDialogList(mData, mClick, mDrawable);
        list.setAdapter(mAdapter);
        list.scrollToPosition(position);
        if (mType == ConstantsManager.DIALOG_LIST_TYPE_ONE) {
            ((LinearLayout) mDialog.getWindow().findViewById(R.id.dialog_list_header_layout))
                    .removeView(mDialog.getWindow().findViewById(R.id.dialog_list_left));
            ((LinearLayout) mDialog.getWindow().findViewById(R.id.dialog_list_header_layout))
                    .removeView(mDialog.getWindow().findViewById(R.id.dialog_list_right));
            int padding = TaskListApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.spacing_small_16dp);
            mDialog.getWindow().findViewById(R.id.dialog_list_title).setPadding(padding, 0, padding, 0);
        } else {
            mDialog.getWindow().findViewById(R.id.dialog_list_left).setOnClickListener(mOnClickListenerLeftRight);
            mDialog.getWindow().findViewById(R.id.dialog_list_right).setOnClickListener(mOnClickListenerLeftRight);
        }
        mDialog.show();
    }

    /**
     * Sets previous or next year if type of DialogList object is ConstantsManager.DIALOG_LIST_TYPE_ONE
     */
    private View.OnClickListener mOnClickListenerLeftRight = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_list_left:
                    mTitle = "" + (Integer.parseInt(mTitle) - 1);
                    break;
                case R.id.dialog_list_right:
                    mTitle = "" + (Integer.parseInt(mTitle) + 1);
                    break;
            }
            ((TextView) mDialog.getWindow().findViewById(R.id.dialog_list_title)).setText(mTitle);
        }
    };

    /**
     * Updates list of DialogList object
     * @param data new data for list
     */
    public void updateList(List<DataForDialogListItem> data) {
        mData = data;
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Getter for Dialog object
     * @return Dialog object
     */
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * Getter for title of Dialog object
     * @return title of Dialog
     */
    public String getTitle() {
        return mTitle;
    }
}
