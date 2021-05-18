package es.fdi.stickerlab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class MakerFragment  extends Fragment {

 private static final int PICK_IMAGE = 100;
    private final Context mContext;
    Uri imageUri;
    ImageView imagen;


    public MakerFragment(Context context) {
        super();
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_maker, container, false);


    }

    public void onClick(View view){
        cargarImagen();
    }

    public void onClickFab(View v) {
        new addCategoryDialog(getContext());

                /*Category category;

                db.categoryDAO().insertCategoty(Category);*/
    }
    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imagen.setImageURI(imageUri);
            

        }
        if (imageUri != null) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // cambiar y coger directamente de la bbdd para que no de problemas de null pointer, que los da jaja
            ArrayList<String> categories = CategoriesFragment.getCategories(mContext);

            new ReceivedStickerDialog(mContext, bitmap, categories);
        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button butt = view.findViewById(R.id.btnCargarImg);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();

            }
        });
        imagen = (ImageView)view.findViewById(R.id.imagenId);


    }




}
