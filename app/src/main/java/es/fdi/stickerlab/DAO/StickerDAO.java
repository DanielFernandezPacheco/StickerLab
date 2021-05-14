package es.fdi.stickerlab.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.fdi.stickerlab.Model.Sticker;

@Dao
public interface StickerDAO {

    //Obtener cuantos stickers tenemos
    @Query("SELECT COUNT(*) FROM " + Sticker.TABLE_NAME)
    int count();


    //Obtener toda la tabla de stickers -> LiveData nos da la actualización
    @Query("SELECT * FROM " + Sticker.TABLE_NAME)
    LiveData<List<Sticker>> getAllStickers();

    //Insertar un nuevo sticker
    @Insert
    void insertAll(Sticker ... stickers);

    //Eliminar un sticker
    @Query("DELETE FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.COLUMN_ID + " = :id")
    int deleteById(long id);

    //Actualizar
    @Update
    int updateStickers(Sticker obj);

    //Otra forma de insertar
    @Insert
    long insert(Sticker stickers);


}
