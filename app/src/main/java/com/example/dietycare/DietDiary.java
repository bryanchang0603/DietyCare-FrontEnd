package com.example.dietycare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DietDiary extends AppCompatActivity {

    private ImageButton back_bt;
    private ListView listview;
    private ArrayList<SingleRecord> records_arr = new ArrayList<SingleRecord>();
    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_diary);

        //Back Button
        back_bt = (ImageButton) findViewById(R.id.diaryBack);
        back_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(DietDiary.this, progress.class);
                                           //Starting of the Intent
                                           startActivity(intent);

                                           //Prevent overlapping records
                                           adapter.clear();
                                       }
                                   }
        );

        //Set records list
        // Construct the data source
        Thread thread = new Thread() {
            public void run() {
                try {
                    records_arr = SingleRecord.getRecords();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create the adapter to convert the array to views
        adapter= new RecordAdapter(this, records_arr);
        // Attach the adapter to a ListView
        listview = findViewById(R.id.dailyRecord);
        listview.setAdapter(adapter);
    }
    public class RecordAdapter extends ArrayAdapter<SingleRecord>{
        public RecordAdapter(Context context, ArrayList<SingleRecord> records) {
            super(context, 0, records);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_record, parent, false);
            }

            // Get the data item for this position
            SingleRecord record = getItem(position);

            // Lookup view for data population
            TextView tvDate = (TextView) convertView.findViewById(R.id.r_date);
            TextView tvMeal = (TextView) convertView.findViewById(R.id.r_meal);
            TextView tvFood = (TextView) convertView.findViewById(R.id.r_Food);
            TextView tvAmount = (TextView) convertView.findViewById(R.id.r_amount);
            ImageButton btDelete = (ImageButton) convertView.findViewById(R.id.r_delete);
            // Populate the data into the template view using the data object
            tvDate.setText(record.getDate());
            tvMeal.setText(record.getMeal());
            tvFood.setText(record.getFood());
            tvAmount.setText(record.getAmount());

            // Cache row position inside the button using `setTag`
            btDelete.setTag(position);
            // Attach the click event handler
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    // Access the row position here to get the correct data item
                    SingleRecord record_to_delete = getItem(position);
                    //Delete record in db
                    String baseUrl = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com";
                    String path = "/dishIntake?";
                    String params = "user_id=123&" + "intake_date=2022-01-14&" + "meal_type=Lunch&" + "nth_dish_of_meal=2";
                    Thread thread = new Thread() {
                        public void run() {
                            try {
                                del(baseUrl + path + params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Delete selected record
                    records_arr.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

            // Return the completed view to render on screen
            return convertView;
        }

        public void del(String uri) throws Exception {
            URL url = new URL(uri) ;
            // Open a connection(?) on the URL(??) and cast the response(???)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Now it's "open", we can set the request method, headers etc.
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestMethod("DELETE");

            // This line makes the request
            InputStream responseStream = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(responseStream);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            for (int result = bis.read(); result != -1; result = bis.read()) {
                buf.write((byte) result);
            }
            String response = buf.toString("UTF-8");
        }
    }






}
