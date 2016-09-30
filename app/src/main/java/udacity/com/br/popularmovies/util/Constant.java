package udacity.com.br.popularmovies.util;


public interface Constant {

    //api url
    String MOVIE_SCHEME = "http";
    String MOVIE_AUTHORITY = "api.themoviedb.org";
    String LEVEL_PATH = "3";
    String DATA_PATH = "movie";
    String API_KEY_PARAM = "api_key";
    String PAGE_PARAM    = "page";


    //JSON Object
    String TMDB_RESULTS = "results";
    String TMDB_POSTER_PATH = "poster_path";
    String TMDB_RELEASE_DATE = "release_date";
    String TMDB_ORIGINAL_TITLE = "original_title";
    String TMDB_ID = "id";
    String TMDB_USER_RATING = "vote_average";
    String TMDB_SYNOPSIS = "overview";


    //poster path
    String TMDB_POSTER_THUMBNAIL_IMG = "http://image.tmdb.org/t/p/w185//";
    String TMDB_POSTER_IMG = "http://image.tmdb.org/t/p/w342//";

    //intents
    String INTENT_MAIN_MOVIE = "main movie";

}
