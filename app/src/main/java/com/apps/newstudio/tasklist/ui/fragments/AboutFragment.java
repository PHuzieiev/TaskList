package com.apps.newstudio.tasklist.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfInfoList;
import com.apps.newstudio.tasklist.data.adapters.DataForInfoListItem;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AboutFragment extends Fragment {

    @BindView(R.id.fragment_list)
    public RecyclerView mRecyclerView;

    private AdapterOfInfoList mAdapter;
    private Unbinder mUnbinder;

    private String[] mTitles, mText;

    /**
     * Creates View object of fragment
     *
     * @param inflater           LayoutInflater object
     * @param container          ViewGroup object
     * @param savedInstanceState Bundle object
     * @return inflated View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        setLang();
        createList();
        return view;
    }

    /**
     * Prepares data for main list
     *
     * @return DataForInfoListItem list object
     */
    private List<DataForInfoListItem> getDataForList() {
        List<DataForInfoListItem> data = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            DataForInfoListItem dataParent=new DataForInfoListItem(i + 1, mTitles[i], null);
            dataParent.setOpen(true);
            List<DataForInfoListItem> dataChild = new ArrayList<>();
            dataChild.add(new DataForInfoListItem(0, mText[i], null));
            dataParent.setChildList(dataChild);
            data.add(dataParent);
            data.addAll(dataChild);
        }
        return data;
    }

    /**
     * Creates main drop-down list
     */
    private void createList() {
        mAdapter = new AdapterOfInfoList(getDataForList(),
                new AdapterOfInfoList.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        List<DataForInfoListItem> data = mAdapter.getData();
                        if (data.get(position).getParentId() != 0) {
                            if (data.get(position).isOpen()) {
                                data.get(position).setOpen(false);
                                for (int i = 1; i <= data.get(position).getChildList().size(); i++) {
                                    data.remove(position + 1);
                                }
                                mAdapter.notifyItemRangeRemoved(position + 1, data.get(position).getChildList().size());
                                mAdapter.notifyItemChanged(position);
                            } else {
                                data.get(position).setOpen(true);
                                List<DataForInfoListItem> childData = data.get(position).getChildList();
                                data.addAll(position + 1, childData);
                                mAdapter.notifyItemRangeInserted(position + 1, childData.size());
                                mAdapter.notifyItemChanged(position);
                                mRecyclerView.scrollToPosition(position + 1);
                            }
                        }
                    }
                });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskListApplication.getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Does unbind process when Fragment object is on destroy stage
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * Performs text objects using language parameter
     */
    private void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mTitles = getResources().getStringArray(R.array.about_app_titles_eng);
                mText = getResources().getStringArray(R.array.about_app_text_eng);
            }

            @Override
            public void ukrLanguage() {
                mTitles = getResources().getStringArray(R.array.about_app_titles_ukr);
                mText = getResources().getStringArray(R.array.about_app_text_ukr);
            }

            @Override
            public void rusLanguage() {
                mTitles = getResources().getStringArray(R.array.about_app_titles_rus);
                mText = getResources().getStringArray(R.array.about_app_text_rus);
            }
        };
    }
}
