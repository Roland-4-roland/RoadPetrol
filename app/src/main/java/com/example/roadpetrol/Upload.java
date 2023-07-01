package com.example.roadpetrol;

public class Upload {
    private String Desc;
    private String Latitude;
    private String Longitude;
    private String Image;
    private String Address;
    private static String DocID;
    private String CurrentuserID;
    public  Upload(){

    }

    public Upload(String desc, String latitude, String longitude, String image, String docID, String currentuserID,String address) {
        Desc = desc;
        Latitude = latitude;
        Longitude = longitude;
        Image = image;
        DocID = docID;
        CurrentuserID = currentuserID;
        Address=address;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDocID() {
        return DocID;
    }

    public static void setDocID(String docID) {
        DocID = docID;
    }

    public String getCurrentuserID() {
        return CurrentuserID;
    }

    public void setCurrentuserID(String currentuserID) {
        CurrentuserID = currentuserID;
    }

    public String getAddress() {return Address;}

    public void setAddress(String address) {Address = address;}
}



