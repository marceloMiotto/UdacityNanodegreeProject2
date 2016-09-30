package udacity.com.br.popularmovies.network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import udacity.com.br.popularmovies.BuildConfig;
import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.util.Constant;


public class FetchMoviesNetwork {

    private final String LOG_TAG = FetchMoviesNetwork.class.getSimpleName();
    private List<Movies> mMoviesList;

    private Context mContext;

    public FetchMoviesNetwork() {
    }

    public FetchMoviesNetwork(Context context) {
        this.mContext = context;

    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    public List<Movies> getMoviesList() {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;


        if(isConnected()){

            try {

                SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(mContext);
                String orderMoviesBy = sharedPrefs.getString(
                        mContext.getString(R.string.listPreference_key),
                        mContext.getString(R.string.pref_order_movies_by_default));

                Uri.Builder builder = new Uri.Builder();
                builder.scheme(Constant.MOVIE_SCHEME)
                        .authority(Constant.MOVIE_AUTHORITY)
                        .appendPath(Constant.LEVEL_PATH)
                        .appendPath(Constant.DATA_PATH)
                        .appendPath(orderMoviesBy)
                        .appendQueryParameter(Constant.PAGE_PARAM, "1")
                        .appendQueryParameter(Constant.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY);

                Log.d("Debug","movies :"+builder.build().toString());


                URL url = new URL(builder.build().toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(mContext.getString(R.string.get_request));
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    moviesJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    moviesJsonStr = null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, moviesJsonStr);

            } catch (IOException e) {
                moviesJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        moviesJsonStr = null;
                    }
                }

            }

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            return null;
        }
        return null;
    }

    private String getReadableDateString(String time) {

        SimpleDateFormat fromDate = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat toDate   = new SimpleDateFormat("mm-dd-yyyy");

        try {
            return toDate.format(fromDate.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error";
        }

    }

    private List<Movies> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        mMoviesList = new ArrayList<>();
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(Constant.TMDB_RESULTS);

        String[] moviesStr = new String[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {

            JSONObject movies = moviesArray.getJSONObject(i);

            String movieOriginalTitle = movies.getString(Constant.TMDB_ORIGINAL_TITLE);
            String moviePosterPath    = movies.getString(Constant.TMDB_POSTER_PATH);
            String movieReleaseDate   = getReadableDateString(movies.getString(Constant.TMDB_RELEASE_DATE));
            String movieId            = movies.getString(Constant.TMDB_ID);
            String movieUserRating    = movies.getString(Constant.TMDB_USER_RATING);
            String movieSynopsis      = movies.getString(Constant.TMDB_SYNOPSIS);

            Log.d("Debug","movie: "+movieOriginalTitle);

            mMoviesList.add(new Movies(moviePosterPath,movieOriginalTitle, moviePosterPath, movieSynopsis, movieUserRating, movieReleaseDate));

        }

        return mMoviesList;

    }


}
