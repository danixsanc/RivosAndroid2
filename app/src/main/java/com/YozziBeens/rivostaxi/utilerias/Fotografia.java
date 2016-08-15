package com.YozziBeens.rivostaxi.utilerias;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fotografia {

    private static final String TAG = "FOTOGRAFIA";
    public static final int REQUEST_IMAGE_CAPTURE = 0020;
    public static final int REQUEST_IMAGE_GALERY = 0030;

    private File file;
    private Bitmap bitmap;
    private String ubicacion;
    private Activity activity;
    private String nombreProyecto;
    private int Height = 620, Width = 480;


    public Fotografia(Activity activity, String nombreProyecto) {
        this.activity = activity;
        this.nombreProyecto = nombreProyecto;
    }

    public void setUbicacion(String _dir) {
        this.ubicacion = _dir;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void tomarFotografia() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            file = new File(file, "temp_picture.jpeg");
            ubicacion = file.getAbsolutePath();
            file = file;
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } catch (Exception e) {
            Log.v(TAG, "Can't create file to take picture!");
        }
    }

    public Bitmap mostrarImagen(ImageView imgView) {
        // Get the dimensions of the View
        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();

        if (targetH <= 0)
            targetH = Height;
        if (targetW <= 0)
            targetW = Width;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(ubicacion, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(ubicacion, bmOptions);
        bitmap = bitmap;
        imgView.setImageBitmap(bitmap);
        return bitmap;
    }

    public Bitmap mostrarFotografiaDesdeDireccion(String path, ImageView imgView) {
        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();

        if (targetH <= 0)
            targetH = Height;
        if (targetW <= 0)
            targetW = Width;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / Width, photoH / Height);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        bitmap = bitmap;
        imgView.setImageBitmap(bitmap);
        return bitmap;
    }

    public Bitmap obtenerBitmap(String path, int width, int height) {


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / width, photoH / height);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        bitmap = bitmap;
        return bitmap;
    }

    public Bitmap mostrarImagenDesdeGaleria(ImageView imgView, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = activity.getContentResolver().query(selectedImage, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        ubicacion = c.getString(columnIndex);
        c.close();
        return mostrarFotografiaDesdeDireccion(ubicacion, imgView);
    }



    public String ubicacionImagenDesdeGaleria(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = activity.getContentResolver().query(selectedImage, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        ubicacion = c.getString(columnIndex);
        c.close();
        return ubicacion;
    }


    public File guardarImagenEnMemoriaInterna() {
        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String nombreImagen = "JPEG_" + timeStamp;
            File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            storageDir = new File(storageDir + "/" + nombreImagen + ".jpg");
            file.renameTo(storageDir);
            ubicacion = storageDir.getAbsolutePath();
            return file;
        } catch (Exception error) {
            Log.e(TAG, error.getMessage());
            return null;
        }
    }

    public String guardarBitmapMemoriaInterna(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(activity);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "temp.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }

    public File guardarImagenEnMemoriaPublica() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String nombreImagen = "JPEG_" + timeStamp;
            File storageDir = Environment.getExternalStorageDirectory();
            storageDir = new File(storageDir.getAbsolutePath() + "/" + nombreProyecto);
            if (!storageDir.exists())
                storageDir.mkdir();
            if (storageDir != null) {
                File image = new File(storageDir + "/" + nombreImagen + ".jpg");
                if (image.exists()) {
                    image.delete();
                    image.createNewFile();
                } else
                    image.createNewFile();
                boolean p = file.renameTo(image);
                ubicacion = image.getAbsolutePath();
            }
            return file;
        } catch (Exception error) {
            Log.e(TAG, error.getMessage());
            return null;
        }
    }

    public static void eliminarFotografia(String path) {
        try {
            File image = new File(path);
            if (image.exists()) {
                image.delete();
            }
        } catch (Exception error) {
            Log.e(TAG, error.getMessage());
        }
    }

    public void tomarFotografiaDesdeGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }

    public void mostrarImagen(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "image/*");
        activity.startActivity(intent);
    }

    public void mostrarImagenAsyncTask(ImageView imageView, String path) {
        LoadImage loadImage = new LoadImage(imageView, path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            loadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            loadImage.execute();
        }
    }

    class LoadImage extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imv;
        private String path;

        public LoadImage(ImageView imv, String path) {
            this.imv = imv;
            this.path = path;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = obtenerBitmap(path, Width, Height);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null && imv != null) {
                imv.setImageBitmap(result);
            } else {
                //imv.setImageBitmap();
            }
        }
    }

}