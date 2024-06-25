package com.example.newsnow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    List<Article>articleList=new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler_view);
        progressIndicator=findViewById(R.id.progress_bar);
        searchView=findViewById(R.id.search_view);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btn4=findViewById(R.id.btn4);
        btn5=findViewById(R.id.btn5);
        btn6=findViewById(R.id.btn6);
        btn7=findViewById(R.id.btn7);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setRecyclerView();
        getNews("GENERAL",null);

        }
        void changeInProgress(boolean show) {
            if (show) {
                progressIndicator.setVisibility(RecyclerView.VISIBLE);
            } else {
                progressIndicator.setVisibility(recyclerView.INVISIBLE);
            }
        }

        void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
        }
        void getNews(String category,String query){
        changeInProgress(true);
            NewsApiClient newsApiClient= new NewsApiClient("cf9f090a9089462ab6cf34d60ad6727d");
            newsApiClient.getTopHeadlines(
                    new TopHeadlinesRequest.Builder().language("en")

                            .category(category)
                            .q(query)
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        @Override
                        public void onSuccess(ArticleResponse response) {
                            runOnUiThread(() -> {
                                changeInProgress(false);
                                articleList = response.getArticles();
                                adapter.updateData(articleList);
                                adapter.notifyDataSetChanged();
                            });
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.i("Got failure",throwable.getMessage());

                        }
                    }
            );
           
    }



    private void runOnUiThread() {
    }

    @Override
    public void onClick(View v) {
        Button btn=(Button) v;
        String category=btn.getText().toString();
        getNews(category,null);

    }
}