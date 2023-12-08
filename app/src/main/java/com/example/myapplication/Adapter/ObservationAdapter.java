package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.AddObservation;
import com.example.myapplication.DatabaseHelper.SQLiteHelper;
import com.example.myapplication.DetailsHike;
import com.example.myapplication.DetailsObservation;
import com.example.myapplication.Model.Observation;
import com.example.myapplication.R;

import java.util.List;

public class ObservationAdapter extends BaseAdapter {
    private Context context;
    private List<Observation> observationList;
    private int layoutResId;
    private SQLiteHelper databaseHelper;

    public ObservationAdapter(Context context, int layoutResId, List<Observation> observationList,SQLiteHelper databaseHelper) {
        this.context = context;
        this.layoutResId = layoutResId;
        this.observationList = observationList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public int getCount() {
        return observationList.size();
    }

    @Override
    public Object getItem(int position) {
        return observationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        observationList.clear();
    }

    public void addAll(List<Observation> ob) {
        observationList.addAll(ob);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResId, null);
        }

        Observation ob = observationList.get(position);

        ImageView obView = view.findViewById(R.id.image_ob);
        TextView obNameTextView = view.findViewById(R.id.obNameTextView);
        TextView obTimeTextView = view.findViewById(R.id.obTimeTextView);

        Bitmap bitmap = BitmapFactory.decodeFile(ob.getOb_image());

        obView.setImageBitmap(bitmap);
        obNameTextView.setText(ob.getOb_name());
        obTimeTextView.setText(ob.getOb_time());

        Button deleteButton = view.findViewById(R.id.btn_delete_ob);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.delete_observation(ob.getOb_id());
                Log.d("DeleteHike", "Deleted hike with ID: " + ob.getOb_id());
                observationList.remove(ob);
                notifyDataSetChanged();

                Toast.makeText(context, "Observation deleted", Toast.LENGTH_SHORT).show();
            }
        });

        Button detailButton = view.findViewById(R.id.btn_detail_ob);
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsObservation.class);
                intent.putExtra("obId", ob.getOb_id());
                context.startActivity(intent);
            }
        });

        Button editButton = view.findViewById(R.id.btn_edit_ob);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddObservation.class);
                intent.putExtra("obId", ob.getOb_id());
                intent.putExtra("obHikeId", ob.getOb_hike_id());
                intent.putExtra("obName", ob.getOb_name());
                intent.putExtra("obTime", ob.getOb_time());
                intent.putExtra("obImage", ob.getOb_image());
                intent.putExtra("is_edit_ob", true);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
