package com.example.musicplayerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.musicplayerv2.Database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText lastname;
    TextInputEditText email;
    TextInputEditText phone;
    TextInputEditText password;

    private DatabaseHelper db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db_helper = new DatabaseHelper(SignUpActivity.this);

        name = (TextInputEditText) findViewById(R.id.nameTextBox);
        lastname = (TextInputEditText) findViewById(R.id.lastnameTextBox);
        email = (TextInputEditText) findViewById(R.id.emailTextBox);
        phone = (TextInputEditText) findViewById(R.id.phoneTextBox);
        password = (TextInputEditText) findViewById(R.id.passwordTextBox);
    }

    public void signUpButtonPressed(View v) {
        String first_name = name.getText().toString();
        String last_name = lastname.getText().toString();
        String email_text = email.getText().toString();
        String phone_text = phone.getText().toString();
        String password_text = password.getText().toString();

        if(first_name.isEmpty() || last_name.isEmpty() || email_text.isEmpty() || phone_text.isEmpty() || password_text.isEmpty()){
            Toast.makeText(this, "Empty Field Detected!", Toast.LENGTH_SHORT).show();
        }

        if(phone_text.length() != 10){
            Toast.makeText(this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email_text.contains("@")){
            Toast.makeText(this, "Invalid Email!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(!email_text.substring(email_text.lastIndexOf("@")+1).contains(".")){
                Toast.makeText(this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(password_text.length() < 8){
            Toast.makeText(this, "Password is too short!", Toast.LENGTH_SHORT).show();
            return;
        }
        saveUser(v);
        finish();
        sendMail(v);

    }

    public void sendMail(View v){
        name = (TextInputEditText) findViewById(R.id.nameTextBox);
        lastname = (TextInputEditText) findViewById(R.id.lastnameTextBox);
        email = (TextInputEditText) findViewById(R.id.emailTextBox);
        phone = (TextInputEditText) findViewById(R.id.phoneTextBox);
        password = (TextInputEditText) findViewById(R.id.passwordTextBox);

        String body = "Dear " + name.getText().toString() + ' ' + lastname.getText().toString() + ',' + "Thanks for using our application." + "\nEmail: " + email.getText().toString() + "\nPhone: " + phone.getText().toString();
        System.out.println(body);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "SignUp Info");
        intent.putExtra(Intent.EXTRA_TEXT, body);
//
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void saveUser(View v){


        String first_name = name.getText().toString();
        String last_name = lastname.getText().toString();
        String email_text = email.getText().toString();
        String phone_text = phone.getText().toString();
        String password_text = password.getText().toString();

        long userId = db_helper.insertUser(first_name, last_name, email_text, phone_text, password_text);
        System.out.println("userID:" + userId);
    }
}