package es.fdi.stickerlab.DAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.util.List;

import es.fdi.stickerlab.DAO.CategoryDAO;
import es.fdi.stickerlab.Model.Category;

@Database(entities = {Category.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDAO categoryDAO();

}