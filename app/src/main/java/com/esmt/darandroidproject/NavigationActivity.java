package com.esmt.darandroidproject;

import android.app.ProgressDialog;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout containerLayout;
    private ProgressDialog progressDialog;
    private static AppDatabase dbInstance;
    private Utilisateur utilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement en cours ...");
        progressDialog.show();

        AppDatabase db = NavigationActivity.getDbInstance(getApplicationContext());
        utilisateur = db.userDao().findLastUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        containerLayout = findViewById(R.id.container_layout);

        int idFournisseur = getIdFournisseur();
        if (idFournisseur == 0) {
            displayDefaultContent();
        } else {
            displayProductsList( idFournisseur );
        }

/*
        ArrayList<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant("Ndiaye", "Ibrahima"));
        etudiants.add(new Etudiant("Diallo", "Maimouna"));
        etudiants.add(new Etudiant("Ndiaye", "Diadji"));
        etudiants.add(new Etudiant("Daye", "Fatou"));
*/
        /*
        Etudiant etudiants [] = {new Etudiant("Ndiaye", "Ibrahima"),
                new Etudiant("Diallo", "Maimouna"),
                new Etudiant("Ndiaye", "Diadji"),
                new Etudiant("Gaye", "Fatou")};

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "easyorder")
                .addMigrations(new Migration(1, 2) {
                    @Override
                    public void migrate(SupportSQLiteDatabase database) {}
                })
                .allowMainThreadQueries()
                .build();
        db.esmtDao().createEtudiant(etudiants);


        List<Etudiant> etudiantList = db.esmtDao().getAllEtudiant();
        String rslt = "";
        for(Etudiant etudiant : etudiantList) {
            rslt += etudiant.toString() + " ";
        }

        Toast.makeText(this, rslt, Toast.LENGTH_LONG).show();
        */
        progressDialog.hide();
    }

    private int getIdFournisseur () {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        int idFournisseur = preferences.getInt(getString(R.string.fournisseur_id), 0);
        return idFournisseur;
    }

    private void displayDefaultContent () {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.default_layout_0,null);
        containerLayout.addView(layout);

        Button button = (Button) findViewById(R.id.btn_selectionner_fournisseur);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationActivity.this, FournisseursListeActivity.class);
                startActivity(i);
            }
        });
    }

    private void displayProductsList (int idFournisseur) {
        Map<String, String> params = new HashMap<>();
        params.put("idFournisseur", String.valueOf(idFournisseur));
        params.put("idUser", String.valueOf(utilisateur.getIdUser()));

        AppDatabase db = getDbInstance(getApplicationContext());
        List<Produit> produits = db.produitDao().getAllProducts();

        if (produits.size() == 0) {
            GetProductsTask task = new GetProductsTask();
            task.execute( params );
            //Toast.makeText(this, "Data was fetched from the server", Toast.LENGTH_SHORT).show();
        } else {
            GetNewProductsTask task = new GetNewProductsTask();
            task.execute( params );
            updateUI( produits );
            //Toast.makeText(this, "Data was fetched locally", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetNewProductsTask extends AsyncTask<Map<String, String>, Void, List<Produit>> {
        @Override
        protected void onPreExecute() {
            //
        }

        @Override
        protected List<Produit> doInBackground(Map<String, String>... params) {
            return QueryUtils.getNouveauxProduits(params[0]);
        }

        @Override
        protected void onPostExecute(List<Produit> produits) {
            AppDatabase db = NavigationActivity.getDbInstance(getApplicationContext());
            db.produitDao().createProducts(produits);
        }
    }

    private class GetProductsTask extends AsyncTask<Map<String, String>, Void, List<Produit>> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected List<Produit> doInBackground(Map<String, String> ... params) {
            List<Produit> produits = QueryUtils.getProduits(params[0]);
            return produits;
        }

        @Override
        protected void onPostExecute(List<Produit> produits) {
            progressDialog.hide();
            saveProductsLocally( produits );
            updateUI( produits );
        }
    }

    private void saveProductsLocally (List<Produit> products) {
        AppDatabase db = getDbInstance(getApplicationContext());
        db.produitDao().createProducts(products);
    }

    public static AppDatabase getDbInstance (Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context, AppDatabase.class, "easyorder3")
                    /* .addMigrations(new Migration(1, 2) {
                        @Override
                        public void migrate(SupportSQLiteDatabase database) {
                        }
                    })*/
                    .allowMainThreadQueries()
                    .build();
        }
        return dbInstance;
    }

    private void updateUI(List<Produit> produits) {
        List<Integer> idCategories = new ArrayList<>();
        List<Categorie> categories = new ArrayList<>();

        //Categories contient les catégories de produits
        //recus
        for (Produit produit : produits) {
            int idCategorie = produit.getCategorie().getId();
            if (!idCategories.contains(idCategorie)) {
                idCategories.add(idCategorie);
                categories.add(new Categorie(idCategorie));
                categories.get(categories.size()-1).addProduit(produit);
            } else {
                categories.get( idCategories.indexOf(idCategorie) ).addProduit(produit);
            }
        }
        ListView listViewProducts = (ListView) findViewById(R.id.listview_produits);
        CategoriesAdapater adapater = new CategoriesAdapater(this, categories);
        listViewProducts.setAdapter(adapater);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(getString(R.string.jwt));
            editor.remove(getString(R.string.fournisseur_id));
            editor.commit();

            AppDatabase db = getDbInstance(getApplicationContext());
            db.produitDao().deleteAllProducts();
            
            Toast.makeText(NavigationActivity.this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(NavigationActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
