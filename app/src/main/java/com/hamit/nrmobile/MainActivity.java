package com.hamit.nrmobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hamit.nrmobile.Adapters.CategoryPageAdapter;
import com.hamit.nrmobile.Adapters.NewsRecycler;
import com.hamit.nrmobile.CategoryFragments.GeneralFragment;
import com.hamit.nrmobile.Network.NewsApiService;
import com.hamit.nrmobile.Network.RetrofitClient;
import com.hamit.nrmobile.data.model.NewsResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CategoryPageAdapter pagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private NewsRecycler newsAdapter;
    private static final String API_KEY = "3bc2bc0f618346a58c09a18dc3b39216";
    // list our categories
    private final List<String> categories = Arrays.asList("general", "business", "technology", "sports", "entertainment", "health", "science");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // setup our tabview
        viewPager= findViewById(R.id.viewPager);
        tabLayout= findViewById(R.id.tabLayout);


        //testApiConnection();
        setupViewPager();
        fetchNewsByCategory("general");

    }

    private void setupViewPager() {
        pagerAdapter= new CategoryPageAdapter(this, categories);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(categories.get(position))).attach();

        // listen for tab changes in order to change tab and data
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String category = categories.get(position);
                fetchNewsByCategory(category);
            }
        });
    }

    private void fetchNewsByCategory(String category) {
        NewsApiService apiService= RetrofitClient.getService();
        Call<NewsResponse> call= apiService.getTopHeadlines("us", category, API_KEY);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()){
                    NewsResponse newsResponse= response.body();
                    updateCurrentFragment(newsResponse.getArticles());
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

    private void updateCurrentFragment(List<NewsResponse.Article> articles) {
        int currentItem= viewPager.getCurrentItem();
        Fragment fragment= getSupportFragmentManager().findFragmentByTag("f" + currentItem);
        if (fragment instanceof GeneralFragment){
            GeneralFragment generalFragment= (GeneralFragment) fragment;
            generalFragment.updateNews(articles);
        }

    }



    // simple Api test
    private void testApiConnection(String category) {
        NewsApiService apiService = RetrofitClient.getService();
        Call<NewsResponse> call = apiService.getTopHeadlines("us", category, API_KEY);

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