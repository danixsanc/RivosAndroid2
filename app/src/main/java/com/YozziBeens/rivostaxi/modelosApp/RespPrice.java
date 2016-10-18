package com.YozziBeens.rivostaxi.modelosApp;

/**
 * Created by danixsanc on 24/09/16.
 */
public class RespPrice {



    public RespPrice() {
    }


    public RespPrice(String cabbie_Id, String cabbie_Name, String price, String latCabbie, String longCabbie, String gcmIdCabbie, String model, String passengers, String brand, String image, String dits) {
        Cabbie_Id = cabbie_Id;
        Cabbie_Name = cabbie_Name;
        Price = price;
        LatCabbie = latCabbie;
        LongCabbie = longCabbie;
        GcmIdCabbie = gcmIdCabbie;
        Model = model;
        Passengers = passengers;
        Brand = brand;
        Image = image;
        Dist = dits;
    }

    public String getCabbie_Id() {
        return Cabbie_Id;
    }

    public void setCabbie_Id(String cabbie_Id) {
        Cabbie_Id = cabbie_Id;
    }

    public String getCabbie_Name() {
        return Cabbie_Name;
    }

    public void setCabbie_Name(String cabbie_Name) {
        Cabbie_Name = cabbie_Name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getLatCabbie() {
        return LatCabbie;
    }

    public void setLatCabbie(String latCabbie) {
        LatCabbie = latCabbie;
    }

    public String getLongCabbie() {
        return LongCabbie;
    }

    public void setLongCabbie(String longCabbie) {
        LongCabbie = longCabbie;
    }

    public String getGcmIdCabbie() {
        return GcmIdCabbie;
    }

    public void setGcmIdCabbie(String gcmIdCabbie) {
        GcmIdCabbie = gcmIdCabbie;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPassengers() {
        return Passengers;
    }

    public void setPassengers(String passengers) {
        Passengers = passengers;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDits() {
        return Dist;
    }

    public void setDits(String dits) {
        Dist = dits;
    }

    private String Cabbie_Id;
    private String Cabbie_Name;
    private String Price;
    private String LatCabbie;
    private String LongCabbie;
    private String GcmIdCabbie;
    private String Model;
    private String Passengers;
    private String Brand;
    private String Image;
    private String Dist;

}
