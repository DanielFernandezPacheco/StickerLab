package es.fdi.stickerlab;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.fdi.stickerlab.DAO.AppDatabase;
import es.fdi.stickerlab.DAO.StickerDAO;
import es.fdi.stickerlab.Model.Sticker;


public class StickerRepository {

    private StickerDAO myStickerDAO;
    private LiveData<List<Sticker>> myAllSticker;

    StickerRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);

        //Creamos el DAO de stickers
        myStickerDAO = db.stickerDAO();
        myAllSticker = myStickerDAO.getAllStickers();

        /*
        //Room ejecuta todas las consultas en un hilo separado
        public static LiveData<List<Sticker>> getAllStickers() {
            return myAllSticker;
        }*/

        /*
        void insert(Sticker mySticker){
            AppDatabase.databaseWriteExecutor.execute(() -> myStickerDAO.insert(mySticker));
        }*/
    }
}
