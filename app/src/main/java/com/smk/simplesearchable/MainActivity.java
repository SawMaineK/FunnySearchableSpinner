package com.smk.simplesearchable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.smk.funnysearchablespinner.FnSearchableSpinner;
import com.smk.simplesearchable.clients.NetworkEngine;
import com.smk.simplesearchable.models.Destination;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private FnSearchableSpinner fnSearchable;
    private ArrayList<Destination> lists;
    private ArrayAdapter<Destination> adapter;
    private boolean isLoading = false;
    private boolean isLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fnSearchable = (FnSearchableSpinner) findViewById(R.id.spn_searchable);


        lists = new ArrayList<>();

        adapter = new ArrayAdapter<Destination>(this, android.R.layout.simple_spinner_item, lists);
        fnSearchable.setAdapter(adapter);
        fnSearchable.setTitle("Search your word...");
        fnSearchable.isLocalFilter(false);
        fnSearchable.setScrollItemLimit(6);
        fnSearchable.setOnQueryTextListener(new FnSearchableSpinner.OnQueryTextListener() {
            @Override
            public void onQueryTextSubmit(String query) {

            }

            @Override
            public void onQueryTextChange(String newText) {
                getDestination(newText, 1);
            }
        });
        fnSearchable.setOnScrollOffsetListener(new FnSearchableSpinner.OnScrollOffsetListener() {
            @Override
            public void onOffset(String filterQuery ,int offset) {
                Log.i("Offset", "Offset: "+ offset);
                getDestination(filterQuery, offset);
            }
        });

        getDestination("", 1);
    }

    private void getDestination(String keywords, final int offset){
        isLoading = true;
        NetworkEngine.getInstance().getDestinations(keywords, offset, 6, new Callback<List<Destination>>() {
            @Override
            public void success(List<Destination> destinations, Response response) {
                if(offset == 1)
                    lists.clear();
                lists.addAll(destinations);
                fnSearchable.notifyDataSetChanged();
                isLoading = false;
                if(destinations.size() < 6){
                    isLoadMore = false;
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
