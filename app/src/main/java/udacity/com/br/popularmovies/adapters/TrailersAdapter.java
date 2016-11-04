package udacity.com.br.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.model.Trailers;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.DataObjectHolder>{

    private Context mContext;
    private List<Trailers> mTrailers;

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button trailerButton;

        public DataObjectHolder(View itemView) {
            super(itemView);
            trailerButton = (Button) itemView.findViewById(R.id.trailer_item_button);
            trailerButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            //TODO call youtube

        }
    }

    public TrailersAdapter() {
    }

    public TrailersAdapter(Context mContext,List<Trailers> trailers) {
        this.mContext = mContext;
        this.mTrailers = trailers;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer_fragment_detail, parent, false);
        return new DataObjectHolder(view);

    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.trailerButton.setText(mTrailers.get(position).getName());
    }


    @Override
    public int getItemCount() {

        return mTrailers.size();
    }

}



