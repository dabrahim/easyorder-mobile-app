package com.esmt.darandroidproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class CategoriesAdapater extends ArrayAdapter<Categorie> {

    private static final String LOG_TAG = CategoriesAdapater.class.getSimpleName();

    private List<Categorie> mCategories;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param categories A List of Product objects to display in a list
     */
    public CategoriesAdapater(Activity context, List<Categorie> categories) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, categories);
        mCategories = categories;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.linearlayout_block_produits, parent, false);
        }


        // Get the {@link Categorie} object located at this position in the list
       final Categorie currentCategorie = getItem(position);


        Button btnVoirPlus = listItemView.findViewById(R.id.btn_voir_plus);
        btnVoirPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProductsListActivity.class);
                i.putExtra("idCategorie", currentCategorie.getId());
                getContext().startActivity(i);
            }
        });

        AppDatabase db = NavigationActivity.getDbInstance(getContext());
        final Categorie cat = db.categorieDao().findById(currentCategorie.getId());

        //Nom de la catégorie
        TextView textView = listItemView.findViewById(R.id.nom_categorie_textView);
        textView.setText(cat.getNom());


        //Liste des produits
        List<Produit> produits = currentCategorie.getProduits();
        RecyclerView recyclerView = listItemView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ProductsPreviewAdapter adapter = new ProductsPreviewAdapter(produits);
        recyclerView.setAdapter(adapter);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
