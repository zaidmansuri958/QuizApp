package com.zaidMansuri.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaidMansuri.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    ImageView logo;
    Animation bounce;
    TextView name;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth=FirebaseAuth.getInstance();
        logo=findViewById(R.id.logo);
        name=findViewById(R.id.name);
        bounce= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        logo.startAnimation(bounce);
        player=MediaPlayer.create(SplashScreen.this,R.raw.spin);
        player.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player.release();
                if(mAuth.getCurrentUser()!=null){
                    Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(SplashScreen.this,SignUp.class);
                    startActivity(intent);
                }
                finish();
            }
        },4000);

    }
}