package com.example.ardiani.myapplication;

import android.annotation.SuppressLint;
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

public class tambahKesehatan extends AppCompatActivity implements View.OnClickListener{
    EditText txTglPeriksa, txDiagnosa,txVaksin,txObat,txIdRfid;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private String host ="http://peternakan.xyz/rd/regisrtSehat.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kesehatan);

        txTglPeriksa = (EditText) findViewById(R.id.editTextTgl);
        txDiagnosa = (EditText) findViewById(R.id.editTextDiagnosa);
        txVaksin = (EditText) findViewById(R.id.editTextVaksin);
        txObat = (EditText) findViewById(R.id.editTextPengobatan);
        txIdRfid=(EditText) findViewById(R.id.editTextUid);

        btnRegister = (Button) findViewById(R.id.btn_tambah);
        progressDialog = new ProgressDialog(this);
        btnRegister.setOnClickListener(this);

    }

    private void registKesehatan(){
        final String tgl_periksa = txTglPeriksa.getText().toString().trim();
        final String diagnosa = txDiagnosa.getText().toString().trim();
        final String vaksin = txVaksin.getText().toString().trim();
        final String pengobatan = txObat.getText().toString().trim();
        final String id_rfid = txIdRfid.getText().toString().trim();

        progressDialog.setMessage("Submitting Data...");
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
                params.put("tgl_periksa", tgl_periksa);
                params.put("diagnosa", diagnosa);
                params.put("vaksin", vaksin);
                params.put("pengobatan", pengobatan);
                params.put("id_rfid", id_rfid);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        if(v == btnRegister);
        registKesehatan();

    }
}
