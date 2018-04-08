package com.example.ardiani.myapplication.model;

/**
 * Created by ardiani on 3/15/2018.
 */

public class DataModel  {
    private  String id_rfid, no_telinga, nama_sapi, ras_sapi,tgl_lahir,status;

    public DataModel(){

    }
    public DataModel(String id_rfid, String no_telinga, String nama_sapi , String ras_sapi, String tgl_lahir, String status){
        this.id_rfid = id_rfid;
        this.no_telinga= no_telinga;
        this.nama_sapi = nama_sapi;
        this.ras_sapi=ras_sapi;
        this.tgl_lahir=tgl_lahir;
        this.status=status;
    }

    public String getId_rfid() {return id_rfid;}

    public void setId_rfid(String id_rfid) {this.id_rfid = id_rfid;}

    public String getNo_telinga() {return no_telinga;}

    public void setNo_telinga(String no_telinga) {this.no_telinga = no_telinga;}

    public String getNama_sapi() {return nama_sapi;}

    public void setNama_sapi(String nama_sapi) {this.nama_sapi = nama_sapi;}

    public String getRas_sapi() {return ras_sapi;}

    public void setRas_sapi(String ras_sapi) {this.ras_sapi = ras_sapi;}

    public String getTgl_lahir() {return tgl_lahir;}

    public void setTgl_lahir(String tgl_lahir) {this.tgl_lahir = tgl_lahir;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}


