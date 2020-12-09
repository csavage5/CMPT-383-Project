package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.AbstractModelObject;
import com.wrapper.spotify.model_objects.IModelObject;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.AbstractRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;
import com.wrapper.spotify.requests.data.follow.legacy.FollowPlaylistRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import com.wrapper.spotify.requests.data.tracks.GetSeveralTracksRequest;
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

    /**
     * Retrieves initial refresh and access tokens from Spotify using the code given to the web server
     * when the user grants access. Saves tokens in spotifyApi.
     * @param spotifyApi
     * @param userCode
     */
    public static Boolean getTokens(SpotifyApi spotifyApi, String userCode) {
        return authManager.getInitialCredentials(spotifyApi, userCode);
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

        GetListOfFeaturedPlaylistsRequest getListOfFeaturedPlaylistsRequest = spotifyApi.getListOfFeaturedPlaylists().limit(50).build();

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

        GetPlaylistsItemsRequest playlistItemsRequest = null;
        Paging<PlaylistTrack> playlist = null;
        ArrayList<PlaylistTrack> items = new ArrayList<PlaylistTrack>();
        int offset = 0;

        do {
            playlistItemsRequest = spotifyApi.getPlaylistsItems(playlistID)
                    .additionalTypes("track")
                    .offset(offset)
                    .build();

            try {
                playlist = playlistItemsRequest.execute();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } catch (SpotifyWebApiException e) {
                System.out.println("Error with the Spotify API, please try again.");
            }

            if (playlist != null) {
                items.addAll(Arrays.asList(playlist.getItems()));
            } else {
                break;
            }

            if (offset == 0) {
                offset = 101;
            } else {
                offset += 100;
            }

        } while(playlist.getNext() != null);

        return items;

    }

    public static ArrayList<Track> getTopUserTracks(SpotifyApi spotifyApi) {
        authManager.refreshCredentials(spotifyApi);

        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks().limit(50).build();

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

    public static ArrayList<Artist> getTopUserArtists(SpotifyApi spotifyApi) {
        authManager.refreshCredentials(spotifyApi);

        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists().limit(50).build();

        Paging<Artist> topArtists = null;

        try {
           topArtists = getUsersTopArtistsRequest.execute();
        }  catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            System.out.println("Error: something went wrong with the Spotify API. Please try again.");
        }

        if (topArtists != null) {
            return new ArrayList<Artist>(Arrays.asList(topArtists.getItems()));
        }

        return new ArrayList<Artist>();
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
