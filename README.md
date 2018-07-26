# Popular Movies

## Description
* Sorted grid view of movie thumbnails from [themoviedb.org API](https://developers.themoviedb.org/3/movies/get-top-rated-movies)
* Sorting allowed via "most popular", "top rated", or "favorites"
* Clicking on thumbnail will open to activity with more details about movie
   * Details include reviews and trailers that when clicked launch the Youtube app
* Allowed favoriting of movies by selecting the star icon on bottom right of movie

## Running the App
Interacting with themoviedb.org requires an API key to be created in order to pull the data to populate the app. To do so, complete the following steps:

1. Create an account at themoviedb.org and request an API key.

2. Place the API key in a created `apikey.properties` file in the root of the app directory in the following format:

```
apikey="<insertYourApiKeyHere>"
```

3. Resync the Gradle build and the app should now be able to pull needed data.

## Screenshots

![verticle example](https://github.com/coreen/popular-movies/blob/master/Vertical_Example.png)

![horizontal example](https://github.com/coreen/popular-movies/blob/master/Horizontal_Example.png)
