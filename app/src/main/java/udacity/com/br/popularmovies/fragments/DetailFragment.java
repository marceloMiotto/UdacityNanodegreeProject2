package udacity.com.br.popularmovies.fragments;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.adapters.ReviewsAdapter;
import udacity.com.br.popularmovies.adapters.TrailersAdapter;
import udacity.com.br.popularmovies.data.PopularMoviesContract;
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.model.Reviews;
import udacity.com.br.popularmovies.model.Trailers;
import udacity.com.br.popularmovies.network.FetchDetailVideoReviewsNetwork;
import udacity.com.br.popularmovies.services.MoviesService;
import udacity.com.br.popularmovies.util.Constant;
import udacity.com.br.popularmovies.util.Utility;

public class DetailFragment extends Fragment {


    private ShareActionProvider mShareActionProvider;
    private List<Trailers> mTrailers;
    private String mMenuChoose;
    private Movies movies;
    private ImageView mPoster;

    public DetailFragment() {
        // Required empty public constructor
    }


    private Intent createShareMoviesIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //noinspection deprecation
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,Constant.YOUTUBE_LINK+mTrailers.get(0).getKey() );
        return shareIntent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            movies = arguments.getParcelable(Constant.INTENT_MAIN_MOVIE);
            mMenuChoose = arguments.getString(Constant.INTENT_MENU_CHOOSE);
        }else{
            Bundle bundle = getActivity().getIntent().getExtras();
            if(bundle != null) {
                movies = bundle.getParcelable(Constant.INTENT_MAIN_MOVIE);
                mMenuChoose = getActivity().getIntent().getStringExtra(Constant.INTENT_MENU_CHOOSE);
            }
        }

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView mOriginalTitle = (TextView) view.findViewById(R.id.textView_fdetail_original_title);
        TextView mReleaseDate = (TextView) view.findViewById(R.id.textView_fdetail_release_date);
        mPoster = (ImageView) view.findViewById(R.id.imageView_fdetail_poster);
        TextView mSynopsis = (TextView) view.findViewById(R.id.textView_fdetail_synopsis);
        RatingBar mUserRating = (RatingBar) view.findViewById(R.id.ratingBar_fdetail_user_rating);

        TextView textViewTrailer = (TextView) view.findViewById(R.id.textView_trailer_title);
        TextView textViewReview = (TextView) view.findViewById(R.id.textView_review_title);

        setHasOptionsMenu(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (movies != null) {

            mOriginalTitle.setText(movies.getOriginalTitle());
            mReleaseDate.setText(movies.getReleaseDate());
            mSynopsis.setText(movies.getSynopsis());

            mUserRating.setMax(10);
            mUserRating.setRating(Float.valueOf(movies.getUserRating()) / 2);

            if (mMenuChoose.equals(getActivity().getString(R.string.pref_order_favorite))) {
                mPoster.setImageBitmap(Utility.getImage(movies.getPosterImage()));
            } else {
                Picasso.with(getActivity()).load(Constant.TMDB_POSTER_IMG + movies.getMoviePoster()).into(mPoster);
            }


            FetchDetailVideoReviewsNetwork fetchDetail = new FetchDetailVideoReviewsNetwork(getActivity());

            RecyclerView rv = (RecyclerView) view.findViewById(R.id.trailer_container_recycler_view);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setHasFixedSize(true);

            mTrailers = fetchDetail.getTrailers(String.valueOf(movies.getId()));
            if (mTrailers != null) {
                RecyclerView.Adapter mTrailerAdapter = new TrailersAdapter(getActivity(), mTrailers);

                if (mShareActionProvider != null && mTrailers.size()>0) {
                    mShareActionProvider.setShareIntent(createShareMoviesIntent());
                }
                rv.setAdapter(mTrailerAdapter);
            } else {
                rv.setVisibility(View.GONE);
                textViewTrailer.setVisibility(View.GONE);
            }


            RecyclerView rvs = (RecyclerView) view.findViewById(R.id.review_container_recycler_view);
            rvs.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvs.setHasFixedSize(true);

            List<Reviews> mReviews = fetchDetail.getReviews(String.valueOf(movies.getId()));
            if (mReviews != null) {
                RecyclerView.Adapter mReviewAdapter = new ReviewsAdapter(getActivity(), mReviews);
                rvs.setAdapter(mReviewAdapter);
            } else {
                rvs.setVisibility(View.GONE);
                textViewReview.setVisibility(View.GONE);
            }


            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            if (mMenuChoose.equals(getActivity().getString(R.string.pref_order_favorite))) {
                fab.setImageResource(R.drawable.star_remove);
            } else {
                fab.setImageResource(R.drawable.star_add);
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long result;
                    MoviesService moviesService = new MoviesService(getActivity());

                    if (mMenuChoose.equals(getActivity().getString(R.string.pref_order_favorite))) {
                        //Remove
                        result = moviesService.removeMovieFromFavorite(getActivity(), PopularMoviesContract.MovieEntry._ID + " = ?", new String[]{String.valueOf(movies.getSQLId())});
                        if (result > 0) {
                            Toast.makeText(getActivity(), R.string.msg_removed_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.msg_not_removed, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        //Add
                        movies.setPosterImage(Utility.getBytes(((BitmapDrawable) mPoster.getDrawable()).getBitmap()));
                        result = moviesService.addMovieToFavorite(getActivity(), movies);
                        if (result < 0) {
                            Toast.makeText(getActivity(), R.string.msg_favorite_already, Toast.LENGTH_SHORT).show();
                        } else if (result == 0) {
                            Toast.makeText(getActivity(), R.string.msg_error_saving_to_favorite, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.msg_saved_to_favorite, Toast.LENGTH_SHORT).show();
                        }

                    }

                }

            });

        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.detailfragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);


        if(mTrailers != null && mTrailers.size()>0) {
            mShareActionProvider.setShareIntent(createShareMoviesIntent());
        }


    }

}
