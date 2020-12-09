package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.model_objects.IPlaylistItem;
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
            // CASE: export list of playlists to CSV
            JSONUtility.ConvertPlaylistsToJSON(playlists);
            // TODO call JavaScript utility to convert to CSV

        }
    }

    // (1) - 2 / (5) - 2
    public static void ViewPlaylistTracks(SpotifyApi spotifyApi, String playlistID) {
        ArrayList<PlaylistTrack> pTracks = QueryManager.getPlaylistTracks(spotifyApi, playlistID);

        ArrayList<Track> tracks = new ArrayList<>();
        ArrayList<PlaylistTrack> localTracks = new ArrayList<>();

        // retrieve extended track information
        System.out.println("Retrieving extended track information for playlist...");
        Track track;
        for (PlaylistTrack pTrack : pTracks) {
            // get track information
            if (pTrack.getTrack().getType() == ModelObjectType.EPISODE) {
                // do not support podcast episodes, only TRACKS
                continue;
            }

            track = (Track) pTrack.getTrack();
            tracks.add(track);
        }

        // print out tracks
        OutputController.outputTracks(tracks, localTracks);

        // prompt user - return to main menu or export songs to CSV
        int index = InputManager.promptChoosePlaylistTrackAction(1);

        if (index == 0) {
            // CASE: return to main menu
            return;
        } else if (index == 1) {
            // CASE: write track list to CSV

            // write to JSON file
            JSONUtility.ConvertTracksToJSON(tracks);

            // TODO call JavaScript utility to convert to CSV
        }
    }

    // (2)
    public static void ViewUserTopTracks(SpotifyApi spotifyApi) {
        ArrayList<Track> tracks = QueryManager.getTopUserTracks(spotifyApi);

        OutputController.outputTracks(tracks, new ArrayList<>());

        int input = InputManager.promptChoosePlaylistTrackAction(1);

        if (input == 0) {
            // CASE: return to main menu
            return;
        } else if (input == 1) {
            // CASE: write track list to CSV
            JSONUtility.ConvertTracksToJSON(tracks);
            // TODO call JavaScript utility to convert to CSV

        }
    }

    // (3)
    public static void ViewUserTopArtists(SpotifyApi spotifyApi) {
        ArrayList<Artist> artists = QueryManager.getTopUserArtists(spotifyApi);

        OutputController.outputArtists(artists);

        int input = InputManager.promptChooseArtistListOption(1);

        if (input == 0) {
            // CASE: return to main menu
            return;
        } else if (input == 1) {
            // CASE: write artist list to CSV
            JSONUtility.ConvertArtistsToJSON(artists);
            // TODO call JavaScript utility to convert to CSV

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
            // CASE: export list of playlists to CSV
            JSONUtility.ConvertPlaylistsToJSON(playlists);
            // TODO call JavaScript utility to convert to CSV

        } else if (index == playlists.size() + 2) {
            // CASE: follow playlist publicly
            index = InputManager.promptChoosePlaylistToFollow(playlists.size());
            QueryManager.followPlaylist(spotifyApi, playlists.get(index-1));
        }
    }


}
