package com.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.data.RequestParams;
import com.popularmovies.data.models.MovieReviewsResponse;
import com.popularmovies.data.models.MoviesVideoResponse;
import static com.popularmovies.data.RequestParams.ADULT;
import static com.popularmovies.data.RequestParams.BACKGROP_PATH;
import static com.popularmovies.data.RequestParams.GENRE_IDS;
import static com.popularmovies.data.RequestParams.ID;
import static com.popularmovies.data.RequestParams.ORIGINAL_LANGUAGE;
import static com.popularmovies.data.RequestParams.ORIGINAL_TITLE;
import static com.popularmovies.data.RequestParams.OVERVIEW;
import static com.popularmovies.data.RequestParams.POPULARITY;
import static com.popularmovies.data.RequestParams.POSTER_PATH;
import static com.popularmovies.data.RequestParams.RELEASE_DATE;
import static com.popularmovies.data.RequestParams.TITLE;
import static com.popularmovies.data.RequestParams.VIDEO;
import static com.popularmovies.data.RequestParams.VOTE_AVERAGE;
import static com.popularmovies.data.RequestParams.VOTE_COUNT;

/**
 * Representation of MovieItem
 */
public class MovieItem implements Parcelable {

    public static final String MOVIE_DATA = "movie_data";

    @SerializedName(ID)
    private long id;
    @SerializedName(POSTER_PATH)
    private String posterPath;
    @SerializedName(ADULT)
    private boolean isAdult;

    @SerializedName(OVERVIEW)
    private String overview;

    @SerializedName(RELEASE_DATE)
    private String releaseDate;

    @SerializedName(GENRE_IDS)
    private String[] genreIds;


    @SerializedName(ORIGINAL_TITLE)
    private String originalTitle;

    @SerializedName(ORIGINAL_LANGUAGE)
    private String originalLanguage;

    @SerializedName(TITLE)
    private String title;

    @SerializedName(BACKGROP_PATH)
    private String backdropPath;

    @SerializedName(POPULARITY)
    private double popularity;
    @SerializedName(VOTE_COUNT)
    private long voteCount;

    @SerializedName(VIDEO)
    private boolean hasVideo;

    @SerializedName(VOTE_AVERAGE)
    private float voteAverage;


    @SerializedName(RequestParams.REVIEWS)
    private MovieReviewsResponse reviews;

    @SerializedName(RequestParams.VIDEOS)
    private MoviesVideoResponse videos;

    public MovieReviewsResponse getReviews() {
        return reviews;
    }

    public MoviesVideoResponse getVideos() {
        return videos;
    }

    private boolean isFavorite;

    public MovieItem() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieItem movieItem = (MovieItem) o;

        return id == movieItem.id && title.equals(movieItem.title);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + title.hashCode();
        return result;
    }

    @SuppressWarnings("WeakerAccess")
    protected MovieItem(Parcel in) {
        id = in.readLong();
        posterPath = in.readString();
        isAdult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        genreIds = in.createStringArray();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readLong();
        hasVideo = in.readByte() != 0;
        voteAverage = in.readFloat();

        isFavorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(posterPath);
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeStringArray(genreIds);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeLong(voteCount);
        dest.writeByte((byte) (hasVideo ? 1 : 0));
        dest.writeFloat(voteAverage);

        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };


    public String getPosterPath() {
        return posterPath;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }
}
