package com.example.user.routeaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 01/12/2016.
 */

public class Logincreaclave extends AppCompatActivity {

    Button botonlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginclave);

        //////////////////ACCION BOTON PARA SIGUIENTE PAGINA ///////////////////////////

        botonlogin = (Button)findViewById(R.id.botonlogin);
        botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent botonsiguno = new Intent(Logincreaclave.this, MainActivity.class);
                startActivity(botonsiguno);
            }
        });

        //////////////////ACCION BOTON PARA SIGUIENTE PAGINA //////////////////////////////

    }
}
