package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.io.IOException;

public class AuthenticationManager {

    public static String CLIENT_ID = "37641c22bf3c461da365d108d3279158";
    public static String CLIENT_SECRET = "5c996a8371c84b2c89383c037212f1e4";

    private float timeAtLastRefresh = 0;       // in milliseconds
    private float lastAccessTokenLifespan = 0; // in milliseconds
    private Calendar cal = Calendar.getInstance();
    //String _userCode;

    public AuthenticationManager() {
        //_userCode = userCode;

    }

    private void updateRefreshTime(int NewAccessTokenLifespan) {
        timeAtLastRefresh = cal.getTimeInMillis();
        lastAccessTokenLifespan = NewAccessTokenLifespan * 1000;
    }

    public void getInitialCredentials(SpotifyApi spotifyApi, String userCode) {
        //System.out.println("User code: " + _userCode);
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(userCode).build();
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Save access and refresh tokens in spotifyApi
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            //System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());

            this.updateRefreshTime(authorizationCodeCredentials.getExpiresIn());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void refreshCredentials(SpotifyApi spotifyApi) {

        if (cal.getTimeInMillis() > ( timeAtLastRefresh + (lastAccessTokenLifespan * 0.8) ) ) {
            // CASE: current access token has not elapsed at least 80% of its lifespan - do not replace it
            return;
        }

        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

            // Set new access token
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());

            // update expiration time
            this.updateRefreshTime(authorizationCodeCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void saveNewTokens(SpotifyApi spotifyApi) {

    }

}
