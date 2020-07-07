package com.example.nonisalon.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    public String uid;
    public String treatment;
    public String NameTreatment;
    public String price;
    public String total;
    public Cart(){}
    public Cart(String uid, String treatment, String NameTreatment, String price, String total) {
        this.uid = uid;
        this.treatment = treatment;
        this.NameTreatment = NameTreatment;
        this.price = price;
        this.total = total;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("treatment", treatment);
        result.put("NameTreatment", NameTreatment);
        result.put("price", price);
        result.put("total", total);
        return result;
    }
}
