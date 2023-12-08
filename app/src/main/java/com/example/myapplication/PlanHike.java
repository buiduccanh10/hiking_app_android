package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.DatabaseHelper.SQLiteHelper;

import java.util.Calendar;

public class PlanHike extends AppCompatActivity {

    EditText hike_name ;
    EditText hike_description ;
    EditText from ;
    EditText to ;
    EditText hike_length ;
    EditText hike_duration ;
    Spinner dropdown;
    RadioGroup radioGroupParking;
    RadioButton radioButton;
    private SQLiteHelper databaseHelper;
    private String selectedDate;
    private String parking;
    private Boolean is_edit;

    private String _name,_des,_from,_to,_length,_duration,_park,_level,_date;
    private int _id;

    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_hike);

        Toolbar toolbar = findViewById(R.id.toolbar_plan);
        setSupportActionBar(toolbar);

        databaseHelper = new SQLiteHelper(this);

        dropdown = findViewById(R.id.dropdown_menu);
        String[] items = new String[]{"Very hard","Hard", "Normal", "Easy"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        radioGroupParking = findViewById(R.id.radio_group);
        radioGroupParking.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                if (radioButton.getText().equals("Yes")) {
                    parking = "Yes";
                }
                if (radioButton.getText().equals("No")) {
                    parking = "No";
                }
            }
        });

        Intent intent = getIntent();
        is_edit = intent.getBooleanExtra("is_edit",false);
        if(is_edit){
            getSupportActionBar().setTitle("Edit hike");
            _id = intent.getIntExtra("hikeId",-1);
            _name = intent.getStringExtra("hikeName");
            _des = intent.getStringExtra("hikeDes");
            _from = intent.getStringExtra("hikeFrom");
            _to = intent.getStringExtra("hikeTo");
            _length=intent.getStringExtra("hikeLength");
            _duration=intent.getStringExtra("hikeDuration");
            _park = intent.getStringExtra("hikePark");
            _level = intent.getStringExtra("hikeLevel");
            _date = intent.getStringExtra("hikeDate");

            hike_name = findViewById(R.id.editTextHikeName);
            hike_description = findViewById(R.id.editTextDescription);
            from = findViewById(R.id.editTextLocationFrom);
            to = findViewById(R.id.editTextLocationTo);
            hike_length = findViewById(R.id.editTextLength);
            hike_duration = findViewById(R.id.editTextDuration);
            dropdown = findViewById(R.id.dropdown_menu);


            hike_name.setText(_name);
            hike_description.setText(_des);
            from.setText(_from);
            to.setText(_to);
            hike_length.setText(_length);
            hike_duration.setText(_duration);

            if(_park.equals("Yes")){
                radioGroupParking.check(R.id.radio_yes);
            }
            if(_park.equals("No")){
                radioGroupParking.check(R.id.radio_no);
            }
//
            if (_level != null) {
                if (_level.equals("Very hard")) {
                    dropdown.setSelection(0);
                } else if (_level.equals("Hard")) {
                    dropdown.setSelection(1);
                } else if (_level.equals("Normal")) {
                    dropdown.setSelection(2);
                } else if (_level.equals("Easy")) {
                    dropdown.setSelection(3);
                }
            }

            Log.d("YourTag", "Hike Name: " + _name.toString());
            Log.d("YourTag", "Hike Description: " + _des.toString());
            Log.d("YourTag", "From: " + _from.toString());
            Log.d("YourTag", "To: " + _to.toString());
            Log.d("YourTag", "Hike Length: " + _length.toString());
            Log.d("YourTag", "Hike Duration: " + _duration.toString());
            Log.d("YourTag", "Date: " + _date.toString());
            Log.d("YourTag", "Level: " + _level.toString());
            Log.d("YourTag", "Parking: " + _park.toString());
        }else {
            getSupportActionBar().setTitle("Hike add");
        }

    }

    public void save_click(View view) {
        try {
            hike_name = findViewById(R.id.editTextHikeName);
            hike_description = findViewById(R.id.editTextDescription);
            from = findViewById(R.id.editTextLocationFrom);
            to = findViewById(R.id.editTextLocationTo);
            hike_length = findViewById(R.id.editTextLength);
            hike_duration = findViewById(R.id.editTextDuration);
            dropdown = findViewById(R.id.dropdown_menu);

            Log.d("YourTag", "Hike Name: " + hike_name.getText().toString());
            Log.d("YourTag", "Hike Description: " + hike_description.getText().toString());
            Log.d("YourTag", "From: " + from.getText().toString());
            Log.d("YourTag", "To: " + to.getText().toString());
            Log.d("YourTag", "Hike Length: " + hike_length.getText().toString());
            Log.d("YourTag", "Hike Duration: " + hike_duration.getText().toString());
            Log.d("YourTag", "Level: " + dropdown.getSelectedItem().toString());
            Log.d("YourTag", "Parking: " + parking.toString());
            Log.d("YourTag", "Date: " + selectedDate.toString());


            String hikeName = hike_name.getText().toString();
            String description = hike_description.getText().toString();
            String locationFrom = from.getText().toString();
            String locationTo = to.getText().toString();
            String hike_date = selectedDate.toString();
            String length = hike_length.getText().toString();
            String duration = hike_duration.getText().toString();
            String is_parking = parking.toString();
            String level = dropdown.getSelectedItem().toString();

            String message = "Hike Name:  " + hikeName + "\n"
                    + "Description:  " + description + "\n"
                    + "From:  " + locationFrom + "\n"
                    + "To:  " + locationTo + "\n"
                    + "Hike date:  " + hike_date + "\n"
                    + "Hike Length:  " + length + "\n"
                    + "Hike Duration:  " + duration + "\n"
                    + "Level:  " + level;
            if (is_edit) {
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Confirm Edit");
                dialog.setMessage(message);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            databaseHelper.editHikeById(_id, hikeName, description, locationFrom, locationTo,
                                    hike_date, length, duration, level, is_parking);
                            Intent intent = new Intent(PlanHike.this, MainActivity.class);
                            intent.putExtra("refreshList", true);
                            startActivity(intent);
                            Toast.makeText(PlanHike.this, "Hike data edited!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(PlanHike.this, "Error edit hike data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            } else {
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Confirm Save");
                dialog.setMessage(message);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int insertedId=databaseHelper.plan_hike( hikeName, description, locationFrom, locationTo,
                                    hike_date, length, duration, level, is_parking);
                            Intent intent = new Intent(PlanHike.this, AddObservation.class);
                            intent.putExtra("refreshList", true);
                            intent.putExtra("hikeId",insertedId);
                            startActivity(intent);
//
                            Log.d("YourTag", "Id press: " + insertedId);
                            Toast.makeText(PlanHike.this, "Hike data saved!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(PlanHike.this, "Error save hike data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        }catch (Exception e){
            Log.d("YourTag", "error: " + e);
            Toast.makeText(PlanHike.this, "Please check and fill all the fields", Toast.LENGTH_SHORT).show();
        }

            //}
    }


    public void show_calendar(View view) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = String.format("%d/%d/%d", dayOfMonth, (month + 1), year);
                Toast.makeText(PlanHike.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        }, year, month, day);

        if (_date != null) {
            String[] dateParts = _date.split("/");
            int selectedYear = Integer.parseInt(dateParts[2]);
            int selectedMonth = Integer.parseInt(dateParts[1]) - 1;
            int selectedDay = Integer.parseInt(dateParts[0]);
            datePickerDialog.updateDate(selectedYear, selectedMonth, selectedDay);
        }

        datePickerDialog.show();

    }

}
