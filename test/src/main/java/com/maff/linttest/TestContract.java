package com.maff.linttest;

/**
 * Created by mshtutman on 08/01/2018.
 */

public interface TestContract
{
    interface View
    {
        void onUfoLanded();
        void showUfoView();
        void populateUfoData(/* List<UfoData> data */);
        void hideUfoView();
    }

    interface Presenter
    {
        void onUfoClicked();
        void onAttach();
        void onDetach();
        void onStart();
        void onPause();
        int getAliensCount();
        boolean shouldShowUfo();
    }
}
