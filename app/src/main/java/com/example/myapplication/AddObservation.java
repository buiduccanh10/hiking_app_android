package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.ObservationAdapter;
import com.example.myapplication.DatabaseHelper.SQLiteHelper;
import com.example.myapplication.Model.Hike;
import com.example.myapplication.Model.Observation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddObservation extends AppCompatActivity {

    private TextView hikeName_ob;
    private TextView hikeFrom_ob;
    private TextView hikeTo_ob;
    private TextView hikeLength_ob;
    private TextView hikeDuration_ob;
    ListView listView;
    private int _ob_id,_ob_hike_id;
    private String _ob_name,_ob_time,_ob_image;
    private ObservationAdapter obAdapter;

    private List<Observation> observationList;
    private int IMAGE_PICK_CODE =200;
    private SQLiteHelper databaseHelper;
    private int hikeId;
    private EditText ob_name;
    Date currentTime = new Date();
    private String imageUrl;
    private Bitmap selectedImage;
    ImageView imageView;
    private Boolean is_edit_ob;
    private Intent intent;
    private String formattedTime;
    private Button buttonFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        hikeId = getIntent().getIntExtra("hikeId",-1);
        if (hikeId != -1) {
            databaseHelper = new SQLiteHelper(this);
            Hike hike = databaseHelper.getHikeById(hikeId);

            if (hike != null) {
                hikeName_ob = findViewById(R.id.hike_name_ob);
                hikeName_ob.setText(hike.getHikeName());

                hikeFrom_ob = findViewById(R.id.hike_from_ob);
                hikeFrom_ob.setText(hike.getLocationFrom());

                hikeTo_ob = findViewById(R.id.hike_to_ob);
                hikeTo_ob.setText(hike.getLocationTo());

                hikeLength_ob = findViewById(R.id.hike_length_ob);
                hikeLength_ob.setText(hike.getLength());

                hikeDuration_ob = findViewById(R.id.hike_duration_ob);
                hikeDuration_ob.setText(hike.getDuration());
            }
        }

        databaseHelper = new SQLiteHelper(this);
        observationList = databaseHelper.getAllObservationsByHikeId(hikeId);

        listView = findViewById(R.id.listview_observation);
        obAdapter = new ObservationAdapter(this, R.layout.observation_list_item, observationList,databaseHelper);
        listView.setAdapter(obAdapter);

        if (getIntent().getBooleanExtra("refreshList_ob", false)) {
            refreshListView();
        }

        Toolbar toolbar = findViewById(R.id.toolbar_observation);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.image_preview);
        imageView.setVisibility(View.GONE);

        Button buttonImage = findViewById(R.id.btn_image);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_PICK_CODE);
            }
        });
        buttonFinish = findViewById(R.id.btn_finish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(
                        getApplicationContext(),
                        MainActivity.class
                );
                startActivity(i);
            }
        });

        intent = getIntent();
        is_edit_ob = intent.getBooleanExtra("is_edit_ob",false);

        getSupportActionBar().setTitle("Recording");

        if(is_edit_ob){
            Button btnsaveob = findViewById(R.id.btn_save_ob);
            btnsaveob.setText("Edit observation");
            getSupportActionBar().setTitle("Edit observation");
            hikeName_ob = findViewById(R.id.hike_name_ob);
            hikeFrom_ob = findViewById(R.id.hike_from_ob);
            hikeTo_ob = findViewById(R.id.hike_to_ob);
            hikeLength_ob = findViewById(R.id.hike_length_ob);
            hikeDuration_ob = findViewById(R.id.hike_duration_ob);
            hikeName_ob.setVisibility(View.GONE);
            hikeDuration_ob.setVisibility(View.GONE);
            hikeFrom_ob.setVisibility(View.GONE);
            hikeTo_ob.setVisibility(View.GONE);
            hikeLength_ob.setVisibility(View.GONE);
            buttonFinish = findViewById(R.id.btn_finish);
            buttonFinish.setVisibility(View.GONE);

            TextView lbname = findViewById(R.id.label_name);
            lbname.setVisibility(View.GONE);
            TextView lbfrom = findViewById(R.id.label_from);
            lbfrom.setVisibility(View.GONE);
            TextView lbto = findViewById(R.id.label_to);
            lbto.setVisibility(View.GONE);
            TextView lblength = findViewById(R.id.label_length);
            lblength.setVisibility(View.GONE);
            TextView lbdu = findViewById(R.id.label_duration);
            lbdu.setVisibility(View.GONE);
            TextView lbques = findViewById(R.id.label_ques);
            lbques.setVisibility(View.GONE);
            TextView lblist = findViewById(R.id.label_list);
            lblist.setVisibility(View.GONE);
            Button btn_ob = findViewById(R.id.btn_save_ob);

            _ob_id = intent.getIntExtra("obId",-1);
            _ob_hike_id = intent.getIntExtra("obHikeId",-1);
            _ob_name=intent.getStringExtra("obName");
            _ob_time=intent.getStringExtra("obTime");
            _ob_image=intent.getStringExtra("obImage");

            ob_name = findViewById(R.id.editTextObName);

            formattedTime = _ob_time;
            ob_name.setText(_ob_name);

            imageView.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(_ob_image);
            imageView.setImageBitmap(bitmap);

        }
    }
    private String saveImageToFile(Bitmap image) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + ".jpg";

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File imageFile = new File(storageDir, imageFileName);

            FileOutputStream fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            try {
                Uri selectedImageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);

                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void save_ob_click (View view){
            try {
                ob_name = findViewById(R.id.editTextObName);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                formattedTime = sdf.format(currentTime);
                imageUrl = saveImageToFile(selectedImage);
                Log.d("YourTag", "url: " + imageUrl);

                String obName = ob_name.getText().toString();
                String image = imageUrl.toString();

                if (is_edit_ob){
                    databaseHelper.editObservationById(_ob_id,_ob_hike_id,obName,_ob_time,image);
                    Toast.makeText(AddObservation.this, "Observation data edited!", Toast.LENGTH_SHORT).show();

                    goBackToObservationAdd();
                }else {
                    try {
                        databaseHelper.add_observation(obName,hikeId,formattedTime,image);
                        Toast.makeText(AddObservation.this, "Observation data saved!", Toast.LENGTH_SHORT).show();

                        List<Observation> updatedObList = databaseHelper.getAllObservationsByHikeId(hikeId);

                        obAdapter.clear();
                        obAdapter.addAll(updatedObList);

                        obAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(AddObservation.this, "Observation save fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e){
                Toast.makeText(AddObservation.this, "Please enter ob!", Toast.LENGTH_SHORT).show();
            }
    }
    private void refreshListView() {
        List<Observation> updatedObList = databaseHelper.getAllObservationsByHikeId(hikeId);

        obAdapter.clear();
        obAdapter.addAll(updatedObList);

        obAdapter.notifyDataSetChanged();
    }
    public void goBackToObservationAdd() {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }
}
