package com.smk.funnysearchablespinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.smk.funnysearchablespinner.utils.EndlessRecyclerOnScrollListener;
import com.smk.funnysearchablespinner.utils.ItemClickSupport;

import java.util.ArrayList;

public class FnSearchableList extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static FnSearchableRVAdapter fnSearchableRVAdapter;
    private LinearLayoutManager mLayoutManager;
    private MaterialSearchView searchView;
    private String searchViewTitle;
    public static ArrayList<String> lists;
    private static OnSearchItemClicked mOnSearchItemClicked;
    private static OnQueryTextListener mOnQueryTextListener;
    private static OnScrollOffsetListener mOnScrollOffsetListener;
    private boolean localFilter = true;
    private int visibleItem;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    public FnSearchableList(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fn_searchable_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            searchViewTitle = bundle.getString("searchViewTitle", "Search");
            localFilter = bundle.getBoolean("isLocalFilter", true);
            visibleItem = bundle.getInt("visibleItem", 12);
            getSupportActionBar().setTitle(searchViewTitle);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        fnSearchableRVAdapter = new FnSearchableRVAdapter(this, lists);
        recyclerView.setAdapter(fnSearchableRVAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                if(mOnSearchItemClicked != null){
                    mOnSearchItemClicked.onClick(fnSearchableRVAdapter.getItem(position));
                    onBackPressed();
                }
            }
        });

        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager, visibleItem) {
            @Override
            public void onLoadMore(int current_page) {
                if(mOnScrollOffsetListener != null)
                    mOnScrollOffsetListener.onOffset(current_page);

            }
        };

        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setHint(searchViewTitle);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(mOnQueryTextListener != null){
                    mOnQueryTextListener.onQueryTextSubmit(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(fnSearchableRVAdapter != null && localFilter)
                    fnSearchableRVAdapter.filter(newText);

                if(mOnQueryTextListener != null){
                    endlessRecyclerOnScrollListener.reset(0,true);
                    mOnQueryTextListener.onQueryTextChange(newText);
                }
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                onBackPressed();
            }
        });

        searchView.showSearch(false);
    }

    public static void setOnSearchItemClicked(OnSearchItemClicked onSearchItemClicked){
        mOnSearchItemClicked = onSearchItemClicked;
    }

    public interface OnSearchItemClicked{
        void onClick(String item);
    }

    public static void setOnQueryTextListener(OnQueryTextListener onQueryTextListener){
        mOnQueryTextListener = onQueryTextListener;
    }

    public interface OnQueryTextListener{
        void onQueryTextSubmit(String query);
        void onQueryTextChange(String newText);
    }

    public static void setOnScrollOffsetListener(OnScrollOffsetListener onScrollOffsetListener){
        mOnScrollOffsetListener = onScrollOffsetListener;
    }

    public interface OnScrollOffsetListener{
        void onOffset(int offset);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fnsearchablemenu, menu);

        MenuItem item = menu.findItem(R.id.action_fnsearch);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}
