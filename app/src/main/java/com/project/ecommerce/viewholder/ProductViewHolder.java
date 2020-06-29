package com.project.ecommerce.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ecommerce.R;
import com.project.ecommerce.clickinterface.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvProductName, tvProductDescription, tvProductPrice;
    public ImageView productImage;
    public ItemClickListener itemClickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImage = itemView.findViewById(R.id.product_image);
        tvProductName = itemView.findViewById(R.id.tvProductName);
        tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
        tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
