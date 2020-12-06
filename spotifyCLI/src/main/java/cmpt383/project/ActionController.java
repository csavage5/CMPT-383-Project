package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ActionController {

    private static String getPlaylistAccessType(Boolean access) {
        if (access) {
            return "Public";
        } else if (access == null) {
            return "N/A";
        } else {
            return "Private";
        }
    }

    // (1)
    public static void EnterPlaylistMenu(SpotifyApi spotifyApi) {

        // get list of playlists
        ArrayList<PlaylistSimplified> playlists = QueryManager.getPlaylistList(spotifyApi);

        int index = 1;
        //display columns
        System.out.println(String.format("Index   Tracks   Owner   Access Type   Name   SpotifyID"));
        for (PlaylistSimplified playlist : playlists) {
            System.out.println(index + ") " +
                    playlist.getName() + " " +
                    playlist.getTracks().getTotal() + " " +
                    getPlaylistAccessType(playlist.getIsPublicAccess())
            );
            index += 1;
        }

        // prompt user - view songs in specific playlist, or write CSV of all playlists to disk
        index = InputManager.displayPlaylistTracks(playlists.size());

        if (index == 0) {
            return;
        } else if (index <= playlists.size()) {
            // CASE: user wants to view songs in specific playlist
            ViewPlaylistTracks(spotifyApi, playlists.get(index-1).getId());
        } else if (index == playlists.size() + 1) {
            // TODO CASE: user wants to export list of playlists to CSV
        }
    }

    // (1) - 2
    public static void ViewPlaylistTracks(SpotifyApi spotifyApi, String playlistID) {
        ArrayList<PlaylistTrack> tracks = QueryManager.getPlaylistTracks(spotifyApi, playlistID);

        System.out.println(String.format("Index   Tracks   Owner   Access Type   Name   SpotifyID"));
        int index = 1;
        for (PlaylistTrack track : tracks) {
            System.out.println(index + ") " +
                    track.getTrack().getName());
            index += 1;
        }

    }


    // (4)
    public static void EnterUserProfileMenu(SpotifyApi spotifyApi) {

        User user = QueryManager.getUserInfo(spotifyApi);

        // display account info
        System.out.println("Name: " + user.getDisplayName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Region: " + user.getCountry());
        System.out.println("Followers: " + user.getFollowers().getTotal());
        System.out.println("Account status: " + user.getProduct());
        System.out.println("Spotify ID: " + user.getId());
    }

    // (5)
    public static void EnterSpotifyTopPlaylistsMenu(SpotifyApi spotifyApi) {
    }

}
