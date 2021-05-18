package es.fdi.stickerlab.Model;

import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = StickerEntity.TABLE_NAME)
public class StickerEntity {
    public static final String TABLE_NAME = "stickers";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_CATEGORIA = "categoria";

    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "categoria")
    private String categoria;

    @ColumnInfo(name = "ruta")
    private String ruta;


    public StickerEntity(long id, String nombre, String categoria, String ruta) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.ruta = ruta;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
