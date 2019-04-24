package com.apps.newstudio.tasklist.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.AdapterOfFragmentCategoriesList;
import com.apps.newstudio.tasklist.data.adapters.DataForCategoriesListItem;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.ui.activities.CategoryActivity;
import com.apps.newstudio.tasklist.ui.activities.MainActivity;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CategoriesFragment extends Fragment {

    @BindView(R.id.fragment_list)
    public RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private String mDeleteCategoryToast;
    private String[] mCategoriesTitle;
    private AdapterOfFragmentCategoriesList mAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        getLang();
        createList();
        return view;
    }

    /**
     * Does unbind process when Fragment object is on destroy stage
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    private List<DataForCategoriesListItem> prepareDataForList() {
        List<DataForCategoriesListItem> data = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            data.add(new DataForCategoriesListItem(i, mCategoriesTitle[i],
                    DataManager.getInstance().getDatabaseManager().getCountByCategoryId(i).size()));
        }
        return data;
    }

    private void createList() {
        mAdapter = new AdapterOfFragmentCategoriesList(prepareDataForList(),
                new AdapterOfFragmentCategoriesList.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(getActivity(), CategoryActivity.class);
                        intent.putExtra(ConstantsManager.TASK_CATEGORY_FLAG,mAdapter.getData().get(position).getId());
                        startActivityForResult(intent, ConstantsManager.TASK_REQUEST_CODE);
                    }
                }, new AdapterOfFragmentCategoriesList.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                DataManager.getInstance().getDatabaseManager().deleteTasksByCategory(mAdapter.getData().get(position).getId());
                updateList();
                ((MainActivity) getActivity())
                        .showToast(mDeleteCategoryToast, R.drawable.ic_delete_uncircle_white,
                                R.drawable.rectangle_for_toast_red, Toast.LENGTH_LONG);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskListApplication.getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateList() {
        mAdapter.setData(prepareDataForList());
        mAdapter.notifyDataSetChanged();
        ((MainActivity) getActivity()).updateNavigationView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateList();

    }

    private void getLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                mCategoriesTitle = getResources().getStringArray(R.array.categories_eng);
                mDeleteCategoryToast = getString(R.string.toast_clear_task_eng);
            }

            @Override
            public void ukrLanguage() {
                mCategoriesTitle = getResources().getStringArray(R.array.categories_ukr);
                mDeleteCategoryToast = getString(R.string.toast_clear_task_ukr);
            }

            @Override
            public void rusLanguage() {
                mCategoriesTitle = getResources().getStringArray(R.array.categories_rus);
                mDeleteCategoryToast = getString(R.string.toast_clear_task_rus);
            }
        };
    }

}
