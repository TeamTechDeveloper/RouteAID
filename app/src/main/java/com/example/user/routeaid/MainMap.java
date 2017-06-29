package com.example.user.routeaid;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainMap extends AppCompatActivity implements ManejoDB.OnPostExecute, OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;

    //timerTask
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;

    final Handler handler1 = new Handler();
    Timer timer1;
    TimerTask timerTask1;

    //BD
    String urlConsultaDeMarcadoresNinos = "http://www.talentsw.com/RoutAid/ConsultaPosicionesNinos.php";
    String[] datos = {"latitud","longitud"};
    ManejoDB consultaLatitud;
    ManejoDB consultaLongitud;

    //Ruta
    String numeroRuta = "A01SL";

    //Array con los datos de la BD
    ArrayList<String> arrayLatitud = new ArrayList<>();
    ArrayList<String> arrayLongitud = new ArrayList<>();

    //Variables de uso logico
    int queDelegate = 0;
    LatLng puntoProximo;
    double distancia = 0;
    int ubicacionArray = 0;


    //Firebase
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference posicionActualLatitud = ref.child("latitud");
    DatabaseReference posicionActualLongitud = ref.child("longitud");

    //Ubicacion GPS
    private LocationManager locationManager;
    double latitudActual;
    double longitudActual;

    //Serivios de ubicacion
    ServiciosDeUbicacion serviciosDeUbicacion = new ServiciosDeUbicacion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        String variablesPOST[] = {"idRuta"};
        String valoresPOST[] = {numeroRuta};

        //DB
        consultaLatitud = new ManejoDB("CONSULTA",datos[0],urlConsultaDeMarcadoresNinos,"POST");
        consultaLatitud.delegate = this;
        consultaLatitud.execute(variablesPOST,valoresPOST);

        consultaLongitud = new ManejoDB("CONSULTA",datos[1],urlConsultaDeMarcadoresNinos,"POST");
        consultaLongitud.delegate = this;
        consultaLongitud.execute(variablesPOST,valoresPOST);


        //GPS
        obtenerPosicionGps();

        //FireBase
        posicionActualLatitud.setValue(String.valueOf(latitudActual));
        posicionActualLongitud.setValue(String.valueOf(longitudActual));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //marcador de la posicion actual del dispositivo
        LatLng dispositivoPosicion = new LatLng(latitudActual,longitudActual);

        mMap.addMarker(new MarkerOptions()
                .position(dispositivoPosicion)
                .title("Van")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.van)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dispositivoPosicion,14));

        //indicar punto mas cercano desde la posicion
        for(int i=0;i<arrayLatitud.size();i++){
            double latitud  = Double.parseDouble(arrayLatitud.get(i));
            double longitud = Double.parseDouble(arrayLongitud.get(i));
            LatLng marcador = new LatLng(latitud,longitud);

            double distanciaDePuntos = serviciosDeUbicacion.distanciaEntreDosPuntos(dispositivoPosicion,marcador);

            if(distancia > distanciaDePuntos){
                puntoProximo = marcador;
                ubicacionArray = i;
            }
        }

        //adicion de marcadores de todos los puntos de paradas
        for (int i=0;i<arrayLongitud.size();i++){
            double latitud  = Double.parseDouble(arrayLatitud.get(i));
            double longitud = Double.parseDouble(arrayLongitud.get(i));
            LatLng marcador = new LatLng(latitud,longitud);

            if(i!=ubicacionArray){
                mMap.addMarker(new MarkerOptions()
                        .position(marcador)
                        .title("Usuario "+i)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.nino)));
            }else{
                mMap.addMarker(new MarkerOptions()
                        .position(marcador)
                        .title("Usuario "+i)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ninoarecoger)));
            }

        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void iniciarMapa() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void refreshMap(){
        mMap.clear();
    }

    public void obtenerPosicionGps(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);

        if (provider != null & !provider.equals("")) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(provider, 2000, 1, this);
            if(location!=null){
                onLocationChanged(location);
            }else{
                Toast.makeText(getApplicationContext(),"location not found", Toast.LENGTH_LONG ).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Provider is null", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    public void startTimer(){
        timer = new Timer();
        timer1 = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 5000, 10000);
        timer1.schedule(timerTask1,14900,10000);


    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //show the toast
                        //refreshMap();
                        iniciarMapa();
                        arriboMarcador();
                    }
                });
            }
        };

        timerTask1 = new TimerTask() {
            @Override
            public void run() {
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshMap();
                    }
                });
            }
        };
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitudActual  = (location.getLatitude()); // Cambio el valor de la varible por la latitud que arroja el gps
        longitudActual = (location.getLongitude()); // Cambio el valor de la varible por la longitud que arroja el gps
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void Execute() {
        switch (queDelegate){
            case 0:
                arrayLatitud = consultaLatitud.getListaFinal();
                queDelegate = 1;
                break;
            case 1:
                arrayLongitud = consultaLongitud.getListaFinal();
                queDelegate = 2;
                iniciarMapa();
                break;

        }
    }

    public void arriboMarcador(){
        LatLng dispositivoPosicion = new LatLng(latitudActual,longitudActual);
        double latitud  = Double.parseDouble(arrayLatitud.get(ubicacionArray));
        double longitud = Double.parseDouble(arrayLongitud.get(ubicacionArray));
        LatLng marcador = new LatLng(latitud,longitud);
        double distanciaPutnos = serviciosDeUbicacion.distanciaEntreDosPuntos(dispositivoPosicion,marcador);

        if(distanciaPutnos>0 && distanciaPutnos<25){
            //Aqui va lo de mandar alerta cuando se haga
            //Eliminar marcadores del recorrido
            eliminarMarcador();
        }

    }

    public void eliminarMarcador(){
        arrayLatitud.remove(ubicacionArray);
        arrayLongitud.remove(ubicacionArray);
    }
}
