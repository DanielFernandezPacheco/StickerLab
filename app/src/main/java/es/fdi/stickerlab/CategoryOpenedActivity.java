package es.fdi.stickerlab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;

import static es.fdi.stickerlab.MainActivity.myStickerViewModel;

public class CategoryOpenedActivity extends AppCompatActivity implements View.OnClickListener{
    private int count;
    private Bitmap[] stickers;
    private boolean[] stickersSelection;
    private ImageAdapter imageAdapter;
    private String categoryTitle;
    private File[] stickerList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_opened);
        TextView title = findViewById(R.id.categoryOpenedTitle);
        categoryTitle = getIntent().getExtras().get("title").toString();
        title.setText(categoryTitle);

        stickerList = getStickerList();
        this.count = stickerList==null? 0: stickerList.length;

        this.stickers = new Bitmap[this.count];
        
        this.stickersSelection = new boolean[this.count];

        for (int i = 0; i < this.count; i++) {
            stickers[i] = getBitmapFromFile(stickerList[i], 300);
        }

        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);

        final Button selectBtn = (Button) findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                final int len = stickersSelection.length;
                int cnt = 0;
                int selection = -1;
                //String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (stickersSelection[i]) {
                        cnt++;
                        if (selection == -1)
                            selection = i;
                    }

                }
                if (cnt == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Selecciona algún sticker",
                            Toast.LENGTH_LONG).show();
                } else if (cnt == 1) {
                    Uri imageUri = Uri.parse(stickerList[selection].getAbsolutePath());
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    //Target whatsapp:
                    shareIntent.setPackage("com.whatsapp");
                    //Add text and then Image URI

                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Compartido vía StickerLab");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/webp");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(shareIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Has seleccionado " + cnt + " stickers, solo puedes compartir uno",
                            Toast.LENGTH_LONG).show();
                    //Log.d("SelectedImages", selectImages);
                }
            }
        });
        
        final Button deleteBtn = (Button) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                final int len = stickersSelection.length;
                int cnt = 0;
                //String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (stickersSelection[i]) {
                        cnt++;
                        //selectImages = selectImages + arrPath[i] + "|";
                    }
                }
                if (cnt == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Selecciona algún sticker",
                            Toast.LENGTH_LONG).show();
                } else {
                    finish();
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.getAppContext());
                    dialogo.setMessage("¿ Estás seguro de que quieres eliminar estos stickers ?");
                    dialogo.setCancelable(false);
                    dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo, int id) {
                            int c = 0;
                            for (int i = 0; i < len; i++) {
                                if (stickersSelection[i]) {
                                    c++;
                                    stickerList[i].delete();

                                    //Eliminamos el sticker de la base de datos
                                    myStickerViewModel.deleteByPath(stickerList[i].getAbsolutePath());
                                    //selectImages = selectImages + arrPath[i] + "|";
                                }
                            }
                            Toast.makeText(getApplicationContext(),
                                    "Has eliminado " + c + " stickers.",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MainActivity.getAppContext(), CategoryOpenedActivity.class);
                            i.putExtra("title", categoryTitle);
                            //i.putExtra("c",  context);
                            MainActivity.getAppContext().startActivity(i);

                        }
                    });
                    dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo, int id) {
                            finish();
                            Intent i = new Intent(MainActivity.getAppContext(), CategoryOpenedActivity.class);
                            i.putExtra("title", categoryTitle);
                            //i.putExtra("c",  context);
                            MainActivity.getAppContext().startActivity(i);
                        }
                    });
                    dialogo.show();
                }


            }
        });

        final Button moveBtn = (Button) findViewById(R.id.moveBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                final int len = stickersSelection.length;
                int cnt = 0;
                //String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (stickersSelection[i]) {
                        cnt++;
                        //selectImages = selectImages + arrPath[i] + "|";
                    }
                }
                if (cnt == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Selecciona algún sticker",
                            Toast.LENGTH_LONG).show();
                } else {
                    finish();
                    ArrayList<String> categories = CategoriesFragment.getCategories(MainActivity.getAppContext());
                    new ChangeCategoryStickerDialog(MainActivity.getAppContext(), stickers, stickersSelection,stickerList, categories);

                    // Intent i = new Intent(CategoriesFragment.getAppContext(), CategoryOpenedActivity.class);
                    // i.putExtra("title", categoryTitle);
                    //CategoriesFragment.getAppContext().startActivity(i);

                }
            }
        });


    }

    private File[] getStickerList(){
        File stickersDir = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/stickers/" + categoryTitle);

        File[] stickerList = stickersDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name)
            {
                return ((name.endsWith(".webp")));
            }

        });

        return stickerList;
    }

    public static Bitmap getBitmapFromFile(File bitmapFile, int sideSizeLimit){
        if (bitmapFile==null || !bitmapFile.exists() || !bitmapFile.canRead())
            return null;
        int maxWidth = 0, maxHeight = 0;
        if (sideSizeLimit>0){
            maxWidth = sideSizeLimit;
            maxHeight = sideSizeLimit;
        }

        try {
            //decode image size
            BitmapFactory.Options bmfOtions = new BitmapFactory.Options();
            bmfOtions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(bitmapFile),null,bmfOtions);

            //Find the correct scale value. It should be the power of 2.
            //final int REQUIRED_SIZE=70;
            int width_tmp=bmfOtions.outWidth, height_tmp=bmfOtions.outHeight;
            int scale=1;
            while(width_tmp > maxWidth || height_tmp > maxHeight){
                width_tmp/=2;
                height_tmp/=2;
                scale++;
            }
            bmfOtions.inSampleSize = scale;
            bmfOtions.inJustDecodeBounds = false;

            //decode with inSampleSize
            //BitmapFactory.Options o2 = new BitmapFactory.Options();
            return BitmapFactory.decodeStream(new FileInputStream(bitmapFile), null, bmfOtions);
        } catch (FileNotFoundException e) {}
        return null;
    }


    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ViewHolder holder;
        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.sticker_item, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.stickerImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (stickersSelection[id]){
                        cb.setChecked(false);
                        stickersSelection[id] = false;
                    } else {
                        cb.setChecked(true);
                        stickersSelection[id] = true;
                    }
                }
            });

            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    int id = v.getId();

                    /*
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/webp");
                    startActivity(intent);*/
                }
            });
            holder.imageview.setImageBitmap(stickers[position]);
            holder.checkbox.setChecked(stickersSelection[position]);
            holder.id = position;
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.categoryOpenedTitle || v.getId() == R.id.categoryOpenedArrow)
            finish();
    }
}
