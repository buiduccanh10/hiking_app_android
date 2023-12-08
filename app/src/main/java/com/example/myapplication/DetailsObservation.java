package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.DatabaseHelper.SQLiteHelper;
import com.example.myapplication.Model.Hike;
import com.example.myapplication.Model.Observation;

import java.util.List;

public class DetailsObservation extends AppCompatActivity {

    private TextView obName;
    private TextView obTime;
    private ImageView obImage;
    private int _id;
    private SQLiteHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_observation);

        _id = getIntent().getIntExtra("obId", -1);
        if (_id != -1) {
            databaseHelper = new SQLiteHelper(this);
            Observation ob = databaseHelper.getObservationById(_id);

            if (ob != null) {
                obName = findViewById(R.id.name_ob_detail);
                obTime =findViewById(R.id.time_ob_detail);
                obImage=findViewById(R.id.image_ob_detail);
                Bitmap bitmap = BitmapFactory.decodeFile(ob.getOb_image());

                obName.setText(ob.getOb_name());
                obTime.setText(ob.getOb_time());
                obImage.setImageBitmap(bitmap);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar_ob_detail);
        setSupportActionBar(toolbar);

        if (obName != null) {
            getSupportActionBar().setTitle(obName.getText().toString());
        }

    }

}