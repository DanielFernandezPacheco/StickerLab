package es.fdi.stickerlab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SearchView searchView;
    SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        final AppBarLayout appBar = findViewById(R.id.appbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Implementar añadir categoría", 2000).show();
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


        searchView = findViewById(R.id.searchView);

        // Listener para cuando se pulsa el SearchView
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleImage.setVisibility(View.GONE);
                titleText.setVisibility(View.GONE);
            }
        });

        // Listener para cuando se cierra el SearchView
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleImage.setVisibility(View.VISIBLE);
                titleText.setVisibility(View.VISIBLE);
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
                ArrayList<String> categories = CategoriesFragment.getCategories();


                new ReceivedStickerDialog(this, bitmap, categories);




            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}