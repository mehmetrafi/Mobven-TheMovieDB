package com.mobven.moviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mobven.moviedb.R;
import com.mobven.moviedb.adapter.base.BaseBasicAdapter;
import java.util.ArrayList;
import java.util.List;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

public class MovieDetailAdapter extends BaseBasicAdapter {

    private class Section {
        public String sectionName;
        public Integer viewType;

        public Section(String sectionName, Integer viewType) {
            this.sectionName = sectionName;
            this.viewType = viewType;
        }
    }

    private List<Section> sections;

    private static final String GENERAL_MOVIE_SECTION_KEY = "GENERAL_MOVIE_SECTION";
    private static final String CREDITS_SECTION_KEY = "CREDITS_SECTION";

    private MovieDb movieDb;
    private Credits credits;

    public MovieDetailAdapter(Context context) {
        super(context);
        initSections();
    }

    private void initSections() {
        if(sections == null) {
            sections = new ArrayList<>();
        }
        //Add predefined sections;
        //We can fetch and show more data for detail as future work
        sections.add(new Section(GENERAL_MOVIE_SECTION_KEY,sections.size()));
        sections.add(new Section(CREDITS_SECTION_KEY,sections.size()));
    }

    @Override
    public int getCount() {
        int count = 0;
        for (Section section : sections) {
            count += getSectionCount(section);
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemSection(position).viewType;
    }

    @Override
    public int getViewTypeCount() {
        return sections.size();
    }

    @Override
    public Object getItem(int position) {
        Object o = null;
        Section section = getItemSection(position);
        switch (section.sectionName) {
            case GENERAL_MOVIE_SECTION_KEY: {
                o = movieDb;
            }
            break;
            case CREDITS_SECTION_KEY: {
                o = credits.getCast().get(getCorrectedSectionInternalPosition(position));
            }
            break;
        }
        return o;
    }

    @Override
    public long getItemId(int i) {
        return (long)i;
    }

    private Section getItemSection(int position) {
        Section section = null;
        int rowCountSoFar = 0;
        for (Section s : sections) {
            section = s;
            if((rowCountSoFar += getSectionCount(section)) > position) {
                break;
            }
        }
        return section;
    }

    private int getCorrectedSectionInternalPosition(int generalPosition) {
        int position = generalPosition;
        for (Section s : sections) {
            if(position - getSectionCount(s) < 0) {
                break;
            } else {
                position -= getSectionCount(s);
            }
        }
        return position;
    }

    private int getSectionCount(Section section) {
        int count = 0;
        switch (section.sectionName) {
            case GENERAL_MOVIE_SECTION_KEY: {
                count = (movieDb != null) ? count + /*section.rowCount*/ 1 : count;
            }
            break;
            case CREDITS_SECTION_KEY: {
                count = (credits != null) ? count + /*section.rowCount*/credits.getCast().size() : count;
            }
            break;
        }
        return count;
    }

    private class GeneralItemHolder {
        public SimpleDraweeView thumb;
        public TextView title;
        public TextView releaseDate;
        public TextView vote;
        public TextView summary;
        public TextView status;
        public TextView revenue;
        public TextView runtime;
    }

    private class CreditItemHolder {
        public SimpleDraweeView thumb;
        public TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        Object itemHolder = null;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (getItemSection(position).sectionName) {
                case GENERAL_MOVIE_SECTION_KEY: {
                    rowView = inflater.inflate(R.layout.listview_row_movie_detail_general_item, parentView, false);
                    GeneralItemHolder holder = new GeneralItemHolder();
                    holder.thumb = rowView.findViewById(R.id.thumb);
                    holder.title = rowView.findViewById(R.id.title);
                    holder.releaseDate = rowView.findViewById(R.id.releaseDate);
                    holder.vote = rowView.findViewById(R.id.vote);
                    holder.summary = rowView.findViewById(R.id.summary);
                    holder.status = rowView.findViewById(R.id.status);
                    holder.revenue = rowView.findViewById(R.id.revenue);
                    holder.runtime = rowView.findViewById(R.id.runtime);
                    itemHolder = holder;
                }
                break;
                case CREDITS_SECTION_KEY: {
                    rowView = inflater.inflate(R.layout.listview_row_movie_detail_credit_item, parentView, false);
                    CreditItemHolder holder = new CreditItemHolder();
                    holder.thumb = rowView.findViewById(R.id.thumb);
                    holder.name = rowView.findViewById(R.id.name);
                    itemHolder = holder;
                }
                break;
            }
            if(itemHolder != null) {
                rowView.setTag(itemHolder);
            }
        }

        Object o = getItem(position);
        if(rowView.getTag() instanceof GeneralItemHolder && o instanceof MovieDb) {
            MovieDb movieDb = (MovieDb)o;
            GeneralItemHolder generalItemHolder = (GeneralItemHolder)rowView.getTag();
            String posterPath;
            if((posterPath = movieDb.getPosterPath()) != null) {
                generalItemHolder.thumb.setImageURI("https://image.tmdb.org/t/p/w500/" + posterPath);
            }
            generalItemHolder.title.setText(movieDb.getTitle());
            generalItemHolder.releaseDate.setText(movieDb.getReleaseDate());
            generalItemHolder.status.setText(movieDb.getStatus());
            generalItemHolder.revenue.setText(String.valueOf(movieDb.getRevenue()));
            generalItemHolder.runtime.setText(String.valueOf(movieDb.getRuntime()));
            generalItemHolder.vote.setText(movieDb.getVoteAverage() + " " + "(" + movieDb.getVoteCount() + " " + ")");
            generalItemHolder.summary.setText(movieDb.getOverview());
        } else if(rowView.getTag() instanceof CreditItemHolder && o instanceof PersonCast) {
            PersonCast personCast = (PersonCast)o;
            CreditItemHolder creditItemHolder = (CreditItemHolder)rowView.getTag();
            String profile_path;
            if((profile_path = personCast.getProfilePath()) != null) {
                creditItemHolder.thumb.setImageURI("https://image.tmdb.org/t/p/w500" + profile_path);
            }
            creditItemHolder.name.setText(personCast.getCharacter() + " " + "-" + " " + personCast.getName());
        }
        return rowView;
    }

    public void setMovieDb(MovieDb movieDb) {
        this.movieDb = movieDb;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }
}
