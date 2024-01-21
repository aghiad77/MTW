package com.example.mtw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class VerificationActivity extends AppCompatActivity {

    EditText editOne,editTwo;
    ImageView confirm;
    String name,email,phone;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initializ();
        getintentInfo();
        db=new Database(this);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setEnable(false);
                if(inputVerify()) {
                    // Call api to send your info + code when api return response "success" then open Main Activity
                    db.saveLogin(name,email,phone,editOne.getText().toString()+editTwo.getText().toString());
                    Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void initializ() {
        editOne = (EditText) findViewById(R.id.edit_number_one);
        editTwo = (EditText) findViewById(R.id.edit_number_two);
        confirm = (ImageView) findViewById(R.id.confirm);
        textWatcher();
    }
    private void getintentInfo() {

        try {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
        }catch (Exception e){
            name = "";
            email = "";
            phone = "";
        }

    }
    private boolean inputVerify(){

        if (editOne.getText().toString().equals("") || editTwo.getText().toString().equals("") )
        {

            Toast.makeText(getBaseContext(), "الرجاء إدخال جميع البيانات", Toast.LENGTH_SHORT).show();
            setEnable(true);
            return false;
        }

        return true;
    }
    private void setEnable(boolean status) {

        editOne.setEnabled(status);
        editTwo.setEnabled(status);
        confirm.setEnabled(status);


    }
    public void textWatcher(){

        editOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (editOne.getText().toString().trim().length() == 3) {
                    editTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (editTwo.getText().toString().trim().length() == 0) {
                    editOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
