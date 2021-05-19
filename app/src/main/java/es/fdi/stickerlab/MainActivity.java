package es.fdi.stickerlab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import es.fdi.stickerlab.DAO.AppDatabase;

public class MainActivity extends AppCompatActivity {
    public static AppDatabase db;
    SearchView searchView;
    SectionsPagerAdapter sectionsPagerAdapter;
    private static Context context;
    public static StickerViewModel myStickerViewModel;


    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        final AppBarLayout appBar = findViewById(R.id.appbar);



        /*------------------------INSTANCIACIÓN DE LA BASE DE DATOS---------------------*/
        //Creamos un ViewModel con el Provider, para la Base de datos
        myStickerViewModel = new ViewModelProvider(this).get(StickerViewModel.class);

        //Añadimos un observador de LiveData
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        myStickerViewModel.getAll().observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            //adapter.submitList(words);
        });

        /*------------------------------------------------------------------------------*/
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new addCategoryDialog(MainActivity.this);

                /*Category category;

                db.categoryDAO().insertCategoty(Category);*/
            }
        });


        final ImageView titleImage = findViewById(R.id.logoImageTitle);
        final TextView titleText = findViewById(R.id.titleText);

        // Listener para cuando hay un cambio en las pestañas
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                appBar.setExpanded(true);

            }

            @Override
            public void onPageSelected(int position) {
                // cuando se selecciona la pestaña maker
                if(position==1){
                    titleImage.setVisibility(View.VISIBLE);
                    titleText.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    searchView.setVisibility(View.INVISIBLE);
                }
                // cuando se selecciona la pestaña stickers (position == 0)
                else{
                    searchView.setIconified(true);
                    fab.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                }

                appBar.setExpanded(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                appBar.setExpanded(true);
            }
        });

        SearchFragment searchFragment = new SearchFragment(this);
        FragmentContainerView searchFragmentContainer = findViewById(R.id.searchFragment);
        searchFragmentContainer.setVisibility(View.INVISIBLE);

        searchView = findViewById(R.id.searchView);

        // Listener para cuando se pulsa el SearchView
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleImage.setVisibility(View.GONE);
                titleText.setVisibility(View.GONE);
                searchFragmentContainer.setVisibility(View.VISIBLE);
                tabs.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }
        });

        // Listener para cuando se cierra el SearchView
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleImage.setVisibility(View.VISIBLE);
                titleText.setVisibility(View.VISIBLE);
                searchFragmentContainer.setVisibility(View.INVISIBLE);
                tabs.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.trim();
                if(!newText.isEmpty())
                    searchFragment.OnQueryChanged(newText);
                return false;
            }
        });
        Intent intent = getIntent();
        receiveStickerIntent(intent);

        // para que receiveStickerIntent no lo trate más, así se evita que al girar la pantalla
        // vuelva a aparecer el Dialog aún habiéndolo cerrado
        intent.setType(null);

    }

    @Override
    // cierra el SearchView al pulsar botón de atrás
    public void onBackPressed(){
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    // si recibe un intent con una imagen WebP lo captura y lo manda a handleReceivedSticker
    private void receiveStickerIntent(Intent intent){
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/webp")) {// con image/webp solo recibe stickers
                try {
                    handleReceivedSticker(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("RECIBIDO", "otro");
        }
    }

    // trata el contenido de la imagen recibida y la manda a ReceivedStickerDialog para mostrarla
    private void handleReceivedSticker(Intent intent) throws IOException {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Log.d("RECIBIDO", "imagen");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                // cambiar y coger directamente de la bbdd para que no de problemas de null pointer, que los da jaja
                ArrayList<String> categories = CategoriesFragment.getCategories(this);

                new ReceivedStickerDialog(this, bitmap, categories);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    public void onStickerInsertResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            StickerEntity sticker = new StickerEntity(data.getStringExtra(StickerEntity.EXTRA_REPLY));
            myStickerViewModel.insert(sticker);
        } else {
                //Error de inserción
        }
    }*/

    public static Context getAppContext() {
        return context;
    }
}