package com.YozziBeens.rivostaxi.servicios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.respuesta.Direcciones;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddresLocationAsyncTask extends AsyncTask<String, String, HashMap<String, Object>> {

    private static final String TAG = "DIRECCION";

	private AsyncTaskListener asyncTaskListener;
    private HashMap<String, Object> results;
    private Gson gson ;

    private Context context;
    private double latitud,longitud;
    private int numeroResultados;

    public AddresLocationAsyncTask(Context prContext, double prLatitud, double prLongitud, int prNumeroResultados){
        this.gson = new Gson();
        this.results = new HashMap<String, Object>();

	    this.context = prContext;
        this.latitud = prLatitud;
        this.longitud = prLongitud;
        this.numeroResultados = prNumeroResultados;

    }
	
	@Override
	protected void onPreExecute() {
		asyncTaskListener.onTaskStart();
		super.onPreExecute();
	}
	
	@Override
	protected HashMap<String, Object> doInBackground(String... params)
	{
		try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(latitud,longitud, numeroResultados);
            if (!list.isEmpty()) {
                Direcciones oAddresses = new Direcciones();
                oAddresses.setAddresses(list);
                results.put("esValido", true);
                results.put("Resultado",oAddresses);
                onTaskDownloadedFinished(results);
            }
            else {
                Log.i(TAG, "No se encontro la direccion");
                results.put("esValido", false);
            }
            return results;
        }
        catch (Exception e){
            Log.i(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
	}

    private void onTaskDownloadedFinished(HashMap<String, Object> result){
        asyncTaskListener.onTaskDownloadedFinished(result);
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress[0]);
        asyncTaskListener.onTaskUpdate(progress[0]);
    }


    @Override
	protected void onPostExecute(HashMap<String, Object> result)  {
		asyncTaskListener.onTaskComplete(result);
    }
	
	@SuppressLint("NewApi")
	@Override
	protected void onCancelled(HashMap<String, Object> result)  {
		asyncTaskListener.onTaskCancelled(null);
		super.onCancelled(result);
	}
	
	public void setOnCompleteListener(AsyncTaskListener asyncTaskListener){
		this.asyncTaskListener = asyncTaskListener;
	}
	
}
