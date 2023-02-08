package com.zaidMansuri.quizapp;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaidMansuri.quizapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Achievement extends Fragment {
    FirebaseAuth mAuth;
    DatabaseReference database;
    TextView coin,level;
    ImageView trophy;
    String score;
    LinearLayout watchAD;
    ProgressDialog pd;
    MediaPlayer player;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Achievement() {

    }

    public static Achievement newInstance(String param1, String param2) {
        Achievement fragment = new Achievement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_achievement, container, false);
        player=MediaPlayer.create(getContext(),R.raw.coinsound);
        pd=new ProgressDialog(getContext());
        pd.setTitle("Rewarded ad");
        pd.setMessage("Ads loading");
        pd.create();
        Context context=container.getContext();
        watchAD=root.findViewById(R.id.watchAD);
        coin=root.findViewById(R.id.totalCoin);
        level=root.findViewById(R.id.level);
        trophy=root.findViewById(R.id.trophy);
        mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getUid().toString();
        database= FirebaseDatabase.getInstance().getReference("User");
        database.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                score=snapshot.child("score").getValue().toString();
                int data=parseInt(score);
                coin.setText(score);
                if(data<10000){
                    trophy.setImageResource(R.drawable.bronze);
                    level.setText("Beginer");
                    level.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else if(data>10000 && data<25000){
                    trophy.setImageResource(R.drawable.silver);
                    level.setText("Master");
                    level.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
                else if(data>25000){
                    trophy.setImageResource(R.drawable.golden);
                    level.setText("Grand Master");
                    level.setBackgroundColor(getResources().getColor(R.color.darkblue));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        watchAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(getContext(), "ca-app-pub-9829502421511765/3430422358",
                        adRequest, new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                pd.dismiss();
                                rewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        database.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String old = snapshot.child("score").getValue().toString();
                                                int newScore = parseInt(old) + (25);
                                                snapshot.getRef().child("score").setValue(newScore);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.start();
                                    }
                                });
                            }
                        });
            }
        });
        return root;
    }
}