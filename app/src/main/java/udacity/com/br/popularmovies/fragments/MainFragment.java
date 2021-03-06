package udacity.com.br.popularmovies.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.adapters.MoviesAdapter;
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.network.FetchMoviesNetwork;
import udacity.com.br.popularmovies.services.MoviesService;


public class MainFragment extends Fragment {

    private GridView mPostersGridView;
    private MoviesAdapter mMoviesAdapter;
    private Movies movie;
    private TextView mNoConnectionMsg;
    private String mPrefChoose;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mPostersGridView = (GridView) view.findViewById(R.id.gridView_movies);
        mNoConnectionMsg = (TextView) view.findViewById(R.id.textView_no_connection_msg);
        mPostersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                movie =  mMoviesAdapter.getMovie(position);
                ((Callback) getActivity())
                        .onItemSelected(movie,mPrefChoose );
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.fragment_main_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mPrefChoose = getString(R.string.pref_order_movies_by_default);

        switch (id){
            case R.id.action_most_popular:
                mPrefChoose = getString(R.string.pref_order_popular_value);
                break;
            case R.id.action_top_rated:
                mPrefChoose = getString(R.string.pref_order_rating_value);
                break;
            case R.id.action_favorite:
                mPrefChoose = getString(R.string.pref_order_favorite);
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getActivity().getString(R.string.pref_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_order_movies_by_key) , mPrefChoose);
        editor.apply();

        updateMovies();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovies();

    }

    private void updateMovies() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity());
        fetchMoviesTask.execute();
    }

    public interface Callback {

        void onItemSelected(Movies movies, String prefChosen);
    }




    @SuppressWarnings("WeakerAccess")
    protected class FetchMoviesTask extends AsyncTask<String, Void, List<Movies>> {


        private Context mContext;

        public FetchMoviesTask() {
        }

        public FetchMoviesTask(Context context) {
            this.mContext = context;
            SharedPreferences sharedPrefs = getActivity().getSharedPreferences(getActivity().getString(R.string.pref_file_key), Context.MODE_PRIVATE);
            mPrefChoose = sharedPrefs.getString(
                    getActivity().getString(R.string.pref_order_movies_by_key),
                    getActivity().getString(R.string.pref_order_movies_by_default));
        }


        @Override
        protected void onPostExecute(List<Movies> movies) {
            super.onPostExecute(movies);

            if(movies == null){

                mNoConnectionMsg.setVisibility(View.VISIBLE);

            } else {
                mNoConnectionMsg.setVisibility(View.GONE);
                mMoviesAdapter = new MoviesAdapter(mContext, movies,mPrefChoose);
                mMoviesAdapter.notifyDataSetChanged();
                mPostersGridView.setAdapter(mMoviesAdapter);
            }

        }


        @Override
        protected List<Movies> doInBackground(String... params) {


            if(mPrefChoose.equals(getString(R.string.pref_order_favorite))){
                MoviesService moviesService = new MoviesService(getActivity());
                return moviesService.getMovies();

            }else{
                FetchMoviesNetwork fetchMoviesNetwork = new FetchMoviesNetwork(getActivity());
                return fetchMoviesNetwork.getMoviesList();
            }


        }
    }

}







