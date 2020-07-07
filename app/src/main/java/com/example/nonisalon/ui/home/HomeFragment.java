package com.example.nonisalon.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nonisalon.BaseActivity;
import com.example.nonisalon.CartActivity;
import com.example.nonisalon.KoreanLashActivity;
import com.example.nonisalon.R;
import com.example.nonisalon.RetouchLashActivity;
import com.example.nonisalon.VolumeLashActivity;
import com.example.nonisalon.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    RelativeLayout btnVolume, btnKorean, btnRetouch;
    TextView tv_name;
    private HomeViewModel homeViewModel;
    private static final String TAG = "HomeFragment";
    DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tv_name = root.findViewById(R.id.tv_Name);
        btnKorean = root.findViewById(R.id.btn_koreanlash);
        btnRetouch = root.findViewById(R.id.btn_retouchlash);
        btnVolume = root.findViewById(R.id.btn_volumelash);
        btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Volume = new Intent (getContext(), VolumeLashActivity.class);
                startActivity(Volume);
            }
        });
        btnRetouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Volume = new Intent (getContext(), RetouchLashActivity.class);
                startActivity(Volume);
            }
        });
        btnKorean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Retouch = new Intent (getContext(), KoreanLashActivity.class);
                startActivity(Retouch);
            }
        });
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        final String userId = BaseActivity.getUid();
        mDatabase.child("users").child(userId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        String name = user.getFirstname();
                        String greet = "Hei " + name + " !";
                        tv_name.setText(greet);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
}
