package com.YozziBeens.rivostaxi.utilerias;

/**
 * Created by danixsanc on 24/01/2016.
 */
public class TarjetasBD {

    public String ocultarTarjeta(String tarjeta)
    {
        int cont = tarjeta.length();
        int num = cont - 4;
        String tarjetaSeg = tarjeta.substring(num, tarjeta.length());
        String tarjF;
        if (tarjeta.length() == 16)
        {
            tarjF = "**** **** **** " + tarjetaSeg;
        }
        else
        {
            tarjF = "**** **** " + tarjetaSeg;
        }

        return tarjF;
    }

}
