package com.example.spendwise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Collections;
import java.util.List;

public class transactionHistoryPage extends AppCompatActivity implements TransactionSelectListener {
    MaterialToolbar app;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_page);

        //adding the top bar to go back to main page
        app = findViewById(R.id.topAppBar);
        app.setNavigationOnClickListener(v -> {
            finish();
        });

        //showing the transaction history as a list
        dataBaseHelper db = new dataBaseHelper(this);
        List<TransactionDetails> list = db.getData();
        recyclerView = findViewById(R.id.recyclerview);
        Log.d("mytag", "onCreate: sending list for display");
        Collections.reverse(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TransactionAdapter(getApplicationContext(), list, this));


    }

    @Override
    public void onClick(TransactionDetails item) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);

        //getting the elements
        ImageButton bin = dialog.findViewById(R.id.bin);
        TextView amount = dialog.findViewById(R.id.amount);
        TextView date = dialog.findViewById(R.id.date);
        TextView category = dialog.findViewById(R.id.category);
        TextView content = dialog.findViewById(R.id.content);
        ImageView type = dialog.findViewById(R.id.type);

        //setting the values to the dialogBox
        amount.setText("₹" + String.valueOf(item.getAmount()));
        date.setText(item.getDate());
        category.setText(item.getCategory());
        if(item.getNote().equals(null))
            content.setText("No note saved");
        else
            content.setText(item.getNote());
        if(item.getType().equals("add"))
            type.setImageResource(R.drawable.transaction_plus_button);
        else
            type.setImageResource(R.drawable.transaction_minus_button);

        //delete button
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper db = new dataBaseHelper(transactionHistoryPage.this);
                db.deleteEntry(item);
                List<TransactionDetails> list = db.getData();
                recyclerView = findViewById(R.id.recyclerview);
                Log.d("mytag", "onCreate: sending list for display");
                Collections.reverse(list);
                recyclerView.setLayoutManager(new LinearLayoutManager(transactionHistoryPage.this));
                recyclerView.setAdapter(new TransactionAdapter(getApplicationContext(), list, transactionHistoryPage.this));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}