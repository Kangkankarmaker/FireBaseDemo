package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    EditText input_email,input_password;
    TextView textView;
    private FirebaseAuth auth;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //View
        btnLogin = findViewById(R.id.login_btn_login);
        input_email = findViewById(R.id.login_email);
        input_password=findViewById(R.id.login_password);
        textView=findViewById(R.id.txt_Reg);

        textView.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();
        //Check already session
        if(auth.getCurrentUser() != null) {

            Toast.makeText(this, "Welcome User" +auth.getUid(), Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn_login)
        {
            final String email = input_email.getText().toString().trim();
            String password = input_password.getText().toString().trim();
            if (email.isEmpty()) {
                input_email.setError("Email Can't be Empty");
                input_email.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                input_password.setError("Password Can't be Empty");
                input_password.requestFocus();
                return;
            }
            loginUser(email,password);
            displayLoader();
        }

        if(view.getId() == R.id.txt_Reg)
        {
            startActivity(new Intent(LogInActivity.this,RegistractionActivity.class));
        }
    }

    private void loginUser(String email, final String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            pDialog.dismiss();
                            Toast.makeText(LogInActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                            buildDialog(LogInActivity.this).show();

                        }
                        else{
                            Toast.makeText(LogInActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    }
                });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(LogInActivity.this);
        pDialog.setMessage("Logging In, Please Wait ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public AlertDialog.Builder buildDialog(Context c) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Error");
        builder.setMessage("The email/password does not match with our credentials ");

        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }
}