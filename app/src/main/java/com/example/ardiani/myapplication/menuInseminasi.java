package com.example.ardiani.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ardiani.myapplication.adapter.Adapter_inseminasi;
import com.example.ardiani.myapplication.app.appControler;
import com.example.ardiani.myapplication.model.DataModelInseminasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class menuInseminasi extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener {

    ProgressDialog pDialog;
    List<DataModelInseminasi> listData = new ArrayList<DataModelInseminasi>();
    AlertDialog.Builder dialog;
    View dialogView;
    Adapter_inseminasi adapter;
    SwipeRefreshLayout swipe;
    ListView list_view;
    private Button btnTambah;
    LayoutInflater inflater;
    int success;
    EditText txid_rfid, tx_tglinseminasi, txkode_segmen, txpetugas;
    String  id_rfid, tanggal_inseminasi, kode_segmen, petugas;
    //public static String id_rfid_g = "";

    public static final String url_data = "http://peternakan.xyz/rd/data_inseminasi.php";
    public static final String url_cari = "http://peternakan.xyz/rd/cari_inseminasi.php";
    public static final String url_edit = "http://peternakan.xyz/rd/editInseminasi.php";

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String TAG_ID_RFID = "id_rfid";
    public static final String TAG_KODE_SEGMEN = "kode_segmen";
    public static final String TAG_Petugas = "petugas";
    public static final String Tag_Tanggal_Inseminasi = "tanggal_inseminasi";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";
    public static final String TAG_SUCCESS="success";

    //TextView textview;

    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inseminasi);
        list_view = (ListView) findViewById(R.id.list_view);

        bluet.gethandler(mHandler);

        btnTambah = (Button) findViewById(R.id.tambah);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.ardiani.myapplication.menuInseminasi.this, tambahInseminasi.class);
                com.example.ardiani.myapplication.menuInseminasi.this.startActivity(intent);
            }
        });

        adapter = new com.example.ardiani.myapplication.adapter.Adapter_inseminasi(com.example.ardiani.myapplication.menuInseminasi.this, listData);
        list_view.setAdapter((ListAdapter) adapter);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                callData();
            }
        });
        //listview ditekan lama
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                final String idx = listData.get(position).getId_rfid();
                final CharSequence[] dialogitem = { "Print"};
                dialog = new AlertDialog.Builder(menuInseminasi.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //TODO Auto-generated method stub
                        switch (which){
                            case 0:
                                print(idx);
                                break;
                        }
                    }

                }).show();

                return false;
            }
        });
    }

//memanggil data untuk dicetak
    private void print(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx     = jObj.getString(TAG_ID_RFID);
                        String tanggalx_inseminasi    = jObj.getString(Tag_Tanggal_Inseminasi);
                        String kodex_segmen  = jObj.getString(TAG_KODE_SEGMEN);
                        String petugasx = jObj.getString(TAG_Petugas);


                        DialogForm(idx, tanggalx_inseminasi,kodex_segmen,petugasx, "Print");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(menuInseminasi.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(menuInseminasi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_rfid", idx);

                return params;
            }

        };

        appControler.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
//menampilkan kotak dialog form
    private void DialogForm(String idx, String tanggalx_inseminasi, String kodex_segmen, String petugasx, String Button){
        dialog = new AlertDialog.Builder(menuInseminasi.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_inseminasi, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Data");

        txid_rfid      = (EditText) dialogView.findViewById(R.id.txt_id);
        tx_tglinseminasi   = (EditText) dialogView.findViewById(R.id.txt_tgl_inseminasi);
        txkode_segmen = (EditText) dialogView.findViewById(R.id.txt_kode_segmen);
        txpetugas      = (EditText) dialogView.findViewById(R.id.txt_petugas);

        if (!idx.isEmpty()){
            txid_rfid.setText(idx);
            tx_tglinseminasi.setText(tanggalx_inseminasi);
            txkode_segmen.setText(kodex_segmen);
            txpetugas.setText(petugasx);
        } else {
            kosong();
        }

        dialog.setPositiveButton(Button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_rfid      = txid_rfid.getText().toString();
                tanggal_inseminasi    = tx_tglinseminasi.getText().toString();
                kode_segmen  = txkode_segmen.getText().toString();
                petugas = txpetugas.getText().toString();

                printer();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });

        dialog.show();
    }

    private void kosong(){
        txid_rfid.setText(null);
        tx_tglinseminasi.setText(null);
        txpetugas.setText(null);
        txkode_segmen.setText(null);

    }
    private void printer(){
        String pesan = txid_rfid.getText().toString();
        String pesan1 = tx_tglinseminasi.getText().toString();
        String pesan2 = txkode_segmen.getText().toString();
        String pesan3 = txpetugas.getText().toString();
        txid_rfid.setText("");
        tx_tglinseminasi.setText("/n");
        txkode_segmen.setText("/n");
        txpetugas.setText("/n");
        kirim(pesan);
        kirim(pesan1);
        kirim(pesan2);
        kirim(pesan3);
    }

    private void kirim(String data) {
        if (bluet.connectedThread != null)
            bluet.connectedThread.write(data);
    }

    private void write (String data, String namafile) {
        File file = new File(Environment.getExternalStorageDirectory(),
                namafile);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void callData() {
        listData.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(url_data, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataModelInseminasi item = new DataModelInseminasi();

                        item.setId_rfid(obj.getString(TAG_ID_RFID));
                        item.setPetugas(obj.getString(TAG_Petugas));

                        item.setTanggal_inseminasi(obj.getString(Tag_Tanggal_Inseminasi));
                        item.setKode_segmen(obj.getString(TAG_KODE_SEGMEN));

                        listData.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(com.example.ardiani.myapplication.menuInseminasi.this, error.getMessage(), Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        });

        // Adding request to request queue
        appControler.getInstance().addToRequestQueue(jArr);
    }

    @Override
    public void onRefresh() {
        callData();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        cariData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.type_name));
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }
    //MENGIRIM DATA READ/WRITE
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case bluet.SUCCESS_CONNECT:
                    bluet.connectedThread = new bluet.ConnectedThread((BluetoothSocket) msg.obj);
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
                    bluet.connectedThread.start();
                    break;
                case bluet.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String strIncom = new String(readBuf);
                    //.append(strIncom);

            }
        }

    };


    private void cariData(final String keyword) {
        pDialog = new ProgressDialog(menuInseminasi.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_cari, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    int value = jObj.getInt(TAG_VALUE);

                    if (value == 1) {
                        listData.clear();
                        adapter.notifyDataSetChanged();

                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            DataModelInseminasi data = new DataModelInseminasi();

                            data.setId_rfid(obj.getString(TAG_ID_RFID));
                            data.setTanggal_inseminasi(obj.getString(Tag_Tanggal_Inseminasi));
                            data.setKode_segmen(obj.optString(TAG_KODE_SEGMEN));
                            data.setPetugas(obj.getString(TAG_Petugas));

                            listData.add(data);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyword", keyword);

                return params;
            }

        };

        appControler.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    //ArrayList<String> id_rfid = new ArrayList<String>();

}
