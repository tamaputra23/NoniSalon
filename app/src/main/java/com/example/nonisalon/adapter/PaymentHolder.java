package com.example.nonisalon.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nonisalon.R;
import com.example.nonisalon.model.Payment;

public class PaymentHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView tvTotal, tvMenu, tvPrice;
    public PaymentHolder(View itemView) {
        super(itemView);
        mView = itemView;
        tvMenu = mView.findViewById(R.id.tv_holdertreatmentpayment);
        tvPrice = mView.findViewById(R.id.tv_holderpricepayment);
        tvTotal = mView.findViewById(R.id.tv_totalpayment);
    }
    public void bindToPost(Payment payment, View.OnClickListener removeClickListener){
        tvMenu.setText(payment.NameTreatment);
        tvPrice.setText("Rp " + payment.price);
        tvTotal.setText(payment.total + "x");
    }
}
