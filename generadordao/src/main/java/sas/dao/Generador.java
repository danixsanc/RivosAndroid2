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

        Entity usuario = schema.addEntity("Client");
        usuario.addIdProperty().primaryKey().autoincrement();
        usuario.addStringProperty("Client_Id");
        usuario.addStringProperty("Name");
        usuario.addStringProperty("Email");
        usuario.addStringProperty("Phone");
        oPlantillaGenerador.generarController("Client", false);

        Entity historial = schema.addEntity("Historial");
        historial.addIdProperty().primaryKey().autoincrement();
        historial.addStringProperty("Request_Id");
        historial.addStringProperty("Inicio");
        historial.addStringProperty("Destino");
        historial.addStringProperty("Date");
        historial.addStringProperty("Cabbie");
        oPlantillaGenerador.generarController("Historial", false);

        Entity favorite_Place = schema.addEntity("Favorite_Place");
        favorite_Place.addIdProperty().primaryKey().autoincrement();
        favorite_Place.addStringProperty("PlaceFavoriteId");
        favorite_Place.addStringProperty("Name");
        favorite_Place.addStringProperty("Desc");
        favorite_Place.addStringProperty("Latitude");
        favorite_Place.addStringProperty("Longitude");
        oPlantillaGenerador.generarController("Favorite_Place", false);

        Entity favorite_Cabbie = schema.addEntity("Favorite_Cabbie");
        favorite_Cabbie.addIdProperty().primaryKey().autoincrement();
        favorite_Cabbie.addStringProperty("CabbieFavoriteId");
        favorite_Cabbie.addStringProperty("Name");
        favorite_Cabbie.addStringProperty("Company");
        oPlantillaGenerador.generarController("Favorite_Cabbie", false);


        new DaoGenerator().generateAll(schema, args[0]);
        oPlantillaGenerador.generarDB();
    }
}
