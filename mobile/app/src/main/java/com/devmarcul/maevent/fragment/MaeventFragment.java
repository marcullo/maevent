package com.devmarcul.maevent.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.interfaces.ViewScroller;

public class MaeventFragment extends Fragment implements ViewScroller {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_maevent, container, false);
        return view;
    }

    @Override
    public NestedScrollView getScrollView() {
        NestedScrollView view = this.view.findViewById(R.id.sv_main_maevent);
        return view;
    }
}
