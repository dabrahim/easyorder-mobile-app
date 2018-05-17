package com.esmt.darandroidproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class FournisseursAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Fournisseur> fournisseurs;

    public FournisseursAdapter(Context context, List<Fournisseur> fournisseurs) {
        this.mContext = context;
        this.fournisseurs = fournisseurs;
    }

    @Override
    public int getCount() {
        return fournisseurs.size();
    }

    @Override
    public Fournisseur getItem(int i) {
        return fournisseurs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Fournisseur fournisseur = getItem(position);

        if (convertView == null){
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.linearlayout_fournisseur, null);
        }

        final TextView textView = (TextView) convertView.findViewById(R.id.fournisseur_name);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.fournisseur_image);

        textView.setText(fournisseur.getNomSociete());
        new DownloadImageTask(imageView)
                .execute("http://"+QueryUtils.IP_ADDRESS+"/easyorder/uploads/" + fournisseur.getNomImageProfil());

        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
