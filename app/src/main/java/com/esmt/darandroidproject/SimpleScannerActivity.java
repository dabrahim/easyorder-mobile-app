package com.esmt.darandroidproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setSquareViewFinder(true);
        setContentView(mScannerView);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here
        Log.v(TAG, result.getText()); // Prints scan results
        Log.v(TAG, result.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        AfficherCommandeTask task = new AfficherCommandeTask();
        task.execute(result.getText());

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    class AfficherCommandeTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return QueryUtils.fetchDetailsCommande(urls[0]);
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            //Toast.makeText(SimpleScannerActivity.this, jsonResponse, Toast.LENGTH_SHORT).show();
            try{
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject commandeJSON = jsonObject.getJSONObject("commande");
                final int montant = commandeJSON.getInt("montant");

                AlertDialog.Builder dialog = new AlertDialog.Builder(SimpleScannerActivity.this);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Cofirmation");
                dialog.setMessage("Vous etes sur le point de payer une commande de " + montant + "Frcs. Etres vous surs de vouloir continuer ?");
                dialog.setPositiveButton("Oui Payer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PayerCommandeTask task = new PayerCommandeTask();
                        task.execute(montant);
                        Toast.makeText(SimpleScannerActivity.this, "Paiement en cours vous allez recevoir une notification", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(SimpleScannerActivity.this, NavigationActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SimpleScannerActivity.this, NavigationActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                dialog.show();

            } catch (JSONException e){

            }
        }
    }

    class PayerCommandeTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            return QueryUtils.payerCommande(params[0]);
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            //Toast.makeText(SimpleScannerActivity.this, jsonResponse, Toast.LENGTH_SHORT).show();
        }
    }
}
