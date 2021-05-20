package es.fdi.stickerlab;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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

import java.io.File;
import java.util.ArrayList;

public class ChangeCategoryStickerDialog extends AlertDialog.Builder {
    View view;
    String categoria;
    private Bitmap[] stickers;
    private boolean[] stickersSelection;
    private File[] stickerList;



    public ChangeCategoryStickerDialog(@NonNull Context context,Bitmap[] stickerss, boolean[] stickersSelections,File[] stickerLists, ArrayList<String> categories) {
        super(context, R.style.RoundedCornersDialog);

        stickers = stickerss;
        stickersSelection = stickersSelections;
        stickerList = stickerLists;

        this.createView(context);
        this.buttonsActions();

        int pos = categories.indexOf("Sin categoría");
        if(pos != -1) {
            for (int i = pos; i > 0; i--)
                categories.set(i, categories.get(i - 1));
            categories.set(0, "Sin categoría");
        }
        this.spinnerActions(categories);

        this.show();
    }

    private void createView(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);

        // Titulo del dialog e icono
        this.setTitle("Mover Stickers");

        // establece el layout personalizado
        view = inflater.inflate(R.layout.changecategory_dialog, null);

        this.setView(view);
    }

    // configuración de los dos botones del dialog
    private void buttonsActions(){
        // Acción del botón mover
        this.setPositiveButton(R.string.move, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int c = 0;
                String nombre = "hola";
                for (int i = 0; i < stickersSelection.length; i++) {
                    if (stickersSelection[i]) {
                        c++;
                        SaveStickerMemory saveStickerMemory = new SaveStickerMemory();
                        EditText nameText = view.findViewById(R.id.newCategoryName);
                        //-------MeTER EL NOMBRE Y CAMBIAR BASE DE DATOS
                        saveStickerMemory.SaveImage(getContext(), stickers[i], nombre, categoria);
                        stickerList[i].delete();
                        //selectImages = selectImages + arrPath[i] + "|";
                    }
                }

                Toast.makeText(getContext(),
                        "Has movido " + c + " stickers.",
                        Toast.LENGTH_LONG).show();

            }
        });
        this.setNegativeButton(android.R.string.no, null);
    }

    private  void spinnerActions(final ArrayList<String> categories){
        Spinner spinner = view.findViewById(R.id.spinnerCategory);


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