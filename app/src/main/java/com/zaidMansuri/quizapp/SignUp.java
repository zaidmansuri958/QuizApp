package com.zaidMansuri.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    EditText name,email,number,password;
    Button signUp;
    TextView login;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressDialog pd;
    ImageView logo;
    Animation bounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        number=findViewById(R.id.number);
        password=findViewById(R.id.password);
        signUp=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        logo=findViewById(R.id.logo);
        bounce= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        logo.startAnimation(bounce);

        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setTitle("Sign Up");
        pd.setMessage("Trying to Sign Up");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Chek user internet connection is open or not */
                if (!isConnected(SignUp.this)) {
                    showDialog();

                } else {
                    String user_email = email.getEditableText().toString();
                    String user_password = password.getEditableText().toString();
                    String user_name = name.getEditableText().toString();
                    String user_number = number.getEditableText().toString();

                    if(user_password.length()<6){
                        Toast.makeText(SignUp.this,"Password should contain more than 6 letters",Toast.LENGTH_SHORT).show();
                    }


                    if (user_password.isEmpty()||user_name.isEmpty()||user_email.isEmpty()) {
                        Toast.makeText(SignUp.this, "Enter Credentials ", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.show();
                        mAuth.createUserWithEmailAndPassword(user_email, user_password)
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUp.this, "Successful", Toast.LENGTH_SHORT).show();
                                            String user_id = mAuth.getUid().toString();
                                            AuthModal data = new AuthModal(user_name, user_email,user_number,user_password,0);
                                            database = FirebaseDatabase.getInstance();
                                            DatabaseReference reference = database.getReference().child("User");
                                            reference.child(user_id).setValue(data);
                                            pd.dismiss();
                                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Toast.makeText(SignUp.this, "Error" + user_email, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                }
            }
            private boolean isConnected(SignUp signup) {
                ConnectivityManager connectivityManager= (ConnectivityManager) signup.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if((wifi!=null&&wifi.isConnected())||(mobile!=null&&mobile.isConnected())){
                    return true;
                }
                else{
                    return false;
                }
            }

            private void showDialog() {
                AlertDialog.Builder dialog=new AlertDialog.Builder(SignUp.this);
                dialog.setMessage("Please connect to the internet to proceed further").
                        setCancelable(false).
                        setPositiveButton("connect", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(intent);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });


    }
}