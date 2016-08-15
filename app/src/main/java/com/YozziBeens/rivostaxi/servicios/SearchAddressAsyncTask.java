package com.YozziBeens.rivostaxi.servicios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;


import com.YozziBeens.rivostaxi.listener.AsyncTaskListener;
import com.YozziBeens.rivostaxi.modelo.Ciudad;
import com.YozziBeens.rivostaxi.respuesta.Direcciones;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SearchAddressAsyncTask extends AsyncTask<String, String, HashMap<String, Object>> {

    private static final String TAG = "COORDENADAS";

	private AsyncTaskListener asyncTaskListener;
    private HashMap<String, Object> results;
	private Ciudad ciudad;

    private Context context;
    private String direccion;
    private int numeroResultados;

	public SearchAddressAsyncTask(Context prContext, String prDireccion, int prNumeroDeResultados, Ciudad prCiudad){
		this.results = new HashMap<String, Object>();
		this.ciudad = prCiudad;

        context = prContext;
        this.numeroResultados = prNumeroDeResultados;
        this.direccion = prDireccion;
	}

	@Override
	protected void onPreExecute() {
		asyncTaskListener.onTaskStart();
		super.onPreExecute();
	}

	@Override
	protected HashMap<String, Object> doInBackground(String... params)
	{
		try{
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
			List<Address> list = geocoder.getFromLocationName(direccion, numeroResultados, ciudad.getLowerLeftLatitude(), ciudad.getLowerLeftLongitude(),
					ciudad.getUpperRightLatitude(), ciudad.getUpperRightLongitude());
            Direcciones oAddresses = new Direcciones();
            oAddresses.setAddresses(list);
            results.put("esValido", true);
            results.put("Resultado",oAddresses);
            onTaskDownloadedFinished(results);
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
