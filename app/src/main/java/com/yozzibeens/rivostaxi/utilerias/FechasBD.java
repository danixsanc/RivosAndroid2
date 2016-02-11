package com.YozziBeens.rivostaxi.utilerias;

/**
 * Created by danixsanc on 24/01/2016.
 */
public class FechasBD {

    public String ObtenerFecha(String date)
    {

        String[] date2 = date.split(" ");
        String fecha1 = date2[0];


        String[] fechaSplit = fecha1.split("-");
        String año = fechaSplit[0];
        String mes = fechaSplit[1];
        String dia = fechaSplit[2];

        if (Integer.valueOf(mes) == 1){
            return dia + " de Enero del "+año;
        }
        if (Integer.valueOf(mes) == 2){
            return dia + " de Febrero del "+año;
        }
        if (Integer.valueOf(mes) == 3){
            return dia + " de Marzo del "+año;
        }
        if (Integer.valueOf(mes) == 4){
            return dia + " de Abril del "+año;
        }
        if (Integer.valueOf(mes) == 5){
            return dia + " de Mayo del "+año;
        }
        if (Integer.valueOf(mes) == 6){
            return dia + " de Junio del "+año;
        }
        if (Integer.valueOf(mes) == 7){
            return dia + " de Julio del "+año;
        }
        if (Integer.valueOf(mes) == 8){
            return dia + " de Agosto del "+año;
        }
        if (Integer.valueOf(mes) == 9){
            return dia + " de Septiembre del "+año;
        }
        if (Integer.valueOf(mes) == 10){
            return dia + " de Octubre del "+año;
        }
        if (Integer.valueOf(mes) == 11){
            return dia + " de Noviembre del "+año;
        }
        if (Integer.valueOf(mes) == 12){
            return dia + " de Diciemre del "+año;
        }

        return "";
    }

    public String ObtenerHora(String date)
    {

        String[] date2 = date.split(" ");
        String hora2 = date2[1];


        String[] horaSplit = hora2.split(":");
        String hora = horaSplit[0];
        String minuto = horaSplit[1];
        String segundo = horaSplit[2];

        return hora + ":" + minuto + " Hrs.";
    }
}
