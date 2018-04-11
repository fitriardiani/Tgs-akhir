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
import com.example.ardiani.myapplication.model.DataModelSehat;

import java.util.List;

/**
 * Created by ardiani on 3/23/2018.
 */

public class Adapter_sehat extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModelSehat> item;

    public Adapter_sehat(Activity activity, List<DataModelSehat> item) {
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
            convertView = inflater.inflate(R.layout.list_sehat, null);

        TextView txt_id_rfid = (TextView) convertView.findViewById(R.id.txt_id_rfid);
        TextView txt_tgl_periksa = (TextView) convertView.findViewById(R.id.txt_tgl_periksa);
        TextView txt_pengobatan = (TextView) convertView.findViewById(R.id.txt_pengobatan);
        TextView txt_vaksin = (TextView) convertView.findViewById(R.id.txt_vaksin);
        TextView txt_diagnosa = (TextView) convertView.findViewById(R.id.txt_diagnosa);


        txt_id_rfid.setText(item.get(position).getId_rfid());
        txt_tgl_periksa.setText(item.get(position).getTgl_periksa());
        txt_pengobatan.setText(item.get(position).getPengobatan());
        txt_vaksin.setText(item.get(position).getVaksin());
        txt_diagnosa.setText(item.get(position).getVaksin());

        DataModelSehat data = item.get(position);

        txt_id_rfid.setText(data.getId_rfid());
        txt_tgl_periksa.setText(data.getTgl_periksa());
        txt_pengobatan.setText(data.getPengobatan());
        txt_vaksin.setText(data.getVaksin());
        txt_diagnosa.setText(data.getDiagnosa());

        return convertView;
    }
}


