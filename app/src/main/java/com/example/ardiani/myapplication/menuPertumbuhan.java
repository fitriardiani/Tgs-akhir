package com.example.ardiani.myapplication;

import android.annotation.SuppressLint;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.ardiani.myapplication.adapter.Adapter_tumbuh;
import com.example.ardiani.myapplication.app.appControler;
import com.example.ardiani.myapplication.model.DataModelTumbuh;

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
import static com.example.ardiani.myapplication.menuInseminasi.Tag_Tanggal_Inseminasi;

public class menuPertumbuhan extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener{

        ProgressDialog pDialog;
        AlertDialog.Builder dialog;
        List<DataModelTumbuh> listData = new ArrayList<DataModelTumbuh>();
        Adapter_tumbuh adapter;
        SwipeRefreshLayout swipe;
        ListView list_view;
        private Button btnTambah;
        View dialogView;
        LayoutInflater inflater;
        int success;
        EditText txid_rfid, txxtgl_ukur, txberat, txumur,txpetugas;
        String  id_rfid, tgl_ukur,umur, berat,petugas;
        TextView textview;
        SearchView searchViewx;


        public static final String url_data = "http://peternakan.xyz/rd/data_pertumbuhan.php";
        public static final String url_cari = "http://peternakan.xyz/rd/cari_pertumbuhan.php";
        public static final String url_edit = "http://peternakan.xyz/rd/editTumbuh.php";

        private static final String TAG = MainActivity.class.getSimpleName();

        public static final String TAG_ID_RFID = "id_rfid";
        public static final String TAG_Tgl_ukur = "tgl_ukur";
        public static final String TAG_Berat = "berat";
        public static final String Tag_Umur = "umur";
        public static final String Tag_Petugas = "petugas";
        public static final String TAG_RESULTS = "results";
        public static final String TAG_MESSAGE = "message";
        public static final String TAG_VALUE = "value";
        public static final String TAG_SUCCESS="success";

        String tag_json_obj = "json_obj_req";

    //MENGIRIM DATA READ/WRITE
    @SuppressLint("HandlerLeak")
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
                    strIncom = strIncom.trim();
                    //try{
                    searchViewx.setQuery(strIncom,true);//}catch (Exception e){Log.e("ga",strIncom);}
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pertumbuhan);

        bluet.gethandler(mHandler);

        list_view = (ListView) findViewById(R.id.list_view);

        btnTambah = (Button) findViewById(R.id.tambah);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        btnTambah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuPertumbuhan.this, tambahPertumbuhan.class);
                menuPertumbuhan.this.startActivity(intent);
            }
        });

        adapter = new com.example.ardiani.myapplication.adapter.Adapter_tumbuh(com.example.ardiani.myapplication.menuPertumbuhan.this, listData);
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
               dialog = new AlertDialog.Builder(menuPertumbuhan.this);
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

                DataModelTumbuh item = new DataModelTumbuh();

                item.setId_rfid(obj.getString(TAG_ID_RFID));
                item.setPetugas(obj.getString(Tag_Petugas));

                item.setTgl_ukur(obj.getString(TAG_Tgl_ukur));
                item.setBerat(obj.getString(TAG_Berat));
                item.setUmur(obj.getString((Tag_Umur)));

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
                Toast.makeText(com.example.ardiani.myapplication.menuPertumbuhan.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
       // final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchViewx = (SearchView)MenuItemCompat.getActionView(item);
        searchViewx.setQueryHint(getString(R.string.type_name));
        searchViewx.setIconified(true);
        searchViewx.setOnQueryTextListener(this);
        return true;
    }

    private void cariData(final String keyword) {
        pDialog = new ProgressDialog(menuPertumbuhan.this);
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

                            DataModelTumbuh data = new DataModelTumbuh();

                            data.setId_rfid(obj.getString(TAG_ID_RFID));
                            data.setTgl_ukur(obj.getString(TAG_Tgl_ukur));
                            data.setBerat(obj.optString(TAG_Berat));
                            data.setPetugas(obj.getString(TAG_Petugas));
                            data.setUmur(obj.getString(Tag_Umur));

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
                        String tglx_ukur    = jObj.getString(TAG_Tgl_ukur);
                        String beratx  = jObj.getString(TAG_Berat);
                        String umurx = jObj.getString(Tag_Umur);
                        String petugasx = jObj.getString(TAG_Petugas);


                        DialogForm(idx, tglx_ukur,beratx,umurx,petugasx, "PRINT");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(menuPertumbuhan.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(menuPertumbuhan.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
    private void DialogForm(String idx, String tglx_ukur, String beratx, String umurx, String petugasx, String Button){
        dialog = new AlertDialog.Builder(menuPertumbuhan.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_tumbuh, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Data");

        txid_rfid      = (EditText) dialogView.findViewById(R.id.txt_id);
        txxtgl_ukur   = (EditText) dialogView.findViewById(R.id.txt_tgl_ukur);
        txberat = (EditText) dialogView.findViewById(R.id.txt_berat);
        txumur   = (EditText) dialogView.findViewById(R.id.txt_umur);
        txpetugas   = (EditText) dialogView.findViewById(R.id.txt_petugas);

        if (!idx.isEmpty()){
            txid_rfid.setText(idx);
            txxtgl_ukur.setText(tglx_ukur);
            txberat.setText(beratx);
            txumur.setText(umurx);
            txpetugas.setText(petugasx);
        } else {
            kosong();
        }

        dialog.setPositiveButton(Button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_rfid     = txid_rfid.getText().toString();
                tgl_ukur    = txxtgl_ukur.getText().toString();
                umur        = txumur.getText().toString();
                berat       = txberat.getText().toString();
                petugas     = txpetugas.getText().toString();

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
        txxtgl_ukur.setText(null);
        txberat.setText(null);
        txumur.setText(null);
        txpetugas.setText(null);

    }
    private void printer(){
        String pesan = txid_rfid.getText().toString();
        String pesan1= txxtgl_ukur.getText().toString();
        String pesan2 = txberat.getText().toString();
        String pesan3 = txumur.getText().toString();
        String pesan4 = txpetugas.getText().toString();
        txid_rfid.setText("");
        txxtgl_ukur.setText("");
        txberat.setText("");
        txpetugas.setText("");
        txpetugas.setText("");
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




