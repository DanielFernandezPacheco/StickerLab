package es.fdi.stickerlab;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.util.ArrayList;

import es.fdi.stickerlab.Model.StickerEntity;

import static es.fdi.stickerlab.MainActivity.myStickerViewModel;

public class ChangeCategoryStickerDialog extends AlertDialog.Builder {
    View view;
    String categoria;
    private Bitmap[] stickers;
    private boolean[] stickersSelection;
    private File[] stickerList;



    public ChangeCategoryStickerDialog(@NonNull Context context, Bitmap[] stickerss, boolean[] stickersSelections, File[] stickerLists, ArrayList<String> categories) {
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
        this.setIcon(R.drawable.ic_baseline_compare_arrows_24);
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
                for (int i = 0; i < stickersSelection.length; i++) {
                    if (stickersSelection[i]) {
                        c++;
                        SaveStickerMemory saveStickerMemory = new SaveStickerMemory();
                        //-------MeTER EL NOMBRE Y CAMBIAR BASE DE DATOS


                        myStickerViewModel.getStickerEntityFromPath(stickerList[i], stickers[i], categoria);

                        stickerList[i].delete();
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