package com.juma.truckdoctor.js.fragment;


import android.app.Fragment;
import android.content.Context;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BackHandledFragment extends android.support.v4.app.Fragment {

    protected BackHandledInterface mBackHandledInterface;

    public BackHandledFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BackHandledInterface) {
            mBackHandledInterface = (BackHandledInterface) context;
            mBackHandledInterface.setSelectedFragment(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BackHandledInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBackHandledInterface = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBackHandledInterface.setSelectedFragment(this);
    }

    public abstract boolean onBackPressed();
}
