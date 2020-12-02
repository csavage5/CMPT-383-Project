package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.net.URI;

public class Main {

    public static void main(String[] args) {
        System.out.println("hello world");

        SpotifyApi.Builder spotifyApiBuilder = new SpotifyApi.Builder();
        spotifyApiBuilder.setClientId("37641c22bf3c461da365d108d3279158");
        spotifyApiBuilder.setClientSecret("5c996a8371c84b2c89383c037212f1e4");
        spotifyApiBuilder.setRedirectUri(SpotifyHttpManager.makeUri(""));
        SpotifyApi spotifyApi = spotifyApiBuilder.build();

        //spotifyApi.
        //spotifyApi.setAccessToken("");
    }

}
