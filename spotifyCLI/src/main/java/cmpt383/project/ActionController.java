package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.*;

import java.util.ArrayList;

public class ActionController {

    public static String getPlaylistAccessType(Boolean access) {
        if (access == null) {
            return "N/A";
        } else if (access) {
            return "Public";
        } else {
            return "Private";
        }
    }

    // (1)
    public static void ViewUserPlaylistList(SpotifyApi spotifyApi) {

        // get list of playlists
        ArrayList<PlaylistSimplified> playlists = QueryManager.getPlaylistList(spotifyApi);

        // display query result
        OutputController.outputPlaylistList(playlists);

        // prompt user - view songs in specific playlist, or write CSV of all playlists to disk
        int index = InputManager.promptChoosePlaylistAction(playlists.size());

        if (index == 0) {
            return;
        } else if (index <= playlists.size()) {
            // CASE: user wants to view songs in specific playlist
            ViewPlaylistTracks(spotifyApi, playlists.get(index-1).getId());
        } else if (index == playlists.size() + 1) {
            // TODO CASE: export list of playlists to CSV
        }
    }

    // (1) - 2 / (5) - 2
    public static void ViewPlaylistTracks(SpotifyApi spotifyApi, String playlistID) {
        ArrayList<PlaylistTrack> tracks = QueryManager.getPlaylistTracks(spotifyApi, playlistID);

        OutputController.outputPlaylistTrackList(spotifyApi, tracks);

        // prompt user - return to main menu or export songs to CSV
        int index = InputManager.promptChoosePlaylistTrackAction(1);

        if (index == 0) {
            // CASE: return to main menu
            return;
        } else if (index == 1) {
            // TODO CASE: write track list to CSV
            System.out.println("ERROR: unimplemented.");
        }
    }

    // (2)
    public static void ViewUserTopTracks(SpotifyApi spotifyApi) {
        ArrayList<Track> tracks = QueryManager.getTopUserTracks(spotifyApi);

        OutputController.outputTracks(tracks);

        int input = InputManager.promptChoosePlaylistTrackAction(20);

        if (input == 0) {
            // CASE: return to main menu
            return;
        } else if (input == 1) {
            // TODO CASE: write track list to CSV
            System.out.println("ERROR: unimplemented.");
        }
    }

    // (3)
    public static void ViewTopUserArtists(SpotifyApi spotifyApi) {
        ArrayList<Artist> artists = QueryManager.getTopUserArtists(spotifyApi);

        OutputController.outputArtists(artists);

        int input = InputManager.promptChooseArtistListOption(1);

        if (input == 0) {
            // CASE: return to main menu
            return;
        } else if (input == 1) {
            // TODO CASE: write artist list to CSV
            System.out.println("ERROR: unimplemented.");
        }
    }

    // (4)
    public static void ViewUserProfile(SpotifyApi spotifyApi) {

        User user = QueryManager.getUserInfo(spotifyApi);

        // display account info
        System.out.println("Name: " + user.getDisplayName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Region: " + user.getCountry());
        System.out.println("Followers: " + user.getFollowers().getTotal());
        System.out.println("Account status: " + user.getProduct());
        System.out.println("Spotify ID: " + user.getId());

        InputManager.waitForUserInteraction("Press Enter/Return to return to main menu. > ");
    }

    // (5)
    public static void ViewTopSpotifyPlaylistList(SpotifyApi spotifyApi) {
        ArrayList<PlaylistSimplified> playlists = QueryManager.getTopSpotifyPlaylistList(spotifyApi);

        OutputController.outputPlaylistList(playlists);

        // prompt user - view songs in specific playlist, or write CSV of all playlists to disk
        int index = InputManager.promptChooseTopPlaylistAction(playlists.size());

        if (index == 0) {
            // CASE: return to main menu
            return;
        } else if (index <= playlists.size()) {
            // CASE: user wants to view songs in specific playlist
            ViewPlaylistTracks(spotifyApi, playlists.get(index-1).getId());
        } else if (index == playlists.size() + 1) {
            // TODO CASE: export list of playlists to CSV
        } else if (index == playlists.size() + 2) {
            // CASE: follow playlist publicly
            index = InputManager.promptChoosePlaylistToFollow(playlists.size());
            QueryManager.followPlaylist(spotifyApi, playlists.get(index-1));
        }
    }


}
