package com.example.nonisalon.adapter;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nonisalon.R;
import com.example.nonisalon.model.Payment;

public class HistoryHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView tvTotal, tvMenu, tvDate;
    ImageView iv_history;
    public HistoryHolder(View itemView) {
        super(itemView);
        mView = itemView;
        tvMenu = mView.findViewById(R.id.tv_holdertreatmenthistory);
        tvDate = mView.findViewById(R.id.tv_holderdatehistory);
        tvTotal = mView.findViewById(R.id.tv_totalhistory);
        iv_history = mView.findViewById(R.id.iv_history);
    }
    public void bindToPost(Payment payment, View.OnClickListener removeClickListener){
        tvMenu.setText(payment.NameTreatment);
        tvDate.setText(payment.tanggal);
        tvTotal.setText(payment.total +" Item | Rp " + payment.price);
        if(payment.treatment.equals("VL")){
            iv_history.setImageResource(R.drawable.volume_lash);
        }
        else if(payment.treatment.equals("KL")){
            iv_history.setImageResource(R.drawable.korean_lash);
        }
        else if (payment.treatment.equals("RL")){
            iv_history.setImageResource(R.drawable.retouch_lash);
        }
    }
}
