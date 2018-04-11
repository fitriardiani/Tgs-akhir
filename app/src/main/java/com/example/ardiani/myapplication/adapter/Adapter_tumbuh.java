package com.example.ardiani.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ardiani.myapplication.R;
import com.example.ardiani.myapplication.model.DataModelLahir;
import com.example.ardiani.myapplication.model.DataModelPemilik;
import com.example.ardiani.myapplication.model.DataModelTumbuh;

import java.util.List;

/**
 * Created by ardiani on 3/23/2018.
 */

public class Adapter_tumbuh extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModelTumbuh> item;

    public Adapter_tumbuh(Activity activity, List<DataModelTumbuh> item) {
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
            convertView = inflater.inflate(R.layout.list_tumbuh, null);

        TextView txt_id_rfid = (TextView) convertView.findViewById(R.id.txt_id_rfid);
        TextView txt_tgl_ukur = (TextView) convertView.findViewById(R.id.txt_tgl_ukur);
        TextView txt_umur = (TextView) convertView.findViewById(R.id.txt_umur);
        TextView txt_berat = (TextView) convertView.findViewById(R.id.txt_berat);
        TextView txt_petugas = (TextView) convertView.findViewById(R.id.txt_petugas);

        txt_id_rfid.setText(item.get(position).getId_rfid());
        txt_tgl_ukur.setText(item.get(position).getTgl_ukur());
        txt_umur.setText(item.get(position).getUmur());
        txt_petugas.setText(item.get(position).getPetugas());
        txt_berat.setText(item.get(position).getBerat());

        DataModelTumbuh data = item.get(position);

        txt_id_rfid.setText(data.getId_rfid());
        txt_tgl_ukur.setText(data.getTgl_ukur());
        txt_umur.setText(data.getUmur());
        txt_berat.setText(data.getBerat());
        txt_petugas.setText(data.getPetugas());

        return convertView;
    }
}

