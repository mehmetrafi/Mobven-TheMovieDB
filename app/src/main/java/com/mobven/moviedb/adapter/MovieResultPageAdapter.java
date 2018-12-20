package com.mobven.moviedb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mobven.moviedb.R;
import com.mobven.moviedb.adapter.base.BaseBasicAdapter;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MovieResultPageAdapter extends BaseBasicAdapter {

    private MovieResultsPage movieResultsPage;

    public MovieResultPageAdapter(Context context, @NonNull MovieResultsPage movieResultsPage) {
        super(context);
        this.movieResultsPage = movieResultsPage;
    }

    private boolean hasMovieResultsPageContent() {
        return movieResultsPage != null && !movieResultsPage.getResults().isEmpty();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(hasMovieResultsPageContent()) {
            count = movieResultsPage.getResults().size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if(hasMovieResultsPageContent() && position >= 0 && position < getCount()) {
            item = movieResultsPage.getResults().get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int i) {
        return (long)i;
    }

    private class MovieListItemHolder {
        public SimpleDraweeView thumb;
        public TextView rating;
        public TextView totalVotes;
        public TextView title;
        public TextView summary;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        MovieListItemHolder itemHolder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.listview_row_movie_list_item, parentView, false);
            itemHolder = new MovieListItemHolder();
            itemHolder.thumb = rowView.findViewById(R.id.thumb);
            itemHolder.title = rowView.findViewById(R.id.title);
            itemHolder.totalVotes = rowView.findViewById(R.id.totalVotes);
            itemHolder.rating = rowView.findViewById(R.id.rating);
            itemHolder.summary = rowView.findViewById(R.id.summary);

            rowView.setTag(itemHolder);
        } else {
            itemHolder = (MovieListItemHolder) rowView.getTag();
        }

        MovieDb movieDb = (MovieDb) getItem(position);
        itemHolder.title.setText(movieDb.getTitle());
        itemHolder.totalVotes.setText("(" + String.valueOf(movieDb.getVoteCount()) + ")");
        itemHolder.rating.setText(String.valueOf(movieDb.getVoteAverage()));
        itemHolder.summary.setText(movieDb.getOverview());
        String posterPath;
        if((posterPath = movieDb.getPosterPath()) != null) {
            itemHolder.thumb.setImageURI("https://image.tmdb.org/t/p/w500/" + posterPath);
        }



        return rowView;
    }

}
