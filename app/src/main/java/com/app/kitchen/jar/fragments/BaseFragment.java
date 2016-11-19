package com.app.kitchen.jar.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Lalit T. Poptani on 10/27/2016.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    abstract void initComponents(View view);

    abstract void addListeners();
}
