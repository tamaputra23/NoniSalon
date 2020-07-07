package com.example.nonisalon.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nonisalon.R;
import com.example.nonisalon.model.Cart;

public class CartHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView tvTotal, tvMenu, tvPrice,btnRemove;
    public CartHolder(View itemView) {
        super(itemView);
        mView = itemView;
        btnRemove = mView.findViewById(R.id.tv_remove);
        tvMenu = mView.findViewById(R.id.tv_holdertreatmen);
        tvPrice = mView.findViewById(R.id.tv_holderprice);
        tvTotal = mView.findViewById(R.id.tv_total);

    }
    public void bindToPost(Cart cart, View.OnClickListener removeClickListener){
        tvMenu.setText(cart.NameTreatment);
        tvPrice.setText("Rp " + cart.price);
        tvTotal.setText(cart.total + "x");
        btnRemove.setOnClickListener(removeClickListener);
    }
}
