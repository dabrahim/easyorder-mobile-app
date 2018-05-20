package com.esmt.darandroidproject;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static final String  IP_ADDRESS =  "192.168.43.246";

    private static final String CLIENT_REGISTRATION_URL = "http://"+IP_ADDRESS+"/easyorder/client/add";
    private static final String USER_LOGIN_URL = "http://"+IP_ADDRESS+"/easyorder/rest/connexion";
    private static final String GET_LISTE_FOURNISSEURS_URL = "http://"+IP_ADDRESS+"/easyorder/fournisseur/all";
    private static final String GET_PRODUITS = "http://"+IP_ADDRESS+"/easyorder/produit/findAll";
    private static final String GET_NOUVEAUX_PRODUITS = "http://"+IP_ADDRESS+"/easyorder/produit/getUpdates";
    private static final String SEND_ORDER_URL = "http://"+IP_ADDRESS+"/easyorder/rest/publierCommande";
    private static final String PAYER_COMMANDE = "http://"+IP_ADDRESS+":3000/notification/";
//    private static final String CLIENT_REGISTRATION_URL = "http://192.168.2.76/easyorder/client/add";

    private QueryUtils (){
    }

    /**
     *
     * @param urlString
     * @return
     */
    public static URL createUrl(String urlString) {
        URL url = null;
        try{
            url = new URL(urlString);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Unable to create URL", e);
        }
        return url;
    }

    /**
     *
     * @param params
     * @return
     */
    public static String registerClient (HashMap<String, String> params) {
        URL url = createUrl( CLIENT_REGISTRATION_URL );
        String jsonResponse = makeHttpRequest(url, "POST", params);
        return jsonResponse;
    }

    public static String connectUser (Map<String, String> params){
        URL url = createUrl( USER_LOGIN_URL );
        String jsonResponse = makeHttpRequest(url, "POST", params);
        return jsonResponse;
    }

    public static List<Fournisseur> fetchFournisseursList () {
        URL url = createUrl( GET_LISTE_FOURNISSEURS_URL );
        String jsonResponse  = makeHttpRequest(url, "GET", null);
        return extractFournisseurs( jsonResponse );
    }

    public static String fetchDetailsCommande(String urlStr) {
        URL url = createUrl(urlStr);
        return makeHttpRequest(url, "POST", null);
    }

    public static String payerCommande (int montant) {
        URL url = createUrl( PAYER_COMMANDE + montant );
        return makeHttpRequest(url, "POST", null);
    }

    public static List<Produit> getNouveauxProduits(Map<String, String> params){
        URL url = createUrl(GET_NOUVEAUX_PRODUITS);
        String jsonResponse = makeHttpRequest(url, "POST", params);
        return extractProduits(jsonResponse);
    }

    public static List<Produit> getProduits (Map<String, String> params) {
        URL url = createUrl( GET_PRODUITS );
        String jsonResponse = makeHttpRequest(url, "POST", params);
        return extractProduits( jsonResponse );
    }

    public static String sendOrder(Map<String, String> params) {
        URL url = createUrl( SEND_ORDER_URL );
        return makeHttpRequest(url, "POST", params);
    }

    private static List<Produit> extractProduits ( String jsonString ) {
        List<Produit> produits = new ArrayList<>();
        try{
            JSONObject rootObject = new JSONObject(jsonString);
            boolean success = rootObject.getBoolean("success");

            if (success) {
                JSONArray produitsArray = rootObject.getJSONArray("data");
                for(int i = 0; i < produitsArray.length(); i++) {
                    JSONObject produitObj = produitsArray.getJSONObject(i);

                    Produit produit = new Produit();
                    produit.setDescription(produitObj.getString("description"));
                    produit.setNomFichier(produitObj.getString("nom_fichier"));
                    produit.setPrix(produitObj.getInt("prix"));
                    produit.setTitre(produitObj.getString("titre"));
                    produit.setId(produitObj.getInt("id_produit"));

                    Categorie categorie = new Categorie();
                    categorie.setId(produitObj.getInt("id_categorie"));
                    produit.setCategorie( categorie );

                    Fournisseur fournisseur = new Fournisseur();
                    fournisseur.setIdUser(produitObj.getInt("id_fournisseur"));
                    produit.setFournisseur(fournisseur);

                    produits.add(produit);
                }
            } else {
                String message = rootObject.getString("message");
                Log.e(LOG_TAG, message);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON Object", e);
        }
        return produits;
    }

    /**
     *
     * @param url
     * @param verb
     * @param params
     * @return
     */
    public static String makeHttpRequest(URL url, String verb, Map<String, String> params) {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod(verb);

            if (params != null) {
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();

                Iterator it = params.entrySet().iterator();
                while (it.hasNext()){
                    Map.Entry pair = (Map.Entry) it.next();
                    builder.appendQueryParameter((String)pair.getKey(), (String) pair.getValue());
                    it.remove();
                }
                String query = builder.build().getEncodedQuery();

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }

            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            jsonResponse = readInputStream(inputStream);

        } catch (IOException e){
            Log.e(LOG_TAG, "Error making HTTP request", e);
        }

        return jsonResponse;
    }

    /**
     *
     * @param inputStream
     * @return
     */
    private static String readInputStream(InputStream inputStream) {
        StringBuffer jsonResponse = new StringBuffer ();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String currentLine = null;
            do {
                currentLine = bufferedReader.readLine();
                if( currentLine != null ) {
                    jsonResponse.append(currentLine);
                }
            } while (currentLine != null);

        } catch (IOException e){
            Log.e(LOG_TAG, "ERROR reading InputStream", e);
        }

        return  jsonResponse.toString();
    }

    private static List<Fournisseur> extractFournisseurs( String jsonString ) {
        List<Fournisseur> fournisseurs = null;
        try{
            JSONObject root = new JSONObject( jsonString );
            boolean success = root.getBoolean("success");
            if ( success ) {
                JSONArray jsonArray = root.getJSONArray("data");
                fournisseurs = new ArrayList<>();

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Fournisseur fournisseur = new Fournisseur();

                    fournisseur.setIdUser( jsonObject.getInt("id_user") );
                    fournisseur.setEmail( jsonObject.getString("email") );
                    fournisseur.setTelephone( jsonObject.getString("telephone") );
                    fournisseur.setNomSociete( jsonObject.getString("nom_societe") );
                    fournisseur.setNomImageProfil( jsonObject.getString("nom_image_profil") );

                    fournisseurs.add( fournisseur );
                }
            } else {
                String message = root.getString("message");
                Log.e(LOG_TAG, message);
            }
        } catch (JSONException e){
            Log.e(LOG_TAG, "Error parsing JSON response", e);
        }
        return fournisseurs;
    }
}
