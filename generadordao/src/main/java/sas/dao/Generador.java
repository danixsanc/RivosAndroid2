package sas.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Generador {

    public static void main(String args[]) throws Exception {

        String packageName = "com.YozziBeens.rivostaxi";
        String dbName = "Rivos";

        PlantillaGenerador oPlantillaGenerador = new PlantillaGenerador(dbName, packageName, args[0]);
        Schema schema = new Schema(1, packageName + ".modelo");


        Entity ciudad = schema.addEntity("Ciudad");
        ciudad.addLongProperty("ciudadID").primaryKey();
        ciudad.addStringProperty("nombreCiudad").notNull();
        ciudad.addDoubleProperty("lowerLeftLatitude");
        ciudad.addDoubleProperty("lowerLeftLongitude");
        ciudad.addDoubleProperty("upperRightLatitude");
        ciudad.addDoubleProperty("upperRightLongitude");
        ciudad.addDoubleProperty("latitud");
        ciudad.addDoubleProperty("longitud");
        ciudad.addIntProperty("zoom");
        oPlantillaGenerador.generarController("Ciudad", false);

        Entity lugar = schema.addEntity("Lugar");
        lugar.addLongProperty("lugarID").primaryKey();
        lugar.addStringProperty("nombre").notNull();
        lugar.addDoubleProperty("latitud").notNull();
        lugar.addDoubleProperty("longitud").notNull();
        lugar.addStringProperty("imagen").notNull();
        lugar.addStringProperty("direccion").notNull();
        oPlantillaGenerador.generarController("Lugar", false);



        Entity usuario = schema.addEntity("Client");
        usuario.addLongProperty("id").primaryKey().autoincrement();
        usuario.addStringProperty("Client_Id");
        usuario.addStringProperty("Name");
        usuario.addStringProperty("Email");
        usuario.addStringProperty("Phone");
        oPlantillaGenerador.generarController("Client", false);

        Entity imagePerfil = schema.addEntity("ImagePerfil");
        imagePerfil.addIdProperty().primaryKey().autoincrement();
        imagePerfil.addByteArrayProperty("Image");
        oPlantillaGenerador.generarController("ImagePerfil", false);

        Entity historial = schema.addEntity("Historial");
        historial.addIdProperty().primaryKey().autoincrement();
        historial.addStringProperty("Request_Id");
        historial.addStringProperty("Date");
        historial.addStringProperty("Cabbie_Name");
        historial.addStringProperty("Inicio");
        historial.addStringProperty("Destino");
        historial.addStringProperty("Ref");
        historial.addStringProperty("Cabbie");
        historial.addStringProperty("Price");
        oPlantillaGenerador.generarController("Historial", false);

        Entity historialPendiente = schema.addEntity("HistorialPendiente");
        historialPendiente.addIdProperty().primaryKey().autoincrement();
        historialPendiente.addStringProperty("Request_Id");
        historialPendiente.addStringProperty("Inicio");
        historialPendiente.addStringProperty("Destino");
        historialPendiente.addStringProperty("Date");
        historialPendiente.addStringProperty("Cabbie_Id");
        historialPendiente.addStringProperty("Cabbie_Name");
        historialPendiente.addStringProperty("Ref");
        historialPendiente.addStringProperty("Price");
        oPlantillaGenerador.generarController("HistorialPendiente", false);

        Entity favorite_Place = schema.addEntity("Favorite_Place");
        favorite_Place.addIdProperty().primaryKey().autoincrement();
        favorite_Place.addStringProperty("Place_Favorite_Id");
        favorite_Place.addStringProperty("Name");
        favorite_Place.addStringProperty("Description");
        favorite_Place.addStringProperty("Latitude");
        favorite_Place.addStringProperty("Longitude");
        oPlantillaGenerador.generarController("Favorite_Place", false);

        Entity tarjeta = schema.addEntity("Tarjeta");
        tarjeta.addIdProperty().primaryKey().autoincrement();
        tarjeta.addStringProperty("Card_Id");
        tarjeta.addStringProperty("Name");
        tarjeta.addStringProperty("Last4");
        tarjeta.addStringProperty("Exp_Month");
        tarjeta.addStringProperty("Exp_Year");
        tarjeta.addStringProperty("Brand");
        oPlantillaGenerador.generarController("Tarjeta", false);



        new DaoGenerator().generateAll(schema, args[0]);
        oPlantillaGenerador.generarDB();
    }
}
