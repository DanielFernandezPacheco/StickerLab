package es.fdi.stickerlab;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.fdi.stickerlab.Model.Sticker;

public class StickerViewModel extends AndroidViewModel {

    //Referencia al repositorio
    private StickerRepository myRepository;

    //Devuelve la lista de palabras almacenada en caché
    private final LiveData<List<Sticker>> myAllSticker;

    //Constructor que inicializa con LiveData myAllSticker usando el repositorio
    public StickerViewModel (Application application){
        super(application);
        myRepository = new StickerRepository(application);
        myAllSticker = myRepository.getAll();
    }

    LiveData<List<Sticker>> getAll() { return myAllSticker; }

    //Método insert que llama al insert() del repositorio y se encapsula así el insert desde la UI
    public void insert(Sticker sticker){ myRepository.insert(sticker);}
}
