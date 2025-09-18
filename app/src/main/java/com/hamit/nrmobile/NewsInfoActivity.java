package com.hamit.nrmobile;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewsInfoActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView backButton, shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webView= findViewById(R.id.webView);
        backButton= findViewById(R.id.btnBack);
        shareButton= findViewById(R.id.btnShare);

        backButton.setOnClickListener(v -> finish());

        shareButton.setOnClickListener(v -> {
            // create android share intent.ACTION_SEND
            String url= getIntent().getStringExtra("url");
            if(url !=null){
                Intent shareIntent= new Intent(Intent.ACTION_SENDTO);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out This News");
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);

                startActivity(Intent.createChooser(shareIntent, "share via"));
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String url= getIntent().getStringExtra("url");
        if (url != null) webView.loadUrl(url);

    }
}