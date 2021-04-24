package es.fdi.stickerlab;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Intent requestFileIntent;
    private ParcelFileDescriptor inputPFD;
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
        img = (ImageView) findViewById(R.id.image);
        info = (TextView) findViewById(R.id.info);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/webp")) {// con image/webp solo recibe stickers
                handleSendImage(intent); // Handle single image being sent
            }
        } else {
            Log.d("RECIBIDO", "otro");
        }

        Context context = getApplicationContext();
        Log.d("depurar", context.getFilesDir().toString());
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}