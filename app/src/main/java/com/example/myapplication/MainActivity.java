package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.myapplication.Adapter.HikeAdapter;
import com.example.myapplication.DatabaseHelper.SQLiteHelper;
import com.example.myapplication.Model.Hike;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Button btn_hike;
    Button btn_record;
    SearchView searchView;
    private SQLiteHelper databaseHelper;
    ListView listView;
    private HikeAdapter hikeAdapter;

    private List<Hike> hikeList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btn_hike = findViewById(R.id.btn_plan);
        btn_hike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(
                        getApplicationContext(),
                        PlanHike.class
                );

                startActivity(i);
            }
        });


        databaseHelper = new SQLiteHelper(this);
        hikeList = databaseHelper.getAllHikes();

        listView = findViewById(R.id.list_hike);
        hikeAdapter = new HikeAdapter(this, R.layout.hike_list_item, hikeList,databaseHelper);
        listView.setAdapter(hikeAdapter);

        if (getIntent().getBooleanExtra("refreshList", false)) {
            refreshListView();
        }

        if (listView.getAdapter() != null && listView.getAdapter().getCount() == 0) {
            TextView isListTextView = findViewById(R.id.is_list);
            isListTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            TextView isListTextView = findViewById(R.id.is_list);
            isListTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Hike> newList = new ArrayList<>();

                if (newText.isEmpty()) {
                    hikeList = databaseHelper.getAllHikes();
                } else {
                    for (Hike hike : hikeList) {
                        String name = hike.getHikeName().toLowerCase();
                        String date = hike.getDate().toLowerCase();
                        if (name.contains(newText) || date.contains(newText)) {
                            newList.add(hike);
                        }
                    }
                }

                hikeAdapter.clear();
                hikeAdapter.addAll(newText.isEmpty() ? hikeList : newList);
                hikeAdapter.notifyDataSetChanged();
                return true;
            }
        });

        Button buttonDeleteAll = findViewById(R.id.btn_delete_all);
        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseHelper.deleteAllHike();
                hikeAdapter.clear();
                hikeAdapter.notifyDataSetChanged();
                hikeList.clear();

                if (listView.getAdapter() != null && listView.getAdapter().getCount() == 0) {
                    TextView isListTextView = findViewById(R.id.is_list);
                    isListTextView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void refreshListView() {
        List<Hike> updatedHikeList = databaseHelper.getAllHikes();

        hikeAdapter.clear();
        hikeAdapter.addAll(updatedHikeList);

        hikeAdapter.notifyDataSetChanged();
    }
}