package com.esmt.darandroidproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ProductsPreviewAdapter extends RecyclerView.Adapter<ProductsPreviewAdapter.ViewHolder> {
    private List<Produit> mProducts;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mLinearLayout;
        public TextView textView;
        public ImageView imageProduit;
        public ViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
            mLinearLayout = linearLayout;
            textView = linearLayout.findViewById(R.id.txt_titre_produit);
            imageProduit = linearLayout.findViewById(R.id.img_product);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductsPreviewAdapter(List<Produit> products) {
        mProducts = products;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductsPreviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linearlayout_single_product_1, parent, false);
        ///
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Produit currentProduct = mProducts.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.textView.setText(currentProduct.getTitre());
        //new DownloadImageTask(holder.imageProduit)
               // .execute("http://"+ QueryUtils.IP_ADDRESS+"/easyorder/uploads/"+currentProduct.getNomFichier());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mProducts.size();
    }
}
