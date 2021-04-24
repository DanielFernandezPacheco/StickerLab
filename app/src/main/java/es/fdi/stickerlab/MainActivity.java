package es.fdi.stickerlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        img = findViewById(R.id.image);
        info = findViewById(R.id.info);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Implementar cuadro de búsqueda de stickers", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fab.setVisibility(0);
            }
        });


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/webp")) {// con image/webp solo recibe stickers
                try {
                    handleSendImage(intent); // Handle single image being sent
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("RECIBIDO", "otro");
        }

        Context context = getApplicationContext();
        Log.d("depurar", context.getFilesDir().toString());
    }

    void handleSendImage(Intent intent) throws IOException {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            //img.setImageResource(R.drawable.intent.);
            Log.d("RECIBIDO", "imagen");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                /*
                FileOutputStream out = new FileOutputStream("sticker");
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, out); // bmp is your Bitmap instance
                img.setImageBitmap(bitmap);
                info.setVisibility(View.GONE);*/
                new ReceivedStickerDialog(this, bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}