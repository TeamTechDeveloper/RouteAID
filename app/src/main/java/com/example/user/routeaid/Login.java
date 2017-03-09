package com.example.user.routeaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 01/12/2016.
 */

public class Login extends AppCompatActivity implements ManejoDB.OnPostExecute {

    Button botonlogin;
    EditText EtxCodigoLogin;

    //base de datos
    String urlConsultaCodigoEstadoUsuario = "http://www.talentsw.com/RoutAid/consultaCodigoUsuario.php";
    String[] datos = {"codigoCuenta", "estadoCuenta", "contraasenaCuenta"};
    ManejoDB consultaCodigo;
    ManejoDB consultaEstadoCuenta;
    ManejoDB consultaContrasena;


    //Array con los datos de la BD
    ArrayList<String> arrayCodigo;
    ArrayList<String> arrayEstado;
    ArrayList<String> arrayContrasena;

    //variables de uso logico
    int queDelegate;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //cast
        EtxCodigoLogin = (EditText) findViewById(R.id.codigologin);
        botonlogin = (Button)findViewById(R.id.botonlogin);



        //////////////////ACCION BOTON PARA SIGUIENTE PAGINA ///////////////////////////

        botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //valores que  van por post
                String variablesPOST[] = {"codigoUsuario"};
                String valoresPOST[] = {EtxCodigoLogin.getText().toString()};

                queDelegate = 1;

                //consultas a la base de datos
                consultaCodigo = new ManejoDB("CONSULTA",datos[0],urlConsultaCodigoEstadoUsuario,"POST");;
                consultaCodigo.delegate = Login.this;

                consultaEstadoCuenta = new ManejoDB("CONSULTA",datos[1],urlConsultaCodigoEstadoUsuario,"POST");
                consultaEstadoCuenta.delegate = Login.this;

                consultaContrasena = new ManejoDB("CONSULTA",datos[2],urlConsultaCodigoEstadoUsuario,"POST");
                consultaContrasena.delegate = Login.this;

                consultaCodigo.execute(variablesPOST,valoresPOST);
                consultaEstadoCuenta.execute(variablesPOST,valoresPOST);
                consultaContrasena.execute(variablesPOST,valoresPOST);

            }
        });

        //////////////////ACCION BOTON PARA SIGUIENTE PAGINA //////////////////////////////

    }

    @Override
    public void Execute() {
        switch (queDelegate){
            case 1:
                arrayCodigo = new ArrayList<>();
                arrayCodigo = consultaCodigo.getListaFinal();
                if(arrayCodigo.size()!=0){
                    queDelegate = 2;
                }else{
                    Toast.makeText(getApplicationContext(),"El codigo ingresado no existe",Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                arrayEstado = new ArrayList<>();
                arrayEstado = consultaEstadoCuenta.getListaFinal();
                if("0".equals(arrayEstado.get(0))){
                    Toast.makeText(getApplicationContext(),"La cuenta no se encuentra activa",Toast.LENGTH_LONG).show();
                }else{
                    queDelegate = 3;
                }
                break;
            case 3:
                arrayContrasena = new ArrayList<>();
                arrayContrasena = consultaContrasena.getListaFinal();
                String dato = arrayContrasena.get(0);
                if(dato.length()==0){
                    Intent intent = new Intent(getApplicationContext(),Logincreaclave.class);
                    intent.putExtra("codigoLogin",EtxCodigoLogin.getText().toString());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),Loginclave.class);
                    intent.putExtra("codigoLogin",EtxCodigoLogin.getText().toString());
                    startActivity(intent);
                }
                break;
        }
    }
}
