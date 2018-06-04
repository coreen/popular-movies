# Popular Movies

## Description
* Sorted grid view of movie thumbnails from themoviedb.org API
* Sorting allowed via "most popular" or "top rated"
* Clicking on thumbnail will open to activity with more details about movie

## Running the App
Interacting with themoviedb.org requires an API key to be created in order to pull the data to populate the app. To do so, complete the following steps:

1. Create an account at themoviedb.org and request an API key.

2. Place the API key in a created `apikey.properties` file in the root of the app directory in the following format:

```
apikey="<insertYourApiKeyHere>"
```

3. Resync the Gradle build and the app should now be able to pull needed data.
