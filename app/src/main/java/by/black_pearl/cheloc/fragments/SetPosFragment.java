package by.black_pearl.cheloc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.black_pearl.cheloc.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetPosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetPosFragment extends Fragment {

    public SetPosFragment() {
    }

    public static SetPosFragment newInstance() {
        SetPosFragment fragment = new SetPosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_pos, container, false);
    }

}
