package es.fdi.stickerlab;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private static ArrayList<String> categories;
    private LayoutInflater inflater;
    private Context context;

    public CategoryListAdapter(Context context, ArrayList<String> categories){
        inflater = LayoutInflater.from(context);
        this.context = context;

        this.categories = categories;

        // Mantiene Sin categoría en la primera posición
        int pos = this.categories.indexOf("Sin categoría");
        if(pos != -1) {
            for (int i = pos; i > 0; i--)
                this.categories.set(i, this.categories.get(i - 1));
            this.categories.set(0, "Sin categoría");
        }
    }

    // llamar para actualizar la lista cuando se añadan categorías
    public static void setCategories(ArrayList<String> categories) {
        int pos = categories.indexOf("Sin categoría");
        if(pos != -1) {
            for (int i = pos; i > 0; i--)
                categories.set(i, categories.get(i - 1));
            categories.set(0, "Sin categoría");
        }
        CategoryListAdapter.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View categoryView = inflater.inflate(R.layout.category_layout, parent, false);


        return new ViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String categoryName = categories.get(position);
        holder.category.setText(categoryName);

    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.categoryCardTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CategoryOpenedActivity.class);
                    i.putExtra("title", category.getText().toString());
                    context.startActivity(i);
                }
            });
        }

    }

}
