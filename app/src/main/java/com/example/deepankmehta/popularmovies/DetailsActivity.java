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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mDate;
    private TextView mRate;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mPoster = (ImageView) findViewById(R.id.poster);
        mTitle = (TextView) findViewById(R.id.title);
        mDate = (TextView) findViewById(R.id.release_date);
        mRate = (TextView) findViewById(R.id.rate);
        mOverview = (TextView) findViewById(R.id.overview);

        String poster = getIntent().getStringExtra("poster");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("release");
        String rate = getIntent().getStringExtra("rate");
        String overview = getIntent().getStringExtra("overview");

        Picasso.get()
                .load(poster)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(mPoster);

        mTitle.setText(title);
        mDate.setText(date);
        mRate.setText(rate);
        mOverview.setText(overview);

    }
}
