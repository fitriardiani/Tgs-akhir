package com.example.ardiani.myapplication.model;

/**
 * Created by ardiani on 3/23/2018.
 */

public class DataModelTumbuh {
    private String tgl_ukur, umur, berat, id_rfid,petugas;

    public DataModelTumbuh(){
    }
    public DataModelTumbuh (String tgl_ukur, String umur,String berat, String id_rfid, String petugas){
        this.tgl_ukur = tgl_ukur;
        this.umur = umur;
        this.berat = berat;
        this.id_rfid = id_rfid;
        this.petugas = petugas;
    }

    public String getPetugas() {return petugas;}

    public void setPetugas(String petugas) {this.petugas = petugas;}

    public String getTgl_ukur() {return tgl_ukur;}

    public void setTgl_ukur(String tgl_ukur) {
        this.tgl_ukur = tgl_ukur;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getId_rfid() {
        return id_rfid;
    }

    public void setId_rfid(String id_rfid) {
        this.id_rfid = id_rfid;
    }
}
