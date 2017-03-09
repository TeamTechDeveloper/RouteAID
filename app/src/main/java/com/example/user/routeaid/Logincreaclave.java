package com.example.user.routeaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by user on 01/12/2016.
 */

public class Logincreaclave extends AppCompatActivity implements ManejoDB.OnPostExecute {

    Button botonlogin;
    EditText EtxContrasenaCodigo;
    EditText EtxCorreo;

    String codigoLogin;
    String mensajeInsert;

    //base de datos
    String urlGuardacontrasena = "http://www.talentsw.com/RoutAid/guardarContrasenaUsuario.php";
    ManejoDB guardarContrasena;

    //variables de uso logico
    int queDelegate = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logincreaclave);

        EtxContrasenaCodigo = (EditText) findViewById(R.id.codigologin);
        EtxCorreo = (EditText) findViewById(R.id.correologin);

        Intent intent = getIntent();
        codigoLogin = intent.getStringExtra("codigoLogin");

        Toast.makeText(getApplicationContext(),codigoLogin,Toast.LENGTH_LONG).show();

        //////////////////ACCION BOTON PARA SIGUIENTE PAGINA ///////////////////////////

        botonlogin = (Button)findViewById(R.id.botonlogin);
        botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dato = EtxContrasenaCodigo.getText().toString();
                String dato2 = EtxCorreo.getText().toString();
                if("".equals(dato)|| "".equals(dato2)){
                    Toast.makeText(getApplicationContext(),"Contraseña o correo no pueden estar vacios",Toast.LENGTH_LONG).show();
                }else{
                    String variablesPOST[] = {"codigoUsuario","passUsuario","correoUsuario"};
                    String valoresPOST[] = {codigoLogin,dato,dato2};

                    guardarContrasena = new ManejoDB("",urlGuardacontrasena,"POST");
                    guardarContrasena.delegate = Logincreaclave.this;
                    guardarContrasena.execute(variablesPOST,valoresPOST);
                }
            }
        });

        //////////////////ACCION BOTON PARA SIGUIENTE PAGINA //////////////////////////////

    }

    @Override
    public void Execute() {
        switch (queDelegate){
            case 1:
                mensajeInsert = guardarContrasena.getMensajeQuery();
                if(mensajeInsert.indexOf("true")==0){
                    Toast.makeText(getApplicationContext(),"Exito al guardar datos",Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent2);
                }else{
                    Toast.makeText(getApplicationContext(),"Error al guardar datos",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
