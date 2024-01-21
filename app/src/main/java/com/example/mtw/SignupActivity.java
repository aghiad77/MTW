package com.example.mtw;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    EditText name,email,phone;
    ImageView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializ();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setEnable(false);
                if(inputVerify()) {
                  // Call api to send code to Email when api return response "code is sent to your Email" then open Verification Activity
                    Intent intent = new Intent(SignupActivity.this, VerificationActivity.class);
                    intent.putExtra("name",name.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("phone",phone.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void initializ() {

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        signup = (ImageView) findViewById(R.id.signup);
    }
    private boolean inputVerify(){

        if (name.getText().toString().equals("") || email.getText().toString().equals("") ||
                phone.getText().toString().equals("")) {

            Toast.makeText(getBaseContext(), "الرجاء إدخال جميع البيانات", Toast.LENGTH_SHORT).show();
            setEnable(true);
            return false;
        }

        return true;
    }
    private void setEnable(boolean status) {

        signup.setEnabled(status);
        name.setEnabled(status);
        email.setEnabled(status);
        phone.setEnabled(status);


    }
}
