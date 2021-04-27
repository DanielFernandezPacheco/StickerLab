package es.fdi.stickerlab;

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

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    public static final int NUM_CATEGORIES_TEST = 20;
    private ArrayList<String> categories;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private TextView noStickerInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        categories = new ArrayList<String>();
        for (int i = 1; i <= NUM_CATEGORIES_TEST; i++) categories.add("Categoría " + String.valueOf(i));

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
}