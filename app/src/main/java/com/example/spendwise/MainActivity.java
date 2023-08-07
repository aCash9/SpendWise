package com.example.spendwise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView availableBalance;
    private TextView firstexp;
    private TextView secondexp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showGreeting();

        //Getting the button ids
        ImageButton addFunds = findViewById(R.id.addFunds_button);
        ImageButton subFunds = findViewById(R.id.subtractFunds_button);
        Button showGraph = findViewById(R.id.showAll_button);
        Button transactionHistory = findViewById(R.id.showTransactions);
        firstexp = findViewById(R.id.firstExpense);
        secondexp = findViewById(R.id.secondExpense);

        //code to get the user name and display it on the card
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            showNameInputDialog();
        } else {
            // Name already exists, retrieve and display it
            String userName = sharedPreferences.getString("userName", "");
            userName = userName.toUpperCase();
            TextView textView = findViewById(R.id.username);
            textView.setText(userName);
        }

        //setting up the available balance and updating it
        availableBalance = findViewById(R.id.availableBalance);
        dataBaseHelper db = new dataBaseHelper(this);
        availableBalance.setText(db.getLastRowData(dataBaseHelper.KEY_BALANCE));
        availableBalance.setText(db.getLastRowData(dataBaseHelper.KEY_BALANCE));

        //setting up the top category
        PriorityQueue<categoryDetails> pq = db.getCategoryData();
        categoryDetails cd1 = pq.poll();
        categoryDetails cd2 = pq.poll();
        if(cd1.getCategoryAmount() != 0 && cd2.getCategoryAmount() == 0) {
            firstexp.setText(cd1.getCategoryName());
            secondexp.setText("");
        }
        if (cd1.getCategoryAmount() != 0 && cd2.getCategoryAmount() != 0) {
            firstexp.setText(cd1.getCategoryName());
            secondexp.setText(cd2.getCategoryName());
        }

        //setting up the on click listener for all the buttons
        addFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFunds.class);
                startActivity(intent);
            }
        });
        subFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubFunds.class);
                startActivity(intent);
            }
        });
        transactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, transactionHistoryPage.class);
                Log.d("mytag", "showList: success");
                startActivity(intent);
            }
        });
        showGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryGraph.class);
                Log.d("mytag", "showList: success");
                startActivity(intent);
            }
        });
    }


    //Code to update the greeting
    private void showGreeting() {
        TextView greeting = findViewById(R.id.greeting);
        // Get the current hour of the day
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        // Set the greeting based on the current hour
        if (currentHour >= 0 && currentHour < 12) {
            greeting.setText("Good Morning");
        } else if (currentHour >= 12 && currentHour < 18) {
            greeting.setText("Good Afternoon");
        } else {
            greeting.setText("Good Evening");
        }
    }


    //code for input dialog box
    private void showNameInputDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_name_box);
        dialog.setCancelable(false);

        TextView name = dialog.findViewById(R.id.name);
        Button submit = dialog.findViewById(R.id.submitButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString().trim();
                if (!userName.isEmpty()) {
                    // Store the entered name in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", userName);
                    editor.putBoolean("isFirstTime", false);
                    editor.apply();

                    // Display the name in the appropriate TextView
                    TextView textView = findViewById(R.id.username);
                    userName = userName.toUpperCase();
                    textView.setText(userName);

                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    //updating the available balance on every refresh
    @Override
    protected void onResume() {
        super.onResume();
        dataBaseHelper db = new dataBaseHelper(this);
        if (db.getLastRowData(dataBaseHelper.KEY_BALANCE) == null)
            availableBalance.setText("₹0");
        else
            availableBalance.setText("₹" + db.getLastRowData(dataBaseHelper.KEY_BALANCE));

        PriorityQueue<categoryDetails> pq = db.getCategoryData();
        categoryDetails cd1 = pq.poll();
        categoryDetails cd2 = pq.poll();
        if(cd1.getCategoryAmount() != 0 && cd2.getCategoryAmount() == 0) {
            firstexp.setText(cd1.getCategoryName());
            secondexp.setText("");
        }
        if (cd1.getCategoryAmount() != 0 && cd2.getCategoryAmount() != 0) {
            firstexp.setText(cd1.getCategoryName());
            secondexp.setText(cd2.getCategoryName());
        }
    }
}