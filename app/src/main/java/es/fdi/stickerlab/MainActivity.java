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
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "implementar cuadro de búsqueda de stickers", 2000).show();
            }
        });

        final AppBarLayout appBar = findViewById(R.id.appbar);
        //expande appBar cuando se cambia de pestaña
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                appBar.setExpanded(true);
            }

            @Override
            public void onPageSelected(int position) {
                appBar.setExpanded(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                appBar.setExpanded(true);
            }
        });

        Intent intent = getIntent();
        receiveStickerIntent(intent);
    }

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

    private void handleReceivedSticker(Intent intent) throws IOException {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Log.d("RECIBIDO", "imagen");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                new ReceivedStickerDialog(this, bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}