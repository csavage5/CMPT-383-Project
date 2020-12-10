package cmpt383;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wrapper.spotify.model_objects.specification.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class JSONUtility {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();


    // *** POJOs for converting tracks & playlists to JSON
    static class jsonPlaylist {
        String name;
        Integer tracks;
        String owner;
        String accessType;
        String spotifyID;

        public jsonPlaylist(PlaylistSimplified playlist) {
            this.name = playlist.getName();
            this.tracks = playlist.getTracks().getTotal();
            this.owner = playlist.getOwner().getDisplayName();
            this.accessType = ActionController.getPlaylistAccessType(playlist.getIsPublicAccess());
            this.spotifyID = playlist.getId();
        }
    }

    static class jsonTrack {
        String name;
        String artist;
        String album;
        Integer popularity;
        String spotifyID;

        public jsonTrack(Track track) {
            this.name = track.getName();
            this.artist = track.getArtists()[0].getName();
            this.album = track.getAlbum().getName();
            this.popularity = track.getPopularity();
            this.spotifyID = track.getId();
        }
    }

    static class jsonArtist {
        String name;
        Integer followers;
        Integer popularity;
        String spotifyID;

        public jsonArtist(Artist artist) {
            this.name = artist.getName();
            this.followers = artist.getFollowers().getTotal();
            this.popularity = artist.getPopularity();
            this.spotifyID = artist.getId();
        }
    }


    // *** Converter static methods

    private static void writeObjectsToJSONFile(Collection objects) {
        try {
            FileWriter writer = new FileWriter("/app/out/output.json");
            gson.toJson(objects, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Runtime.getRuntime().exec("Rscript /app/CSVConverter.R");
        } catch (IOException e) {
            System.out.println();
        }

    }

    public static void ConvertPlaylistsToJSON(ArrayList<PlaylistSimplified> playlists)  {
        ArrayList<jsonPlaylist> jsonPlaylists = new ArrayList<>();

        // convert playlists received from Spotify API to simplified, JSONable objects
        for (PlaylistSimplified playlist : playlists) {
            jsonPlaylists.add(new jsonPlaylist(playlist));
        }

        writeObjectsToJSONFile(jsonPlaylists);

    }

    public static void ConvertTracksToJSON(ArrayList<Track> tracks)  {
        ArrayList<jsonTrack> jsonTracks = new ArrayList<>();

        // convert Tracks received from Spotify API to simplified, JSONable objects
        for (Track track : tracks) {
            jsonTracks.add(new jsonTrack(track));
        }

        writeObjectsToJSONFile(jsonTracks);
    }

    public static void ConvertArtistsToJSON(ArrayList<Artist> artists)  {
        ArrayList<jsonArtist> jsonTracks = new ArrayList<>();

        // convert Artists received from Spotify API to simplified, JSONable objects
        for (Artist artist : artists) {
            jsonTracks.add(new jsonArtist(artist));
        }

        writeObjectsToJSONFile(jsonTracks);
    }


}
