package com.example.iamsa.emptyfolderremover;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    File root = android.os.Environment.getExternalStorageDirectory();
    private static final String FOLDER_NAME = "";
    final String FOLDER_LOCATION = root.getAbsolutePath() + "/" + FOLDER_NAME;
    boolean isFinished = false;
    int total_deleted;
    TextView deletionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                +ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ||ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
            }
        } else {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletionInfo = findViewById(R.id.deletionText);
                    deletionInfo.setText("Scanning for empty folders....\n");
                    total_deleted = 0;
                    do {
                        isFinished = true;
                        removeFolders(FOLDER_LOCATION);
                    } while (!isFinished);
                    TextView welcomeText = findViewById(R.id.welcomeText);
                    deletionInfo.append("Finished.");
                    welcomeText.setText(total_deleted + " folders deleted.");
                    Toast.makeText(getApplicationContext(),"Total empty folders deleted " + total_deleted, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void removeFolders(String folder_location) {
        deletionInfo = findViewById(R.id.deletionText);
        deletionInfo.setMovementMethod(new ScrollingMovementMethod());
        File folder = new File(folder_location);
        File[] listofFiles = folder.listFiles();

        if (listofFiles.length == 0) {
            //System.out.println("Folder Name :: " + folder.getAbsolutePath() + " is deleted.");
            total_deleted++;
            //Toast.makeText(getApplicationContext(),folder.getAbsolutePath() + " deleted.", Toast.LENGTH_SHORT).show();
            deletionInfo.append(folder.getAbsolutePath() + " deleted.\n");
            //delInfoScroll.addView(deletionInfo);
            //noinspection ResultOfMethodCallIgnored
            folder.delete();
            isFinished = false;
        } else {
            for (File file : listofFiles) {
                if (file.isDirectory()) {
                    removeFolders(file.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
