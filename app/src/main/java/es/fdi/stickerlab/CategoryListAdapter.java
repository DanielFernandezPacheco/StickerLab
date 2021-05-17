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
    private ArrayList<String> categories;
    private LayoutInflater inflater;
    private Context context;

    public CategoryListAdapter(Context context, ArrayList<String> categories){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.categories = categories;
    }

    // llamar para actualizar la lista cuando se añadan categorías
    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
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
