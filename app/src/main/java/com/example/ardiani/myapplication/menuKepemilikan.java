package com.example.ardiani.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ardiani.myapplication.adapter.Adapter_pemilik;
import com.example.ardiani.myapplication.app.appControler;
import com.example.ardiani.myapplication.model.DataModelPemilik;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class menuKepemilikan extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener{
        ProgressDialog pDialog;
        AlertDialog.Builder dialog;
        List<DataModelPemilik> listData = new ArrayList<DataModelPemilik>();
        Adapter_pemilik adapter;
        SwipeRefreshLayout swipe;
        ListView list_view;
        private Button btnTambah;
        public static final String url_data = "http://peternakan.xyz/rd/data_pemilik.php";
        public static final String url_cari = "http://peternakan.xyz/rd/cari_pemilik.php";
        private static final String TAG = MainActivity.class.getSimpleName();

        public static final String TAG_ID_RFID = "id_rfid";
        public static final String TAG_Nama_Pemilik= "nama_pemilik";
        public static final String TAG_tgl_memilki = "tgl_memiliki";
        public static final String Tag_Pemilikke = "kepemilikan_ke";
        public static final String TAG_RESULTS = "results";
        public static final String TAG_MESSAGE = "message";
        public static final String TAG_VALUE = "value";


        String tag_json_obj = "json_obj_req";

        @Override

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kepemilikan);
        btnTambah = (Button) findViewById(R.id.tambah);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        list_view = (ListView) findViewById(R.id.list_view);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(menuKepemilikan.this, tambahKepemilikan.class);
            menuKepemilikan.this.startActivity(intent);}
        });

        adapter = new com.example.ardiani.myapplication.adapter.Adapter_pemilik(com.example.ardiani.myapplication.menuKepemilikan.this, listData);
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
                                dialog = new AlertDialog.Builder(menuKepemilikan.this);
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
        private void print(String idx){


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
                        DataModelPemilik item = new DataModelPemilik();

                        item.setId_rfid(obj.getString(TAG_ID_RFID));
                        item.setNama_pemilik(obj.getString(TAG_Nama_Pemilik));

                        item.setTgl_memiliki(obj.getString(TAG_tgl_memilki));
                        item.setKepemilikan_ke(obj.getString(Tag_Pemilikke));

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
                Toast.makeText(com.example.ardiani.myapplication.menuKepemilikan.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
                pDialog = new ProgressDialog(menuKepemilikan.this);
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

                DataModelPemilik data = new DataModelPemilik();

                data.setId_rfid(obj.getString(TAG_ID_RFID));
                //data.setNama(obj.getString(TAG_NAMA));

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
