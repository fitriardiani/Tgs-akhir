package com.example.ardiani.myapplication.model;

/**
 * Created by ardiani on 3/23/2018.
 */

public class DataModelLahir {
    private String tgl_lahir, petugas, keterangan, jenis_kelamin, id_rfid;

    public DataModelLahir(){
    }

    public DataModelLahir(String tgl_lahir,String petugas,String keterangan,String jenis_kelamin,String id_rfid){
        this.id_rfid = id_rfid;
        this.tgl_lahir = tgl_lahir;
        this.keterangan = keterangan;
        this.jenis_kelamin = jenis_kelamin;
        this.petugas = petugas;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getPetugas() {
        return petugas;
    }

    public void setPetugas(String petugas) {
        this.petugas = petugas;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getId_rfid() {
        return id_rfid;
    }

    public void setId_rfid(String id_rfid) {
        this.id_rfid = id_rfid;
    }
}
