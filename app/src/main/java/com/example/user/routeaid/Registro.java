package com.example.user.routeaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by user on 09/12/2016.
 */

public class Registro extends AppCompatActivity{

    private TextInputLayout tilNombre;
    private TextInputLayout tilTelefono;
    private TextInputLayout tilCorreo;
    private TextInputLayout tilNombreseg;
    private TextInputLayout tilApellido;
    private TextInputLayout tilApellidoseg;
    private TextInputLayout tilDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registro de Estudiante");

        // Referencias TILs
        tilNombre = (TextInputLayout) findViewById(R.id.til_nombre);
        tilTelefono = (TextInputLayout) findViewById(R.id.til_telefono);
        tilCorreo = (TextInputLayout) findViewById(R.id.til_correo);
        tilNombreseg = (TextInputLayout) findViewById(R.id.til_nombreseg);
        tilApellido = (TextInputLayout) findViewById(R.id.til_apellidopri);
        tilApellidoseg = (TextInputLayout) findViewById(R.id.til_apellidoseg);
        tilDireccion = (TextInputLayout) findViewById(R.id.til_direccion);

        // Referencia Botón
        Button botonAceptar = (Button) findViewById(R.id.boton_aceptar);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });


    }

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
            tilNombre.setError("Primer nombre inválido");
            return false;
        } else {
            tilNombre.setError(null);
        }

        return true;
    }

    private boolean esNombreSegValido(String nombreseg) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nombreseg).matches() || nombreseg.length() > 30) {
            tilNombreseg.setError("Segundo nombre inválido");
            return false;
        } else {
            tilNombreseg.setError(null);
        }

        return true;
    }

    private boolean esApellidoValido(String apellido) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(apellido).matches() || apellido.length() > 30) {
            tilApellido.setError("Primer apellido inválido");
            return false;
        } else {
            tilApellido.setError(null);
        }

        return true;
    }

    private boolean esApellidoSegValido(String apellidoseg) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(apellidoseg).matches() || apellidoseg.length() > 30) {
            tilApellidoseg.setError("Segundo apellido inválido");
            return false;
        } else {
            tilApellidoseg.setError(null);
        }

        return true;
    }

    private boolean esDireccionValido(String direccion) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(direccion).matches() || direccion.length() > 30) {
            tilDireccion.setError("Direccion inválida");
            return false;
        } else {
            tilDireccion.setError(null);
        }

        return true;
    }

    private boolean esTelefonoValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches()) {
            tilTelefono.setError("Teléfono inválido");
            return false;
        } else {
            tilTelefono.setError(null);
        }

        return true;
    }

    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.setError("Correo electrónico inválido");
            return false;
        } else {
            tilCorreo.setError(null);
        }

        return true;
    }

    private void validarDatos() {
        String nombre = tilNombre.getEditText().getText().toString();
        String telefono = tilTelefono.getEditText().getText().toString();
        String correo = tilCorreo.getEditText().getText().toString();
        String nombreseg = tilNombreseg.getEditText().getText().toString();
        String apellido = tilApellido.getEditText().getText().toString();
        String apellidoseg = tilApellidoseg.getEditText().getText().toString();
        String direccion = tilDireccion.getEditText().getText().toString();

        boolean a = esNombreValido(nombre);
        boolean b = esTelefonoValido(telefono);
        boolean c = esCorreoValido(correo);
        boolean d = esNombreSegValido(nombreseg);
        boolean e = esApellidoValido(apellido);
        boolean f = esApellidoSegValido(apellidoseg);
        boolean g = esDireccionValido(direccion);

        if (a && b && c && d && e && f && g) {
            // OK, se pasa a la siguiente acción
            Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
            Intent boton_aceptar = new Intent(Registro.this, Registroacudiente.class);
            startActivity(boton_aceptar);
            
        }

    }

}