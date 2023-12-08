package com.example.myapplication;

import android.os.Bundle;

import com.example.myapplication.Adapter.ObservationAdapter;
import com.example.myapplication.DatabaseHelper.SQLiteHelper;
import com.example.myapplication.Model.Hike;
import com.example.myapplication.Model.Observation;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.List;


public class DetailsHike extends AppCompatActivity {
    private TextView hikeName;
    private TextView hikeDes;
    private TextView hikeFrom;
    private TextView hikeTo;
    private TextView hikeLength;
    private TextView hikeDuration;
    private TextView hikePark;
    private TextView hikeLevel;
    private TextView hikeDate;
    private int _id;
    private SQLiteHelper databaseHelper;
    ListView listView;
    private ObservationAdapter observationAdapter;
    private List<Observation> ob_hike_detail_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_hike);

        _id = getIntent().getIntExtra("hikeId", -1);

        if (_id != -1) {
            databaseHelper = new SQLiteHelper(this);
            Hike hike = databaseHelper.getHikeById(_id);

            if (hike != null) {
                hikeName = findViewById(R.id.detail_hike_name);
                hikeName.setText(hike.getHikeName()+" "+"details");

                hikeDes = findViewById(R.id.detail_hike_des);
                hikeDes.setText(hike.getDescription());

                hikeFrom = findViewById(R.id.detail_hike_from);
                hikeFrom.setText(hike.getLocationFrom());

                hikeTo = findViewById(R.id.detail_hike_to);
                hikeTo.setText(hike.getLocationTo());

                hikeLength = findViewById(R.id.detail_hike_length);
                hikeLength.setText(hike.getLength());

                hikeDuration = findViewById(R.id.detail_hike_duration);
                hikeDuration.setText(hike.getDuration());

                hikePark = findViewById(R.id.detail_hike_park);
                hikePark.setText(hike.getParking());

                hikeLevel = findViewById(R.id.detail_hike_level);
                hikeLevel.setText(hike.getLevel());

                hikeDate = findViewById(R.id.detail_hike_date);
                hikeDate.setText(hike.getDate());
            }

            ob_hike_detail_list = databaseHelper.getAllObservationsByHikeId(_id);

            listView = findViewById(R.id.listview_ob_hike_detail);

            observationAdapter = new ObservationAdapter(this, R.layout.observation_list_item, ob_hike_detail_list,databaseHelper);
            listView.setAdapter(observationAdapter);

//            obHikeAdapter.clear();
//            obHikeAdapter.addAll(ob_hike_detail_list);

            observationAdapter.notifyDataSetChanged();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (hikeName != null) {
            getSupportActionBar().setTitle(hikeName.getText().toString());
        }

    }
}