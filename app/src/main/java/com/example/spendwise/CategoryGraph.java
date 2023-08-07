package com.example.spendwise;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class CategoryGraph extends AppCompatActivity {
    PieChart pieChart;
    MaterialToolbar app;
    int[] colors = new int[]{Color.RED, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.YELLOW};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_graph);

        //adding the top bar to go back to main page
        app = findViewById(R.id.topAppBar);
        app.setNavigationOnClickListener(v -> {
            finish();
        });

        //setting up the pie chart
        pieChart = findViewById(R.id.pieChart);
        PieDataSet pieDataSet = new PieDataSet(dataValue(), "");
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setValueTextSize(14f);
        pieChart.animateY(2000, Easing.EaseInOutQuad);
        pieChart.animateX(2000, Easing.EaseInOutQuad);

        Description description = pieChart.getDescription();
        description.setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText("Expense Chart");
    }

    private List<PieEntry> dataValue() {
        List<PieEntry> list = new ArrayList<>();
        dataBaseHelper db = new dataBaseHelper(this);
        PriorityQueue<categoryDetails> pq = db.getCategoryData();
        for(categoryDetails cd : pq) {
            list.add(new PieEntry(cd.getCategoryAmount(), cd.getCategoryName()));
        }
        return list;
    }
}