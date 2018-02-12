package com.hly.keyboarddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hly.keyboard.KeyBoardEditText;

/**
 * Created by hly on 2018/2/12.
 */

public class EditTextDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeyBoardEditText keyBoardEditText = view.findViewById(R.id.fragmentDialogEditText);
        keyBoardEditText.setWindow(getDialog().getWindow());
    }
}

