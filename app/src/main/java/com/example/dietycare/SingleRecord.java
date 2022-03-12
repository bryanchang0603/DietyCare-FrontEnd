package com.example.dietycare;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class SingleRecord {

    private static ArrayList<SingleRecord> records = new ArrayList<>();
    //declare private data instead of public to ensure the privacy of data field of each class
    private String date;
    private String meal;
    private String food;
    private String amount;

    public SingleRecord(String date, String meal, String food, String amount) {
        this.date = date;
        this.meal = meal;
        this.food = food;
        this.amount = amount;
    }

    //retrieve date
    public String getDate(){
        return date;
    }

    //retrieve meal
    public String getMeal(){
        return meal;
    }

    //retrieve food
    public String getFood(){
        return food;
    }

    //retrieve amount
    public String getAmount(){
        return amount;
    }


    public static ArrayList<SingleRecord> getRecords(String user_id) throws Exception {
        String baseUrl = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com";
        String path = "/dishIntakeList?";
        //get current date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-M-d");
        String formatted_date = format1.format(cal.getTime());
        String params = "user_id="+user_id+"&" + "intake_date="+formatted_date;
        get(baseUrl + path + params);
        return records;
    }

    public static void get(String uri) throws Exception {
        URL url = new URL(uri) ;
        // Open a connection(?) on the URL(??) and cast the response(???)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestMethod("GET");

        // This line makes the request
        InputStream responseStream = connection.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(responseStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }

        String response = buf.toString("UTF-8");
        JSONArray arr = new JSONArray(response);
        for(int i = 0; i < arr.length(); i++){
            JSONObject obj = arr.getJSONObject(i);
            String obj_date = obj.getString("intake_date");
            String obj_meal = obj.getString("meal_type");
            String obj_food = obj.getString("food_name");
            String obj_amount = obj.getString("food_intake_amount");
            records.add(new SingleRecord(obj_date,obj_meal,obj_food,obj_amount));
        }
    }
}