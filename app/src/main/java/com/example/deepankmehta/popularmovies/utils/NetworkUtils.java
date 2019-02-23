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

package com.example.deepankmehta.popularmovies.utils;

import android.content.Context;
import android.net.Uri;

import com.example.deepankmehta.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    /**
     * Variables
     */
    static String BASE_URL = "https://api.themoviedb.org/3/movie";
    static String PARAM_API_KEY = "api_key";
    static String API_KEY = "enter_your_api_key";
    static String PARAM_LANGUAGE = "language";
    static String LANGUAGE = "en-US";
    static String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    static String RESULTS = "results";
    static String POSTER_PATH = "poster_path";
    static String TITLE = "title";
    static String VOTE = "vote_average";
    static String OVERVIEW = "overview";
    static String RELEASE_DATE = "release_date";

    public static URL buildUrl(String searchQuery) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(searchQuery)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Movie[] getMovieJson(Context context, String json) throws JSONException {
        JSONObject movieJson = new JSONObject(json);
        JSONArray movieArray = movieJson.getJSONArray(RESULTS);
        Movie[] movieResults = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String poster_path;
            String title;
            String date;
            String rate;
            String overview;

            Movie movie = new Movie();
            poster_path = movieArray.getJSONObject(i).getString(POSTER_PATH);
            title = movieArray.getJSONObject(i).getString(TITLE);
            date = movieArray.getJSONObject(i).getString(RELEASE_DATE);
            rate = movieArray.getJSONObject(i).getString(VOTE);
            overview = movieArray.getJSONObject(i).getString(OVERVIEW);

            movie.setPoster(POSTER_BASE_URL + poster_path);
            movie.setTitle(title);
            movie.setDate(date);
            movie.setRate(rate);
            movie.setOverview(overview);

            movieResults[i] = movie;
        }
        return movieResults;
    }
}
