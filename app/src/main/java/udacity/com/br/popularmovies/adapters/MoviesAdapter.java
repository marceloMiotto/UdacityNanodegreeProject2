package udacity.com.br.popularmovies.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.util.Constant;

public class MoviesAdapter extends BaseAdapter {
    private final Context mContext;
    private final int     mMoviesCount;
    private final List<Movies> mMovies;

    public MoviesAdapter(Context c,  int moviesCount, List<Movies> moviesArray) {

        mContext = c;
        mMoviesCount = moviesCount;
        mMovies      = moviesArray;

    }

    public Movies getMovie(int position){
        return mMovies.get(position);
    }
    public int getCount() {
        return mMoviesCount;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_item_main,parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        if(mMovies!= null) {
            Picasso.with(mContext).load(Constant.TMDB_POSTER_THUMBNAIL_IMG + mMovies.get(position).getMoviePoster()).into(viewHolder.imageView);
        }


        return view;
    }

    private class ViewHolder{
        private final ImageView imageView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.imageView_gridView_item);
        }

    }
}
