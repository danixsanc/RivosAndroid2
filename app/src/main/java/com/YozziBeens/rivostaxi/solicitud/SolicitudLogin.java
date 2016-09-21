package com.YozziBeens.rivostaxi.solicitud;

public class SolicitudLogin {


    public SolicitudLogin(String email, String password, String gcm_Id, String user_Type, boolean login_Fb) {
        Email = email;
        Password = password;
        Gcm_Id = gcm_Id;
        User_Type = user_Type;
        Login_Fb = login_Fb;
    }

    public SolicitudLogin() {
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

    public boolean isLogin_Fb() {
        return Login_Fb;
    }

    public void setLogin_Fb(boolean login_Fb) {
        Login_Fb = login_Fb;
    }



    private String Email;
    private String Password;
    private String Gcm_Id;
    private String User_Type;
    private boolean Login_Fb;


}
