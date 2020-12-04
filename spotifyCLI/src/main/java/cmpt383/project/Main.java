package cmpt383.project;

import com.wrapper.spotify.IHttpManager;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;
//import java.net.http.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URI;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // start SpringBoot server to authenticate account

        System.out.println("hello world");

        SpotifyApi.Builder spotifyApiBuilder = new SpotifyApi.Builder();
        spotifyApiBuilder.setClientId("37641c22bf3c461da365d108d3279158");
        spotifyApiBuilder.setClientSecret("5c996a8371c84b2c89383c037212f1e4");
        spotifyApiBuilder.setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8888/callback"));
        SpotifyApi spotifyApi = spotifyApiBuilder.build();


        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri().build();
        URI uri = authorizationCodeUriRequest.execute();
        System.out.println(uri);

        // TODO Wait for user to confirm their verification
        System.out.println("Press Enter/Return once spotifyCLI has been granted access to a Spotify account.");
        Scanner inputReader = new Scanner(System.in);
        inputReader.next();

        // TODO check value of response from server - accepted (code) or denied (empty)
        SpotifyHttpManager http = new SpotifyHttpManager.Builder().build();
        try {
            String response = http.get(SpotifyHttpManager.makeUri("http://localhost:8888/getcode"), new Header[0]);
            System.out.println("response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //http.
//        spotifyApi.
//
//        IHttpManager httpman = authorizationCodeUriRequest.getHttpManager();
//        //httpman.

        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode("").build();

//        try {
//            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
//
//            // Set access and refresh token for further "spotifyApi" object usage
//            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
//            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
//            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SpotifyWebApiException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }



        //uri.

        //uri.


    }

}
