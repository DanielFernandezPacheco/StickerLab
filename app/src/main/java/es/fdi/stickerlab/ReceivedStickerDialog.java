package es.fdi.stickerlab;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import es.fdi.stickerlab.Model.StickerEntity;

import static androidx.core.content.ContextCompat.startActivity;
import static es.fdi.stickerlab.MainActivity.myStickerViewModel;

public class ReceivedStickerDialog extends AlertDialog.Builder {
    public static final String NO_CATEGORY = "Sin categoría";
    View view;
    Bitmap imagen;
    String nombre, categoria;

    public ReceivedStickerDialog(@NonNull Context context, Bitmap bitmap, ArrayList<String> categories) {
        super(context, R.style.RoundedCornersDialog);
        imagen = bitmap;
        this.createView(context, imagen);
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
                SaveStickerMemory saveStickerMemory = new SaveStickerMemory();
                EditText nameText = view.findViewById(R.id.newCategoryName);
                nombre = nameText.getText().toString().equals("")? "noNamedSticker" : nameText.getText().toString();
                saveStickerMemory.SaveImage(getContext(), imagen, nombre, categoria);

            }
        });
        this.setNegativeButton(android.R.string.no, null);
    }

    private  void spinnerActions(final ArrayList<String> categories){
        Spinner spinner = view.findViewById(R.id.spinnerCategory);

        categories.add(0, NO_CATEGORY);
        spinner.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}