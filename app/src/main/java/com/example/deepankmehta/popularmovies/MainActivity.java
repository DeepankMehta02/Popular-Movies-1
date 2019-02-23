/*
 * Copyright 2019 Deepank Mehta. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * You may not use this file; except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * Distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * Limitations under the License.
 *
 */

package com.example.deepankmehta.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.deepankmehta.popularmovies.utils.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private Movie[] jsonData;
    private String query = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mErrorMessage = (TextView) findViewById(R.id.error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int numberOfColumns = calculateNoOfColumns(getApplicationContext());
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        loadMovies();
    }

    public void loadMovies() {
        String queryData = query;
        showJsonData();
        new FetchMoviesTask().execute(queryData);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public void showJsonData() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(int position) {
        Intent settingsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        settingsIntent.putExtra(Intent.EXTRA_TEXT, position);
        settingsIntent.putExtra("poster", jsonData[position].getPoster());
        settingsIntent.putExtra("title", jsonData[position].getTitle());
        settingsIntent.putExtra("release", jsonData[position].getDate());
        settingsIntent.putExtra("rate", jsonData[position].getRate());
        settingsIntent.putExtra("overview", jsonData[position].getOverview());
        startActivity(settingsIntent);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String sortBy = params[0];
            URL movieUrl = NetworkUtils.buildUrl(sortBy);
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                jsonData = NetworkUtils.getMovieJson(MainActivity.this, jsonResponse);
                return jsonData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (data != null) {
                showJsonData();
                mAdapter = new MovieAdapter(data, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            loadMovies();
        }

        if (id == R.id.popular) {
            query = "popular";
            loadMovies();
            return true;
        }

        if (id == R.id.top_rated) {
            query = "top_rated";
            loadMovies();
            return true;
        }

        if (id == R.id.now_playing) {
            query = "now_playing";
            loadMovies();
            return true;
        }

        if (id == R.id.upcoming) {
            query = "upcoming";
            loadMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
