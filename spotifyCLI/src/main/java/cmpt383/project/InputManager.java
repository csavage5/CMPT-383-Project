package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;

import java.util.Arrays;
import java.util.Scanner;

public class InputManager {
    Scanner scanner = new Scanner(System.in);

    public InputManager() {

    }

    /**
     * Translates a user inputted integer >= 0 into a single positive integer.
     * @param input value to evaluate
     * @return integer if successful, -1 if input was invalid
     */
    private int translateInputToInt(String input) {
        int ret = -1;
        try {
            if ( (ret = Integer.parseInt(input)) >= 0 );
        } catch (NumberFormatException e) {
            ret = -1;
        }

        return ret;
    }

    public void waitForUserInteraction(String message) {
        System.out.print(message);
        scanner.nextLine();
    }

    /**
     * Entry point for user to select an operation.
     * @return TRUE if main menu should appear again,
     *      * FALSE if program should terminate.
     */
    public Boolean mainMenu(SpotifyApi spotifyApi) {
        System.out.print(
                "Please choose an option: \n" +
                "1) View your playlists\n" +
                "2) List your top songs\n" +
                "3) List your top artists\n" +
                "4) View your profile information\n" +
                "5) View featured playlists from Spotify\n" +
                "6) Exit program\n" +
                " > ");

        String input = scanner.nextLine();

        switch(translateInputToInt(input)) {
            case 1:
                Paging<PlaylistSimplified> playlists = QueryManager.getPlaylistList(spotifyApi);
                System.out.println(Arrays.toString(playlists.getItems()));
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                System.out.println("Exiting program...");
                return false;
            default:
                System.out.println("Error: invalid selection.");
                return true;
        }

        return true;

    }

}
