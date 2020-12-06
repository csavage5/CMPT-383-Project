package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class QueryManager {

    public QueryManager() {

    }

    // ** Playlist Queries **

    /**
     * Retrieves a list of all playlists in the user's library
     * @param spotifyApi
     * @return a populated Paging<PlaylistSimplified> of any discovered playlists, NULL if
     * error is encountered
     */
    public static Paging<PlaylistSimplified> getPlaylistList(SpotifyApi spotifyApi) {
        AuthenticationManager.refreshCredentials(spotifyApi);
        GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                .getListOfCurrentUsersPlaylists()
//          .limit(10)
//          .offset(0)
                .build();

        Paging<PlaylistSimplified> playlistSimplifiedPaging = null;
        try {
            playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();
            //playlistSimplifiedPaging.getItems()
            //System.out.println("Total playlists: " + playlistSimplifiedPaging.getTotal());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return playlistSimplifiedPaging;
    }

    public static int getPlaylistCount(Paging<PlaylistSimplified> playlist) {
        return playlist.getTotal();
    }

    public static Playlist getPlaylist(SpotifyApi spotifyApi, String playlistID) {
        GetPlaylistRequest playlistRequest = spotifyApi.getPlaylist(playlistID).build();

        Playlist playlist = null;

        try {
           playlist = playlistRequest.execute();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            System.out.println("Error with the Spotify API, please try again.");
        }

        return playlist;

    }

}
