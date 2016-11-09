package udacity.com.br.popularmovies.network;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.List;

import udacity.com.br.popularmovies.BuildConfig;
import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.model.Reviews;
import udacity.com.br.popularmovies.model.Trailers;
import udacity.com.br.popularmovies.util.Constant;

public class FetchDetailVideoReviewsNetwork {

    private final String LOG_TAG = FetchDetailVideoReviewsNetwork.class.getSimpleName();

    private Context mContext;


    private FetchDetailVideoReviewsNetwork() {
    }

    public FetchDetailVideoReviewsNetwork(Context context) {
        this.mContext = context;

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public List<Trailers> getTrailers(String movieId) {

        if(isConnected()) {
            try {
                String trailers = getDetailVideoReviewList(movieId, "videos");
                return getTrailersDataFromJson(trailers);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    public List<Reviews> getReviews(String movieId) {
        if(isConnected()) {
            try {
                String reviews = getDetailVideoReviewList(movieId, "reviews");
                return getReviewsDataFromJson(reviews);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    private String getDetailVideoReviewList(String movieId, String videoReviewType) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String videoReviewJsonStr = null;


        if (isConnected()) {

            try {


                Uri.Builder builder = new Uri.Builder();
                builder.scheme(Constant.MOVIE_SCHEME)
                        .authority(Constant.MOVIE_AUTHORITY)
                        .appendPath(Constant.LEVEL_PATH)
                        .appendPath(Constant.DATA_PATH)
                        .appendPath(movieId)
                        .appendPath(videoReviewType)
                        .appendQueryParameter(Constant.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY);

                Log.e("Debug2","link "+builder.build().toString());

                URL url = new URL(builder.build().toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(mContext.getString(R.string.get_request));
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    videoReviewJsonStr = null;
                }
                assert inputStream != null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    videoReviewJsonStr = null;
                }
                videoReviewJsonStr = buffer.toString();
                Log.v(LOG_TAG, videoReviewJsonStr);

            } catch (IOException e) {
                videoReviewJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        videoReviewJsonStr = null;
                    }
                }

            }

            return videoReviewJsonStr;

        } else {
            return null;
        }

    }


    @SuppressWarnings("UnusedAssignment")
    private List<Trailers> getTrailersDataFromJson(String detailJsonStr)
            throws JSONException {

        List<Trailers> mTrailers = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(detailJsonStr);

        JSONArray detailsArray = moviesJson.getJSONArray(Constant.TMDB_RESULTS);


        for (int i = 0; i < detailsArray.length(); i++) {

            JSONObject details = detailsArray.getJSONObject(i);

            String name = details.getString(Constant.TMDB_NAME);
            String key = details.getString(Constant.TMDB_KEY);

            mTrailers.add(new Trailers(name, key));

        }

        return mTrailers;

    }


    @SuppressWarnings("UnusedAssignment")
    private List<Reviews> getReviewsDataFromJson(String detailJsonStr)
            throws JSONException {

        List<Reviews> mReviews = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(detailJsonStr);

        JSONArray detailsArray = moviesJson.getJSONArray(Constant.TMDB_RESULTS);


        for (int i = 0; i < detailsArray.length(); i++) {

            JSONObject details = detailsArray.getJSONObject(i);

            String author = details.getString(Constant.TMDB_AUTHOR);
            String content = details.getString(Constant.TMDB_CONTENT);

            mReviews.add(new Reviews(author, content));

        }

        return mReviews;

    }

}

