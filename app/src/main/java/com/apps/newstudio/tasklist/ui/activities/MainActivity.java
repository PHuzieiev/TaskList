package com.apps.newstudio.tasklist.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.view.View;
import android.widget.TextView;


import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.managers.DataManager;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;
import com.apps.newstudio.tasklist.ui.fragments.AboutFragment;
import com.apps.newstudio.tasklist.ui.fragments.CategoriesFragment;
import com.apps.newstudio.tasklist.ui.fragments.DaysFragment;
import com.apps.newstudio.tasklist.ui.fragments.InfoFragment;
import com.apps.newstudio.tasklist.ui.fragments.SettingsFragment;
import com.apps.newstudio.tasklist.ui.fragments.StatisticFragment;
import com.apps.newstudio.tasklist.utils.ConstantsManager;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    public NavigationView mNavigationView;

    private int checkedItemId = 0;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private String mDayOrMonthFormat = "%02d", mSubtitleForNavigationView;
    private String[] mTitlesOfMonths;


    /**
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState Bundle object of saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getFragmentManager();
        setLang();
        if (savedInstanceState != null) {
            checkedItemId = savedInstanceState.getInt(ConstantsManager.SAVED_FRAGMENT_ID);
            mToolbar.setTitle(mNavigationView.getMenu().findItem(checkedItemId).getTitle());
            mNavigationView.setCheckedItem(checkedItemId);
        } else {
            checkItemOfNavigationView(R.id.item_tasks);
        }

        mNavigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedItemId = -1;
                checkItemOfNavigationView(R.id.item_tasks);
            }
        });
    }


    /**
     * Saves data in Bundle object
     *
     * @param outState object for saving data
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ConstantsManager.SAVED_FRAGMENT_ID, checkedItemId);
        super.onSaveInstanceState(outState);
    }

    /**
     * Closes app or drawer menu
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Action function of drawer menu
     *
     * @param item item of drawer menu which was selected
     * @return boolean value of result
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        checkItemOfNavigationView(item.getItemId());
        return true;
    }

    /**
     * Updates fragments in main layout
     *
     * @param id id value of chosen item of drawer menu
     */
    public void checkItemOfNavigationView(int id) {
        updateNavigationView();
        if (id != checkedItemId) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            switch (id) {
                case R.id.item_tasks:
                    mFragment = new DaysFragment();
                    break;
                case R.id.item_categories:
                    mFragment = new CategoriesFragment();
                    break;
                case R.id.item_statistic:
                    mFragment = new StatisticFragment();
                    break;
                case R.id.item_settings:
                    mFragment = new SettingsFragment();
                    break;
                case R.id.item_help:
                    mFragment = new InfoFragment();
                    break;
                case R.id.item_about:
                    mFragment = new AboutFragment();
                    break;
            }

            mToolbar.setTitle(mNavigationView.getMenu().findItem(id).getTitle());
            mNavigationView.setCheckedItem(id);
            checkedItemId = id;
            fragmentTransaction.replace(R.id.frame_for_fragments, mFragment);
            fragmentTransaction.commit();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Performs text objects using language parameter
     */
    public void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                Menu navMenu = mNavigationView.getMenu();
                navMenu.findItem(R.id.item_tasks).setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[0]);
                navMenu.findItem(R.id.item_categories).setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[1]);
                navMenu.findItem(R.id.item_statistic).setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[2]);
                navMenu.findItem(R.id.item_others).setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[3]);
                navMenu.findItem(R.id.item_settings).setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[4]);
                navMenu.findItem(R.id.item_help).setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[5]);
                navMenu.findItem(R.id.item_about).setTitle(getResources().getStringArray(R.array.drawer_menu_items_eng)[6]);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_eng);
                mDayOrMonthFormat = "%d";
                mSubtitleForNavigationView = getString(R.string.nav_header_title_two_eng);
            }

            @Override
            public void ukrLanguage() {
                Menu navMenu = mNavigationView.getMenu();
                navMenu.findItem(R.id.item_tasks).setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[0]);
                navMenu.findItem(R.id.item_categories).setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[1]);
                navMenu.findItem(R.id.item_statistic).setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[2]);
                navMenu.findItem(R.id.item_others).setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[3]);
                navMenu.findItem(R.id.item_settings).setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[4]);
                navMenu.findItem(R.id.item_help).setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[5]);
                navMenu.findItem(R.id.item_about).setTitle(getResources().getStringArray(R.array.drawer_menu_items_ukr)[6]);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_ukr);
                mSubtitleForNavigationView = getString(R.string.nav_header_title_two_ukr);
            }

            @Override
            public void rusLanguage() {
                Menu navMenu = mNavigationView.getMenu();
                navMenu.findItem(R.id.item_tasks).setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[0]);
                navMenu.findItem(R.id.item_categories).setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[1]);
                navMenu.findItem(R.id.item_statistic).setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[2]);
                navMenu.findItem(R.id.item_others).setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[3]);
                navMenu.findItem(R.id.item_settings).setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[4]);
                navMenu.findItem(R.id.item_help).setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[5]);
                navMenu.findItem(R.id.item_about).setTitle(getResources().getStringArray(R.array.drawer_menu_items_rus)[6]);
                mTitlesOfMonths = getResources().getStringArray(R.array.month_titles_rus);
                mSubtitleForNavigationView = getString(R.string.nav_header_title_two_rus);
            }
        };
        if (checkedItemId != 0) {
            mToolbar.setTitle(mNavigationView.getMenu().findItem(checkedItemId).getTitle());
        }
    }

    /**
     * Getter for context object of MainActivity
     *
     * @return Contect object
     */
    public Context getContext() {
        return this;
    }

    /**
     * Updates text objects of NavigationView object
     */
    public void updateNavigationView() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH), month = calendar.get(Calendar.MONTH) + 1,
                year = calendar.get(Calendar.YEAR);
        String date = String.format(Locale.ENGLISH, mDayOrMonthFormat + " ", day) +
                mTitlesOfMonths[month - 1] + " " + year;
        ((TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_title_tv))
                .setText(date);

        String tasksCount = mSubtitleForNavigationView +
                DataManager.getInstance().getDatabaseManager()
                        .getTasksUsingDate(day, month, year, ConstantsManager.STATE_FLAG_ACTIVE).size();
        ((TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_subtitle_tv))
                .setText(tasksCount);
    }
}
