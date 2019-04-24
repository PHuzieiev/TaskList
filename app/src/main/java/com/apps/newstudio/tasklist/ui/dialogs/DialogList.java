package com.apps.newstudio.tasklist.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfDialogList;
import com.apps.newstudio.tasklist.data.adapters.DataForDialogListItem;
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

    public DialogList(Context context, String title,
                      List<DataForDialogListItem> data, AdapterOfDialogList.OnItemClickListener click,
                      int position, Integer[] drawable) {
        mContext = context;
        mTitle = title;
        mData = data;
        mClick = click;
        mDrawable = drawable;
        createDialog(position);
    }

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
        mAdapter = new AdapterOfDialogList(mData, mClick,mDrawable);
        list.setAdapter(mAdapter);
        list.scrollToPosition(position);
        mDialog.show();
    }


    public void updateList(List<DataForDialogListItem> data) {
        mData = data;
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
    }

    public Dialog getDialog() {
        return mDialog;
    }


}
