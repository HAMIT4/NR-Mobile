package com.hamit.nrmobile.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamit.nrmobile.R;
import com.hamit.nrmobile.data.model.NewsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecycler extends RecyclerView.Adapter<NewsRecycler.MyViewHolder> {

    private Context context;
    private List<NewsResponse.Article> articles;

    public NewsRecycler(Context context, List<NewsResponse.Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsRecycler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder= new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.news_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecycler.MyViewHolder holder, int position) {

        NewsResponse.Article article= articles.get(position);

        holder.newsTitle.setText(article.getTitle());
        holder.newsDescription.setText(article.getDescription());
        holder.publishedAt.setText("Published AT: "+article.getPublishedAt());

        //use picasso to load the images
        Picasso.get()
                .load(article.getUrlToImage())
                .placeholder(R.drawable.news_placeholder)
                .fit()
                .centerCrop()
                .into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return articles ==null? 0:articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsImage;
        private TextView newsTitle, newsDescription, publishedAt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);
            publishedAt = itemView.findViewById(R.id.published_at);
        }
    }
}
