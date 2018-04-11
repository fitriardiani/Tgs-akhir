package com.example.ardiani.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ardiani.myapplication.R;
import com.example.ardiani.myapplication.model.DataModelInseminasi;
import com.example.ardiani.myapplication.model.DataModelLahir;
import com.example.ardiani.myapplication.model.DataModelPemilik;

import java.util.List;

/**
 * Created by ardiani on 3/23/2018.
 */

public class Adapter_pemilik extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModelPemilik> item;

    public Adapter_pemilik(Activity activity, List<DataModelPemilik> item) {
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
            convertView = inflater.inflate(R.layout.list_pemilik, null);

        TextView txt_id_rfid = (TextView) convertView.findViewById(R.id.txt_id_rfid);
        TextView txt_tgl_memiliki = (TextView) convertView.findViewById(R.id.txt_tgl_memiliki);
        TextView txt_nama_pemilik = (TextView) convertView.findViewById(R.id.txt_nama_pemilik);
        TextView txt_kepemilikan_ke = (TextView) convertView.findViewById(R.id.txt_pemilikke);

        txt_id_rfid.setText(item.get(position).getId_rfid());
        txt_tgl_memiliki.setText(item.get(position).getTgl_memiliki());
        txt_nama_pemilik.setText(item.get(position).getNama_pemilik());
        txt_kepemilikan_ke.setText(item.get(position).getKepemilikan_ke());

        DataModelPemilik data = item.get(position);

        txt_id_rfid.setText(data.getId_rfid());
        txt_tgl_memiliki.setText(data.getTgl_memiliki());
        txt_nama_pemilik.setText(data.getNama_pemilik());
        txt_kepemilikan_ke.setText(data.getKepemilikan_ke());


        return convertView;
    }
}

