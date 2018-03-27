package com.example.ardiani.myapplication.model;

/**
 * Created by ardiani on 3/15/2018.
 */

public class DataModel  {
    private  String id_rfid;

    public DataModel(){
    }
    public DataModel (String id_rfid){
        this.id_rfid = id_rfid;
    }

    public String getId_rfid() {
        return id_rfid;
    }

    public void setId_rfid(String id_rfid) {
        this.id_rfid = id_rfid;
    }
}


