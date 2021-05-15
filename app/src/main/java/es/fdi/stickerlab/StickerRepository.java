package es.fdi.stickerlab;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.fdi.stickerlab.DAO.AppDatabase;
import es.fdi.stickerlab.DAO.StickerDAO;
import es.fdi.stickerlab.Model.Sticker;

//El repositorio proporciona una API para acceder cómodamente a la base de datos
//con una capa de abstracción
public class StickerRepository {

    private static StickerDAO myStickerDAO;
    private static LiveData<List<Sticker>> myAllSticker;

    StickerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);

        //Creamos el DAO de stickers
        myStickerDAO = db.stickerDAO();
        myAllSticker = myStickerDAO.getAllStickers();
    }

        //Room ejecuta todas las consultas en un hilo separado
        public static LiveData<List<Sticker>> getAll() {
            return myAllSticker;
        }


        //Debe llamar en un hilo que no sea de UI o sino peta
        //Sería una expresión lambda pero a veces no está soportada por determinadas versiones de Java
        void insert(final Sticker mySticker){
            AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    myStickerDAO.insert(mySticker);
                }
            });
        }

}
