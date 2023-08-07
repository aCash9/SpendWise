package com.example.spendwise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {
    Context context;
    List<TransactionDetails> items;
    TransactionSelectListener listener;

    public TransactionAdapter(Context context, List<TransactionDetails> items, TransactionSelectListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.transactions_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.amt.setText("₹" + String.valueOf(items.get(position).getAmount()));
        holder.cat.setText(items.get(position).getCategory());
        holder.dt.setText(items.get(position).getDate());
        if(items.get(position).getType().equals("add"))
            holder.tp.setImageResource(R.drawable.transaction_plus_button);
        else
            holder.tp.setImageResource(R.drawable.transaction_minus_button);

        holder.layout.setOnClickListener(v -> listener.onClick(items.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
