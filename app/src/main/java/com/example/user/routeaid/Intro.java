package com.example.user.routeaid;

/**
 * Created by user on 01/12/2016.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;



public class Intro extends AppCompatActivity {

    private CircularFillableLoaders circularFillableLoaders;
    int progressStatusCounter = 0;
    TextView textView;
    ProgressBar androidProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        final Handler progressHandler = new Handler();


        circularFillableLoaders = (CircularFillableLoaders) findViewById(R.id.circularFillableLoaders);
        textView = (TextView) findViewById(R.id.textView);

        androidProgressBar = (ProgressBar) findViewById(R.id.horizontal_progress_bar);

        textView.setText(progressStatusCounter + "/" + androidProgressBar.getMax());

        circularFillableLoaders.setProgress(10);

        // PROGRESS
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                circularFillableLoaders.setProgress(55);

                new Thread(new Runnable() {
                    public void run() {
                        ///////
                        while (progressStatusCounter < 53) {
                            progressStatusCounter += 2;
                            progressHandler.post(new Runnable() {
                                public void run() {
                                    androidProgressBar.setProgress(progressStatusCounter);
                                    //Status update in textview
                                    textView.setText(progressStatusCounter + "/" + androidProgressBar.getMax());
                                }
                            });
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }, 3000);


        boolean conexion = conectividadComprobar();

        if(conexion){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    circularFillableLoaders.setProgress(100);

                    new Thread(new Runnable() {
                        public void run() {
                            while (progressStatusCounter < 100) {
                                progressStatusCounter += 2;
                                progressHandler.post(new Runnable() {
                                    public void run() {
                                        androidProgressBar.setProgress(progressStatusCounter);
                                        //Status update in textview
                                        textView.setText(progressStatusCounter + "/" + androidProgressBar.getMax());

                                      Intent Login = new Intent(getApplicationContext(), Login.class);
                                       startActivity(Login);
                                    }
                                });
                                try {
                                    Thread.sleep(30);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }, 9000);
        }else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Intro.this);
                    builder.setTitle("Error de conexión");
                    builder.setMessage("No hay conexión con el servidor. Por favor comprueba tu conexión a internet y vuelve a intentar.");
                    builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent Intro = new Intent(getApplicationContext(), Intro.class);
                            startActivity(Intro);

                        }
                    });
                    builder.create();
                    builder.show();
                }
            }, 5000);
        }

        // BORDER
        circularFillableLoaders.setBorderWidth(15 * getResources().getDisplayMetrics().density);

        // AMPLITUDE
        circularFillableLoaders.setAmplitudeRatio((float) 50 / 1000);

        /* COLOR
                String color = "#3f51b5";
                circularFillableLoaders.setColor(Integer.parseInt(color));

*/

    }

    public boolean conectividadComprobar(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            //Toast.makeText(getApplicationContext(),"Conexion exitosa",Toast.LENGTH_SHORT).show();
            return true;
        }else{
            //Toast.makeText(getApplicationContext(),"Conexion no exitosa",Toast.LENGTH_SHORT).show();
            return false;
        }
    }



}