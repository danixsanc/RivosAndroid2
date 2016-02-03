package com.yozzibeens.rivostaxi.utilerias;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Servicio {

    private JSONParser jsonParser;


    private static String loginURL = "http://appm.rivosservices.com/";
    private static String notfcURL = "http://appm.rivosservices.com/push.php";
    private static String processPayURL = "http://appm.rivosservices.com/conekta/proces_pay.php";

    private static String get_close_cabbie = "GetCloseCabbie";
    private static String get_if_is_airport = "GetIfIsAirpot";
    private static String register_gcmId = "Register_GcmId";
    private static String process_pay = "Process_Pay";
    private static String login_tag = "Login";
    private static String registerfbphone_tag = "registerfbphone";
    private static String loginf_tag = "Login_Fb";
    private static String delete_history_client = "Delete_History_Client";
    private static String get_card = "Get_Card";
    private static String set_card = "Set_Card";
    private static String get_favorite_cabbie = "Get_Favorite_Cabbie";
    private static String set_favorite_cabbie = "Set_Favorite_Cabbie";
    private static String set_favorite_place = "Set_Favorite_Place";
    private static String update_favorite_place = "Update_Favorite_Place";
    private static String get_price_of_travel = "GetPriceOfTravel";
    private static String delete_favorite_cabbie = "Delete_Favorite_Cabbie";
    private static String delete_favorite_place = "Delete_Favorite_Place";
    private static String delete_card = "Delete_Card";
    private static String get_cabbie_history = "Get_Cabbie_History";
    private static String get_place_history = "Get_Place_History";
    private static String get_client_history = "Get_Client_History";
    private static String get_client_history_details = "Get_Client_History_Details";
    private static String get_favorite_place = "Get_Favorite_Place";
    private static String get_favorite_place_for_id = "Get_Favorite_Place_For_Id";
    private static String update_tag = "UpdateUser";
    private static String register_tag = "Register";
    private static String registerfb_tag = "Register_Fb";
    private static String getuser_tag = "GetUser";
    private static String modify_data_tag = "ModifyData";
    private static String code_tag = "Code";
    private static String get_taxista_data = "gettaxistadata";
    private static String SendNotificationToTaxista = "sendnotification";
    private static String Send_Message = "Message";
    private static String set_client_history = "Set_Client_History";
    private static String set_client_history_pending = "Set_Client_History_Pending";
    private static String get_client_history_pending = "Get_Client_History_Pending";
    private static String get_request_for_id = "Get_Request_For_Id";

    public Servicio()
    {
        jsonParser = new JSONParser();
    }

    public JSONObject send_message(String subject, String message, String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", Send_Message);
            params.put("Subject", subject);
            params.put("Message", message);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public JSONObject getRequestForId(String Request_Id){

        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_request_for_id);
            params.put("Request_Id", Request_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject GetCloseCabbie(double latitude, double longitude)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_close_cabbie);
            params.put("Latitude", String.valueOf(latitude));
            params.put("Longitude", String.valueOf(longitude));

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);


            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject GetIfIsAriport(double latitude, double longitude)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_if_is_airport);
            params.put("Latitude", String.valueOf(latitude));
            params.put("Longitude", String.valueOf(longitude));

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);


            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject Register_GcmId(String gcm_id, String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", register_gcmId);
            params.put("GcmId", gcm_id);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);


            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject ProcessPay(String Price, String Token, String Name, String Email, String Phone)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", process_pay);
            params.put("Price", Price);
            params.put("Token", Token);
            params.put("Name", Name);
            params.put("Email", Email);
            params.put("Phone", Phone);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(processPayURL, params);


            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getTaxistaData(String name)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("tag", get_taxista_data);
            params.put("name", name);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /*public JSONObject sendNotification(String telefono)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("tag", SendNotificationToTaxista);
            params.put("phone", telefono);
            params.put("push", "1");
            //params.put("phone", phone);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
            /*
            if (json != null)
            {
                Log.d("JSON result", json.toString());
                JSONObject json1 = (JSONObject)json;
                return json;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }*/

    public JSONObject modify_data(String email)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", modify_data_tag);
            params.put("Email", email);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject verifyCode(String Client_Id, String Code)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", code_tag);
            params.put("Client_Id", Client_Id);
            params.put("Code", Code);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject loginUser(String email, String password) throws IOException, JSONException {
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
*/
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", login_tag);
            params.put("Email", email);
            params.put("Password", password);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;



    }

    public JSONObject sendNotification(String reg_Id) throws IOException, JSONException {

        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("push", "1");
            params.put("message", "Nueva Solicitud. Click para ver");
            params.put("reg_id", reg_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(notfcURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public JSONObject getPendingRequest(String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_client_history_pending);
            params.put("Client_Id", Client_Id);


            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }


    public JSONObject set_Client_History(String Latitude_In, String Longitude_In, String Latitude_Fn, String Longitude_Fn,
                                         String Client_Id, String Cabbie_Id, String Price_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", set_client_history);
            params.put("Latitude_In", Latitude_In);
            params.put("Longitude_In", Longitude_In);
            params.put("Latitude_Fn", Latitude_Fn);
            params.put("Longitude_Fn", Longitude_Fn);
            params.put("Client_Id", Client_Id);
            params.put("Cabbie_Id", Cabbie_Id);
            params.put("Price_Id", Price_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public JSONObject set_Client_History_Pending(String Latitude_In, String Longitude_In, String Latitude_Fn, String Longitude_Fn,
                                         String Client_Id, String Price_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", set_client_history_pending);
            params.put("Latitude_In", Latitude_In);
            params.put("Longitude_In", Longitude_In);
            params.put("Latitude_Fn", Latitude_Fn);
            params.put("Longitude_Fn", Longitude_Fn);
            params.put("Client_Id", Client_Id);
            params.put("Price_Id", Price_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public JSONObject loginUserFB(String email)
    {
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", loginf_tag));
        params.add(new BasicNameValuePair("email", email));
        //JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
        */
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", loginf_tag);
            params.put("Email", email);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }


    public JSONObject Delete_History_Client(String Request_Id, String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", delete_history_client);
            params.put("Client_Id", Client_Id);
            params.put("Request_Id", Request_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public JSONObject getCard(String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_card);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public JSONObject setCard(String Client_Id, String card)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", set_card);
            params.put("Client_Id", Client_Id);
            params.put("Card", card);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getFavoriteCabbie(String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_favorite_cabbie);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject deleteFavoriteCabbie(String Client_Id, String Cabbie_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", delete_favorite_cabbie);
            params.put("Client_Id", Client_Id);
            params.put("Cabbie_Id", Cabbie_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject deleteFavoritePlace(String Client_Id, String place_id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", delete_favorite_place);
            params.put("Client_Id", Client_Id);
            params.put("Place_Id", place_id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject deleteCard(String Client_Id, String Number_Card)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", delete_card);
            params.put("Client_Id", Client_Id);
            params.put("Number_Card", Number_Card);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getCabbieHistory(String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_cabbie_history);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getPlaceHistory(String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_place_history);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getClientHistory(String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_client_history);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getClientHistoryDetails(String Request_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_client_history_details);
            params.put("Request_Id", Request_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject setFavoriteCabbie(String Client_Id, String Cabbie_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", set_favorite_cabbie);
            params.put("Client_Id", Client_Id);
            params.put("Cabbie_Id", Cabbie_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getPriceOfTravel(double Latitude, double Longitude)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_price_of_travel);
            params.put("Latitude", String.valueOf(Latitude));
            params.put("Longitude", String.valueOf(Longitude));

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject setFavoritePlace(String Client_Id, String Place_Name, String Latitude, String Longitude ,String Desc_Place)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", set_favorite_place);
            params.put("Client_Id", Client_Id);
            params.put("Place_Name", Place_Name);
            params.put("Latitude", Latitude);
            params.put("Longitude", Longitude);
            params.put("Desc_Place", Desc_Place);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject updateFavoritePlace(String Client_Id, String Place_Name, String Latitude, String Longitude ,
                                          String Desc_Place, String Place_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", update_favorite_place);
            params.put("Place_Id", Place_Id);
            params.put("Client_Id", Client_Id);
            params.put("Place_Name", Place_Name);
            params.put("Latitude", Latitude);
            params.put("Longitude", Longitude);
            params.put("Desc_Place", Desc_Place);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getFavoritePlace(String Client_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_favorite_place);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getFavoritePlaceForId(String Place_Id)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", get_favorite_place_for_id);
            params.put("Place_Id", Place_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public JSONObject registerUser(String name, String phone, String email, String password)
    {
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        //JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
        */
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", register_tag);
            params.put("Name", name);
            params.put("Phone", phone);
            params.put("Email", email);
            params.put("Password", password);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject registerUserFb(String name, String phone, String email, String password)
    {
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", registerfb_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        //JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
        */
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", registerfb_tag);
            params.put("Name", name);
            params.put("Phone", phone);
            params.put("Email", email);
            params.put("Password", password);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public JSONObject registerUserFbPhone(String name, String phone, String email, String password)
    {

        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("tag", registerfbphone_tag);
            params.put("name", name);
            params.put("phone", phone);
            params.put("email", email);
            params.put("password", password);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }


    public JSONObject updateUser(String Client_Id, String name, String phone, String email, String password)
    {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", update_tag);
            params.put("Client_Id", Client_Id);
            params.put("Name", name);
            params.put("Phone", phone);
            params.put("Email", email);
            params.put("Password", password);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getUser(String Client_Id)
    {
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getuser_tag));
        params.add(new BasicNameValuePair("email", email));
        //JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;*/
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("Tag", getuser_tag);
            params.put("Client_Id", Client_Id);

            Log.d("request", "starting");
            JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

            if (json != null)
            {
                Log.d("JSON result", json.toString());
                return json;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isUserLoggedIn(Context context)
    {
        /*DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0)
        {
            return true;
        }*/
        return false;
    }

    public boolean logoutUser(Context context)
    {
        /*DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();*/
        return true;
    }

}
