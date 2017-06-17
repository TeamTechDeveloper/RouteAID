package com.example.user.routeaid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by WilliamFelipe on 29/03/2017.
 */

public class ListaAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    String[] titulos;
    String[] subtitulos;
    int[] imagenes;
    LayoutInflater inflater;

    public ListaAdapter(Context context, String[] titulos, String[] subtitulos, int[] imagenes) {
        this.context = context;
        this.titulos = titulos;
        this.subtitulos =subtitulos;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Declare Variables
        TextView txtTitle;
        TextView txtSubTitle;
        ImageView imgImg;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View itemView = inflater.inflate(R.layout.diseno_lista_rutas, parent, false);

        // Locate the TextViews in listview_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.tituloLista);
        txtSubTitle = (TextView) itemView.findViewById(R.id.subtituloLista);
        imgImg = (ImageView) itemView.findViewById(R.id.iconLista);


        // Capture position and set to the TextViews
        txtTitle.setText(titulos[position]);
        txtSubTitle.setText(subtitulos[position]);
        imgImg.setImageResource(imagenes[position]);


        return itemView;
    }
}
