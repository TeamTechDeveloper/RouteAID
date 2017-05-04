package com.example.user.routeaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 01/12/2016.
 */

public class Loginclave extends AppCompatActivity implements ManejoDB.OnPostExecute {

    Button botonlogin;
    TextView TxtRecordarContra;
    EditText EtxContrasenaa;


    String codigoLogin;
    String mensajeInsert;

    //Base de datos
    String urlRecordarContrasena = "http://www.talentsw.com/RoutAid/CrearContrasena.php";
    String urlValidarContrasena = "http://www.talentsw.com/RoutAid/validarContrasena.php";
    String urlPerfilCuenta = "http://www.talentsw.com/RoutAid/PerfilQueUsa.php";
    ManejoDB recordContrasena;
    ManejoDB validarContrasena;
    ManejoDB perfilCuenta;

    //Array con los datos de la BD
    ArrayList<String> arrayPerfil;

    //variables de uso logico
    int queDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginclave);

        TxtRecordarContra = (TextView) findViewById(R.id.recordarContrasena);
        EtxContrasenaa = (EditText) findViewById(R.id.contrasenalogin);


        Intent intent = getIntent();
        codigoLogin = intent.getStringExtra("codigoLogin");

        //////////////////ACCION BOTON PARA SIGUIENTE PAGINA ///////////////////////////

        botonlogin = (Button)findViewById(R.id.botonlogin);
        botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dato = EtxContrasenaa.getText().toString();
                if("".equals(dato)){
                    Toast.makeText(getApplicationContext(),"Ingrese una contraseña",Toast.LENGTH_LONG).show();
                }else{
                    queDelegate = 1;
                    String variablesPOST[] = {"contraIngresada","codigoIngresado"};
                    String valoresPOST[] = {dato,codigoLogin};

                    String variablesPOST2[] = {"codigoUsuario"};
                    String valoresPOST2[] = {codigoLogin};

                    validarContrasena = new ManejoDB("",urlValidarContrasena,"POST");
                    validarContrasena.delegate = Loginclave.this;

                    perfilCuenta = new ManejoDB("CONSULTA","nombreRol",urlPerfilCuenta,"POST");
                    perfilCuenta.delegate = Loginclave.this;

                    validarContrasena.execute(variablesPOST,valoresPOST);
                    perfilCuenta.execute(variablesPOST2,valoresPOST2);
                }

            }
        });

        //////////////////ACCION BOTON PARA SIGUIENTE PAGINA //////////////////////////////

        //////////////////ACCION BOTON RECORDAR CONTRASEÑA //////////////////////////////
        TxtRecordarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queDelegate = 2;
                String variablesPOST[] = {"codigoIngresado"};
                String valoresPOST[] = {codigoLogin};

                recordContrasena = new ManejoDB("",urlRecordarContrasena,"POST");
                recordContrasena.delegate = Loginclave.this;
                recordContrasena.execute(variablesPOST,valoresPOST);
            }
        });
        //////////////////ACCION BOTON RECORDAR CONTRASEÑA //////////////////////////////

    }

    @Override
    public void Execute() {
        switch (queDelegate){
            case 1:
                mensajeInsert = validarContrasena.getMensajeQuery();
                if(mensajeInsert.indexOf("true")==0){
                    //Toast.makeText(getApplicationContext(),"Exito al guardar datos",Toast.LENGTH_LONG).show();
                    queDelegate = 3;
                }else{
                    Toast.makeText(getApplicationContext(),"La contraseña ingresada no coincide con el codigo.",Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                mensajeInsert = recordContrasena.getMensajeQuery();
                if(mensajeInsert.indexOf("true")==0){
                    Toast.makeText(getApplicationContext(),"Correo enviado",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),mensajeInsert,Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                arrayPerfil = new ArrayList<>();
                arrayPerfil = perfilCuenta.getListaFinal();
                String perfil = arrayPerfil.get(0);

                switch (perfil){
                    case "Conductor":
                        Toast.makeText(getApplicationContext(),codigoLogin,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),ListaRutas.class);
                        intent.putExtra("codigoLogin",codigoLogin);
                        startActivity(intent);
                        break;
                    case "Alumno":
                        Toast.makeText(getApplicationContext(),"ALUMNO",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"MONITOR",Toast.LENGTH_LONG).show();
                        break;
                }
                //Intent botonsiguno = new Intent(Loginclave.this, MainActivity.class);
                //startActivity(botonsiguno);
                break;
        }
    }
}
