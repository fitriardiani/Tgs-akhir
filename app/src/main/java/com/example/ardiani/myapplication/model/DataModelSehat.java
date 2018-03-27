package com.example.ardiani.myapplication.model;

/**
 * Created by ardiani on 3/23/2018.
 */

public class DataModelSehat {
    private String tgl_periksa, diagnosa,vaksin, pengobatan, id_rfid;

    public DataModelSehat(){
    }
    public DataModelSehat (String tgl_periksa, String diagnosa,String vaksin,String pengobatan,String id_rfid){
        this.tgl_periksa = tgl_periksa;
        this.diagnosa =diagnosa;
        this.vaksin =vaksin;
        this.pengobatan =pengobatan;
        this.id_rfid = id_rfid;
    }

    public String getTgl_periksa() {return tgl_periksa;}

    public void setTgl_periksa(String tgl_periksa) {this.tgl_periksa = tgl_periksa;}

    public String getDiagnosa() {return diagnosa;}

    public void setDiagnosa(String diagnosa) {this.diagnosa = diagnosa;}

    public String getVaksin() {return vaksin;}

    public void setVaksin(String vaksin) {this.vaksin = vaksin;}

    public String getPengobatan() {return pengobatan;}

    public void setPengobatan(String pengobatan) {this.pengobatan = pengobatan;}

    public String getId_rfid() {return id_rfid;}

    public void setId_rfid(String id_rfid) {this.id_rfid = id_rfid;}

}
