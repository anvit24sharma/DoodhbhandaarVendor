package com.doodhbhandaarvendor.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doodhbhandaarvendor.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Addaddress extends BottomSheetDialogFragment {
    @Nullable
    Button btnAddress;
    EditText etAddress;
    BottomSheetListner bottomSheetListner;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_addadress,container,false);
        btnAddress=view.findViewById(R.id.btn_addAddress);
        etAddress=view.findViewById(R.id.et_addAddress);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = etAddress.getText().toString();
                bottomSheetListner.onButtonClicked(address);
            }
        });
    }

    public interface BottomSheetListner{
        void onButtonClicked(String string);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        bottomSheetListner=(BottomSheetListner)context;
    }
}
