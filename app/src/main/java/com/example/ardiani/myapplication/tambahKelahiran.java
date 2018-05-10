package com.example.ardiani.myapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
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

public class tambahKelahiran extends AppCompatActivity implements View.OnClickListener{

    private EditText txTgllahir,txPetugas, txKet,txJk, txRfid;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private String host ="http://peternakan.xyz/rd/registLahir.php";

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
                    txRfid.append(strIncom);

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kelahiran);

        txTgllahir =(EditText) findViewById(R.id.editTextTL);
        txPetugas  = (EditText) findViewById(R.id.editTexNP);
        txKet      = (EditText) findViewById(R.id.editTextKet);
        txJk       = (EditText) findViewById(R.id.editTextJK);
        txRfid     = (EditText) findViewById(R.id.editTextRf);

        btnRegister = (Button) findViewById(R.id.btn_tambah);
        progressDialog = new ProgressDialog(this);
        btnRegister.setOnClickListener(this);
    }

    private void registerKelahiran(){
        final String tgl_lahir = txTgllahir.getText().toString().trim();
        final String petugas = txPetugas.getText().toString().trim();
        final String keterangan = txKet.getText().toString().trim();
        final String id_rfid = txRfid.getText().toString().trim();
        final String jenis_kelamin = txJk.getText().toString().trim();

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
                params.put("tgl_lahir", tgl_lahir);
                params.put("keterangan", keterangan);
                params.put("petugas", petugas);
                params.put("id_rfid", id_rfid);
                params.put("jenis_kelamin", jenis_kelamin);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        if(v == btnRegister)
            registerKelahiran();


    }
}

