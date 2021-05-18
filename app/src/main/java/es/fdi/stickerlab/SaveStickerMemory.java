package es.fdi.stickerlab;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.fdi.stickerlab.Model.StickerEntity;

import static es.fdi.stickerlab.MainActivity.myStickerViewModel;

public class SaveStickerMemory {

    private Context TheThis;
    private String NameOfFolder = "/stickers/";
    private String NameOfFile = "imagen";

    public void SaveImage(Context context, Bitmap ImageToSave, String nombre, String categoria) {

        TheThis = context;
        String file_path = context.getExternalFilesDir(null).getAbsolutePath() + NameOfFolder + categoria;
        String CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, nombre + '_' +CurrentDateAndTime + ".webp");

        try {
            FileOutputStream fOut = new FileOutputStream(file);

            ImageToSave.compress(Bitmap.CompressFormat.WEBP, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();

            //-------------------------GUARDAMOS EN LA BASE DE DATOS--------------------------//

            //Creamos un id único para cada sticker, partiendo de la fecha+hora (siempre única)

            //Pasamos el String por una serie de Splits y Uniones para que al salir solo queden los números
            String [] fecha_1 = CurrentDateAndTime.split("_");
            String unir_fecha_1 = fecha_1[0] + fecha_1[1];
            String [] fecha_2 = unir_fecha_1.split("-");
            String unir_fecha_2 = fecha_2[0]+fecha_2[1]+fecha_2[2]+fecha_2[3]+fecha_2[4];
            String [] fecha_3 = unir_fecha_2.split("\u00AD");
            String unir_fecha_3 = fecha_3[0]+fecha_3[1];

            //Convertimos a long el id
            long id_converted = Long.parseLong(unir_fecha_3);

            StickerEntity sticker = new StickerEntity(id_converted,nombre, categoria, file_path + "/" + nombre + ".webp");
            myStickerViewModel.insert(sticker);

            Toast.makeText(TheThis, sticker.getNombre()+" guardado correctamente en "+sticker.getCategoria(), Toast.LENGTH_SHORT).show();

            //--------------------------------------------------------------------------------//
        }

        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }

    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido guardar el sticker!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Sticker guardado correctamente", Toast.LENGTH_SHORT).show();
    }
}