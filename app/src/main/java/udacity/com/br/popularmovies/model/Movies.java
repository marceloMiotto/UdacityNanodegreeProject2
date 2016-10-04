package udacity.com.br.popularmovies.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {
    private String mMoviePosterThumbnail;
    private String mOriginalTitle;
    private String mMoviePoster;
    private String mSynopsis;
    private String mUserRating;
    private String mReleaseDate;

    public Movies() {
    }

    public Movies(String moviePosterThumbnail, String originalTitle, String moviePoster, String synopsis, String userRating, String releaseDate) {
        this.mMoviePosterThumbnail = moviePosterThumbnail;
        this.mOriginalTitle = originalTitle;
        this.mMoviePoster = moviePoster;
        this.mSynopsis = synopsis;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
    }

    private Movies(Parcel in) {
        this.mMoviePosterThumbnail = in.readString();
        this.mOriginalTitle = in.readString();
        this.mMoviePoster = in.readString();
        this.mSynopsis = in.readString();
        this.mUserRating = in.readString();
        this.mReleaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mMoviePosterThumbnail);
        dest.writeString(mOriginalTitle);
        dest.writeString(mMoviePoster);
        dest.writeString(mSynopsis);
        dest.writeString(mUserRating);
        dest.writeString(mReleaseDate);

    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {

        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };


    public String getMoviePosterThumbnail() {
        return mMoviePosterThumbnail;
    }

    public void setMoviePosterThumbnail(String mMoviePosterThumbnail) {
        this.mMoviePosterThumbnail = mMoviePosterThumbnail;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getMoviePoster() {
        return mMoviePoster;
    }

    public void setMoviePoster(String mMoviePoster) {
        this.mMoviePoster = mMoviePoster;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String mSynopsis) {
        this.mSynopsis = mSynopsis;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public void setUserRating(String mUserRating) {
        this.mUserRating = mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }
}
