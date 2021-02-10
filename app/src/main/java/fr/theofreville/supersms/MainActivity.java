package fr.theofreville.supersms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static ListView dbListView;
    public static DatabaseHelper databaseHelper;
    public static AppCompatActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        INSTANCE = this;

        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ "android.permission.RECEIVE_SMS" },1);
        }

        dbListView = (ListView) findViewById(R.id.lv_bd);
        databaseHelper = new DatabaseHelper(this);

        populateListView();

        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            displayAlertPermission();
        }
    }

    public void displayAlertPermission(){
        new AlertDialog.Builder(this)
                .setTitle("Permission requise")
                .setMessage("Vous devez accepter le fait que l'application reçoive les sms, pour se faire, vous devez aller dans les paramètres de l'application pour changer ses permissions.")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
                        displayAlertPermission();
                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
                        displayAlertPermission();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ "android.permission.RECEIVE_SMS" },1);
        }
    }

    public static void populateListView(){
        Cursor data = databaseHelper.getData();
        ArrayList<String> list = new ArrayList<>();

        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT, new Locale("FR","fr"));

        while(data.moveToNext()){
            StringBuilder message = new StringBuilder();

            message.append(data.getString(1) + " \n\n");
            message.append(data.getString(2) + "\n\n");
            message.append(shortDateFormat.format(new Date(data.getLong(3))));

            list.add(message.toString());
        }

        Collections.reverse(list);

        ListAdapter adapter = new ArrayAdapter<>(INSTANCE, android.R.layout.simple_list_item_activated_1, list);
        dbListView.setAdapter(adapter);
    }

}