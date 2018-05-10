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

public class tambahPertumbuhan extends AppCompatActivity  implements View.OnClickListener{
    EditText txtgl_ukur,txumur,txberat,txptgs,txidrfid;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private String host ="http://peternakan.xyz/rd/registTumbuh.php";

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
                    txidrfid.append(strIncom);

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pertumbuhan);


        txtgl_ukur = (EditText) findViewById(R.id.editTexttanggal);
        txumur = (EditText) findViewById(R.id.editTextumur);
        txberat = (EditText) findViewById(R.id.editTextBerat);
        txptgs = (EditText) findViewById(R.id.editTextPtgs);
        txidrfid = (EditText) findViewById(R.id.editTextUidRfid);
        btnRegister = (Button) findViewById(R.id.btn_tambah);
        progressDialog = new ProgressDialog(this);
        btnRegister.setOnClickListener(this);

    }
    private void registTumbuh(){
        final String tgl_ukur = txtgl_ukur.getText().toString().trim();
        final String umur= txumur.getText().toString().trim();
        final String  berat = txberat.getText().toString().trim();
        final String petugas = txptgs.getText().toString().trim();
        final String id_rfid = txidrfid.getText().toString().trim();

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
                params.put("tgl_ukur", tgl_ukur);
                params.put("berat", berat);
                params.put("umur", umur);
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
        if(v == btnRegister);
        registTumbuh();

    }
}

