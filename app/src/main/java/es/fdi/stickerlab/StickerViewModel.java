package es.fdi.stickerlab;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.File;
import java.util.List;

import es.fdi.stickerlab.Model.StickerEntity;

public class StickerViewModel extends AndroidViewModel {

    //Referencia al repositorio
    private StickerRepository myRepository;

    //Devuelve la lista de palabras almacenada en caché
    private final LiveData<List<StickerEntity>> myAllSticker;

    //Constructor que inicializa con LiveData myAllSticker usando el repositorio
    public StickerViewModel (Application application){
        super(application);
        myRepository = new StickerRepository(application);
        myAllSticker = myRepository.getAll();
    }

    LiveData<List<StickerEntity>> getAll() { return myAllSticker; }

    //Método insert que llama al insert() del repositorio y se encapsula así el insert desde la UI
    public void insert(StickerEntity sticker){ myRepository.insert(sticker);}

    public void deleteByPath(String ruta){ myRepository.deleteByPath(ruta);}

    public int count() {
        return myRepository.count();
    }

    public void getStickerEntityFromPath(File file, Bitmap sticker, String categoria){ myRepository.getStickerEntityFromPath(file, sticker, categoria);}
}