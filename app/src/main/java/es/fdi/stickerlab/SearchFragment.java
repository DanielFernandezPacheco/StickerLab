package es.fdi.stickerlab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.fdi.stickerlab.DAO.AppDatabase;
import es.fdi.stickerlab.Model.StickerEntity;


public class SearchFragment extends Fragment {
    private File[] stickerList;
    private int count;
    private Bitmap[] stickers;
    private boolean[] stickersSelection;
    private SearchFragment.ImageAdapter imageAdapter;
    private String categoryTitle;
    private Context context;
    private GridView imagegrid;
    private String[] pathList;

    public SearchFragment() {
    }
    public SearchFragment(Context context) {
        this.context = context;
        this.pathList = new String[0];
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //cambiar esto cuando se muestren los stickers
        stickersSelection = new boolean[]{false};

        imagegrid = (GridView) view.findViewById(R.id.SearchStickerGrid);
        imageAdapter = new SearchFragment.ImageAdapter();
        imagegrid.setAdapter(imageAdapter);

        final Button selectBtn = (Button) view.findViewById(R.id.searchViewBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                final int len = stickersSelection.length;
                int cnt = 0;
                //String selectImages = "";
                for (int i =0; i<len; i++)
                {
                    if (stickersSelection[i]){
                        cnt++;
                        //selectImages = selectImages + arrPath[i] + "|";
                    }
                }
                if (cnt == 0){
                    Toast.makeText(view.getContext(),
                            "Selecciona algún sticker",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(view.getContext(),
                            "Has seleccionado " + cnt + " stickers.",
                            Toast.LENGTH_LONG).show();
                    //Log.d("SelectedImages", selectImages);
                }
            }
        });
    }

    public void OnQueryChanged(String query){
        
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<String> stickerPaths = Room.databaseBuilder(context, AppDatabase.class, "stickers").allowMainThreadQueries().build()
                        .getDatabase(context).stickerDAO().getStickerByName(query);
                //Log.d("depurar", String.valueOf(stickerPaths));
                pathList = stickerPaths.toArray(new String[stickerPaths.size()]);
            }
        });


        stickerList = getStickerList(pathList);
        this.count = stickerList==null? 0: stickerList.length;

        this.stickers = new Bitmap[this.count];

        this.stickersSelection = new boolean[this.count];

        for (int i = 0; i < this.count; i++) {
            stickers[i] = getBitmapFromFile(stickerList[i], 300);
        }


    }


    private File[] getStickerList(String[] pathList){
        ArrayList<File> stickerArrayList = new ArrayList<File>();

        for (int i = 0; i<pathList.length ; i++){
            stickerArrayList.add(new File(pathList[i]));
        }

        stickerArrayList.toArray(new File[pathList.length]);

        File[] stickerList = new File[stickerArrayList.size()];
        stickerArrayList.toArray(stickerList);

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
        private SearchFragment.ViewHolder holder;

        public ImageAdapter() {
            mInflater = (LayoutInflater) MainActivity.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                holder = new SearchFragment.ViewHolder();
                convertView = mInflater.inflate(R.layout.sticker_item, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.stickerImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

                convertView.setTag(holder);
            }
            else {
                holder = (SearchFragment.ViewHolder) convertView.getTag();
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
}