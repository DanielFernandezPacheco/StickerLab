package es.fdi.stickerlab.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.fdi.stickerlab.Model.Category;

@Dao
public interface CategoryDAO {
    @Query("SELECT * FROM categories")
    List<Category> getAll();


    @Query("SELECT * FROM categories WHERE category_name LIKE :name")
    Category findByName(String name);

    @Insert
    void insertCategory(Category category);

        /*
    @Insert
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);
*/
}
