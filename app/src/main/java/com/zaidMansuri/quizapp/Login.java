package com.zaidMansuri.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaidMansuri.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email,password;
    Button Login;
    TextView signup;
    FirebaseAuth mAuth;
    ProgressDialog pd;
    ImageView logo;
    Animation bounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        email=findViewById(R.id.lEmail);
        password=findViewById(R.id.lPassword);
        Login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        logo=findViewById(R.id.logo);
        bounce= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        logo.startAnimation(bounce);

        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setTitle("Login");
        pd.setMessage("Trying to Login");
        pd.create();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_email = email.getEditableText().toString();
                String u_password = password.getEditableText().toString();
                if (u_email.isEmpty() || u_password.isEmpty()) {
                    Toast.makeText(Login.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    pd.show();
                    mAuth.signInWithEmailAndPassword(u_email, u_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                pd.dismiss();
                                startActivity(intent);
                                finish();
                            } else {
                                pd.dismiss();
                                Toast.makeText(Login.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

    }

}