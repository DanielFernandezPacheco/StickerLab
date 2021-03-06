package es.fdi.stickerlab;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class addCategoryDialog extends AlertDialog.Builder {
    public static final String NO_CATEGORY = "Sin categoría";
    View view;
    EditText categoryName;

    public addCategoryDialog(@NonNull Context context) {
        super(context, R.style.RoundedCornersDialog);

        this.createView(context);
        categoryName = view.findViewById(R.id.newCategoryName);

        this.buttonsActions(context);

        this.show();
    }

    private void createView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Titulo del dialog e icono
        this.setTitle("Nueva categoría");
        this.setIcon(R.drawable.ic_baseline_add_category_green_24);

        // establece el layout personalizado
        view = inflater.inflate(R.layout.add_category_dialog, null);

        this.setView(view);
    }

    // configuración de los dos botones del dialog
    private void buttonsActions(final Context context) {
        // Acción del botón guardar
        this.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                    if(categoryName.getText().toString().trim().equals(""))
                        Toast.makeText(context,"Introduce un nombre para la categoría", Toast.LENGTH_SHORT).show();
                    else {
                        String file_path = context.getExternalFilesDir(null).getAbsolutePath() + "/stickers/" + categoryName.getText().toString();

                        File dir = new File(file_path);

                        if (!dir.exists()){
                            dir.mkdirs();
                            CategoryListAdapter adapter = CategoriesFragment.getAdapter();
                            adapter.setCategories(CategoriesFragment.getCategories(context));
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(context, "Ya existe una categoría con ese nombre", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        this.setNegativeButton("Cancelar", null);
    }

}

