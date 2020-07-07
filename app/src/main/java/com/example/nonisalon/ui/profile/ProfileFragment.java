package com.example.nonisalon.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nonisalon.BaseActivity;
import com.example.nonisalon.R;
import com.example.nonisalon.SplashscreenLogin;
import com.example.nonisalon.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    TextView tv_nameprofile, tv_uname, tv_Phone, tv_Mail;
    private ProfileViewModel notificationsViewModel;
    DatabaseReference mDatabase;
    Button btn_signout;
    private static final String TAG = "ProfileFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tv_nameprofile = root.findViewById(R.id.tv_Nameprofile);
        tv_Mail = root.findViewById(R.id.tv_Email);
        tv_Phone = root.findViewById(R.id.tv_Phone);
        tv_uname = root.findViewById(R.id.tv_Uname);
        btn_signout = root.findViewById(R.id.btn_logout);
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SplashscreenLogin.class));
                getActivity().finish();
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
                        String username = usernameFromEmail(user.getEmail());
                        String phone = user.getPhonenumber();
                        String email = user.getEmail();
                        tv_nameprofile.setText(name);
                        tv_Mail.setText(email);
                        tv_Phone.setText(phone);
                        tv_uname.setText(username);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}

