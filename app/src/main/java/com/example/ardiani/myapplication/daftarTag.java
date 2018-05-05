package com.example.ardiani.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class daftarTag extends AppCompatActivity implements View.OnClickListener {

    private EditText txrfid,txnotel,txnama,txtgl,txras,txstatus;
    private Button btnTambah;
    private ProgressDialog progressDialog;
    private String host ="http://peternakan.xyz/rd/registerRfid.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_tag);

        txrfid =(EditText) findViewById(R.id.txidrfid);
        txnotel=(EditText) findViewById(R.id.txnotelinga);
        txnama = (EditText) findViewById(R.id.txnama);
        txtgl = (EditText) findViewById(R.id.txtgllahir);
        txras = (EditText) findViewById(R.id.txidrassapi);
        txstatus=(EditText)findViewById(R.id.txstatus);

        btnTambah = (Button) findViewById(R.id.btnsubmit);

        progressDialog = new ProgressDialog(this);

        btnTambah.setOnClickListener(this);
    }
    private void registerRfid (){
        final String id_rfid =txrfid.getText().toString().trim();
        final String no_telinga = txnotel.getText().toString().trim();
        final String nama_sapi = txnama.getText().toString().trim();
        final String ras_sapi = txras.getText().toString().trim();
        final String tgl_lahir = txtgl.getText().toString().trim();
        final String status = txstatus.getText().toString().trim();

        progressDialog.setMessage("Submitting rfid...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),"Gagal Menambahkan", Toast.LENGTH_LONG).show();


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_rfid", id_rfid);
                params.put("no_telinga", no_telinga);
                params.put("nama_sapi", nama_sapi);
                params.put("ras_sapi", ras_sapi);
                params.put("tgl_lahir", tgl_lahir);
                params.put("status",status);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    @Override
    public void onClick(View view) {
        if(view == btnTambah)
            registerRfid();

    }


}

