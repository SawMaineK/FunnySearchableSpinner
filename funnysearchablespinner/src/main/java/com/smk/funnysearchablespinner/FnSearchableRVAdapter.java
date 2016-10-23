package com.smk.funnysearchablespinner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
/**
 * Created by SMK on 12/15/2015.
 */
public class FnSearchableRVAdapter extends RecyclerView.Adapter<FnSearchableRVAdapter.MyViewHolder> {
    private ArrayList<String> arraylist;
    private Context mContext;
    List<String> list;
    public FnSearchableRVAdapter(Context ctx, List<String> list){
        mContext = ctx;
        this.list = list;
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(list);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fn_searchable, parent, false);

        return new MyViewHolder(itemView);
    }

    public String getItem(int position){
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txt_item_name.setText(getItem(position).toString());
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        else
        {
            for (String item : arraylist)
            {
                if (item.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_item_name;
        public MyViewHolder(View convertView) {
            super(convertView);
            txt_item_name = (TextView) convertView.findViewById(R.id.txt_fn_searchable_item_name);
        }
    }
}
