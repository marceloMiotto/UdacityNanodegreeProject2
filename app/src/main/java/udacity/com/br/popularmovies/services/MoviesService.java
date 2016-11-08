package udacity.com.br.popularmovies.services;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

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
                        ,MovieEntry.COLUMN_MOVIE_ID
                },
                null,
                null,
                null);

        if(movieCursor!=null) {
            movieCursor.moveToFirst();
            if (movieCursor.getCount() != 0) {
                do {
                    movies.add(new Movies(null, movieCursor.getString(1), null, movieCursor.getString(4), movieCursor.getString(3), movieCursor.getString(2), movieCursor.getLong(6), movieCursor.getBlob(5),movieCursor.getLong(0)));

                    Log.e("Debug2","Movies count ");
                } while (movieCursor.moveToNext());

            }
            movieCursor.close();
        }


        return movies;

    }



    public int removeMovieFromFavorite(Context context,String projection, String[] projectionArgs){

        int movieRemoved = context.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                projection,
                projectionArgs
        );
        return movieRemoved;
    }

    public long addMovieToFavorite(Context context, Movies movie) {


        long movieId = 0;

        Cursor movieCursor = context.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                new String[]{MovieEntry._ID},
                MovieEntry.COLUMN_MOVIE_TITLE + " = ?",
                new String[]{movie.getOriginalTitle()},
                null);


        if (movieCursor != null && movieCursor.getCount() > 0) {
            movieCursor.close();
            return -1;

        }else {

            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieEntry.COLUMN_MOVIE_TITLE, movie.getOriginalTitle());
            movieValues.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
            movieValues.put(MovieEntry.COLUMN_MOVIE_RATING, movie.getUserRating());
            movieValues.put(MovieEntry.COLUMN_MOVIE_REVIEW, movie.getSynopsis());
            movieValues.put(MovieEntry.COLUMN_MOVIE_POSTER, movie.getPosterImage());
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());

            Uri insertedUri = context.getContentResolver().insert(
                    MovieEntry.CONTENT_URI,
                    movieValues
            );


            movieId = ContentUris.parseId(insertedUri);

        }


        return movieId;
    }


}
