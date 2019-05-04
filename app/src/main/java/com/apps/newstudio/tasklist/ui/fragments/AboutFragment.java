package com.apps.newstudio.tasklist.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.managers.LanguageManager;

public class AboutFragment extends Fragment {

    private View mView;

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
        mView = inflater.inflate(R.layout.fragment_about, container, false);
        setLang();
        return mView;
    }

    /**
     * Performs text objects using language parameter
     */
    private void setLang() {
        new LanguageManager() {
            @Override
            public void engLanguage() {
                ((TextView) mView.findViewById(R.id.about_text)).setText(getString(R.string.about_app_eng));
            }

            @Override
            public void ukrLanguage() {
                ((TextView) mView.findViewById(R.id.about_text)).setText(getString(R.string.about_app_ukr));
            }

            @Override
            public void rusLanguage() {
                ((TextView) mView.findViewById(R.id.about_text)).setText(getString(R.string.about_app_rus));
            }
        };
    }


}
