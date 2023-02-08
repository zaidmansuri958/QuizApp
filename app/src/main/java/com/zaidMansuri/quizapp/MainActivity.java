package com.zaidMansuri.quizapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.messaging.FirebaseMessaging;
import com.zaidMansuri.quizapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class MainActivity extends AppCompatActivity {
    SmoothBottomBar bnv;
    DrawerLayout drawer;
    Toolbar tool;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    TextView name, email, number;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseMessaging.getInstance().subscribeToTopic("noti");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Home()).commit();
        if (!internet(MainActivity.this)) {
            showDialog();
        }

        tool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        nav = (NavigationView) findViewById(R.id.nav);
        View headerView = nav.getHeaderView(0);
        toggle = new ActionBarDrawerToggle(this, drawer, tool, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.yellow));
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.finance:
                        intent = new Intent(getApplicationContext(), Question.class);
                        intent.putExtra("catagory", "Finance");
                        startActivity(intent);
                        break;
                    case R.id.science:
                        intent = new Intent(getApplicationContext(), Question.class);
                        intent.putExtra("catagory", "Science");
                        startActivity(intent);
                        break;
                    case R.id.history:
                        intent = new Intent(getApplicationContext(), Question.class);
                        intent.putExtra("catagory", "History");
                        startActivity(intent);
                        break;
                    case R.id.mathematics:
                        intent = new Intent(getApplicationContext(), Question.class);
                        intent.putExtra("catagory", "Maths");
                        startActivity(intent);
                        break;
                    case R.id.sports:
                        intent = new Intent(getApplicationContext(), Question.class);
                        intent.putExtra("catagory", "Sports");
                        startActivity(intent);
                        break;
                    case R.id.computer:
                        intent = new Intent(getApplicationContext(), Question.class);
                        intent.putExtra("catagory", "Computer");
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        logoutDialog();
                        break;
                }
                return true;
            }
        });


        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("User");
        String user_id = mAuth.getUid().toString();
        name = (TextView) headerView.findViewById(R.id.user_name);
        number = (TextView) headerView.findViewById(R.id.user_number);
        email = (TextView) headerView.findViewById(R.id.user_email);

        reference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText("Hello," + " " + snapshot.child("name").getValue().toString());
                number.setText(snapshot.child("number").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "does not fetch user data", Toast.LENGTH_SHORT).show();

            }
        });

        bnv = findViewById(R.id.bottomBar);
        bnv.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                Fragment temp = null;
                switch (i) {
                    case 0:
                        temp = new Home();
                        break;
                    case 1:
                        temp = new Achievement();
                        break;
                    case 2:
                        AdRequest adRequest = new AdRequest.Builder().build();
                        InterstitialAd.load(MainActivity.this, "ca-app-pub-9829502421511765/5602673010", adRequest,
                                new InterstitialAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                        interstitialAd.show(MainActivity.this);
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        mInterstitialAd = null;
                                    }
                                });
                        temp = new Profile();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, temp).commit();
                return false;
            }

        });
    }


    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("Please connect to the internet to proceed further").
                setCancelable(false).
                setPositiveButton("connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    private void logoutDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("Do you rally want to logout ?").
                setCancelable(false).
                setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    private boolean internet(MainActivity mainActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}