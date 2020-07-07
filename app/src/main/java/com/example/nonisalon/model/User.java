package com.example.nonisalon.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String email;
    public String firstname;
    public String lastname;
    public String phonenumber;
    public String id;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User( String email, String firstname, String phonenumber) {
        this.email = email;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String akses) {
        this.lastname = lastname;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}