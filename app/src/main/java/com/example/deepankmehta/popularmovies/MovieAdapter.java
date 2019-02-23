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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Movie[] mMovieData;

    final private MovieItemClickListener mOnClickListener;

    public MovieAdapter(Movie[] movies, MovieItemClickListener clickListener) {
        mMovieData = movies;
        mOnClickListener = clickListener;
    }

    public interface MovieItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            listImageView = (ImageView) itemView.findViewById(R.id.main_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String movieImage = mMovieData[position].getPoster();
        Picasso.get()
                .load(movieImage)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.listImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) {
            return 0;
        }
        return mMovieData.length;
    }

}
