package com.esmt.darandroidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProductsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        Intent intent = getIntent();
        int idCategorie = intent.getIntExtra("idCategorie", 0);

        List<Produit> produits = DbQueryUtils.getAllProducts(getApplicationContext());

        final List<Produit> selectedProducts = new ArrayList<>();
        for(Produit produit : produits) {
            if(produit.getCategorie().getId() == idCategorie) {
                selectedProducts.add(produit);
            }
        }

        final RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        final ProductPreviewAdapter2 adapter = new ProductPreviewAdapter2(selectedProducts);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView)
            .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    int idProduit = selectedProducts.get(position).getId();
                    Intent i = new Intent(ProductsListActivity.this, ShowProductActivity.class);
                    i.putExtra("idProduit", idProduit);
                    startActivity(i);
                    finish();
                }
            });

    }
}
