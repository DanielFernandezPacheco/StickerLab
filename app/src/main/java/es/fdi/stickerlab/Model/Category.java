package es.fdi.stickerlab.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "category_name")
    private String name;

    public Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
