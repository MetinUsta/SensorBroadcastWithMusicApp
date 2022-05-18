package com.example.musicplayerv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.musicplayerv2.Database.DatabaseHelper;
import com.example.musicplayerv2.Database.model.User;
import com.example.musicplayerv2.Model.Playlist;
import com.google.android.material.textfield.TextInputEditText;

public class LogInActivity extends AppCompatActivity {

    private DatabaseHelper db_helper;
    private int login_attempt_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        NotificationManager notificationManager =
                (NotificationManager) LogInActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        db_helper = new DatabaseHelper(LogInActivity.this);
        login_attempt_count = 0;
    }

    public void onSignUpPressed(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void onLogInPressed(View view){
        TextInputEditText email = (TextInputEditText) findViewById(R.id.email);
        TextInputEditText password = (TextInputEditText) findViewById(R.id.password);

        String email_text = email.getText().toString();
        String password_text = password.getText().toString();

        User user = db_helper.getUser(email_text);
        if(user == null){
            Toast.makeText(this, "There is no user registered under this email!", Toast.LENGTH_SHORT).show();
        }else{
            if(user.getPassword().equals(password_text)){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                if(login_attempt_count < 3) {
                    Toast.makeText(this, "You have entered the wrong password " + login_attempt_count + " times ", Toast.LENGTH_SHORT).show();
                    login_attempt_count++;
                }else{
                    Button logInButton = (Button) findViewById(R.id.logInButton);
                    logInButton.setEnabled(false);
                    Toast.makeText(this, "You have entered the wrong password three times. You wont be able to login from now on.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}