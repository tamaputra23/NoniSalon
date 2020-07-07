package com.example.nonisalon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonisalon.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";

     DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField, edtPhonenumber, edtFirstNameField, edtConfirmPassword;
    private Button mSignUpButton;
    private TextView mSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.et_emailsignup);
        mPasswordField = findViewById(R.id.et_passwordsignup);
        edtFirstNameField = findViewById(R.id.et_firstnameignup);
        edtConfirmPassword = findViewById(R.id.et_confirmpassword);
        edtPhonenumber = findViewById(R.id.et_phoneignup);
        mSignInButton = findViewById(R.id.tv_login);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }

    }
    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        if (password.length()<= 6){
            Toast.makeText(RegisterActivity.this, "Password is too short! it should be at least 6 characters",
                    Toast.LENGTH_SHORT).show();
        }

        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());


                            if (task.isSuccessful()) {
                                onAuthSuccess(task.getResult().getUser());
                            } else {
                                Toast.makeText(RegisterActivity.this, "Sign Up Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void onAuthSuccess(FirebaseUser user) {
        String firstname = edtFirstNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String phonenumber = edtPhonenumber.getText().toString();
        // Write new user
        writeNewUser(user.getUid(), email ,firstname, phonenumber);
        // Go to MainActivity
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }
        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }
        if (!edtConfirmPassword.getText().toString().equals(mPasswordField.getText().toString())) {
            edtConfirmPassword.setError("Make sure your Password match");
            result = false;
        } else {
            edtConfirmPassword.setError(null);
        }
        if (TextUtils.isEmpty(edtFirstNameField.getText().toString())) {
            edtFirstNameField.setError("Required");
            result = false;
        } else {
            edtFirstNameField.setError(null);
        }
        if (TextUtils.isEmpty(edtPhonenumber.getText().toString())) {
            edtPhonenumber.setError("Required");
            result = false;
        } else {
            edtPhonenumber.setError(null);
        }
        return result;
    }
    // [START basic_write]
    private void writeNewUser(String userId, String email, String firstname, String phonenumber) {
        User user = new User(email, firstname, phonenumber);
        mDatabase.child("users").child(userId).setValue(user);
    }
    public void Sign_up(View view){
        signUp();
    }
}
