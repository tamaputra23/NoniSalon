package com.example.nonisalon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonisalon.adapter.CartHolder;
import com.example.nonisalon.model.Cart;
import com.example.nonisalon.model.Payment;
import com.example.nonisalon.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends BaseActivity {
    String treatment, email;
    String kodetransaksi;
    private static final String TAG = "CartActivity";
    TextView tv_dateorder, tv_timeorder, tv_nameorder, tv_totalorder, tv_paymnetmethode;
    RecyclerView recyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    DatabaseReference mDatabase;
    SharedPreferences mSharedPref; //for saving sort settings
    LinearLayoutManager mLayoutManager; //for sorting
    FirebaseRecyclerAdapter <Cart, CartHolder> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recyclerView1);
        treatment = getIntent().getStringExtra("treatment");
        tv_paymnetmethode = findViewById(R.id.tv_paymentmethode);
        tv_dateorder = findViewById(R.id.tv_dateorder);
        tv_timeorder = findViewById(R.id.tv_timeorder);
        tv_nameorder = findViewById(R.id.tv_nameorder);
        tv_totalorder = findViewById(R.id.tv_totalpayment);
        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "newest"); //where if no settingsis selected newest will be default
        if (mSorting.equals("newest")) {
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means newest first
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        } else if (mSorting.equals("oldest")) {
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means oldest first
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);
    }
    @Override
    protected void onStart() {
        super.onStart();
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(CartActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                            String name = user.getFirstname();
                            email = user.getEmail();
                            String date1 = "Date : " + date;
                            String time1 = "Time : " + time;
                            String cashier = "Cashier Name : " + name;
                            tv_nameorder.setText(cashier);
                            tv_dateorder.setText(date1);
                            tv_timeorder.setText(time1);
                        }
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        Query postsQuery = mDatabase.child("cart").child(userId);
        FirebaseRecyclerAdapter<Cart, CartHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Cart, CartHolder>(
                        Cart.class,
                        R.layout.cartholder,
                        CartHolder.class,
                        postsQuery
                ) {
                    @Override
                    protected void populateViewHolder(CartHolder viewHolder, final Cart model, final int i) {
                        viewHolder.bindToPost(model, new View.OnClickListener() {
                            final DatabaseReference postRef = getRef(i);
                            @Override
                            public void onClick(View v) {
                                DatabaseReference UserOrderRef = mDatabase.child("cart").child(userId).child(postRef.getKey());
                                UserOrderRef.setValue(null);
                            }
                        });
                    }

                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        mDatabase.child("cart").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                int disc =0;
                int all = 0;
                Cart Order = dataSnapshot.getValue(Cart.class);
                if (Order == null){
                    tv_totalorder.setText("Rp 0");

                }
                else {
                    for (DataSnapshot snapm : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map <String, Object>) snapm.getValue();
                        Object price = map.get("price");
                        Object total = map.get("total");
                        int iPrice = Integer.parseInt(String.valueOf(price));
                        int iTotal = Integer.parseInt(String.valueOf(total));
                        sum = iPrice*iTotal;
                        all+= sum;
                        String sAll = "Rp " + String.valueOf(all);
                        tv_totalorder.setText(sAll);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public class ViewDialog extends BaseActivity {

        public void showDialog(){
            final BottomSheetDialog dialog = new BottomSheetDialog(CartActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.paymentpopup);
            LinearLayout cash = dialog.findViewById(R.id.cash_payment);
            LinearLayout bank = dialog.findViewById(R.id.transfer_payment);
            LinearLayout ovo = dialog.findViewById(R.id.ovo_payment);
            ImageView imgClose = dialog.findViewById(R.id.btn_close);
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            cash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_paymnetmethode.setText("Cash");
                    dialog.dismiss();
                }
            });
            bank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_paymnetmethode.setText("Bank Transfer");
                    dialog.dismiss();
                }
            });
            ovo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_paymnetmethode.setText("OVO");
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
    public void btn_payment(View view){
        ViewDialog alert = new ViewDialog();
        alert.showDialog();
    }
    public void btn_confirm(View view){
        final String userId = getUid();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        kodetransaksi = email.substring(0,2).toUpperCase() + date.substring(0,2) + time.substring(0,2) + time.substring(3,5) + time.substring(6,8);
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra("kodetransaksi", kodetransaksi);
        startActivity(intent);
        finish();
        mDatabase.child("cart").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                for (DataSnapshot snapm : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map <String, Object>) snapm.getValue();
                    Object mPrice = map.get("price");
                    Object name = map.get("NameTreatment");
                    Object total = map.get("total");
                    Object kode = map.get("treatment");
                    writeNewPost(userId, String.valueOf(kode), String.valueOf(name), String.valueOf(mPrice), String.valueOf(total));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference UserOrderRef = mDatabase.child("cart").child(userId);
        UserOrderRef.setValue(null);
    }
    private void writeNewPost(String userId, String treatment, String NameTreatment, String price, String total) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String tanggal =new SimpleDateFormat("E, dd MMM yyyy HH:mm").format(new Date());
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        String key = mDatabase.child("payment").push().getKey();
        Payment payment = new Payment(userId, treatment ,NameTreatment , price, total, kodetransaksi, tanggal);
        Map<String, Object> postValues = payment.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/payment/" + userId + "/" + kodetransaksi + "/" + key, postValues);
        childUpdates.put("/history/" + userId + "/" +  key, postValues);
        mDatabase.updateChildren(childUpdates);
    }
}
