package com.example.dietycare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;


//TODO: add limit to each field
//TODO: 
public class editActivity extends AppCompatActivity{
    private HashMap<String, String> readInfo = new HashMap<String, String>();
    private static final int SELECT_PICTURE = 1;
    private TextView textGet;
    private String bodyT;
    private int BYear, BMonth, BDay;
    private String Sex = "Male";
    private String checkVeg = "False";

    String[] bodyType = {"Ectomorph", "Endomorph", "Mesomorph"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Spinner sp = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bodyType);
        sp.setAdapter(adapter);
        EditText weight = findViewById(R.id.editTextNumber5);
        EditText BFat= findViewById(R.id.editTextNumber6);
        EditText Tweight = findViewById(R.id.editTextNumber7);
        EditText height = findViewById(R.id.editTextNumber8);
        textGet = findViewById(R.id.textView);

        RadioButton Yes = findViewById(R.id.isVeg);
        RadioButton No = findViewById(R.id.notVeg);

        RadioButton maleB = findViewById(R.id.radioMale);
        RadioButton femaleB = findViewById(R.id.radioFemale);

        File path = getApplicationContext().getFilesDir();
        File read = new File(path, "personInfo.txt");
        try{
            Scanner reader = new Scanner(read);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] temp = data.split("\\s+");
                readInfo.put(temp[0], temp[1]);
            }

            weight.setText(readInfo.get("Weight"));
            BFat.setText(readInfo.get("Body_Fat"));
            Tweight.setText(readInfo.get("Target_Weight"));
            height.setText(readInfo.get("Height"));

            BYear = Integer.valueOf(readInfo.get("Year"));
            BMonth = Integer.valueOf(readInfo.get("Month"));
            BDay = Integer.valueOf(readInfo.get("Day"));

            textGet.setText(readInfo.get("Year") + "." + readInfo.get("Month") + "." + readInfo.get("Day"));

            int index = 0;


            if(readInfo.get("Body_Type").equals("Endomorph")){
                index = 1;
            }
            if(readInfo.get("Body_Type").equals("Mesomorph")){
                index = 2;
            }

            sp.setSelection(index);

            if(readInfo.get("Vegetarian").equals("True")) {
                Yes.setChecked(true);
                No.setChecked(false);
            }else{
                No.setChecked(true);
                Yes.setChecked(false);
            }

            if(readInfo.get("Sex").equals("Male")) {
                maleB.setChecked(true);
                femaleB.setChecked(false);
            }else{
                femaleB.setChecked(true);
                maleB.setChecked(false);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        ImageButton btn_register = findViewById(R.id.imageButton2);
        ImageButton btn_Cal = findViewById(R.id.btncal);
        ImageButton btn_gallery = findViewById(R.id.imageButton3);
        Button btn_save = findViewById(R.id.saveBtn);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bodyT = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_save.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*
                try {
                    FileOutputStream fileout=openFileOutput("personInfo.txt", MODE_PRIVATE);
                    OutputStreamWriter infoWriter=new OutputStreamWriter(fileout);
                    infoWriter.write("Year: " + BYear);
                    infoWriter.write("Month: " + BMonth);
                    infoWriter.write("Day: " + BDay);
                    infoWriter.write("Sex: " + Sex);
                    infoWriter.write("Height: " + height.getText());
                    infoWriter.write("Weight: " + weight.getText());
                    infoWriter.write("Target_Weight: " + Tweight.getText());
                    infoWriter.write("Body_Fat: " + BFat.getText());
                    infoWriter.write("Body_Type: " + bodyT);
                    infoWriter.write("Vegetarian: " + checkVeg);
                    infoWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
                File path = getApplicationContext().getFilesDir();
                try {
                    FileOutputStream writer = new FileOutputStream((new File(path, "personInfo.txt")));
                    OutputStreamWriter infoWriter=new OutputStreamWriter(writer);
                    infoWriter.write("Year " + BYear);
                    infoWriter.write(10);
                    infoWriter.write("Month " + BMonth);
                    infoWriter.write(10);
                    infoWriter.write("Day " + BDay);
                    infoWriter.write(10);
                    infoWriter.write("Sex " + Sex);
                    infoWriter.write(10);
                    infoWriter.write("Height " + height.getText());
                    infoWriter.write(10);
                    infoWriter.write("Weight " + weight.getText());
                    infoWriter.write(10);
                    infoWriter.write("Target_Weight " + Tweight.getText());
                    infoWriter.write(10);
                    infoWriter.write("Body_Fat " + BFat.getText());
                    infoWriter.write(10);
                    infoWriter.write("Body_Type " + bodyT);
                    infoWriter.write(10);
                    infoWriter.write("Vegetarian " + checkVeg);
                    infoWriter.close();
                    writer.close();
                    Toast.makeText(getApplicationContext(), "Wrote to file", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_Cal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBir();
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
            }
        });
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    Sex = "Male";
                break;
            case R.id.radioFemale:
                if (checked)
                    Sex = "Female";
                break;
            case R.id.isVeg:
                if (checked)
                    checkVeg = "True";
                break;
            case R.id.notVeg:
                if (checked)
                    checkVeg = "False";
                break;
        }
    }

    private void chooseBir() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_date, null);
        final DatePicker dateGet = (DatePicker) view.findViewById(R.id.st);
        dateGet.updateDate(dateGet.getYear(), dateGet.getMonth(), 01);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose");
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int month = dateGet.getMonth() + 1;
                String st = "" + dateGet.getYear() + "." + month + "." + dateGet.getDayOfMonth();
                BYear = dateGet.getYear();
                BMonth = month;
                BDay = dateGet.getDayOfMonth();
                textGet.setText(st);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}