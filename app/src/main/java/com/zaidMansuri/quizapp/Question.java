package com.zaidMansuri.quizapp;


import static java.lang.Integer.parseInt;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.zaidMansuri.quizapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Question extends AppCompatActivity {
    TextView option1, option2, option3, option4, question, totalQuestion, timer;
    Button quit, next;
    int number = 0, correct = 0, n, optionCount = 0;
    QuestionModal que;
    DatabaseReference database, udb, childCount;
    String ans, catagory;
    FirebaseAuth mAuth;
    MediaPlayer newQuestion, error;
    CountDownTimer countDownTimer;
    private InterstitialAd mInterstitialAd;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-9829502421511765/5390139775", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAd.show(Question.this);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });


        adView=findViewById(R.id.adView);
        AdRequest adReq=new AdRequest.Builder().build();
        adView.loadAd(adReq);

        newQuestion = MediaPlayer.create(this, R.raw.newquestion);
        error = MediaPlayer.create(this, R.raw.wrongans);
        question = findViewById(R.id.question);
        timer = findViewById(R.id.timer);
        totalQuestion = findViewById(R.id.totalQuestion);
        option1 = findViewById(R.id.option_1);
        option2 = findViewById(R.id.option_2);
        option3 = findViewById(R.id.option_3);
        option4 = findViewById(R.id.option_4);
        quit = findViewById(R.id.quitBtn);
        next = findViewById(R.id.nextBtn);
        mAuth = FirebaseAuth.getInstance();
        udb = FirebaseDatabase.getInstance().getReference("User");
        String uid = mAuth.getUid().toString();
        Intent intent = getIntent();

        catagory = intent.getStringExtra("catagory");
        childCount = FirebaseDatabase.getInstance().getReference("TotalQuestion");

        setQuestion();

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number < 10) {
                    setQuestion();
                } else {
                    udb.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String old = snapshot.child("score").getValue().toString();
                            int newScore = parseInt(old) + (correct * 10);
                            snapshot.getRef().child("score").setValue(newScore);
                            Intent intent = new Intent(Question.this, Reward.class);
                            intent.putExtra("correct", correct);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionCount++;
                if (optionCount > 1) {
                    Toast.makeText(Question.this, "You can select only one time", Toast.LENGTH_SHORT).show();
                } else {
                    chekAnswer(option1);
                }

            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionCount++;
                if (optionCount > 1) {
                    Toast.makeText(Question.this, "You can select only one time", Toast.LENGTH_SHORT).show();
                } else {
                    chekAnswer(option2);
                }

            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionCount++;
                if (optionCount > 1) {
                    Toast.makeText(Question.this, "You can select only one time", Toast.LENGTH_SHORT).show();
                } else {
                    chekAnswer(option3);
                }
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionCount++;
                if (optionCount > 1) {
                    Toast.makeText(Question.this, "You can select only one time", Toast.LENGTH_SHORT).show();
                } else {
                    chekAnswer(option4);
                }
            }
        });
    }


//    Out side the function

    void rightAnswer() {

        if (option1.getText().toString().equals(ans)) {
            option1.setBackground(getDrawable(R.drawable.right));
        } else if (option2.getText().toString().equals(ans)) {
            option2.setBackground(getDrawable(R.drawable.right));
        } else if (option3.getText().toString().equals(ans)) {
            option3.setBackground(getDrawable(R.drawable.right));
        } else if (option4.getText().toString().equals(ans)) {
            option4.setBackground(getDrawable(R.drawable.right));
        }
    }

    void chekAnswer(View view) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        switch (view.getId()) {
            case R.id.option_1:
                if (option1.getText().toString().equals(ans)) {
                    correct++;
                    option1.setBackground(getDrawable(R.drawable.right));
                } else {
                    error.start();
                    rightAnswer();
                    option1.setBackground(getDrawable(R.drawable.wrong));
                }
                break;
            case R.id.option_2:
                if (option2.getText().toString().equals(ans)) {
                    correct++;
                    option2.setBackground(getDrawable(R.drawable.right));
                } else {
                    error.start();
                    rightAnswer();
                    option2.setBackground(getDrawable(R.drawable.wrong));
                }
                break;
            case R.id.option_3:
                if (option3.getText().toString().equals(ans)) {
                    correct++;
                    option3.setBackground(getDrawable(R.drawable.right));
                } else {
                    error.start();
                    rightAnswer();
                    option3.setBackground(getDrawable(R.drawable.wrong));
                }
                break;
            case R.id.option_4:
                if (option4.getText().toString().equals(ans)) {
                    correct++;
                    option4.setBackground(getDrawable(R.drawable.right));
                } else {
                    error.start();
                    rightAnswer();
                    option4.setBackground(getDrawable(R.drawable.wrong));
                }
                break;
        }
    }

    void reset() {
        option1.setBackground(getDrawable(R.drawable.unselected));
        option2.setBackground(getDrawable(R.drawable.unselected));
        option3.setBackground(getDrawable(R.drawable.unselected));
        option4.setBackground(getDrawable(R.drawable.unselected));
    }

    void setQuestion() {
        optionCount = 0;
        newQuestion.start();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        resetTimer();
        number++;
        totalQuestion.setText(String.format("%d/%d", number, 10));
        reset();

        childCount.child(catagory).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               n=parseInt(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        int max=n+1;
        final int i = new Random().nextInt((max-1)+1)+1;
        database = FirebaseDatabase.getInstance().getReference(catagory);
        database.child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                question.setText(snapshot.child("question").getValue().toString());
                option1.setText(snapshot.child("option_1").getValue().toString());
                option2.setText(snapshot.child("option_2").getValue().toString());
                option3.setText(snapshot.child("option_3").getValue().toString());
                option4.setText(snapshot.child("option_4").getValue().toString());
                ans = snapshot.child("answer").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void resetTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {
                error.start();
                Toast.makeText(Question.this, "Your time is finished", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setQuestion();
                    }
                }, 2000);

            }
        };
        countDownTimer.start();
    }

    void showDialog(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setMessage("Do you really want to quit the game").setCancelable(false).
                setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                countDownTimer.cancel();
                Intent intent = new Intent(Question.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog Dialog=alertDialog.create();
        Dialog.show();
    }


}