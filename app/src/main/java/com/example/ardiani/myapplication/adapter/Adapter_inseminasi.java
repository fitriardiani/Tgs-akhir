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

import java.util.List;

/**[
 * Created by ardiani on 3/19/2018.
 */

public class Adapter_inseminasi extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModelInseminasi> item;

    public Adapter_inseminasi(Activity activity, List<DataModelInseminasi> item) {
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
            convertView = inflater.inflate(R.layout.list_inseminasi, null);

        TextView txt_id_rfid = (TextView) convertView.findViewById(R.id.txt_id_rfid);
        TextView txt_inseminasi = (TextView) convertView.findViewById(R.id.txt_tgl_inseminasi);
        TextView txt_kode_segmen = (TextView) convertView.findViewById(R.id.txt_kode_segmen);
        TextView txt_petugas = (TextView) convertView.findViewById(R.id.txt_petugas);

        txt_id_rfid.setText(item.get(position).getId_rfid());
        txt_inseminasi.setText(item.get(position).getTanggal_inseminasi());
        txt_kode_segmen.setText(item.get(position).getKode_segmen());
        txt_petugas.setText(item.get(position).getPetugas());

        return convertView;
    }
}

