package com.example.user.routeaid;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by WilliamFelipe on 14/12/2016.
 */

public class ServiciosDeUbicacion {

    //Metodo que permite el medir la distancia entre dos puntos
    public Double distanciaEntreDosPuntos(LatLng puntoA, LatLng puntoB){
        return SphericalUtil.computeDistanceBetween(puntoA,puntoB);
    }
}
