package com.example.rikkeisoft.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikkeisoft.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<String> mUrlImage;
    private ImageOnClickListener imageOnClickListener;

    public ImageAdapter() {
        this.mUrlImage = new ArrayList<>();
    }

    public void setImageOnclickListener(ImageOnClickListener imageOnclickListener) {
        this.imageOnClickListener = imageOnclickListener;
    }

    public void setImages(List<String> mUrlImage) {
        this.mUrlImage = mUrlImage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.itemimage_activity, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder viewHolder, int i) {
        final String url = mUrlImage.get(i);
        viewHolder.itemImage.setImageURI(Uri.parse(url));
        viewHolder.itemImage.setOnClickListener(v -> imageOnClickListener.onClickItem(url));
        viewHolder.buttonRemove.setOnClickListener(v -> imageOnClickListener.onRemove(i));

    }

    @Override
    public int getItemCount() {
        return mUrlImage.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemImage)
        ImageView itemImage;

        @BindView(R.id.buttonRemove)
        ImageView buttonRemove;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface ImageOnClickListener {
        void onClickItem(String url);

        void onRemove(int position);
    }

}
