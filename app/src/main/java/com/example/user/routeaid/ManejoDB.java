package com.example.user.routeaid;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by WilliamFelipe on 30/06/2016.
 * Los parametros ingresados en la posicion cero y uno
 * para esta clase estan reservados para el 'que ejecutar' y la url que contiene el json
 */


public class ManejoDB extends AsyncTask<String[], Void, String> {

    public interface OnPostExecute{
        public void Execute();
    }

    OnPostExecute delegate;


    ArrayList<String> listaFinal = new ArrayList<>(); //Array donde van a depositarse los datos del json
    String mensajeQuery; //Tomara el valor de los mensajes que retorna el Query (elimina, agrega, modifica)
    String tipoQuery; //Me indica que tipo de query se va a manejar - (consulta) - (elimina, agrega, modifica)
    String campoJson; //Que valores del Json son lo que vas a depositarse en el array
    String queModo; //Si el query del php usa POST o no
    String direccionUrl; // Direccion donde se encuentra el archivo PHP que devuelve el JSON

    //Constructor de la clase para consultas
    public ManejoDB(String tipoQuery, String campoJson, String direccionUrl, String queModo) {
        this.tipoQuery = tipoQuery;
        this.campoJson = campoJson;
        this.direccionUrl = direccionUrl;
        this.queModo = queModo;
    }

    //Constructor de la clase para (elimina, agrega, modifica)
    public ManejoDB(String tipoQuery, String direccionUrl, String queModo) {
        this.tipoQuery = tipoQuery;
        this.direccionUrl = direccionUrl;
        this.queModo = queModo;
    }

    @Override
    protected String doInBackground(String[]... params) {

        if(queModo.equals("POST")){
            String variablesPOST[] = params[0];
            String valoresPOST[] = params[1];

            try {
                String line = null; //Guarda la linea
                URL url = new URL(getDireccionUrl());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postData = "";
                for(int i=0;i<valoresPOST.length;i++){
                    //Primero el nombre de la variable POST luego el valor de la variable POST
                    postData = postData + URLEncoder.encode(variablesPOST[i],"UTF-8")+"="+ URLEncoder.encode(valoresPOST[i],"UTF-8");
                    if((i+1) != valoresPOST.length){
                        postData = postData + "&";
                    }
                }
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();

                if(bufferedReader != null){
                    while((line = bufferedReader.readLine()) != null){
                        stringBuffer.append(line+" 'n");
                    }
                }else{
                    return null;
                }

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(queModo.equals("")){
            try {

                String line = null; //Guarda la linea
                URL url = new URL(getDireccionUrl());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();

                if(bufferedReader != null){
                    while((line = bufferedReader.readLine()) != null){
                        stringBuffer.append(line+" 'n");
                    }
                }else{
                    return null;
                }

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void arrayMaterias(String buffer){
        try {
            JSONArray jsonArray = new JSONArray(buffer);
            JSONObject jsonObject = null;
            listaFinal.clear();
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                String datosLista = jsonObject.getString(campoJson.toString());
                listaFinal.add(datosLista);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mensajeQuery(String buffer){
        setMensajeQuery(buffer);
    }

    @Override
    protected void onPostExecute(String s) {
        if(tipoQuery.equals("CONSULTA")){
            arrayMaterias(s);
        }else{
            mensajeQuery(s);
        }

        if(delegate != null){
            delegate.Execute();
        }

    }

    public ArrayList<String> getListaFinal() {
        return listaFinal;
    }

    public void setListaFinal(ArrayList<String> listaFinal) {
        this.listaFinal = listaFinal;
    }

    public String getCampoJson() {
        return campoJson;
    }

    public void setCampoJson(String campoJson) {
        this.campoJson = campoJson;
    }

    public String getQueModo() {
        return queModo;
    }

    public void setQueModo(String queModo) {
        this.queModo = queModo;
    }

    public String getDireccionUrl() {
        return direccionUrl;
    }

    public void setDireccionUrl(String direccionUrl) {
        this.direccionUrl = direccionUrl;
    }

    public String getMensajeQuery() {
        return mensajeQuery;
    }

    public void setMensajeQuery(String mensajeQuery) {
        this.mensajeQuery = mensajeQuery;
    }

    public String getTipoQuery() {
        return tipoQuery;
    }

    public void setTipoQuery(String tipoQuery) {
        this.tipoQuery = tipoQuery;
    }
}
