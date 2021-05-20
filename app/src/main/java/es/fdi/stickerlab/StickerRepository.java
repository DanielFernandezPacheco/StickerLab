package es.fdi.stickerlab;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.io.File;
import java.util.List;

import es.fdi.stickerlab.DAO.AppDatabase;
import es.fdi.stickerlab.DAO.StickerDAO;
import es.fdi.stickerlab.Model.StickerEntity;

import static es.fdi.stickerlab.MainActivity.myStickerViewModel;

//El repositorio proporciona una API para acceder cómodamente a la base de datos
//con una capa de abstracción
public class StickerRepository {
    private static StickerDAO myStickerDAO;
    private static LiveData<List<StickerEntity>> myAllSticker;
    private static int myCount;

    StickerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);

        //Creamos el DAO de stickers
        myStickerDAO = db.stickerDAO();
        myAllSticker = myStickerDAO.getAllStickers();
    }

    //Room ejecuta todas las consultas en un hilo separado
    public static LiveData<List<StickerEntity>> getAll() {
        return myAllSticker;
    }


    //Debe llamar en un hilo que no sea de UI o sino peta
    //Sería una expresión lambda pero a veces no está soportada por determinadas versiones de Java
    void insert(final StickerEntity mySticker){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myStickerDAO.insert(mySticker);
            }
        });
    }

    void deleteByPath(final String ruta){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myStickerDAO.deleteByPath(ruta);
            }
        });
    }

    //------------------------------HACER AQUÍ QUE SE EJECUTE EN OTRO HILO------------------------//
    public static void getStickerEntityFromPath(File sticker, Bitmap bitmap, String categoria){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SaveStickerMemory saveStickerMemory = new SaveStickerMemory();
                StickerEntity mySticker = myStickerDAO.getStickerEntityFromPath(sticker.getAbsolutePath());
                //Obtenemos todos los datos del sticker a mover
                long id = mySticker.getId();
                String nombre = mySticker.getNombre();
                String NewCategoria = categoria;
                String ruta = mySticker.getRuta();

                //Eliminamos el sticker de su categoría antigua
                myStickerViewModel.deleteByPath(ruta);

                //Creamos un nuevo Sticker en la nueva categoria y lo insertamos
                StickerEntity newSticker = new StickerEntity(id,nombre,NewCategoria,ruta);
                myStickerViewModel.insert(newSticker);

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        saveStickerMemory.SaveImage(MainActivity.getAppContext(), bitmap, nombre, categoria);
                    }
                });
            }
        });

    }

    public static int count(){
        return myStickerDAO.count();
    }

}