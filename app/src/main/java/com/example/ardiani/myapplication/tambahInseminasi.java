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

public class tambahInseminasi extends AppCompatActivity implements View.OnClickListener {
    private EditText txKode,txIR,txTgl,txPetugas;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private String host ="http://peternakan.xyz/rd/registInseminasi.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_inseminasi);

        txKode =(EditText) findViewById(R.id.editTextKose);
        txIR =(EditText) findViewById(R.id.editTextUid);
        txTgl =(EditText) findViewById(R.id.editTexttgl);
        txPetugas =(EditText) findViewById(R.id.editTextPetugas);

        btnRegister = (Button) findViewById(R.id.btnTambah1);

        progressDialog = new ProgressDialog(this);

        btnRegister.setOnClickListener(this);
    }
    private void registerInseminasi (){
        final String kode_segmen =txKode.getText().toString().trim();
        final String tanggal_inseminasi =txTgl.getText().toString().trim();
        final String petugas = txPetugas.getText().toString().trim();
        final String id_rfid =txIR.getText().toString().trim();

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
                        Toast.makeText(getApplicationContext(), "Gagal Menambahkan", Toast.LENGTH_LONG).show();


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tanggal_inseminasi", tanggal_inseminasi);
                params.put("kode_segmen", kode_segmen);
                params.put("petugas", petugas);
                params.put("id_rfid", id_rfid);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onClick(View v) {
        if(v == btnRegister)
            registerInseminasi();

    }
}

