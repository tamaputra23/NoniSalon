package com.example.nonisalon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nonisalon.model.Cart;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KoreanLashActivity extends BaseActivity implements View.OnClickListener {
    int natural, lebat, dobel, russian, totalitem, totalprice = 0;
    int naturalPrice = 70000;
    int lebatPrice = 100000;
    int dobelPrice = 120000;
    int russianPrice = 150000;
    TextView tv_totalVolume, tv_lebattotal, tv_naturaltotal, tv_russiantotal, tv_doubletotal;
    TextView tv_naturallashprice, tv_lebatlashprice, tv_doublelashprice, tv_russianlashprice;
    TextView tv_naturallash, tv_lebatlash, tv_doublelash, tv_russianlash;
    LinearLayout btn_addLebat, btn_addNatural, btn_addDoubel, btn_addRussian, btn_koreanCart;
    LinearLayout btn_summLebat, btn_summDouble, btn_summNatural, btn_summRussian;
    ImageView btn_plusLebat, btn_plusNatural, btn_plusDouble, btn_plusRussian;
    ImageView btn_minusLebat, btn_minusNatural, btn_minusDouble, btn_minusRussian;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    DatabaseReference mDatabase;
    SharedPreferences mSharedPref; //for saving sort settings
    LinearLayoutManager mLayoutManager; //for sorting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korean_lash);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Intent intent = new Intent (this, PaymentActivity.class);
        tv_naturallash = findViewById(R.id.tv_naturallash);
        tv_naturallashprice = findViewById(R.id.tv_naturallashprice);
        tv_lebatlash = findViewById(R.id.tv_lebatlash);
        tv_lebatlashprice = findViewById(R.id.tv_lebatlashprice);
        tv_doublelash = findViewById(R.id.tv_doublelash);
        tv_doublelashprice = findViewById(R.id.tv_doublelashprice);
        tv_russianlash = findViewById(R.id.tv_russianlash);
        tv_russianlashprice = findViewById(R.id.tv_russianlashprice);
        tv_russianlashprice.setText("Rp " + String.valueOf(russianPrice));
        tv_doublelashprice.setText("Rp " +String.valueOf(dobelPrice));
        tv_lebatlashprice.setText("Rp " + String.valueOf(lebatPrice));
        tv_naturallashprice.setText("Rp " + String.valueOf(naturalPrice));
        tv_naturallash.setText("Korean Natural Lash");
        tv_doublelash.setText("Korean Double Lash");
        tv_lebatlash.setText("Korean Natural Lebat");
        tv_russianlash.setText("Korean Russian Lash");
        tv_totalVolume = findViewById(R.id.tv_totalkorean);
        tv_doubletotal = findViewById(R.id.tv_doubletotal);
        tv_lebattotal = findViewById(R.id.tv_lebattotal);
        tv_russiantotal = findViewById(R.id.tv_russiantotal);
        tv_naturaltotal = findViewById(R.id.tv_naturaltotal);
        btn_addDoubel = findViewById(R.id.btn_adddouble);
        btn_addLebat = findViewById(R.id.btn_addlebat);
        btn_addNatural = findViewById(R.id.btn_addnatural);
        btn_addRussian = findViewById(R.id.btn_addrussaian);
        btn_minusDouble = findViewById(R.id.btn_minusdouble);
        btn_minusLebat = findViewById(R.id.btn_minuslebat);
        btn_minusNatural = findViewById(R.id.btn_minusnatural);
        btn_minusRussian = findViewById(R.id.btn_minusrussian);
        btn_plusDouble = findViewById(R.id.btn_plusdouble);
        btn_plusNatural = findViewById(R.id.btn_plusnatural);
        btn_plusLebat = findViewById(R.id.btn_pluslebat);
        btn_plusRussian = findViewById(R.id.btn_plusrussian);
        btn_koreanCart = findViewById(R.id.btn_koreancart);
        btn_summDouble = findViewById(R.id.btn_summdouble);
        btn_summLebat = findViewById(R.id.btn_summlebat);
        btn_summNatural = findViewById(R.id.btn_summnatural);
        btn_summRussian = findViewById(R.id.btn_summrussian);
        btn_addDoubel.setOnClickListener(this);
        btn_addNatural.setOnClickListener(this);
        btn_addLebat.setOnClickListener(this);
        btn_addRussian.setOnClickListener(this);
        btn_plusRussian.setOnClickListener(this);
        btn_plusLebat.setOnClickListener(this);
        btn_plusNatural.setOnClickListener(this);
        btn_plusDouble.setOnClickListener(this);
        btn_minusRussian.setOnClickListener(this);
        btn_minusLebat.setOnClickListener(this);
        btn_minusNatural.setOnClickListener(this);
        btn_minusDouble.setOnClickListener(this);
        btn_koreanCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = getUid();
                String treatment = "KL";
                Intent intent = new Intent (getApplicationContext(), CartActivity.class);
                intent.putExtra("treatment", treatment);
                finish();
                startActivity(intent);
                if(dobel!= 0){
                    writeNewPost(userId, treatment, "Korean Double Lash", String.valueOf(dobelPrice), String.valueOf(dobel));
                }
                if(natural!= 0){
                    writeNewPost(userId, treatment, "Korean Natural Lash", String.valueOf(naturalPrice), String.valueOf(natural));
                }
                if(russian!= 0){
                    writeNewPost(userId, treatment, "Korean Russian Lash", String.valueOf(russianPrice), String.valueOf(russian));
                }
                if(lebat!= 0){
                    writeNewPost(userId, treatment, "Korean Natural Lebat", String.valueOf(naturalPrice), String.valueOf(natural));
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == btn_addDoubel.getId()) {
            dobel =+1;
            tv_doubletotal.setText(String.valueOf(dobel));
            totalprice = totalprice + (1*dobelPrice);
            totalitem = totalitem +dobel;
            totalcart();
            btn_addDoubel.setVisibility(View.GONE);
            btn_summDouble.setVisibility(View.VISIBLE);
        }
        if (v.getId() == btn_plusDouble.getId()) {
            dobel =dobel + 1;
            tv_doubletotal.setText(String.valueOf(dobel));
            totalprice = totalprice + (1*dobelPrice);
            totalitem = totalitem +1;
            totalcart();
            btn_addDoubel.setVisibility(View.GONE);
            btn_summDouble.setVisibility(View.VISIBLE);
        }
        if (v.getId() == btn_minusDouble.getId()) {
            dobel =dobel - 1;
            tv_doubletotal.setText(String.valueOf(dobel));
            totalprice = totalprice - (1*dobelPrice);
            totalitem = totalitem - 1;
            totalcart();
            btn_addDoubel.setVisibility(View.GONE);
            btn_summDouble.setVisibility(View.VISIBLE);
            if (dobel==0){
                btn_addDoubel.setVisibility(View.VISIBLE);
                btn_summDouble.setVisibility(View.GONE);
            }
        }
        if (v.getId() == btn_addNatural.getId()) {
            natural =natural +1;
            tv_naturaltotal.setText(String.valueOf(natural));
            totalprice = totalprice + (1*naturalPrice);
            totalitem = totalitem + 1;
            totalcart();
            btn_addNatural.setVisibility(View.GONE);
            btn_summNatural.setVisibility(View.VISIBLE);
        }
        if (v.getId() == btn_plusNatural.getId()) {
            natural =natural+1;
            tv_naturaltotal.setText(String.valueOf(natural));
            totalprice = totalprice + (1*naturalPrice);
            totalitem = totalitem + 1;
            totalcart();
            btn_addNatural.setVisibility(View.GONE);
            btn_summNatural.setVisibility(View.VISIBLE);
        }
        if (v.getId() == btn_minusNatural.getId()) {
            natural = natural- 1;
            tv_naturaltotal.setText(String.valueOf(natural));
            totalprice = totalprice - (1*naturalPrice);
            totalitem = totalitem - 1;
            totalcart();
            btn_addNatural.setVisibility(View.GONE);
            btn_summNatural.setVisibility(View.VISIBLE);
            if (natural==0){
                btn_addNatural.setVisibility(View.VISIBLE);
                btn_summNatural.setVisibility(View.GONE);
            }
        }
        if (v.getId() == btn_addLebat.getId()) {
            lebat =+1;
            tv_lebattotal.setText(String.valueOf(lebat));
            totalprice = totalprice + (1*lebatPrice);
            totalitem = totalitem + lebat;
            totalcart();
            btn_addLebat.setVisibility(View.GONE);
            btn_summLebat.setVisibility(View.VISIBLE);
        }
        if (v.getId() == btn_plusLebat.getId()) {
            lebat =lebat +1;
            tv_lebattotal.setText(String.valueOf(lebat));
            totalprice = totalprice + (1*lebatPrice);
            totalitem = totalitem + 1;
            totalcart();
            btn_addLebat.setVisibility(View.GONE);
            btn_summLebat.setVisibility(View.VISIBLE);
        }
        if (v.getId() == btn_minusLebat.getId()) {
            lebat =lebat - 1;
            tv_lebattotal.setText(String.valueOf(lebat));
            totalprice = totalprice - (1*lebatPrice);
            totalitem = totalitem - 1 ;
            totalcart();
            btn_addLebat.setVisibility(View.GONE);
            btn_summLebat.setVisibility(View.VISIBLE);
            if (lebat==0){
                btn_addLebat.setVisibility(View.VISIBLE);
                btn_summLebat.setVisibility(View.GONE);
            }
        }
        if (v.getId() == btn_addRussian.getId()) {
            russian =+1;
            tv_russiantotal.setText(String.valueOf(russian));
            totalprice = totalprice + (1*russianPrice);
            totalitem = totalitem + 1;
            System.out.print(String.valueOf(russian));
            totalcart();
            btn_addRussian.setVisibility(View.GONE);
            btn_summRussian.setVisibility(View.VISIBLE);
        }
        if (v.getId() == btn_plusRussian.getId()) {
            russian =russian +1;
            tv_russiantotal.setText(String.valueOf(russian));
            totalprice = totalprice + (1*russianPrice);
            totalitem = totalitem + 1;
            totalcart();
            btn_addRussian.setVisibility(View.GONE);
            btn_summRussian.setVisibility(View.VISIBLE);
        }
        if (v.getId() == btn_minusRussian.getId()) {
            russian =russian - 1;
            tv_russiantotal.setText(String.valueOf(russian));
            totalprice = totalprice - (1*russianPrice);
            totalitem = totalitem - 1;
            totalcart();
            btn_addRussian.setVisibility(View.GONE);
            btn_summRussian.setVisibility(View.VISIBLE);
            if (russian==0){
                btn_addRussian.setVisibility(View.VISIBLE);
                btn_summRussian.setVisibility(View.GONE);
            }
        }
    }
    private void totalcart(){
        String sTotalitem = String.valueOf(totalitem);
        String sTotalPrice = String.valueOf(totalprice);
        String cart = sTotalitem + " Items | Rp " + sTotalPrice;
        tv_totalVolume.setText(cart);
    }
    private void writeNewPost(String userId, String treatment, String NameTreatment, String price, String total) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("cart").push().getKey();
        Cart cart = new Cart(userId, treatment ,NameTreatment , price, total);
        Map<String, Object> postValues = cart.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/cart/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
}
