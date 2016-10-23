package com.smk.funnysearchablespinner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class FnSearchableSpinner extends Spinner implements View.OnTouchListener{

    public static final int NO_ITEM_SELECTED = -1;
    private Context _context;
    private ArrayList<String> _items;
    private boolean _isDirty;
    private ArrayAdapter _arrayAdapter;
    private String _strHintText;
    private boolean _isFromInit;
    private String title;
    private String filterQuery = "";
    private OnQueryTextListener mOnQueryTextListener;
    private OnScrollOffsetListener mOnScrollOffsetListener;
    private int visibleItem = 12;
    private boolean localFilter;

    public FnSearchableSpinner(Context context) {
        super(context);
        this._context = context;
        init();
    }

    public FnSearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FnSearchableSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.FnSearchableSpinner_hintText) {
                _strHintText = a.getString(attr);
            }
        }
        a.recycle();
        init();
    }

    public FnSearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init();
    }

    private void init() {
        _items = new ArrayList();
        FnSearchableList.setOnSearchItemClicked(onSearchItemClicked);
        FnSearchableList.setOnQueryTextListener(onQueryTextListener);
        FnSearchableList.setOnScrollOffsetListener(onScrollOffsetListener);
        setOnTouchListener(this);

        _arrayAdapter = (ArrayAdapter) getAdapter();
        if (!TextUtils.isEmpty(_strHintText)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout
                    .simple_list_item_1, new String[]{_strHintText});
            _isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (null != _arrayAdapter) {

                // Refresh content #6
                // Change Start
                // Description: The items were only set initially, not reloading the data in the
                // spinner every time it is loaded with items in the adapter.
                _items.clear();
                for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                    _items.add(_arrayAdapter.getItem(i).toString());
                }

                FnSearchableList.lists = _items;
                // Change end.
                Bundle bundle = new Bundle();
                bundle.putString("searchViewTitle", this.title);
                bundle.putBoolean("isLocalFilter", this.localFilter);
                bundle.putInt("visibleItem", this.visibleItem);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), v, "profile");
                getContext().startActivity(new Intent(getContext(), FnSearchableList.class).putExtras(bundle), options.toBundle());
            }
        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (!_isFromInit) {
            _arrayAdapter = (ArrayAdapter) adapter;
            if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout.simple_list_item_1, new String[]{_strHintText});
                super.setAdapter(arrayAdapter);
            } else {
                super.setAdapter(adapter);
            }

        } else {
            _isFromInit = false;
            super.setAdapter(adapter);

        }
    }


    public void notifyDataSetChanged(){
        _arrayAdapter.notifyDataSetChanged();
        _items.clear();
        for (int i = 0; i < _arrayAdapter.getCount(); i++) {
            _items.add(_arrayAdapter.getItem(i).toString());
        }

        FnSearchableList.lists = _items;
        if(FnSearchableList.fnSearchableRVAdapter != null)
            FnSearchableList.fnSearchableRVAdapter.notifyDataSetChanged();
        // Change end.
    }

    private FnSearchableList.OnSearchItemClicked onSearchItemClicked = new FnSearchableList.OnSearchItemClicked() {
        @Override
        public void onClick(String item) {
            for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                if(_arrayAdapter.getItem(i).toString().equals(item)){
                    setSelection(i);
                }
            }
        }
    };

    private FnSearchableList.OnQueryTextListener onQueryTextListener = new FnSearchableList.OnQueryTextListener() {
        @Override
        public void onQueryTextSubmit(String query) {
            if(mOnQueryTextListener != null) {
                filterQuery = query;
                mOnQueryTextListener.onQueryTextSubmit(query);
            }
        }

        @Override
        public void onQueryTextChange(String newText) {
            if(mOnQueryTextListener != null) {
                filterQuery = newText;
                mOnQueryTextListener.onQueryTextChange(newText);
            }
        }
    };

    private FnSearchableList.OnScrollOffsetListener onScrollOffsetListener = new FnSearchableList.OnScrollOffsetListener() {
        @Override
        public void onOffset(int offset) {
            if(mOnScrollOffsetListener != null)
                mOnScrollOffsetListener.onOffset(filterQuery, offset);
        }
    };

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener){
        mOnQueryTextListener = onQueryTextListener;
    }

    public interface OnQueryTextListener{
        void onQueryTextSubmit(String query);
        void onQueryTextChange(String newText);
    }

    public void setOnScrollOffsetListener(OnScrollOffsetListener onScrollOffsetListener){
        mOnScrollOffsetListener = onScrollOffsetListener;
    }

    public interface OnScrollOffsetListener{
        void onOffset(String filterText, int offset);
    }

    public void setScrollItemLimit(int visibleItem){
        this.visibleItem = visibleItem;
    }

    public void isLocalFilter(boolean localFilter){
        this.localFilter = localFilter;
    }

    public void setTitle(String strTitle) {
        this.title = strTitle;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }
}