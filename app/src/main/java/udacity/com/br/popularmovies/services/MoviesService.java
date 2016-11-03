package udacity.com.br.popularmovies.services;


import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import udacity.com.br.popularmovies.data.PopularMoviesContract.MovieEntry;
import udacity.com.br.popularmovies.model.Movies;

public class MoviesService {


    private Context mContext;

    public MoviesService(Context context){
        this.mContext = context;
    }

    public ArrayList<Movies> getMovies(){

        ArrayList<Movies> movies = new ArrayList<>();

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                new String[]{MovieEntry._ID
                        ,MovieEntry.COLUMN_MOVIE_TITLE
                        ,MovieEntry.COLUMN_MOVIE_RELEASE_DATE
                        ,MovieEntry.COLUMN_MOVIE_RATING
                        ,MovieEntry.COLUMN_MOVIE_REVIEW
                        ,MovieEntry.COLUMN_MOVIE_POSTER
                },
                null,
                null,
                null);

        movieCursor.moveToFirst();
        if (movieCursor.getCount() != 0) {
            do {
                movies.add(new Movies(null, movieCursor.getString(1), null, movieCursor.getString(4), movieCursor.getString(3),movieCursor.getString(2),movieCursor.getLong(0),movieCursor.getBlob(5)));

            } while (movieCursor.moveToNext());

        }

        movieCursor.close();

        return movies;

    }


}
