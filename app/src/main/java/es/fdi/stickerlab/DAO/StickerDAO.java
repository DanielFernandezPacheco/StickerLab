package es.fdi.stickerlab.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.fdi.stickerlab.Model.StickerEntity;

@Dao
public interface StickerDAO {

    //Obtener cuantos stickers tenemos
    @Query("SELECT COUNT(*) FROM " + StickerEntity.TABLE_NAME)
    int count();


    //Obtener toda la tabla de stickers -> LiveData nos da la actualización
    @Query("SELECT * FROM " + StickerEntity.TABLE_NAME)
    LiveData<List<StickerEntity>> getAllStickers();


    //Obtener todos los stickers de una categoria
    @Query("SELECT * FROM " + StickerEntity.TABLE_NAME + " WHERE " + StickerEntity.COLUMN_CATEGORIA + " = :categoria")
    List<StickerEntity> getStickerByCategory(String categoria);


    //Obtener un sticker por nombre
    @Query("SELECT * FROM " + StickerEntity.TABLE_NAME + " WHERE " + StickerEntity.COLUMN_NOMBRE + " = :nombre")
    List<StickerEntity> getStickerByName(String nombre);


    // Permitimos la inserción múltiple evitando conflicto
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StickerEntity sticker);


    /*
    //Insertar un nuevo sticker
    @Insert
    void insertAll(StickerEntity ... stickers);
    */

    /*
    //Eliminar un sticker
    @Query("DELETE FROM " + StickerEntity.TABLE_NAME + " WHERE " + StickerEntity.COLUMN_ID + " = :id")
    int deleteStickerById(long id);
    */

    //Eliminar todos los Stickers
    @Query("DELETE FROM " + StickerEntity.TABLE_NAME)
    void deleteAll();

    //Actualizar -> En verdad con el Live data no hace falta
    @Update
    int updateStickers(StickerEntity obj);
}
