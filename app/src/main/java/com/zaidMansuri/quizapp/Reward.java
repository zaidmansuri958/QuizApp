package com.zaidMansuri.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaidMansuri.quizapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Reward extends AppCompatActivity {
    TextView correct_ans,coins,greets;
    Animation box,coin;
    ImageView boxImg,coinImg;
    Button play;
    MediaPlayer coinSound;
    int correct;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        coinSound=MediaPlayer.create(getApplicationContext(),R.raw.coinsound);
        coinSound.start();
        correct=intent.getIntExtra("correct",0);
        correct_ans=findViewById(R.id.correct_ans);
        coins=findViewById(R.id.coin);
        greets=findViewById(R.id.greets);
        boxImg=findViewById(R.id.boxImg);
        coinImg=findViewById(R.id.coinImg);
        play=findViewById(R.id.play);
        adView=findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        box = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.box);
        coin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.coin);
        boxImg.startAnimation(box);
        coinImg.startAnimation(coin);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinSound.release();
                Intent intent=new Intent(Reward.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        correct_ans.setText(String.format("%d/%d ",correct,10));
        coins.setText(String.format("%d ",10*correct));
        if(correct==0){
            greets.setText("oops!!");
        }
        else{
            greets.setText("Congratulation");
        }

    }
}