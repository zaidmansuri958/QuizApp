package com.zaidMansuri.quizapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zaidMansuri.quizapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends Fragment {
    RecyclerView recycler;
    cardAdapter adapter;
    AdView adView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Home() {

    }
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        adView=root.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        if(adView.isLoading()){
            Toast.makeText(getContext(), "Loaded", Toast.LENGTH_SHORT).show();
        }
        else{
            adView.loadAd(adRequest);
        }
        recycler=root.findViewById(R.id.recycle);
        Context context=getContext();
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        recycler.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<cardModal> options = new FirebaseRecyclerOptions.Builder<cardModal>().
                setQuery(FirebaseDatabase.getInstance().getReference().child("Quiz"), cardModal.class).build();
        adapter = new cardAdapter(options);
        recycler.setAdapter(adapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}