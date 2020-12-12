package cmpt383;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;

import java.awt.*;
import java.io.IOException;
import java.net.URI;


// Code Citations
//      SpotifyApi usage: https://github.com/thelinmichael/spotify-web-api-java/blob/master/README.md
//      Spotify authentication flow: https://developer.spotify.com/documentation/general/guides/authorization-guide/#authorization-code-flow


public class Main {

    public static void main(String[] args) {

        // build API object
        SpotifyApi.Builder spotifyApiBuilder = new SpotifyApi.Builder();
        spotifyApiBuilder.setClientId(AuthenticationManager.CLIENT_ID);
        spotifyApiBuilder.setClientSecret(AuthenticationManager.CLIENT_SECRET);

        // tell Spotify to send the code to the Python Flask web server
        spotifyApiBuilder.setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8888/callback"));
        SpotifyApi spotifyApi = spotifyApiBuilder.build();

        // request permissions
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri().scope(
                "user-top-read " +
                "user-read-recently-played " +
                "user-read-playback-state " +
                "playlist-modify-public " +
                "user-modify-playback-state " +
                "playlist-modify-private " +
                "user-read-currently-playing " +
                "user-library-modify " +
                "user-read-playback-position " +
                "playlist-read-private " +
                "user-read-private " +
                "user-library-read " +
                "playlist-read-collaborative").build();

        URI uri = authorizationCodeUriRequest.execute();

        System.out.println("Welcome to spotifyCLI!\n");

        // attempt to open the web browser
        System.out.println("Attempting to launch web browser...");
        try {
            // from https://stackoverflow.com/questions/5226212/how-to-open-the-default-webbrowser-using-java
            if (Desktop.isDesktopSupported()) {
                // OS that supports Java Desktop API
                Desktop.getDesktop().browse(uri);
            } else {
                // attempt to open on an OS that doesn't
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("xdg-open " + uri);
            }
        } catch (IOException e) {
            System.out.println("Failed to open web browser.");
        }

        System.out.println("If your web browser didn't launch automatically, " +
                           "please copy and paste this link into your browser:\n");
        System.out.println(uri);

        // wait for user to confirm they've granted access to their account

        SpotifyHttpManager http = new SpotifyHttpManager.Builder().build();
        String response = "";

        while (response == "") {
            InputManager.waitForUserInteraction("\nPress Enter/Return when spotifyCLI has been granted access to a Spotify account. > ");

            try {
                // retrieve code caught by Python Flask web server
                response = http.get(SpotifyHttpManager.makeUri("http://localhost:8888/getcode"), new Header[0]);
                //System.out.println("response: " + response);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error: can't reach local web server on port 8888.");
            } catch (SpotifyWebApiException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            switch (response) {
                case "access_denied":
                    // CASE: user did not authorize
                    System.out.println("Error: this application was not authorized with Spotify. Please try again.");
                    break;

                case "":
                    // CASE: user did not open authorization link, or did not interact with the page
                    System.out.println("Error: did not receive a new authorization code from the web server. Please try again.");
                    break;

                default :
                    System.out.println("Success: retrieved authentication code from the web server.\n");
                    break;
            }

        }

        int counter = 0;
        while (!QueryManager.getTokens(spotifyApi, response) && counter < 5){
            if (counter == 4) {
                System.out.println("Error: cannot retrieve access and refresh tokens from Spotify. Please restart the program.");
                return;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter += 1;
        }

        System.out.println("Success: retrieved access and refresh tokens from Spotify.");

        // start main menu
        while (true) {
            InputManager.displayMainMenu();
            switch(InputManager.getInputAsInt()) {
                case 1:
                    ActionController.ViewUserPlaylistList(spotifyApi);
                    break;
                case 2:
                    ActionController.ViewUserTopTracks(spotifyApi);
                    break;
                case 3:
                    ActionController.ViewUserTopArtists(spotifyApi);
                    break;
                case 4:
                    ActionController.ViewUserProfile(spotifyApi);
                    break;
                case 5:
                    ActionController.ViewTopSpotifyPlaylistList(spotifyApi);
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Error: invalid selection, please try again.");
            }
        }


    }

}
