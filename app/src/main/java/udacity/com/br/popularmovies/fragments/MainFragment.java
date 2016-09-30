package udacity.com.br.popularmovies.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import udacity.com.br.popularmovies.DetailActivity;
import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.SettingsActivity;
import udacity.com.br.popularmovies.adapters.MoviesAdapter;
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.network.FetchMoviesNetwork;
import udacity.com.br.popularmovies.util.Constant;


public class MainFragment extends Fragment {

    GridView mPostersGridView;
    MoviesAdapter mMoviesAdapter;
    Movies movie;



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
        mPostersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                movie =  mMoviesAdapter.getMovie(position);
                Log.d("Debug"," movie: "+movie.getOriginalTitle());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constant.INTENT_MAIN_MOVIE,movie);
                startActivity(intent);
            }
        });

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

        switch (id){
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
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


    protected class FetchMoviesTask extends AsyncTask<String, Void, List<Movies>> {


        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private Context mContext;

        public FetchMoviesTask() {
        }

        public FetchMoviesTask(Context context) {
            this.mContext = context;
        }


        @Override
        protected void onPostExecute(List<Movies> movies) {
            super.onPostExecute(movies);

            mMoviesAdapter = new MoviesAdapter(mContext,20,movies);
            mPostersGridView.setAdapter(mMoviesAdapter);

        }


        @Override
        protected List<Movies> doInBackground(String... params) {
            FetchMoviesNetwork fetchMoviesNetwork = new FetchMoviesNetwork(getActivity());
            return fetchMoviesNetwork.getMoviesList();
        }
    }

}







