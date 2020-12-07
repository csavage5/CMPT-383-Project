package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.*;

import java.util.ArrayList;

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
    public static void ViewUserPlaylistList(SpotifyApi spotifyApi) {

        // get list of playlists
        ArrayList<PlaylistSimplified> playlists = QueryManager.getPlaylistList(spotifyApi);

        int index = 1;
        //display columns
        System.out.printf("%-6s %-55s %-7s %-25s %-12s %-22s\n","Index", "Playlist Name", "Tracks", "Owner", "Access Type", "SpotifyID");
        //System.out.println(String.format("Index   Tracks   Owner   Access Type   Name   SpotifyID"));
        for (PlaylistSimplified playlist : playlists) {

            System.out.printf("%-6s %-55s %-7s %-25s %-12s %-22s\n",
                    index + ")",
                    playlist.getName(),
                    playlist.getTracks().getTotal(),
                    playlist.getOwner().getDisplayName(),
                    getPlaylistAccessType(playlist.getIsPublicAccess()),
                    playlist.getId());

            index += 1;
        }

        // prompt user - view songs in specific playlist, or write CSV of all playlists to disk
        index = InputManager.promptChoosePlaylistAction(playlists.size());

        if (index == 0) {
            return;
        } else if (index <= playlists.size()) {
            // CASE: user wants to view songs in specific playlist
            ViewPlaylistTracks(spotifyApi, playlists.get(index-1).getId());
        } else if (index == playlists.size() + 1) {
            // TODO CASE: export list of playlists to CSV
        }
    }

    // (1) - 2
    public static void ViewPlaylistTracks(SpotifyApi spotifyApi, String playlistID) {
        ArrayList<PlaylistTrack> tracks = QueryManager.getPlaylistTracks(spotifyApi, playlistID);

        System.out.printf("%-6s %-60s %-25s %-40s %-5s %-12s %-22s\n","Index", "Title","Artist", "Album", "Duration (s)", "Popularity", "SpotifyID");

        int index = 1;
        Track track;
        for (PlaylistTrack pTrack : tracks) {
            // query more info about each pTrack

            if (pTrack.getIsLocal()) {
                // CASE: track is a local file
                System.out.println(index + ") " +
                        pTrack.getTrack().getName() + " [LOCAL TRACK - CANNOT QUERY MORE INFO]");
                index += 1;
                continue;
            }

            track = QueryManager.getTrackInfo(spotifyApi, pTrack.getTrack().getId());
            //TODO deal with tracks that have multiple artists
            System.out.printf("%-6s %-60s %-25s %-40s %-5s %-12s %-22s\n",
                    index,
                    track.getName(),
                    track.getArtists()[0].getName(),
                    track.getAlbum().getName(),
                    track.getDurationMs()/1000,
                    track.getPopularity(),
                    track.getId());

            index += 1;
        }

        // prompt user - return to main menu or export songs to CSV
        //index = InputManager.displayPlaylistTracks(playlists.size());

//        if (index == 0) {
//            return;
//        } else if (index <= playlists.size()) {
//            // CASE: user wants to view songs in specific playlist
//            ViewPlaylistTracks(spotifyApi, playlists.get(index-1).getId());
//        } else if (index == playlists.size() + 1) {
//            // TODO CASE: user wants to export list of playlists to CSV
//        }

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
    public static void EnterSpotifyTopPlaylistsMenu(SpotifyApi spotifyApi) {
    }

}
