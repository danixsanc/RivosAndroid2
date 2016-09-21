package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudRegistro {


    public SolicitudRegistro(String firstName, String lastName, String phone, String email, String password, String gcm_Id, String user_Type, boolean register_Fb) {
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
        Email = email;
        Password = password;
        Gcm_Id = gcm_Id;
        User_Type = user_Type;
        Register_Fb = register_Fb;
    }

    public SolicitudRegistro() {
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGcm_Id() {
        return Gcm_Id;
    }

    public void setGcm_Id(String gcm_Id) {
        Gcm_Id = gcm_Id;
    }

    public String getUser_Type() {
        return User_Type;
    }

    public void setUser_Type(String user_Type) {
        User_Type = user_Type;
    }

    public boolean isRegister_Fb() {
        return Register_Fb;
    }

    public void setRegister_Fb(boolean register_Fb) {
        Register_Fb = register_Fb;
    }

    private String FirstName;
    private String LastName;
    private String Phone;
    private String Email;
    private String Password;
    private String Gcm_Id;
    private String User_Type;
    private boolean Register_Fb;


}
