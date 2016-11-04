package udacity.com.br.popularmovies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.adapters.ReviewsAdapter;
import udacity.com.br.popularmovies.adapters.TrailersAdapter;
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.network.FetchDetailVideoReviewsNetwork;
import udacity.com.br.popularmovies.util.Constant;
import udacity.com.br.popularmovies.util.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    private RecyclerView.Adapter mTrailerAdapter;
    private RecyclerView.Adapter mReviewAdapter;

    public DetailFragment() {
        // Required empty public constructor
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


        FetchDetailVideoReviewsNetwork fetchDetail = new FetchDetailVideoReviewsNetwork(getActivity());

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.trailer_container_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);


        mTrailerAdapter = new TrailersAdapter(getActivity(),fetchDetail.getTrailers(String.valueOf(movies.getId())));
        rv.setAdapter(mTrailerAdapter);

        RecyclerView rvs = (RecyclerView) view.findViewById(R.id.review_container_recycler_view);
        rvs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvs.setHasFixedSize(true);

        mReviewAdapter = new ReviewsAdapter(getActivity(),fetchDetail.getReviews(String.valueOf(movies.getId())));
        rvs.setAdapter(mReviewAdapter);

        return view;

    }

}
