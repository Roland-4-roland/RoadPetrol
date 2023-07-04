package com.example.roadpetrol;

public class Model {
    String desc;
    String latitude;
    String longitude;
    String address;
    String image;
    String docID;
    String currentuserID;

    String datetime;
    public Model(){

    }

    public Model(String desc, String latitude, String longitude, String image,String address,String docId ,String currentuserID, String datetime) {
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.address=address;
        this.currentuserID=currentuserID;
        this.docID=docId;
        this.datetime=datetime;
    }
    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getCurrentuserID() {
        return currentuserID;
    }

    public void setCurrentuserID(String currentuserID) {
        this.currentuserID = currentuserID;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


}


