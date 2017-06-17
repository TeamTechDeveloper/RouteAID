package com.example.user.routeaid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    ListaAdapter adapter;

    String[] titulo = new String[]{
            "Home",
            "Fotos",
            "Contacto",
    };

    String[] subtitulo = new String[]{
            "Santa Librada",
            "Ucc",
            "Camacho"
    };

    int[] imagenes = {
            R.drawable.bus,
            R.drawable.login,
            R.drawable.descarga
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lista = (ListView) findViewById(R.id.ContenlistView);
        adapter = new ListaAdapter(this, titulo, subtitulo, imagenes);
        lista.setAdapter(adapter);

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


}
