package es.fdi.stickerlab.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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


    //Obtener todos los stickers de una categoria
    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.COLUMN_CATEGORIA + " = :categoria")
    List<Sticker> getStickerByCategory(String categoria);


    //Obtener un sticker por nombre
    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.COLUMN_NOMBRE + " = :nombre")
    List<Sticker> getStickerByName(String nombre);


    // Permitimos la inserción múltiple evitando conflicto
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Sticker sticker);


    /*
    //Insertar un nuevo sticker
    @Insert
    void insertAll(Sticker ... stickers);
    */

    //Eliminar un sticker
    @Query("DELETE FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.COLUMN_ID + " = :id")
    int deleteStickerById(long id);


    //Eliminar todos los Stickers
    @Query("DELETE FROM " + Sticker.TABLE_NAME)
    void deleteAll();

    //Actualizar -> En verdad con el Live data no hace falta
    @Update
    int updateStickers(Sticker obj);
}
