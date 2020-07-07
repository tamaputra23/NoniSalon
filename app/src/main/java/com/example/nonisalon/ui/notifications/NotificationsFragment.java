package com.example.nonisalon.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nonisalon.BaseActivity;
import com.example.nonisalon.R;
import com.example.nonisalon.adapter.CartHolder;
import com.example.nonisalon.adapter.HistoryHolder;
import com.example.nonisalon.adapter.PaymentHolder;
import com.example.nonisalon.model.Cart;
import com.example.nonisalon.model.Payment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    RecyclerView recyclerView3;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    DatabaseReference mDatabase;
    SharedPreferences mSharedPref; //for saving sort settings
    LinearLayoutManager mLayoutManager; //for sorting
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final String userId = BaseActivity.getUid();
        recyclerView3 = root.findViewById(R.id.recyclerView3);
        myRef = FirebaseDatabase.getInstance().getReference();
        mSharedPref = this.getActivity().getSharedPreferences("SortSettings", Context.MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "newest"); //where if no settingsis selected newest will be default
        if (mSorting.equals("newest")) {
            mLayoutManager = new LinearLayoutManager(getContext());
            //this will load the items from bottom means newest first
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        } else if (mSorting.equals("oldest")) {
            mLayoutManager = new LinearLayoutManager(getContext());
            //this will load the items from bottom means oldest first
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }
        recyclerView3.setLayoutManager(mLayoutManager);
        recyclerView3.setHasFixedSize(false);
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        final String userId = BaseActivity.getUid();
        Query postsQuery = myRef.child("history").child(userId);
        FirebaseRecyclerAdapter<Payment, HistoryHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Payment, HistoryHolder>(
                        Payment.class,
                        R.layout.historyholder,
                        HistoryHolder.class,
                        postsQuery
                ) {
                    @Override
                    protected void populateViewHolder(HistoryHolder viewHolder, final Payment model, final int i) {
                        viewHolder.bindToPost(model, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }

                };
        recyclerView3.setAdapter(firebaseRecyclerAdapter);
    }
}
