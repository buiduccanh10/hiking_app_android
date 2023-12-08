package com.example.myapplication.Adapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DatabaseHelper.SQLiteHelper;
import com.example.myapplication.DetailsHike;
import com.example.myapplication.Model.Hike;
import com.example.myapplication.PlanHike;
import com.example.myapplication.R;

import java.util.List;

public class HikeAdapter extends BaseAdapter {
    private Context context;
    private List<Hike> hikeList;
    private int layoutResId;
    private SQLiteHelper databaseHelper;

    public HikeAdapter(Context context, int layoutResId, List<Hike> hikeList,SQLiteHelper databaseHelper) {
        this.context = context;
        this.layoutResId = layoutResId;
        this.hikeList = hikeList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public int getCount() {
        return hikeList.size();
    }

    @Override
    public Object getItem(int position) {
        return hikeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        hikeList.clear();
    }

    public void addAll(List<Hike> hikes) {
        hikeList.addAll(hikes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResId, null);
        }

        Hike hike = hikeList.get(position);

        TextView hikeNameTextView = view.findViewById(R.id.hikeNameTextView);
        TextView hikeDateTextView = view.findViewById(R.id.hikeDateTextView);

        hikeNameTextView.setText(hike.getHikeName());
        hikeDateTextView.setText(hike.getDate());

        Button deleteButton = view.findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteHike(hike.getId());
                Log.d("DeleteHike", "Deleted hike with ID: " + hike.getId());
                hikeList.remove(hike);
                notifyDataSetChanged();

                Toast.makeText(context, "Hike deleted", Toast.LENGTH_SHORT).show();
            }
        });

        Button detailButton = view.findViewById(R.id.btn_detail);
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsHike.class);
                intent.putExtra("hikeId", hike.getId());
                context.startActivity(intent);
            }
        });

        Button editButton = view.findViewById(R.id.btn_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlanHike.class);
                intent.putExtra("hikeId", hike.getId());
                intent.putExtra("hikeName", hike.getHikeName());
                intent.putExtra("hikeDes", hike.getDescription());
                intent.putExtra("hikeFrom", hike.getLocationFrom());
                intent.putExtra("hikeTo", hike.getLocationTo());
                intent.putExtra("hikeLength", hike.getLength());
                intent.putExtra("hikeDuration", hike.getDuration());
                intent.putExtra("hikePark", hike.getParking());
                intent.putExtra("hikeLevel", hike.getLevel());
                intent.putExtra("hikeDate", hike.getDate());
                intent.putExtra("is_edit", true);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
