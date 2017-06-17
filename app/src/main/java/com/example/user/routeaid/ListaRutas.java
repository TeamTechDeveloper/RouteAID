package com.example.user.routeaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaRutas extends AppCompatActivity implements ManejoDB.OnPostExecute{

    ListaAdapter adapter;
    ListView lista;

    String urlListarutas = "http://www.talentsw.com/RoutAid/ListaRutas.php";
    String[] datos = {"idRuta","nombreRutas","nombreDestino"};
    ManejoDB consultaRutas;
    ManejoDB consultaDestino;

    //Array con los datos de la BD
    ArrayList<String> ArrayRutas;
    ArrayList<String> ArrayDestinos;

    //Variables logicas
    int queDelegate;
    String codigoLogin;

    int[] imagenes = {
            R.drawable.bus,
            R.drawable.logo
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rutas);

        lista = (ListView) findViewById(R.id.ContenlistView);

        Intent intent = getIntent();
        codigoLogin = intent.getStringExtra("codigoLogin");
        //Toast.makeText(getApplicationContext(),codigoLogin, Toast.LENGTH_SHORT).show();
        //codigoLogin = "jklmn";

        //Toast.makeText(getApplicationContext(),codigoLogin, Toast.LENGTH_SHORT).show();

        //valores que  van por post
        String variablesPOST[] = {"codigoUsuario"};
        String valoresPOST[] = {codigoLogin};

        //indicador
        queDelegate = 1;

        //consultas a la base de datos
        consultaRutas = new ManejoDB("CONSULTA",datos[1],urlListarutas,"POST");
        consultaRutas.delegate = ListaRutas.this;

        consultaDestino = new ManejoDB("CONSULTA",datos[2],urlListarutas,"POST");
        consultaDestino.delegate = ListaRutas.this;

        consultaRutas.execute(variablesPOST,valoresPOST);
        consultaDestino.execute(variablesPOST,valoresPOST);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "click sobre " + i, Toast.LENGTH_SHORT).show();
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "click Largo " + i, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    @Override
    public void Execute() {
        switch (queDelegate){
            case 1:
                ArrayRutas = consultaRutas.getListaFinal();
                queDelegate = 2;
                break;
            case 2:
                ArrayDestinos = consultaDestino.getListaFinal();
                String[] titulos = ArrayRutas.toArray(new String[0]);
                String[] subtitulos = ArrayDestinos.toArray(new String[0]);

                adapter = new ListaAdapter(this, titulos, subtitulos, imagenes);
                lista.setAdapter(adapter);
                break;
        }
    }
}
