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

import java.util.List;

/**
 * Created by ardiani on 3/23/2018.
 */

public class Adapter_lahir extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModelLahir> item;

    public Adapter_lahir(Activity activity, List<DataModelLahir> item) {
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
                convertView = inflater.inflate(R.layout.list_lahir, null);

            TextView txt_id_rfid = (TextView) convertView.findViewById(R.id.txt_id_rfid);
            TextView txt_tgl_lahir = (TextView) convertView.findViewById(R.id.txt_tgl_lahir);
            TextView txt_keterangan = (TextView) convertView.findViewById(R.id.txt_keterangan);
            TextView txt_jenis_kelamin = (TextView) convertView.findViewById(R.id.txt_jenis_kelamin);
            TextView txt_petugas = (TextView) convertView.findViewById(R.id.txt_petugas);

            txt_id_rfid.setText(item.get(position).getId_rfid());
            txt_tgl_lahir.setText(item.get(position).getTgl_lahir());
            txt_keterangan.setText(item.get(position).getKeterangan());
            txt_jenis_kelamin.setText(item.get(position).getJenis_kelamin());
            txt_petugas.setText(item.get(position).getPetugas());

            return convertView;
        }
    }


