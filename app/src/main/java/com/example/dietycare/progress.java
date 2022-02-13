package com.example.dietycare;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

@RequiresApi(api = Build.VERSION_CODES.O)
public class progress extends AppCompatActivity {

    private PieChart pie_chart;
    private PieData pie_data;
    private LineChart line_chart;
    private LineData line_data;
    private LineDataSet line_data_set;
    private ArrayList Cal_entries_ArrayList, Fat_entries_ArrayList, Ptn_entries_ArrayList, CHO_entries_ArrayList;
    private float Fat_consumed_total = 0, Ptn_consumed_total = 0, CHO_consumed_total = 0;
    private Spinner bar_graph_sp;
    private String bar_selection = "calories";
    private TextView bar_chart_title;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    DateTimeFormatter Localformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private progressHandler handler;
    private LocalDate today;
    private String URLParam;
    private ImageButton home_btn, meal_btn, community_btn, account_btn;
    private TextView begining_date, ending_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        handler = new progress.progressHandler();
        today = LocalDate.now();
        System.out.println(today);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -3);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);

        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                // on below line we are adding a range
                // as start date and end date to our calendar.
                .range(startDate, endDate)
                // on below line we are providing a number of dates
                // which will be visible on the screen at a time.
                .datesNumberOnScreen(7)
                // at last we are calling a build method
                // to build our horizontal recycler view.
                .build();
        // on below line we are setting calendar listener to our calendar view.
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                // on below line we are printing date
                // in the logcat which is selected.
                Date selectedDate = date.getTime();
                begining_date.setText(formatter.format(selectedDate));
                URLParam = String.format("start_date=%s&end_date=%s&user_id=123", formatter.format(selectedDate), today.format(Localformatter));
                URLParam = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/logGeneration?" + URLParam;
                get_nutrient_info(handler, URLParam);
            }
        });



        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        pie_chart = findViewById(R.id.latest_calorie_intake_pie_chart);
        line_chart = findViewById(R.id.latest_calorie_intake_LineChart);

        // Spinner
        String[] bodyType = {"Calories", "Fat", "Protein", "Carbohydrate"};
        bar_graph_sp = (Spinner) findViewById(R.id.spinner_bar_graph);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bodyType);
        bar_graph_sp.setAdapter(adapter);
        bar_graph_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bar_selection = (String) parent.getItemAtPosition(position);
                bar_selection = bar_selection.toLowerCase();
                set_bar_title();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        button_menu_creation();
        //chart creation
        LocalDate today = LocalDate.now();
        LocalDate selectedDate = today.minusDays(7);
        URLParam = String.format("start_date=%s&end_date=%s&user_id=123", selectedDate.format(Localformatter), today.format(Localformatter));
        URLParam = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/logGeneration?" + URLParam;
        get_nutrient_info(handler, URLParam);
        setupPieChart();// this is not in the handler, thus need to call it

        //inital date setup
        begining_date = findViewById(R.id.line_graph_bigining_date);
        ending_date = findViewById(R.id.line_graph_ending_date);
        begining_date.setText(selectedDate.format(Localformatter));
        ending_date.setText(today.format(Localformatter));

    }
    private void button_menu_creation(){
        home_btn = findViewById(R.id.menu_btn_home_progress);
        meal_btn = findViewById(R.id.menu_btn_meal_progress);
        community_btn = findViewById(R.id.menu_btn_community_progress);
        account_btn = findViewById(R.id.main_btn_account_progress);

        home_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(progress.this, MainActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        meal_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(progress.this, MealRecommendationActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        community_btn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(progress.this, communityActivity.class);
                                                 //Starting of the Intent
                                                 startActivity(intent);
                                                 finish();
                                             }
                                         }
        );

        account_btn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent = new Intent(progress.this, accountActivity.class);
                                               //Starting of the Intent
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
        );
    }
    private void setupPieChart() {
        pie_chart.setDrawHoleEnabled(true);
        pie_chart.setUsePercentValues(true);
        pie_chart.setDrawEntryLabels(false);
        pie_chart.setCenterText("Nutrient Intake");
        pie_chart.setCenterTextSize(12);
        pie_chart.getDescription().setEnabled(false);
        Legend l = pie_chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTextSize(8);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Fat_consumed_total, "Fat"));
        entries.add(new PieEntry(Ptn_consumed_total, "Protein"));
        entries.add(new PieEntry(CHO_consumed_total, "Carbohydrate"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colors);

        pie_data = new PieData(dataSet);
        pie_data.setDrawValues(true);
        pie_data.setValueFormatter(new PercentFormatter(pie_chart));
        pie_data.setValueTextSize(12f);
        pie_data.setValueTextColor(Color.BLACK);

        pie_chart.setData(pie_data);
        pie_chart.invalidate();

    }

    private void setup_line_chart(String graphType){
        setup_line_chart_value(graphType);
        line_data_set.setDrawValues(false);
        line_chart.getDescription().setEnabled(false);
        line_chart.getXAxis().setDrawLabels(false);
        line_chart.getDescription().setEnabled(false);
        Legend l = line_chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setEnabled(true);
    }

    private void setup_line_chart_value(String graphType){
        bar_chart_title = findViewById(R.id.text_latest_intake);
        switch (graphType){
            case "calories":
                line_data_set = new LineDataSet(Cal_entries_ArrayList, "Unit: Kcal");
                bar_chart_title.setText("Calories Intake");
                break;
            case "fat":
                line_data_set = new LineDataSet(Fat_entries_ArrayList, "Unit: Gram");
                bar_chart_title.setText("Fat Intake");
                break;
            case "protein":
                line_data_set = new LineDataSet(Ptn_entries_ArrayList, "Unit: Gram");
                bar_chart_title.setText("Protein Intake");
                break;
            case "carbohydrate":
                line_data_set = new LineDataSet(CHO_entries_ArrayList, "Unit:Gram");
                bar_chart_title.setText("Carbohydrate Intake");
                break;
        }
        line_data = new LineData(line_data_set);
        line_chart.clear();
        line_chart.setData(line_data);
        line_chart.invalidate();
    }

    private void getBarEntries(ArrayList<double[]> NutrientArray){
        Cal_entries_ArrayList = new ArrayList<>();
        Fat_entries_ArrayList = new ArrayList<>();
        Ptn_entries_ArrayList = new ArrayList<>();
        CHO_entries_ArrayList = new ArrayList<>();

        for (int i = 0; i < NutrientArray.size(); i++){
            //the format for the double[] is {calories, fat, protein, carbohydrate}
            Cal_entries_ArrayList.add(new BarEntry(i + 1, (float) NutrientArray.get(i)[0]));
            Fat_entries_ArrayList.add(new BarEntry(i + 1, (float) NutrientArray.get(i)[1]));
            Ptn_entries_ArrayList.add(new BarEntry(i + 1, (float) NutrientArray.get(i)[2]));
            CHO_entries_ArrayList.add(new BarEntry(i + 1, (float) NutrientArray.get(i)[3]));
        }

    }

    public String requestData(String inputURL, String reqMethod) {
        HttpURLConnection connection;
        InputStream in;
        InputStreamReader inReader;
        BufferedReader br;
        try {
            URL url = new URL(inputURL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(reqMethod);

            connection.connect();
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);

            in = connection.getInputStream();
            inReader = new InputStreamReader(in);
            br = new BufferedReader(inReader);
            String s;
            StringBuffer strBuf = new StringBuffer();
            while((s = br.readLine()) != null) {
                strBuf.append(s);
            }
            connection.disconnect();
            return strBuf.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "None";
    }

    private void get_nutrient_info(progress.progressHandler handler, String paramURL){

        Fat_consumed_total = 0;
        Ptn_consumed_total = 0;
        CHO_consumed_total = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {

                String output = requestData(paramURL, "GET");
                Message msg = handler.obtainMessage();
                msg.obj = output;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void set_bar_title(){
        try {
            setup_line_chart(bar_selection);
        } catch (Exception e) {
            e.printStackTrace();
            bar_chart_title.setText("Something went wrong.");
        }
    }
    class progressHandler extends Handler {
        public String outputStr;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            outputStr = msg.obj.toString();
            //outputStr=outputStr.replace('[','{');
            //outputStr=outputStr.replace(']','}');

            try {
                //JSONObject jobj = new JSONObject(outputStr);
                double Cal_intake, Fat_intake, Ptn_intake, CHO_intake;
                JSONArray JNutrientArray = new JSONArray(outputStr);
                ArrayList<double[]> NutrientArray = new ArrayList<>();

                for (int i = 0; i < JNutrientArray.length(); i++){
                    if (JNutrientArray.getString(i).equals("Get DailyNutrient failed")){

                        NutrientArray.add(new double[]{0,0,0,0});
                    }else{
                        JSONObject jNutrient = new JSONObject(JNutrientArray.getString(i));
                        Cal_intake = Double.parseDouble(jNutrient.get("cal_consumed").toString());
                        Fat_intake = Double.parseDouble(jNutrient.get("fat_consumed").toString());
                        Ptn_intake = Double.parseDouble(jNutrient.get("pro_consumed").toString());
                        CHO_intake = Double.parseDouble(jNutrient.get("CHO_consumed").toString());
                        Fat_consumed_total += Fat_intake;
                        Ptn_consumed_total += Ptn_intake;
                        CHO_consumed_total += CHO_intake;
                        NutrientArray.add(new double[]{Cal_intake,Fat_intake,Ptn_intake,CHO_intake});
                    }
                }
                loadPieChartData();
                getBarEntries(NutrientArray);
                set_bar_title();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
