package es.fdi.stickerlab;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class CategoriesFragment extends Fragment {
    public static final int NUM_CATEGORIES_TEST = 20;
    private RecyclerView recyclerView;
    private static CategoryListAdapter categoryListAdapter;
    private static TextView noStickerInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> categories = getCategories(view.getContext());

        recyclerView = view.findViewById(R.id.categoriesRecyclerView);

        this.noStickerInfo = view.findViewById(R.id.noStickerTextView);

        // si no hay categorias añadidas muestra mensaje, se podría cambiar por un gif
        if (categories.size() == 0)
            this.noStickerInfo.setVisibility(View.VISIBLE);
        else this.noStickerInfo.setVisibility(View.INVISIBLE);

        categoryListAdapter = new CategoryListAdapter(view.getContext(), categories);
        recyclerView.setAdapter(categoryListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    public static ArrayList<String> getCategories(Context context) {
        ArrayList<String> categories;
        String file_path = context.getExternalFilesDir(null).getAbsolutePath() + "/stickers/";
        File dir = new File(file_path);
        categories = new ArrayList<String>(Arrays.asList(dir.list()));

        return categories;
    }
    public static CategoryListAdapter getAdapter() {
        return categoryListAdapter;
    }

    public static void hideNoStickerInfo(){
        noStickerInfo.setVisibility(View.INVISIBLE);
    }
}