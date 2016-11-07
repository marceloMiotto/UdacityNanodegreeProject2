package udacity.com.br.popularmovies.fragments;


import android.content.Intent;
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
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.model.Trailers;
import udacity.com.br.popularmovies.network.FetchDetailVideoReviewsNetwork;
import udacity.com.br.popularmovies.util.Constant;
import udacity.com.br.popularmovies.util.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    private RecyclerView.Adapter mTrailerAdapter;
    private RecyclerView.Adapter mReviewAdapter;
    private ShareActionProvider mShareActionProvider;
    private List<Trailers> mTrailers;

    public DetailFragment() {
        // Required empty public constructor
    }


    private Intent createShareMoviesIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,Constant.YOUTUBE_LINK+mTrailers.get(0).getKey() );
        return shareIntent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView mOriginalTitle = (TextView) view.findViewById(R.id.textView_fdetail_original_title);
        TextView mReleaseDate = (TextView) view.findViewById(R.id.textView_fdetail_release_date);
        ImageView mPoster = (ImageView) view.findViewById(R.id.imageView_fdetail_poster);
        TextView mSynopsis = (TextView) view.findViewById(R.id.textView_fdetail_synopsis);
        RatingBar mUserRating = (RatingBar) view.findViewById(R.id.ratingBar_fdetail_user_rating);

        setHasOptionsMenu(true);

        Bundle bundle = getActivity().getIntent().getExtras();
        Movies movies = bundle.getParcelable(Constant.INTENT_MAIN_MOVIE);
        String menuChoose = getActivity().getIntent().getStringExtra(Constant.INTENT_MENU_CHOOSE);

        if (movies != null) {

            mOriginalTitle.setText(movies.getOriginalTitle());
            mReleaseDate.setText(movies.getReleaseDate());
            mSynopsis.setText(movies.getSynopsis());

            mUserRating.setMax(10);
            mUserRating.setRating(Float.valueOf(movies.getUserRating()) / 2);

            if(menuChoose.equals(getActivity().getString(R.string.action_favorite))){
                mPoster.setImageBitmap(Utility.getImage(movies.getPosterImage()));
            }else{
                Picasso.with(getActivity()).load(Constant.TMDB_POSTER_IMG + movies.getMoviePoster()).into(mPoster);
            }

        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FetchDetailVideoReviewsNetwork fetchDetail = new FetchDetailVideoReviewsNetwork(getActivity());

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.trailer_container_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);

        mTrailers = fetchDetail.getTrailers(String.valueOf(movies.getId()));
        mTrailerAdapter = new TrailersAdapter(getActivity(),mTrailers);

        if(mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMoviesIntent());
        }

        rv.setAdapter(mTrailerAdapter);

        RecyclerView rvs = (RecyclerView) view.findViewById(R.id.review_container_recycler_view);
        rvs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvs.setHasFixedSize(true);

        mReviewAdapter = new ReviewsAdapter(getActivity(),fetchDetail.getReviews(String.valueOf(movies.getId())));
        rvs.setAdapter(mReviewAdapter);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Test",Toast.LENGTH_SHORT).show();
            }

        });

        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.detailfragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(mTrailers != null) {
            mShareActionProvider.setShareIntent(createShareMoviesIntent());
        }


    }

}
