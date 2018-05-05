package com.example.ardiani.myapplication.model;

/**
 * Created by ardiani on 3/23/2018.
 */

public class DataModelPemilik {
    private  String id_rfid, tgl_memiliki, nama_pemilik, kepemilikan_ke, alamat;

    public DataModelPemilik(){
    }
    public DataModelPemilik (String id_rfid, String tgl_memiliki, String nama_pemilik,String kepemilikan_ke, String alamat){
        this.id_rfid = id_rfid;
        this.tgl_memiliki = tgl_memiliki;
        this.nama_pemilik = nama_pemilik;
        this.kepemilikan_ke = kepemilikan_ke;
        this.alamat = alamat;
    }

    public String getId_rfid() {
        return id_rfid;
    }

    public void setId_rfid(String id_rfid) {
        this.id_rfid = id_rfid;
    }

    public String getTgl_memiliki() {
        return tgl_memiliki;
    }

    public void setTgl_memiliki(String tgl_memiliki) {
        this.tgl_memiliki = tgl_memiliki;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }

    public String getKepemilikan_ke() {
        return kepemilikan_ke;
    }

    public void setKepemilikan_ke(String kepemilikan_ke) {
        this.kepemilikan_ke = kepemilikan_ke;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String kepemilikan_ke) {
        this.alamat = kepemilikan_ke;
    }

}
