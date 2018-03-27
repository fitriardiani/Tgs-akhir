package com.example.ardiani.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ardiani.myapplication.R;
import com.example.ardiani.myapplication.model.DataModel;

import java.util.List;

/**
 * Created by ardiani on 3/15/2018.
 */

public class Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModel> item;

    public Adapter(Activity activity, List<DataModel> item) {
        this.activity = activity;
        this.item = item;
    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_rfid, null);

        TextView txt_id_rfid = (TextView) convertView.findViewById(R.id.txt_id_rfid);

        txt_id_rfid.setText(item.get(position).getId_rfid());

        return convertView;
    }
}

