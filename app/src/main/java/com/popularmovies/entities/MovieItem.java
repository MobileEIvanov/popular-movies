package com.popularmovies.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A dummy item representing a piece of content.
 */
public class MovieItem {

    private String imageUrl;


    public MovieItem(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static List<MovieItem> generateMovies(){
        List<MovieItem> movieItems = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            movieItems.add(new MovieItem("https://picsum.photos/200/300/?random"));
        }
        return movieItems;
    }
}
