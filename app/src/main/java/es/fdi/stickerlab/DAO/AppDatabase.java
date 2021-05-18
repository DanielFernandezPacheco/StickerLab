package es.fdi.stickerlab.DAO;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.fdi.stickerlab.Model.StickerEntity;

@Database(entities = {StickerEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //Singleton para tener la DAO y evitar que se abran varias instancias
    public abstract StickerDAO stickerDAO();

    private static volatile AppDatabase INSTANCE;    //Variable para acceder a la BD

    private static final int NUMBER_OF_THREADS = 4;

    //Grupo de subprocesos para ejecutar las operaciones de la BD de forma asíncrona en segundo plano
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Devuelve el singleton para evitar crear varias instancias
    public static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").build();
                }
            }
        }
        return INSTANCE;
    }

    //Llenamos la base de datos
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);

            //Para guardar los datos en el restart, deberemos comentar el siguiente bloque

            databaseWriteExecutor.execute(() -> {
                StickerDAO dao = INSTANCE.stickerDAO();

                /*StickerEntity sticker = new StickerEntity(1,"sticker","1","C:/");
                dao.insert(sticker);*/
            });
        }
    };

}
