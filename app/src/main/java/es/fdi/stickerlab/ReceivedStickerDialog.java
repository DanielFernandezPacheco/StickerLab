package es.fdi.stickerlab;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class ReceivedStickerDialog extends AlertDialog.Builder {


    public ReceivedStickerDialog(@NonNull Context context, Bitmap bitmap) {
        super(context);
        ImageView sticker = new ImageView(context);
        sticker.setImageBitmap(bitmap);

        this.setTitle("Nuevo Sticker");
        this.setView(sticker);


        this.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Almacenar sticker
                Toast.makeText(getContext(), "Falta por implementar función de guardar", Toast.LENGTH_LONG).show();
            }
        });

        this.setNegativeButton(android.R.string.no, null);
        this.setIcon(R.drawable.ic_baseline_sticker_add_24);
        this.show();
    }
}


/*
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
        // Continue with delete operation
        }
        })

        // A null listener allows the button to dismiss the dialog and take no further action.
        .setNegativeButton(android.R.string.no, null)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
*/