package com.example.ardiani.myapplication.model;

/**
 * Created by ardiani on 3/19/2018.
 */

public class DataModelInseminasi {
    private  String id_rfid, tanggal_inseminasi, kode_segmen, petugas;

    public DataModelInseminasi(){
    }
    public DataModelInseminasi (String id_rfid, String tanggal_inseminasi, String kode_segmen, String petugas){
        this.id_rfid = id_rfid;
        this.tanggal_inseminasi = tanggal_inseminasi;
        this.kode_segmen = kode_segmen;
        this.petugas = petugas;
    }

    public String getId_rfid() {return id_rfid;}

    public void setId_rfid(String id_rfid) {this.id_rfid = id_rfid;}

    public String getTanggal_inseminasi() {return tanggal_inseminasi;}

    public void setTanggal_inseminasi(String tanggal_inseminasi) {this.tanggal_inseminasi = tanggal_inseminasi;}

    public String getKode_segmen() {return kode_segmen;}

    public void setKode_segmen(String kode_segmen) {this.kode_segmen = kode_segmen;}

    public String getPetugas() {return petugas;}

    public void setPetugas(String petugas) {this.petugas = petugas;}
}
