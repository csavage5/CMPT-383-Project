package cmpt383;

import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;

import java.util.ArrayList;

public class OutputController {

    public static String PLAYLIST_HEADER = "%6s %-55s %-7s %-25s %-12s %-22s\n";
    public static String TRACK_HEADER = "%6s %-60s %-25s %-50s %-13s %-12s %-22s\n";
    public static String ARTIST_HEADER = "%6s %-25s %-12s %-12s %-22s\n";

    public static void outputPlaylistList(ArrayList<PlaylistSimplified> playlists) {
        int index = 1;
        //display columns
        System.out.printf(PLAYLIST_HEADER,"Index", "Playlist Name", "Tracks", "Owner", "Access Type", "SpotifyID");
        //System.out.println(String.format("Index   Tracks   Owner   Access Type   Name   SpotifyID"));
        for (PlaylistSimplified playlist : playlists) {

            System.out.printf(PLAYLIST_HEADER,
                    index + ") ",
                    playlist.getName(),
                    playlist.getTracks().getTotal(),
                    playlist.getOwner().getDisplayName(),
                    ActionController.getPlaylistAccessType(playlist.getIsPublicAccess()),
                    playlist.getId());

            index += 1;
        }
    }

    public static void outputTracks(ArrayList<Track> tracks, ArrayList<PlaylistTrack> localTracks) {
        System.out.printf(TRACK_HEADER,"Index", "Title", "Artist", "Album", "Duration (s)", "Popularity", "SpotifyID");
        int index = 1;
        for (Track track : tracks) {
            //TODO deal with tracks that have multiple artists
            System.out.printf(TRACK_HEADER,
                    index + ") ",
                    track.getName(),
                    track.getArtists()[0].getName(),
                    track.getAlbum().getName(),
                    track.getDurationMs() / 1000,
                    track.getPopularity(),
                    track.getId());

            index += 1;
        }

        for (PlaylistTrack localTrack : localTracks) {
            System.out.printf("6s %-60s",
                    index + ") ",
                    localTrack.getTrack().getName() + " [LOCAL TRACK - CANNOT QUERY MORE INFO]");
        }

    }

    public static void outputArtists(ArrayList<Artist> artists) {
        System.out.printf(ARTIST_HEADER,"Index", "Artist", "Followers", "Popularity", "SpotifyID");
        int index = 1;
        for (Artist artist : artists) {

            System.out.printf(ARTIST_HEADER,
                    index + ") ",
                    artist.getName(),
                    artist.getFollowers().getTotal(),
                    artist.getPopularity(),
                    artist.getId());

            index += 1;
        }
    }

}
