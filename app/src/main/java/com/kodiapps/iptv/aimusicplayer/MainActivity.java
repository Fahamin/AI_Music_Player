package com.kodiapps.iptv.aimusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListView listView;

    public String[] itemALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.songRecID);
        if (haveStoragePermission()) {
            displaySongName();
        }

    }

    public boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //  Log.e("Permission error", "You have permission");
                return true;
            } else {

                //  Log.e("Permission error", "You have asked for permission");
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                Toast.makeText(this, "Need to Permission for Download", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else { //you dont need to worry about these stuff below api level 23
            //  Log.e("Permission error", "You already have the permission");
            return true;
        }
    }

    /* public void haveStroagePermission() {
         Dexter.withContext(this)
                 .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                 .withListener(new PermissionListener() {
                     @Override
                     public void onPermissionGranted(PermissionGrantedResponse response) {
                         displaySongName();
                     }

                     @Override
                     public void onPermissionDenied(PermissionDeniedResponse response) {

                     }

                     @Override
                     public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                         token.continuePermissionRequest();
                     }
                 }).check();
     }
 */
    public ArrayList<File> readAudiofile(File file) {
        ArrayList<File> list = new ArrayList<>();

        File[] allfiles = file.listFiles();

        for (File singleFile : allfiles) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                list.addAll(readAudiofile(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".aac") ||
                        singleFile.getName().endsWith(".wav") ||
                        singleFile.getName().endsWith(".wma")) {
                    list.add(singleFile);
                }
            }
        }
        return list;
    }


    public void displaySongName() {
        final ArrayList<File> allsong = readAudiofile(Environment.getExternalStorageDirectory());
        itemALL = new String[allsong.size()];

        for (int i = 0; i < itemALL.length; i++) {

            itemALL[i] = allsong.get(i).getName();
        }

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemALL);
        listView.setAdapter(stringArrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent(MainActivity.this, SmartPlayerActivity.class);
                intent.putExtra("song", allsong);
                intent.putExtra("name", songName);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}