package com.example.nonisalon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonisalon.adapter.PaymentHolder;
import com.example.nonisalon.model.Payment;
import com.example.nonisalon.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends BaseActivity {
    private static final String TAG = "PaymentActivity";
    int requestCode = 1;
    String kodetransaksi;
    TextView tv_datepayment, tv_timepayment, tv_namepayment, tv_sumpayment, tv_transnopayment;
    RecyclerView recyclerView2;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    DatabaseReference mDatabase;
    SharedPreferences mSharedPref; //for saving sort settings
    LinearLayoutManager mLayoutManager;
    private LinearLayout llScroll;
    private Bitmap bitmap;//for sorting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        llScroll = findViewById(R.id.ll_receipt);
        kodetransaksi = getIntent().getStringExtra("kodetransaksi");
        recyclerView2 = findViewById(R.id.recyclerView2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tv_datepayment = findViewById(R.id.tv_datepayment);
        tv_namepayment = findViewById(R.id.tv_namepayment);
        tv_timepayment = findViewById(R.id.tv_timepayment);
        tv_sumpayment = findViewById(R.id.tv_sumpayment);
        tv_transnopayment = findViewById(R.id.tv_transnoPayment);
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
        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.setHasFixedSize(false);
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
                            Toast.makeText(PaymentActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            String date = new SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(new Date());
                            String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                            String name = user.getFirstname();
                            String date1 = "Date : " + date;
                            String time1 = "Time : " + time;
                            String cashier = "Cashier Name : " + name;
                            String nopayment = "Transaction No. " + kodetransaksi;
                            tv_namepayment.setText(cashier);
                            tv_datepayment.setText(date1);
                            tv_timepayment.setText(time1);
                            tv_transnopayment.setText(nopayment);
                        }
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        Query postsQuery = mDatabase.child("payment").child(userId).child(kodetransaksi);
        FirebaseRecyclerAdapter<Payment, PaymentHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Payment, PaymentHolder>(
                        Payment.class,
                        R.layout.paymentholder,
                        PaymentHolder.class,
                        postsQuery
                ) {
                    @Override
                    protected void populateViewHolder(PaymentHolder viewHolder, final Payment model, final int i) {
                        viewHolder.bindToPost(model, new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }

                };
        recyclerView2.setAdapter(firebaseRecyclerAdapter);
        mDatabase.child("payment").child(userId).child(kodetransaksi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                int disc =0;
                int all = 0;
                Payment Order = dataSnapshot.getValue(Payment.class);
                if (Order == null){
                    tv_sumpayment.setText("Rp 0");
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
                        tv_sumpayment.setText(sAll);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
    });
}
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
    public void save(View view){
        if (ContextCompat.checkSelfPermission(PaymentActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);
        }
        Log.d("size"," "+llScroll.getWidth() +"  "+llScroll.getWidth());
        bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());
        createPdf();
    }
    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "storage/emulated/0/Documents/" + kodetransaksi+".pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

        openGeneratedPDF();
    }
    private void openGeneratedPDF(){
        File file = new File("storage/emulated/0/Documents/" + kodetransaksi+".pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".PaymentActivity", file);
            intent.setDataAndType(photoURI, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(getApplicationContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }
}
