package com.example.spendwise;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    TextView amt, cat, dt;
    RelativeLayout layout;
    ImageView tp;
    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        amt = itemView.findViewById(R.id.amount);
        cat = itemView.findViewById(R.id.category);
        dt = itemView.findViewById(R.id.date);
        layout = itemView.findViewById(R.id.layout);
        tp = itemView.findViewById(R.id.type);
    }
}
