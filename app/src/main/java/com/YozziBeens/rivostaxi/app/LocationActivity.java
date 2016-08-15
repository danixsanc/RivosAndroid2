package com.YozziBeens.rivostaxi.app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.listener.LocationTaskListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by SavidSalazar on 30/01/15.
 */
public class LocationActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static int MY_PERMISSION_LOCATION = 1000;
    private static final String TAG = "GPS";
    private static final int MILISECONDS = 1000;

    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private AlertDialog mDialog;
    private LocationTaskListener locationTaskListener;
    private Handler mHandler;

    public int INTERVAL_LOCATION_IN_METERS = 100;
    public int INTERVAL_LOCATION_IN_SECONDS = 0;
    public int FAST_INTERVAL_LOCATION_IN_SECONDS = 5;
    public int EXPIRATION_TIME = 15000;
    public int CONNECTION_FAILURE_RESOLUTION_REQUEST = 10000;


    private Location ubicacionUsuario;
    public boolean activarTiempoDeExpiracion = false;
    public boolean activarUbicacionAlIniciar = true;
    public boolean activarMensajeDeUbicacion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isLocationEnabled() && activarUbicacionAlIniciar) {
            int permissionLocationCheck = ContextCompat.checkSelfPermission(LocationActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int permisionCoarseLocation = ContextCompat.checkSelfPermission(LocationActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                startPeriodicUpdates();
            else if (permissionLocationCheck == 0 && permisionCoarseLocation == 0)
                startPeriodicUpdates();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPeriodicUpdates();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(MILISECONDS * INTERVAL_LOCATION_IN_SECONDS);
        mLocationRequest.setFastestInterval(MILISECONDS * FAST_INTERVAL_LOCATION_IN_SECONDS);
        mLocationRequest.setSmallestDisplacement(INTERVAL_LOCATION_IN_METERS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (locationTaskListener != null) {
            locationTaskListener.onTaskStart();
            mHandler = new Handler();
            mHandler.postDelayed(mExpiredRunnable, EXPIRATION_TIME);
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private final Runnable mExpiredRunnable = new Runnable() {
        @Override
        public void run() {
            locationTaskListener.onTaskComplete(ubicacionUsuario);
        }
    };

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApiClient connection has been failed, Error Code: " + connectionResult.getErrorCode());
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        ubicacionUsuario = location;
        if (locationTaskListener != null)
            locationTaskListener.onTaskComplete(location);
        if (mHandler != null) {
            mHandler.removeCallbacks(mExpiredRunnable);
            mHandler = null;
            stopPeriodicUpdates();
            ubicacionUsuario = null;
        }
        Log.d(TAG, String.format(getString(R.string.latitude_longitude), location.getLatitude(), location.getLongitude()));
    }

    public void setLocationListener(LocationTaskListener locationTaskListener) {
        this.locationTaskListener = locationTaskListener;
    }

    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            errorDialog.show();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Su GPS se encuentra deshabilitado ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        mDialog = alert;
    }

    //METODOS PUBLICOS DE LA ACTIVIDAD

    /**
     * Verifica que la ubicación se encuentre disponible en el dispositivo
     *
     * @return true si la ubicación esta disponible en el dispositivo
     */
    public boolean isLocationEnabled() {
        int locationMode = 0;
        boolean estaActivo;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            estaActivo = locationMode != Settings.Secure.LOCATION_MODE_OFF;
            if (mDialog != null)
                mDialog.cancel();
            if (activarMensajeDeUbicacion && !estaActivo)
                buildAlertMessageNoGps();
            return estaActivo;

        } else {
            locationProviders = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            estaActivo = !TextUtils.isEmpty(locationProviders);
            if (mDialog != null)
                mDialog.cancel();
            if (activarMensajeDeUbicacion && !estaActivo)
                buildAlertMessageNoGps();
            return estaActivo;
        }
    }

    /**
     * Verifica que el dispositivo cuente con los servicios de Google, si no se encuentra instalado mostrara una ventana de dialogo
     * para descargar la aplicación desde la play store
     *
     * @return true si la aplicación de servicios de google se encuentra instalada en el dispositivo
     */
    public boolean isPlayServicesEnabled() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(TAG, getString(R.string.play_services_available));

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            dialog.show();
            return false;
        }
    }

    /**
     * Inicia el periodo de actualizacion para la busqueda de ubicaciones
     */
    public void startPeriodicUpdates() {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }


    private void marshmallowGPSPremissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && this.checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        } else {
            startPeriodicUpdates();
        }
    }

    /**
     * Detiene el periodo de actualizacion para la busqueda de ubicaciones
     */
    public void stopPeriodicUpdates() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mExpiredRunnable);
            mHandler = null;
            locationTaskListener.onTaskCancelled();
            if (locationTaskListener != null)
                locationTaskListener = null;
            Log.d(TAG, "La consulta de ubicación fue cancelada");
        }
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }

    /**
     * Obtiene la ultima ubicación obtenida por el dispositivo
     *
     * @return La ubicacion del dispositivo, en caso de no existir retorna NULL
     */
    public Location getLastLocation() {
        if (isPlayServicesEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        else
            return null;
    }


    public void consultarUbicacion() {
        this.setLocationListener(new LocationTaskListener() {
            @Override
            public void onTaskStart() {
                progressDialog = new ProgressDialog(LocationActivity.this);
                progressDialog.setMessage(getResources().getString(R.string.str_consultando_ubicacion));
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressDialog.dismiss();
                        stopPeriodicUpdates();
                    }
                });
            }

            @Override
            public void onTaskComplete(Location result) {
                progressDialog.dismiss();
                if (result != null)
                    setLocationListener(null);
            }

            @Override
            public void onTaskCancelled() {
                progressDialog.dismiss();
                stopPeriodicUpdates();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSION_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startPeriodicUpdates();
        }else
            Toast.makeText(this, getString(R.string.str_permiso_no_autorizado), Toast.LENGTH_SHORT).show();
    }

}
