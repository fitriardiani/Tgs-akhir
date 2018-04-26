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
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ardiani.myapplication.adapter.Adapter;
import com.example.ardiani.myapplication.app.appControler;
import com.example.ardiani.myapplication.model.DataModel;

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
import java.util.SimpleTimeZone;

import static android.media.CamcorderProfile.get;

public class listRfid extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener {

    ProgressDialog pDialog;
    AlertDialog.Builder dialog;
    Toolbar toolbar;
    View dialogView;
    List<DataModel> listData = new ArrayList<DataModel>();
    Adapter adapter;
    LayoutInflater inflater;
    int success;
    SwipeRefreshLayout swipe;
    ListView list_view;
    private Button btnTambah;
    EditText txid_rfid, txnotelinga, txnamasapi, txrassapi, txtgllahir, txstatus;
    String id_rfid, no_telinga, nama_sapi, ras_sapi, status, tgl_lahir;

    //public static String id_rfid_g = "";

    public static final String url_data = "http://peternakan.xyz/rd/search_rfid.php";
    public static final String url_cari = "http://peternakan.xyz/rd/cari_rfid.php";
    public static final String url_edit = "http://peternakan.xyz/rd/editRfid.php";
    public static final String url_update = "http://peternakan.xyz/rd/update.php";

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String TAG_ID_RFID = "id_rfid";
    public static final String TAG_no_tel = "no_telinga";
    public static final String TAG_nama_sapi = "nama_sapi";
    public static final String TAG_ras_sapi = "ras_sapi";
    public static final String TAG_tgl_lahir = "tgl_lahir";
    public static final String TAG_status = "status";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";
    public static final String TAG_SUCCESS = "success";
    public static final String EMP_ID = "emp_id";

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
                    //textview.append(strIncom);

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rfid);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        list_view = (ListView) findViewById(R.id.list_view);
        bluet.gethandler(mHandler);

        btnTambah = (Button) findViewById(R.id.tambahRfid);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listRfid.this, daftarTag.class);
                listRfid.this.startActivity(intent);
            }
        });

        adapter = new com.example.ardiani.myapplication.adapter.Adapter(listRfid.this, listData);
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
                final CharSequence[] dialogitem = {"PRINT"};
                dialog = new AlertDialog.Builder(listRfid.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Auto-generated method stub
                        switch (which) {
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

    private void kosong() {
        txid_rfid.setText(null);
        txnotelinga.setText(null);
        txnamasapi.setText(null);
        txrassapi.setText(null);
        txtgllahir.setText(null);
        txstatus.setText(null);

    }

    private void DialogForm(String idx, String nox_tel, String namax_sapi, final String rasx_sapi, String tglx_lahir, String statusx, String Button) {
        dialog = new AlertDialog.Builder(listRfid.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_rfid, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Data");

        txid_rfid = (EditText) dialogView.findViewById(R.id.txt_id);
        txnotelinga = (EditText) dialogView.findViewById(R.id.txt_nomor_telinga);
        txnamasapi = (EditText) dialogView.findViewById(R.id.txt_nama_sapi);
        txrassapi = (EditText) dialogView.findViewById(R.id.txt_ras_sapi);
        txtgllahir = (EditText) dialogView.findViewById(R.id.txt_tgl_lahir);
        txstatus = (EditText) dialogView.findViewById(R.id.txt_stt);

        if (!idx.isEmpty()) {
            txid_rfid.setText(idx);
            txnotelinga.setText(nox_tel);
            txnamasapi.setText(namax_sapi);
            txrassapi.setText(rasx_sapi);
            txtgllahir.setText(tglx_lahir);
            txstatus.setText(statusx);
        } else {
            kosong();
        }

        dialog.setPositiveButton(Button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_rfid = txid_rfid.getText().toString();
                no_telinga = txnotelinga.getText().toString();
                nama_sapi = txnamasapi.getText().toString();
                ras_sapi = txrassapi.getText().toString();
                tgl_lahir = txtgllahir.getText().toString();
                status = txstatus.getText().toString();

                cetak();
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
                        String nox_telinga    = jObj.getString(TAG_no_tel);
                        String namax_sapi  = jObj.getString(TAG_nama_sapi);
                        String rasx_sapi = jObj.getString(TAG_ras_sapi);
                        String tglx_lahir = jObj.getString(TAG_tgl_lahir);
                        String statusx = jObj.getString(TAG_status);

                        DialogForm(idx, nox_telinga,namax_sapi,rasx_sapi,tglx_lahir,statusx, "PRINT");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(listRfid.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(listRfid.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                        DataModel item = new DataModel();

                        item.setId_rfid(obj.getString(TAG_ID_RFID));
                        item.setNo_telinga(obj.getString(TAG_no_tel));
                        item.setNama_sapi(obj.getString(TAG_nama_sapi));
                        item.setRas_sapi(obj.getString(TAG_ras_sapi));
                        item.setTgl_lahir(obj.getString(TAG_tgl_lahir));
                        item.setStatus(obj.getString(TAG_status));


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
                Toast.makeText(listRfid.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
        pDialog = new ProgressDialog(listRfid.this);
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

                            DataModel data = new DataModel();

                            data.setId_rfid(obj.getString(TAG_ID_RFID));
                            data.setNo_telinga(obj.getString(TAG_no_tel));
                            data.setNama_sapi(obj.getString(TAG_nama_sapi));
                            data.setRas_sapi(obj.getString(TAG_ras_sapi));
                            data.setTgl_lahir(obj.getString(TAG_tgl_lahir));
                            data.setStatus(obj.getString(TAG_status));

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

    private void cetak(){
        String pesan = txid_rfid.getText().toString();
        String pesan1 = txnotelinga.getText().toString();
        String pesan2 = txnamasapi.getText().toString();
        String pesan3 = txrassapi.getText().toString();
        String pesan4 = txtgllahir.getText().toString();
        String pesan5 = txstatus.getText().toString();
        txid_rfid.setText("");
        txnotelinga.setText("");
        txnamasapi.setText("");
        txrassapi.setText("");
        txtgllahir.setText("");
        txstatus.setText("");
        kirim(pesan);
        kirim(pesan1);
        kirim(pesan2);
        kirim(pesan3);
        kirim(pesan4);
        kirim(pesan5);
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