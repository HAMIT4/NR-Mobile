package com.hamit.nrmobile.CategoryFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hamit.nrmobile.Adapters.NewsRecycler;
import com.hamit.nrmobile.Network.NewsApiService;
import com.hamit.nrmobile.Network.RetrofitClient;
import com.hamit.nrmobile.R;
import com.hamit.nrmobile.data.model.NewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralFragment extends Fragment {
    private static final String ARG_CATEGORY = "category" ;
    private String category;
    private RecyclerView recyclerViewGeneral;
    private NewsRecycler newsAdapter;


    public GeneralFragment() {
        // Required empty public constructor
    }

    public static GeneralFragment newInstance(String category) {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category= getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_general, container, false);

        recyclerViewGeneral= contentView.findViewById(R.id.recyclerViewGeneral);
        recyclerViewGeneral.setLayoutManager(new LinearLayoutManager(getContext()));

        // test using empty list before I fetch data from the api
        fetchNews(category);

        return contentView;
    }
    private void fetchNews(String category) {
        NewsApiService apiService = RetrofitClient.getService();
        Call<NewsResponse> call = apiService.getTopHeadlines("us", category, "YOUR_API_KEY");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newsAdapter = new NewsRecycler(getContext(), response.body().getArticles());
                    recyclerViewGeneral.setAdapter(newsAdapter);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("FragmentAPI", "Error: " + t.getMessage());
            }
        });
    }

    public void updateNews(List<NewsResponse.Article> articles){
        // updating code to show search results
        if (getView() != null){
            if(recyclerViewGeneral != null){
                newsAdapter = new NewsRecycler(getContext(), articles);
                recyclerViewGeneral.setAdapter(newsAdapter);

                // show empty state if no article is found
                if (articles.isEmpty()){
                    // try to create a text to show no article was found
                    Toast.makeText(requireActivity(), "No search results Found", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    public String getCategory() {
        return category;
    }
}