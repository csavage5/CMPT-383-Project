package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;
import com.wrapper.spotify.requests.data.follow.legacy.FollowPlaylistRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class QueryManager {

    private static AuthenticationManager authManager = new AuthenticationManager();

    public QueryManager() {

    }

    public static void getTokens(SpotifyApi spotifyApi, String userCode) {
        authManager.getInitialCredentials(spotifyApi, userCode);
    }

    // ** Playlist Queries ** //

    /**
     * Retrieves a list of all playlists in the user's library
     * @param spotifyApi
     * @return a populated Paging<PlaylistSimplified> of any discovered playlists, NULL if
     * error is encountered
     */
    public static ArrayList<PlaylistSimplified> getPlaylistList(SpotifyApi spotifyApi) {
        authManager.refreshCredentials(spotifyApi);

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
        authManager.refreshCredentials(spotifyApi);

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
        }

        return new ArrayList<PlaylistSimplified>();

    }

    public static ArrayList<PlaylistTrack> getPlaylistTracks(SpotifyApi spotifyApi, String playlistID) {
        authManager.refreshCredentials(spotifyApi);

        GetPlaylistRequest playlistRequest = spotifyApi.getPlaylist(playlistID).build();

        Playlist playlist = null;

        try {
           playlist = playlistRequest.execute();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            System.out.println("Error with the Spotify API, please try again.");
        }

        if (playlist != null) {
            return new ArrayList<PlaylistTrack>(Arrays.asList( playlist.getTracks().getItems() ));
        }

        return new ArrayList<PlaylistTrack>();

    }

    public static ArrayList<Track> getTopUserTracks(SpotifyApi spotifyApi) {
        authManager.refreshCredentials(spotifyApi);

        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks().build();

        Paging<Track> topTracks = null;

        try {
            topTracks = getUsersTopTracksRequest.execute();
        }  catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            System.out.println("Error: something went wrong with the Spotify API. Please try again.");
        }

        if (topTracks != null) {
            return new ArrayList<Track>(Arrays.asList(topTracks.getItems()));
        }

        return new ArrayList<Track>();
    }

    public static Track getTrackInfo(SpotifyApi spotifyApi, String trackID) {
        authManager.refreshCredentials(spotifyApi);

        GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackID).build();

        Track track = null;

        try {
            track = getTrackRequest.execute();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            System.out.println("Error with the Spotify API, please try again.");
        }

        return track;
    }

    public static User getUserInfo(SpotifyApi spotifyApi) {
        authManager.refreshCredentials(spotifyApi);

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

    public static void followPlaylist(SpotifyApi spotifyApi, PlaylistSimplified playlist) {
        authManager.refreshCredentials(spotifyApi);

        FollowPlaylistRequest followPlaylistRequest = spotifyApi.followPlaylist(
                                                        playlist.getOwner().getId(),
                                                        playlist.getId(),
                                                        true).build();

        String response = "";
        try {
            response = followPlaylistRequest.execute();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            System.out.println("Error with the Spotify API, please try again.");
            return;
        }

        System.out.println("Success!");

    }
}
