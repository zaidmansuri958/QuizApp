package com.zaidMansuri.quizapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zaidMansuri.quizapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class cardAdapter extends FirebaseRecyclerAdapter<cardModal, cardAdapter.viewHolder> {
    public cardAdapter(@NonNull FirebaseRecyclerOptions<cardModal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull cardModal model) {
        holder.name.setText(model.getName());
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.img.getContext(),Question.class);
                intent.putExtra("catagory",model.getName().toString());
                holder.img.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        CardView card;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            card = itemView.findViewById(R.id.card);
        }
    }
}
