package com.example.mymaps;

import android.os.Bundle;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * An Activity that uses an Intent to map a location from an address
 * given by the user.
 */
public class ActividadPrincipal
        extends ActividadLoggingCicloDeVida {
    /**
     * TAG usado por el depurador de Android
     */
    private String TAG = getClass().getSimpleName();

    /**
     * Matiene una referencia al EditText donde el usuario agrega una dirección.
     */
    private EditText txtDireccion;

    /**
     * Mantiene el estado de visibilidad del EditText txtDirección.
     */
    private boolean isTxtDireccionVisible;

    /**
     * Mantiene una referencia hacia el botón que es usado como un botón de acción flotante
     */
    private ImageButton btnMostrarEnMapa;

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * class scope variable initialization.
     *
     * @param savedInstanceState that contains saved state information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);

        // Set the default layout.
        setContentView(R.layout.main);

        btnMostrarEnMapa = (ImageButton) findViewById(R.id.btn_add);
        txtDireccion = (EditText) findViewById(R.id.location);

        // Hacer el txtDirección es invisible
        txtDireccion.setVisibility(View.INVISIBLE);
        isTxtDireccionVisible = false;
    }

    /**
     * Callback llamado por el Framework de Android después de que el usuario agrega una dirección.
     *
     * @param view The view.
     */
    public void mapAddress(View view) {
        // Usado para mostrar u ocultar el txtDirección según se requiera.
        Animatable mAnimatable;

        if (isTxtDireccionVisible) {
            // Si está visible, entonces lo ocultamos y guardamos el estado de visibilidad.
            UiUtils.hideEditText(txtDireccion);
            isTxtDireccionVisible = false;

            // Set Image Resource to start the morph animation of the
            // FAB icon from the tick mark to +.
            btnMostrarEnMapa.setImageResource(R.drawable.icon_morph_reverse);
            mAnimatable = (Animatable) (btnMostrarEnMapa).getDrawable();
            mAnimatable.start();

            // Start the appropriate map Activity.
            iniciarMapa();
        } else {
            // If EditText is invisible then reveal it using the
            // animation and set boolean to true.
            UiUtils.revealEditText(txtDireccion);
            isTxtDireccionVisible = true;

            txtDireccion.requestFocus();

            // Set Image Resource to start the morph animation of the
            // FAB icon from + to the tick mark.
            btnMostrarEnMapa.setImageResource(R.drawable.icon_morph);
            mAnimatable = (Animatable) (btnMostrarEnMapa).getDrawable();
            mAnimatable.start();
        }
    }

    /**
     * Iniciar la actividad apropiada para mostrar el mapa
     */
    private void iniciarMapa() {
        try {
            // Obtener la dirección ingresada por el usuario
            String address = txtDireccion.getText().toString();

            // Lanzar la actividad enviando un intent. El framework d Android elegirá el más apropiado
            // o permitira que el usuario lo seleccione.

            // Crea un intent para lanzar una aplicación de Mapas
            final Intent geoIntent = generarIntentMapas(address);

            // Verificar si existe una aplicación para manejar el intent "geo".
            if (geoIntent.resolveActivity(getPackageManager()) != null)
                // Iniciar la app de mapas
                startActivity(geoIntent);
            else
                // Iniciar la app de navegación (browser)
                startActivity(generarIntentBrowser(address));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que retorna un intent para disparar una app de mapas.
     */
    private Intent generarIntentMapas(String address) {
        //¿Intent explícito o implícito?
        return new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(address)));
    }

    /**
     * Método que retorna un intent para disparar una app de navegación (Browser)
     */
    private Intent generarIntentBrowser(String address) {
        // ¿Intent implícito o explícito?

        // Crear el intent
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q="
                        + Uri.encode(address)));

        // Abrir la actividad como una nueva tarea ¿Qué campo de un intent se emplea para definir esta restricción?
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }
}
