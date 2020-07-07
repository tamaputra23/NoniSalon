package com.example.nonisalon.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Payment {
    public String uid;
    public String treatment;
    public String NameTreatment;
    public String price;
    public String total;
    public String kodetransaksi;
    public String tanggal;
    public Payment(){}
    public Payment(String uid, String treatment, String NameTreatment, String price, String total, String kodetransaksi, String tanggal) {
        this.uid = uid;
        this.treatment = treatment;
        this.NameTreatment = NameTreatment;
        this.price = price;
        this.total = total;
        this.kodetransaksi = kodetransaksi;
        this.tanggal = tanggal;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("treatment", treatment);
        result.put("NameTreatment", NameTreatment);
        result.put("price", price);
        result.put("total", total);
        result.put("kodetransaksi", kodetransaksi);
        result.put("tanggal", tanggal);
        return result;
    }
}
