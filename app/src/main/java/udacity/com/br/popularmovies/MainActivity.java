package udacity.com.br.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import udacity.com.br.popularmovies.fragments.DetailFragment;
import udacity.com.br.popularmovies.fragments.MainFragment;
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.util.Constant;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movies_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }


    @Override
    public void onItemSelected(Movies movie, String prefChosen) {

        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(Constant.INTENT_MAIN_MOVIE, movie);
            args.putString(Constant.INTENT_MENU_CHOOSE,prefChosen);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Constant.INTENT_MAIN_MOVIE,movie);
            intent.putExtra(Constant.INTENT_MENU_CHOOSE,prefChosen);
            startActivity(intent);

        }

    }
}
