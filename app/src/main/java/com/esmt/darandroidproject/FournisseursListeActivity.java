package com.esmt.darandroidproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FournisseursListeActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private static final String LOG_TAG = FournisseursListeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fournisseurs_liste);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Chargement de la liste des fournisseurs en cours");

        FetchFournisseursTask task = new FetchFournisseursTask();
        task.execute();
    }

    private class FetchFournisseursTask extends AsyncTask<Void, Void, List<Fournisseur> > {
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected List<Fournisseur> doInBackground(Void ... nothingHere) {
            List<Fournisseur> fournisseurs = QueryUtils.fetchFournisseursList();
            return fournisseurs;
        }

        @Override
        protected void onPostExecute( List<Fournisseur> fournisseurs ) {
            updateUI(fournisseurs);
            dialog.hide();
        }
    }

    private void updateUI(final List<Fournisseur> fournisseurs) {
        GridView gridView = (GridView) findViewById(R.id.gridview);
        FournisseursAdapter adapter = new FournisseursAdapter(this, fournisseurs);
        gridView.setAdapter( adapter );
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Fournisseur fournisseur = fournisseurs.get( position );
                //Toast.makeText(FournisseursListeActivity.this, fournisseur.getNomSociete(), Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.fournisseur_id), fournisseur.getIdUser());
                editor.commit();

                Intent i = new Intent(FournisseursListeActivity.this, NavigationActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
