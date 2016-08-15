package com.YozziBeens.rivostaxi.actividades.Perfil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.YozziBeens.rivostaxi.R;
import com.YozziBeens.rivostaxi.controlador.ClientController;
import com.YozziBeens.rivostaxi.controlador.ImagePerfilController;
import com.YozziBeens.rivostaxi.modelo.Client;
import com.YozziBeens.rivostaxi.modelo.ImagePerfil;
import com.YozziBeens.rivostaxi.utilerias.Fotografia;
import com.YozziBeens.rivostaxi.utilerias.Preferencias;
import com.YozziBeens.rivostaxi.utilerias.Servicio;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by danixsanc on 12/01/2016.
 */
public class Nav_Perfil extends AppCompatActivity {

    TextView txt_phone_user;
    TextView txt_email_user;
    TextView txt_nombre_user;
    TextView txt_datos_personales, txt_nombre, txt_email, txt_phone; //********

    Fotografia oFotofrafia;
    ImageView imgPerfil;

    ImageButton btn_modifydata;
    ImagePerfilController imagePerfilController = new ImagePerfilController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_perfil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface RobotoCondensed_Regular = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        imgPerfil = (ImageView) findViewById(R.id.imgPerfil);
        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oFotofrafia.tomarFotografiaDesdeGaleria();
            }
        });

        this.oFotofrafia = new Fotografia(this,"Rivos");

        txt_nombre = (TextView) findViewById(R.id.txt_nombre);
        txt_nombre.setTypeface(RobotoCondensed_Regular);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_email.setTypeface(RobotoCondensed_Regular);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_phone.setTypeface(RobotoCondensed_Regular);
        txt_phone_user = (TextView) findViewById(R.id.txt_phone_user);
        txt_phone_user.setTypeface(RobotoCondensed_Regular);
        txt_email_user = (TextView) findViewById(R.id.txt_email_user);
        txt_email_user.setTypeface(RobotoCondensed_Regular);
        txt_nombre_user = (TextView) findViewById(R.id.txt_nombre_user);
        txt_nombre_user.setTypeface(RobotoCondensed_Regular);

        final Preferencias preferencias = new Preferencias(getApplicationContext());
        String ClientId = preferencias.getClient_Id();
        ClientController clientController =  new ClientController(getApplicationContext());
        Client client = clientController.obtenerClientPorClientId(ClientId);
        String Nombre = client.getName();
        final String Correo = client.getEmail();
        String Telefono = client.getPhone();

        txt_email_user.setText(Correo);
        txt_nombre_user.setText(Nombre);
        txt_phone_user.setText(Telefono);

        cargarImagen();

        btn_modifydata = (ImageButton) findViewById(R.id.modify_data);
        btn_modifydata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent i = new Intent(Nav_Perfil.this , DataInActivity.class);
                startActivity(i);
                finish();


            }
        });

    }

    private void cargarImagen()
    {
        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
        List<ImagePerfil> listImage = imagePerfilController.obtenerImagePerfil();
        if (listImage.size() > 0)
        {
            byte[] bm2 = listImage.get(0).getImage();
            Bitmap bitfinal = dbBitmapUtility.getImage(bm2);
            imgPerfil.setImageBitmap(bitfinal);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 24:

                    String strImagenDeGaleria = null;
                    if(requestCode == oFotofrafia.REQUEST_IMAGE_GALERY && resultCode == RESULT_OK )
                    {

                        imagePerfilController.eliminarTodo();
                        strImagenDeGaleria = oFotofrafia.ubicacionImagenDesdeGaleria(data);
                        Bitmap bm = oFotofrafia.obtenerBitmap(strImagenDeGaleria, 120, 120);

                        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
                        byte[] img = dbBitmapUtility.getBytes(bm);

                        ImagePerfil imagePerfil = new ImagePerfil(null, img);
                        imagePerfilController.guardarImagePerfil(imagePerfil);

                        cargarImagen();
                    }


                break;
        }
    }

    public class DbBitmapUtility {

        public byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }

        public Bitmap getImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
