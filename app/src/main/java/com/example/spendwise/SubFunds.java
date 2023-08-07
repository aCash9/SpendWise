package com.example.spendwise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;


public class SubFunds extends AppCompatActivity {
    EditText amount;
    EditText note;
    MaterialToolbar app;
    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout tnl;
    ArrayAdapter<String> adapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_funds);

        tnl = findViewById(R.id.textinputlayout);
        tnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.onEditorAction(EditorInfo.IME_ACTION_DONE);
                note.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

        //adding the top bar to go back to main page
        app = findViewById(R.id.topAppBar);
        app.setNavigationOnClickListener(v -> {
            finish();
        });

        //Getting the textview id
        amount = findViewById(R.id.amount);
        note = findViewById(R.id.note);

        //setting up the category drop down
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView4);
        String[] subfundsArray = getResources().getStringArray(R.array.subFundsItems);
        adapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_items_sub, subfundsArray);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Getting the category selected
                if(TextUtils.isEmpty(amount.getText().toString())){
                    amount.setError("Amount is required");
                    return;
                }
                String category = parent.getItemAtPosition(position).toString();

                //Getting the date of the transaction
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate = dateFormat.format(new Date());

                //Getting the amount
                int amt = Integer.parseInt(amount.getText().toString());

                //getting the note
                String nt = note.getText().toString();

                //Creating the transaction item
                TransactionDetails item;
                item = new TransactionDetails(amt, nt, currentDate, "sub",category);
                dataBaseHelper dbhelper = new dataBaseHelper(SubFunds.this);
                boolean b = dbhelper.addItem(item);
                finish();
            }
        });
    }

}