package es.fdi.stickerlab;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ReceivedStickerDialog extends AlertDialog.Builder {
    public static final String NO_CATEGORY = "Sin categoría";
    View view;

    public ReceivedStickerDialog(@NonNull Context context, Bitmap bitmap, ArrayList<String> categories) {
        super(context, R.style.RoundedCornersDialog);

        this.createView(context, bitmap);
        this.buttonsActions();
        this.spinnerActions(categories);

        this.show();
    }

    private void createView(Context context, Bitmap bitmap){
        LayoutInflater inflater = LayoutInflater.from(context);

        // Titulo del dialog e icono
        this.setTitle("Nuevo Sticker");
        this.setIcon(R.drawable.ic_baseline_sticker_add_24);

        // establece el layout personalizado
        view = inflater.inflate(R.layout.received_dialog, null);

        // Añade el sticker al layout
        ImageView sticker = view.findViewById(R.id.stickerDialogImg);
        sticker.setImageBitmap(bitmap);


        this.setView(view);
    }

    // configuración de los dos botones del dialog
    private void buttonsActions(){
        // Acción del botón guardar
        this.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Almacenar sticker
                Toast.makeText(getContext(), "Falta por implementar función de guardar", Toast.LENGTH_LONG).show();

            }
        });
        this.setNegativeButton(android.R.string.no, null);
    }

    private  void spinnerActions(ArrayList<String> categories){
        Spinner spinner = view.findViewById(R.id.spinnerCategory);

        categories.add(0, NO_CATEGORY);
        spinner.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //guardar categoria
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}