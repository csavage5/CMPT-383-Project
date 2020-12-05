package cmpt383.project;

import com.wrapper.spotify.IHttpManager;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;


import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // build API object
        SpotifyApi.Builder spotifyApiBuilder = new SpotifyApi.Builder();
        spotifyApiBuilder.setClientId(AuthenticationManager.CLIENT_ID);
        spotifyApiBuilder.setClientSecret(AuthenticationManager.CLIENT_SECRET);
        spotifyApiBuilder.setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8888/callback"));
        SpotifyApi spotifyApi = spotifyApiBuilder.build();

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
        System.out.println(uri);

        // TODO attempt to open the web browser

        // wait for user to confirm their verification
        System.out.print("Press Enter/Return when spotifyCLI has been granted access to a Spotify account. > ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // TODO check value of response from server - accepted (code) or denied ("access_denied")
        SpotifyHttpManager http = new SpotifyHttpManager.Builder().build();
        String response = "";
        try {
            response = http.get(SpotifyHttpManager.makeUri("http://localhost:8888/getcode"), new Header[0]);
            System.out.println("response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // TODO put in loop, repeat until user accepts
        switch (response) {
            case "access_denied":
                // CASE: user did not authorize
                System.out.println("Error: this application was not authorized with Spotify. Please try again.");
                return;
                //break;

            case "":
                // CASE: user did not open authorization link, or did not interact with the page
                System.out.println("Error: did not receive a new authorization code. Please try again.");
                return;
                //break;

            default :
                break;
        }


        // retrieve authorization and refresh tokens using code in response
        AuthenticationManager authManager = new AuthenticationManager(response);
        authManager.getInitialCredentials(spotifyApi);

//        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(response).build();
//        try {
//            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
//            // Set access and refresh token for further "spotifyApi" object usage
//            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
//            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
//            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SpotifyWebApiException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        // get data from Spotify using access and refresh tokens
        AuthenticationManager.refreshCredentials(spotifyApi);
        GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                .getListOfCurrentUsersPlaylists()
//          .limit(10)
//          .offset(0)
                .build();

        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();
            //playlistSimplifiedPaging
            System.out.println("Total playlists: " + playlistSimplifiedPaging.getTotal());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
