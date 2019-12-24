package com.example.practical3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView seatGridView;
    private ArrayAdapter<String> seatStringArrayAdapter;
    private List<String> seatStringList;

    private Spinner busSpinner;
    private ArrayAdapter<String> busStringArrayAdapter;
    private List<String> busStringList;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seatGridView = findViewById(R.id.seats_grd);
        seatStringList = new ArrayList<>();
        seatStringArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,seatStringList);
        seatGridView.setAdapter(seatStringArrayAdapter);
        registerForContextMenu(seatGridView);

        busSpinner = findViewById(R.id.dst_spn);
        busStringList = new ArrayList<>();
        busStringArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,busStringList);

        databaseHelper = new DatabaseHelper(this);

        busSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshGridData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.reserve){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int bus_id = busSpinner.getSelectedItemPosition() + 1;
            int seat_id = info.position + 1 + DatabaseHelper.BUS_SIZE * busSpinner.getSelectedItemPosition();
            databaseHelper.Reserve(seat_id,bus_id);
            refreshGridData();
        }
        return super.onContextItemSelected(item);
    }

    private void refreshGridData(){
        seatStringList.clear();
        int bus_id = busSpinner.getSelectedItemPosition() + 1;
        Cursor cursor = databaseHelper.fetchBusData(bus_id);
        while (!cursor.isAfterLast()){
            int available = cursor.getInt(1);
            if(available == 1) {
                seatStringList.add("available");
            }else {
                seatStringList.add("reserved");
            }
            cursor.moveToNext();
        }
        seatStringArrayAdapter.notifyDataSetChanged();
    }
}
