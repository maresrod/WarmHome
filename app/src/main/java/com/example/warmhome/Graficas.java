package com.example.warmhome;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class Graficas extends AppCompatActivity {
    private LineChart liner1;
    private LineChart liner2;
    private LineChart liner3;
    private LineChart liner4;
    private PieChart pieChart;

    double media;
    double media2;
    double media3;
    double media4;
    double total;
    private String[] semanas = new String[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
    private String[] hab = new String[]{"Baño", "Cocina", "Salón", "Habitación"};
    private double[] date = new double[]{18.23, 20.32, 19.7, 21.9, 18.07, 20.87, 22.06};
    private double[] date2 = new double[]{20.15, 19.32, 17.9, 20.9, 19.13, 19.87, 20.16};
    private double[] date3 = new double[]{17.23, 19.95, 18.7, 19.19, 17.57, 19.37, 21.06};
    private double[] date4 = new double[]{19.23, 20.12, 19.13, 18.9, 19.07, 20.57, 20.06};

    private int[] color = new int[]{Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.GREEN};

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.graficas);
        liner1 = (LineChart) findViewById(R.id.baño);
        liner2 = (LineChart) findViewById(R.id.cocina);
        liner3 = (LineChart) findViewById(R.id.habitacion);
        liner4 = (LineChart) findViewById(R.id.salon);
        pieChart = (PieChart) findViewById(R.id.comparacion);

        createChart();


    }

    private Chart getSameChart(Chart chart, String description, int textcolor, int background, int animacion) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animacion);
        return chart;
    }
    private Chart getSameChart2(Chart chart, String description, int textcolor, int background, int animacion) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animacion);
        legenda(chart);
        return chart;
    }

    private void legenda(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < color.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor=color[i];
            entry.label = hab[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<Entry> getLinerEntry() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < date.length; i++) {
            entries.add(new BarEntry(i, (float) date[i]));

        }
        return entries;
    }
    private ArrayList<Entry> getLinerEntry2() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < date3.length; i++) {
            entries.add(new BarEntry(i, (float) date3[i]));

        }
        return entries;
    }
    private ArrayList<Entry> getLinerEntry3() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < date4.length; i++) {
            entries.add(new BarEntry(i, (float) date4[i]));

        }
        return entries;
    }
    private ArrayList<Entry> getLinerEntry4() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < date2.length; i++) {
            entries.add(new BarEntry(i, (float) date2[i]));

        }
        return entries;
    }

    private ArrayList<PieEntry> getPieEntry() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < date2.length-1; i=i+2) {

          media=(media+date[i]+date[i+1]);
          media2=(media2+date2[i]+date2[i+1]);
          media3=(media3+date3[i]+date3[i+1]);
          media4=(media4+date4[i]+date4[i+1]);


        }
        Log.i("medias",String.valueOf(media));
        Log.i("medias",String.valueOf(media2));
        Log.i("medias",String.valueOf(media3));
        Log.i("medias",String.valueOf(media4));
        media=media/7;
        media2=media2/7;
        media3=media3/7;
        media4=media4/7;
        total = media+media4+media3+media2;

        media=(media*100)/total;
        media2=(media2*100)/total;
        media3=(media3*100)/total;
        media4=(media4*100)/total;
        Log.i("mediasf",String.valueOf(media));
        Log.i("mediasf",String.valueOf(media2));
        Log.i("mediasf",String.valueOf(media3));
        Log.i("mediasf",String.valueOf(media4));
        double[] date1 = new double[]{media,media2,media3,media4};
        for (int a=0;a<date1.length;a++){
            entries.add(new PieEntry((float)date1[a]));
        }
        return entries;
    }

    private void axisX(XAxis axis) {
        axis.setGranularityEnabled(true);
        axis.setTextSize(10);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(semanas));
    }

    private void axisLeft(YAxis axis) {
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
        axis.setMaxWidth(15);
    }

    private void axisRight(YAxis axis) {
        axis.setEnabled(false);
    }

    public void createChart() {
        liner1 = (LineChart) getSameChart(liner1, "Baño", Color.BLACK, Color.WHITE, 3000);
        liner1.setDrawGridBackground(true);
        liner1.setDrawBorders(true);

        liner2 = (LineChart) getSameChart(liner2, "Cocina", Color.BLACK, Color.WHITE, 3000);
        liner2.setDrawGridBackground(true);
        liner2.setDrawBorders(true);

        liner3 = (LineChart) getSameChart(liner3, "Habitación", Color.BLACK, Color.WHITE, 3000);
        liner3.setDrawGridBackground(true);
        liner3.setDrawBorders(true);

        liner4 = (LineChart) getSameChart(liner4, "Salón", Color.BLACK, Color.WHITE, 3000);
        liner4.setDrawGridBackground(true);
        liner4.setDrawBorders(true);

        axisX(liner1.getXAxis());
        axisX(liner2.getXAxis());
        axisX(liner3.getXAxis());
        axisX(liner4.getXAxis());
        axisLeft(liner1.getAxisLeft());
        axisLeft(liner2.getAxisLeft());
        axisLeft(liner3.getAxisLeft());
        axisLeft(liner4.getAxisLeft());
        axisRight(liner1.getAxisRight());
        axisRight(liner2.getAxisRight());
        axisRight(liner3.getAxisRight());
        axisRight(liner4.getAxisRight());

        pieChart = (PieChart) getSameChart2(pieChart, "Comparación", Color.BLACK, Color.WHITE, 4000);
        pieChart.setHoleRadius(5);
        pieChart.setTransparentCircleRadius(7);

        liner1.setData(getLineData());
        liner1.invalidate();

        liner2.setData(getLineData2());
        liner2.invalidate();

        liner3.setData(getLineData3());
        liner3.invalidate();

        liner4.setData(getLineData4());
        liner4.invalidate();

        pieChart.setData(getPieData());
        pieChart.invalidate();

    }

    private DataSet getData(DataSet dataSet) {
        dataSet.setColor(Color.RED);
        dataSet.setValueTextSize(8);

        dataSet.setValueTextColor(Color.BLACK);
        return dataSet;

    }
    private DataSet getData2(DataSet dataSet) {
        dataSet.setColors(color);
        dataSet.setValueTextSize(8);

        dataSet.setValueTextColor(Color.BLACK);
        return dataSet;

    }

    private LineData getLineData2() {
        LineDataSet lineDataSet = (LineDataSet) getData(new LineDataSet(getLinerEntry2(), ""));
        lineDataSet.setLineWidth(5);
        return new LineData(lineDataSet);
    }
    private LineData getLineData3() {
        LineDataSet lineDataSet = (LineDataSet) getData(new LineDataSet(getLinerEntry3(), ""));
        lineDataSet.setLineWidth(5);
        LineData lineData = new LineData(lineDataSet);
        return lineData;
    }
    private LineData getLineData4() {
        LineDataSet lineDataSet = (LineDataSet) getData(new LineDataSet(getLinerEntry4(), ""));
        lineDataSet.setLineWidth(5);
        LineData lineData = new LineData(lineDataSet);
        return lineData;
    }

    private LineData getLineData() {
        LineDataSet lineDataSet = (LineDataSet) getData(new LineDataSet(getLinerEntry(), ""));
        lineDataSet.setLineWidth(5);
        LineData lineData = new LineData(lineDataSet);
        return lineData;
    }
    private PieData getPieData() {
        PieDataSet pieDataSet = (PieDataSet) getData2(new PieDataSet(getPieEntry(), ""));
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueFormatter(new PercentFormatter());
        return new PieData(pieDataSet);

    }

}
