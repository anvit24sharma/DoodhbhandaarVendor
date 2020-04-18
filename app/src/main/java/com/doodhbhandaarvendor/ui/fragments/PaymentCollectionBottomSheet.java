package com.doodhbhandaarvendor.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doodhbhandaarvendor.R;
import com.doodhbhandaarvendor.model.ProductModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PaymentCollectionBottomSheet extends BottomSheetDialogFragment {

    private Button everyDay,everyWeek,every15Day,every30Day,every45Day;
    PaymentCollectionListner paymentCollectionListner;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_collection_date_bottom_sheet,container,false);
        everyDay=view.findViewById(R.id.checkBox);
        every15Day=view.findViewById(R.id.checkBox2);
        every30Day=view.findViewById(R.id.checkBox3);
        every45Day=view.findViewById(R.id.checkBox4);
        everyWeek=view.findViewById(R.id.checkBox5);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ProductModel productModel = getArguments().getParcelable("productModel");
        everyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "Daily";
                paymentCollectionListner.onPaymentDaySelected(type,productModel);
            }
        });
        everyWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "Weekly";
                paymentCollectionListner.onPaymentDaySelected(type,productModel);
            }
        });
        every15Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "In 15 Day";
                paymentCollectionListner.onPaymentDaySelected(type,productModel);
            }
        });
        every30Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "In 30 Day";
                paymentCollectionListner.onPaymentDaySelected(type,productModel);
            }
        });
        every45Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "In 45 Day";
                paymentCollectionListner.onPaymentDaySelected(type,productModel);
            }
        });
    }

    public interface PaymentCollectionListner{
        void onPaymentDaySelected(String string,ProductModel productModel);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        paymentCollectionListner=(PaymentCollectionListner) context;
    }
}
