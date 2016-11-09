package udacity.com.br.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.model.Reviews;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.DataObjectHolder> {


    private List<Reviews> mReviews;
    private Context mContext;


public class DataObjectHolder extends RecyclerView.ViewHolder  {

    final TextView reviewName;
    final TextView reviewContent;



    public DataObjectHolder(View itemView) {
        super(itemView);
        reviewName = (TextView) itemView.findViewById(R.id.review_name_id);
        reviewContent = (TextView) itemView.findViewById(R.id.review_content_id);
    }

}

    public ReviewsAdapter() {
    }

    public ReviewsAdapter(Context mContext,List<Reviews> reviews) {
        this.mContext = mContext;
        this.mReviews = reviews;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_fragment_detail, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.reviewName.setText(String.format(mContext.getString(R.string.name_review_item), mReviews.get(position).getAuthor()));
        holder.reviewContent.setText(mReviews.get(position).getContent());
    }


    @Override
    public int getItemCount() {
        return mReviews.size();
    }

}
