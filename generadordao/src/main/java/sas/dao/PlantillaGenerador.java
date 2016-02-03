package sas.dao;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by macbookpro on 15/04/15.
 */
public class PlantillaGenerador {

    private String dbName;
    private String packageName;
    private String fileDir;

    public PlantillaGenerador(String prDBName, String prPackageName, String prFileDir){
        dbName = prDBName;
        packageName = prPackageName;
        fileDir = prFileDir;
    }

    public void generarDB(){
        try {
            File contenido = new File("src/main/resources/PlantillaDB.txt");
            String aux = new Scanner(contenido).useDelimiter( "\\Z" ).next();
            aux = aux.replace("nombrePaquete",packageName + ".modelo");
            aux = aux.replace("Plantilla",dbName);
            File outDirFile = new File(fileDir + (packageName+".modelo").replace(".","/") + "/" + dbName +"DB.java");
            System.out.print(outDirFile.getAbsoluteFile());
            if(!outDirFile.exists())
                outDirFile.createNewFile();
            FileUtils.writeStringToFile(outDirFile, aux);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generarController(String prPlantillaName, boolean volverACrear){
        try {
            File contenido = new File("src/main/resources/PlantillaController.txt");
            String aux = new Scanner(contenido).useDelimiter( "\\Z" ).next();
            aux = aux.replace("nombrePaqueteControlador", packageName + ".controlador");
            aux = aux.replace("nombrePaquete",packageName + ".modelo");
            aux = aux.replace("PlantillaDB",dbName+"DB");
            aux = aux.replace("Plantilla",prPlantillaName);
            File outDirFile = new File(fileDir + (packageName + ".controlador").replace(".","/") + "/" + prPlantillaName +"Controller.java");
            System.out.print(outDirFile.getAbsoluteFile());
            if(!outDirFile.exists()) {
                outDirFile.createNewFile();
                FileUtils.writeStringToFile(outDirFile, aux);
            }
            else if(volverACrear)
                FileUtils.writeStringToFile(outDirFile, aux);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
