package es.fdi.stickerlab;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class ReceivedStickerDialog extends AlertDialog.Builder {

    public ReceivedStickerDialog(@NonNull Context context, Bitmap bitmap) {
        super(context, R.style.RoundedCornersDialog);

        LayoutInflater inflater = LayoutInflater.from(context);

        // Titulo del dialog e icono
        this.setTitle("Nuevo Sticker");
        this.setIcon(R.drawable.ic_baseline_sticker_add_24);

        // establece el layout personalizado
        View view = inflater.inflate(R.layout.received_dialog, null);

        // Añade el sticker al layou
        ImageView sticker = view.findViewById(R.id.stickerDialogImg);
        sticker.setImageBitmap(bitmap);


        this.setView(view);

        // Acción del botón guardar
        this.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Almacenar sticker
                Toast.makeText(getContext(), "Falta por implementar función de guardar", Toast.LENGTH_LONG).show();
            }
        });
        this.setNegativeButton(android.R.string.no, null);

        this.show();
    }
}