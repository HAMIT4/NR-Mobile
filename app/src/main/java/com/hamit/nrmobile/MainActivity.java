package com.hamit.nrmobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.hamit.nrmobile.Adapters.NewsRecycler;
import com.hamit.nrmobile.Network.NewsApiService;
import com.hamit.nrmobile.Network.RetrofitClient;
import com.hamit.nrmobile.data.model.NewsResponse;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private RecyclerView newsRecyclerView;
    private NewsRecycler newsAdapter;
    private static final String API_KEY = "3bc2bc0f618346a58c09a18dc3b39216";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        newsRecyclerView= findViewById(R.id.recyclerView_news);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchNews();
        //testApiConnection();

    }

    private void fetchNews() {
        NewsApiService apiService= RetrofitClient.getService();
        Call<NewsResponse> call= apiService.getTopHeadlines("us", API_KEY);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()){
                    NewsResponse newsResponse= response.body();
                    newsAdapter= new NewsRecycler(MainActivity.this, newsResponse.getArticles());
                    newsRecyclerView.setAdapter(newsAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch news", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage());
            }
        });
    }

    // simple Api test
    private void testApiConnection() {
        NewsApiService apiService = RetrofitClient.getService();
        Call<NewsResponse> call = apiService.getTopHeadlines("us", API_KEY);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Test", "Success! Articles: " + response.body().getArticles().size());
                } else {
                    Log.e("API Test", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("API Test", "Failure: " + t.getMessage());
            }
        });
    }
}