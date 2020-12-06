package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class QueryManager {

    public QueryManager() {

    }

    // ** Playlist Queries ** //

    /**
     * Retrieves a list of all playlists in the user's library
     * @param spotifyApi
     * @return a populated Paging<PlaylistSimplified> of any discovered playlists, NULL if
     * error is encountered
     */
    public static ArrayList<PlaylistSimplified> getPlaylistList(SpotifyApi spotifyApi) {
        AuthenticationManager.refreshCredentials(spotifyApi);
        GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                .getListOfCurrentUsersPlaylists()
                .build();

        Paging<PlaylistSimplified> playlistSimplifiedPaging = null;
        try {
            playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return new ArrayList<PlaylistSimplified>(Arrays.asList(playlistSimplifiedPaging.getItems()));

    }

    public static ArrayList<PlaylistSimplified> getTopSpotifyPlaylistList(SpotifyApi spotifyApi) {
        GetListOfFeaturedPlaylistsRequest getListOfFeaturedPlaylistsRequest = spotifyApi.getListOfFeaturedPlaylists().build();

        Paging<PlaylistSimplified> featuredPlaylists = null;
        try {
            featuredPlaylists = getListOfFeaturedPlaylistsRequest.execute().getPlaylists();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }

        if (featuredPlaylists != null) {
            return new ArrayList<PlaylistSimplified>(Arrays.asList(featuredPlaylists.getItems()));
        } else {
            return new ArrayList<PlaylistSimplified>();
        }
    }

    public static ArrayList<PlaylistTrack> getPlaylistTracks(SpotifyApi spotifyApi, String playlistID) {
        GetPlaylistRequest playlistRequest = spotifyApi.getPlaylist(playlistID).build();

        Playlist playlist = null;

        try {
           playlist = playlistRequest.execute();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            System.out.println("Error with the Spotify API, please try again.");
        }

        return new ArrayList<PlaylistTrack>(Arrays.asList( playlist.getTracks().getItems() ));

    }

    public static User getUserInfo(SpotifyApi spotifyApi) {
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile().build();
        User user = null;
        try {
            user = getCurrentUsersProfileRequest.execute();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return user;
    }

}
