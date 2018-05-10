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

public class tambahKepemilikan extends AppCompatActivity implements View.OnClickListener{
    private EditText txNamaPemilik,txalamat,txtglmilik, txke,txrfid;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private String host ="http://peternakan.xyz/rd/registMilik.php";

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
                    txrfid.append(strIncom);

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kepemilikan);

        txNamaPemilik     = (EditText) findViewById(R.id.editTextNapel);
        txalamat          = (EditText) findViewById(R.id.editTextAlamat);
        txtglmilik         = (EditText) findViewById(R.id.editTextTgm) ;
        txke            = (EditText) findViewById(R.id.editTextke);
        txrfid      = (EditText) findViewById(R.id.editTextUid);

        btnRegister = (Button) findViewById(R.id.btn_tambah);
        progressDialog = new ProgressDialog(this);
        btnRegister.setOnClickListener(this);
    }

    private void registerMilik(){
        final String nama_pemilik = txNamaPemilik.getText().toString().trim();
        final String alamat = txalamat.getText().toString().trim();
        final String tgl_memiliki = txtglmilik.getText().toString().trim();
        final String kepemilikan_ke = txke.getText().toString().trim();
        final String id_rfid = txrfid.getText().toString().trim();

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
                        Toast.makeText(getApplicationContext(), "Gagal Menambahkan Data", Toast.LENGTH_LONG).show();


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_pemilik", nama_pemilik);
                params.put("alamat", alamat);
                params.put("tgl_memiliki", tgl_memiliki);
                params.put("id_rfid", id_rfid);
                params.put("kepemilikan_ke", kepemilikan_ke);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        if(v ==btnRegister);
        registerMilik();

    }
}

