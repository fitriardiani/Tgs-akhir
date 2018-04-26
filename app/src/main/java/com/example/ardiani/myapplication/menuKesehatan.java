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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ardiani.myapplication.adapter.Adapter_lahir;
import com.example.ardiani.myapplication.adapter.Adapter_sehat;
import com.example.ardiani.myapplication.app.appControler;
import com.example.ardiani.myapplication.model.DataModelLahir;
import com.example.ardiani.myapplication.model.DataModelSehat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ardiani.myapplication.menuInseminasi.TAG_Petugas;

public class menuKesehatan extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener {
    ProgressDialog pDialog;
    AlertDialog.Builder dialog;
    List<DataModelSehat> listData = new ArrayList<DataModelSehat>();
    Adapter_sehat adapter;
    SwipeRefreshLayout swipe;
    ListView list_view;
    private Button btnTambah;
    LayoutInflater inflater;
    View dialogView;
    int success;
    EditText txid_rfid, txtgl_periksa, txdiagnosa, txvaksin,txpetugas,txpengobatan;
    String  id_rfid, tgl_periksa,diagnosa, vaksin,petugas,pengobatan;
   // TextView textview;

    public static final String url_data = "http://peternakan.xyz/rd/data_kesehatan.php";
    public static final String url_cari = "http://peternakan.xyz/rd/cari_kesehatan.php";
    public static final String url_edit = "http://peternakan.xyz/rd/editSehat.php";

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String TAG_ID_RFID = "id_rfid";
    public static final String TAG_Vaksin= "vaksin";
    public static final String TAG_pengobatan = "pengobatan";
    public static final String Tag_diagnosa = "diagnosa";
    public static final String TAG_tgl_periksa = "tgl_periksa";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";
    public static final String TAG_SUCCESS="success";

    String tag_json_obj = "json_obj_req";
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
                   // textview.append(strIncom);

            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kesehatan);
        btnTambah = (Button) findViewById(R.id.tambah);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        bluet.gethandler(mHandler);

        list_view = (ListView) findViewById(R.id.list_view);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuKesehatan.this, tambahKesehatan.class);
                menuKesehatan.this.startActivity(intent);}
        });

        adapter = new com.example.ardiani.myapplication.adapter.Adapter_sehat(com.example.ardiani.myapplication.menuKesehatan.this, listData);
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
                dialog = new AlertDialog.Builder(menuKesehatan.this);
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

                        DataModelSehat item = new DataModelSehat();

                        item.setId_rfid(obj.getString(TAG_ID_RFID));
                        item.setPengobatan(obj.getString(TAG_pengobatan));
                        item.setTgl_periksa(obj.getString(TAG_tgl_periksa));
                        item.setVaksin(obj.getString(TAG_Vaksin));
                        item.setDiagnosa(obj.getString(Tag_diagnosa));

                        listData.add(item);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(com.example.ardiani.myapplication.menuKesehatan.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void cariData(final String keyword) {
        pDialog = new ProgressDialog(menuKesehatan.this);
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

                            DataModelSehat data = new DataModelSehat();

                            data.setId_rfid(obj.getString(TAG_ID_RFID));
                            data.setTgl_periksa(obj.getString(TAG_tgl_periksa));
                            data.setDiagnosa(obj.optString(Tag_diagnosa));
                            data.setPengobatan(obj.getString(TAG_pengobatan));
                            data.setVaksin(obj.getString(TAG_Vaksin));

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

    //mengambil data ditampilkan pada form
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
                        String tglx_periksa    = jObj.getString(TAG_tgl_periksa);
                        String diagnosax  = jObj.getString(Tag_diagnosa);
                        String vaksinx = jObj.getString(TAG_Vaksin);
                        String pengobatanx =jObj.getString(TAG_pengobatan);


                        DialogForm(idx, tglx_periksa,diagnosax,vaksinx,pengobatanx, "PRINT");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(menuKesehatan.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(menuKesehatan.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
    private void DialogForm(String idx, String tglx_periksa, String diagnosax, String vaksinx,String pengobatanx, String Button){
        dialog = new AlertDialog.Builder(menuKesehatan.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_sehat, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Data");

        txid_rfid      = (EditText) dialogView.findViewById(R.id.txt_id);
        txtgl_periksa   = (EditText) dialogView.findViewById(R.id.txt_tgl_periksa);
        txdiagnosa = (EditText) dialogView.findViewById(R.id.txt_diagnosa);
        txvaksin   = (EditText) dialogView.findViewById(R.id.txt_vaksin);
        txpengobatan =(EditText) dialogView.findViewById(R.id.txt_pengobatan);

        if (!idx.isEmpty()){
            txid_rfid.setText(idx);
            txdiagnosa.setText(diagnosax);
            txvaksin.setText(vaksinx);
            txtgl_periksa.setText(tglx_periksa);
            txpengobatan.setText(pengobatanx);
        } else {
            kosong();
        }

        dialog.setPositiveButton(Button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_rfid      = txid_rfid.getText().toString();
                tgl_periksa    = txtgl_periksa.getText().toString();
                diagnosa   = txdiagnosa.getText().toString();
                vaksin= txvaksin.getText().toString();
                pengobatan = txpengobatan.getText().toString();
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
        txtgl_periksa.setText(null);
        txdiagnosa.setText(null);
        txvaksin.setText(null);
        txpengobatan.setText(null);

    }
    private void printer(){
        String pesan = txid_rfid.getText().toString();
        String pesan1= txtgl_periksa.getText().toString();
        String pesan2 = txdiagnosa.getText().toString();
        String pesan3 = txvaksin.getText().toString();
        String pesan4 = txpengobatan.getText().toString();
        txid_rfid.setText("");
        txtgl_periksa.setText("");
        txdiagnosa.setText("");
        txvaksin.setText("");
        txpengobatan.setText("");
        kirim(pesan);
        kirim(pesan1);
        kirim(pesan2);
        kirim(pesan3);
        kirim(pesan4);
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


}



