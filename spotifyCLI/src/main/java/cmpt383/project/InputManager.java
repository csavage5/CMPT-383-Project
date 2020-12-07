package cmpt383.project;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputManager {
    public static Scanner scanner = new Scanner(System.in);

    public InputManager() {

    }

    /**
     * Translates a user inputted integer >= 0 into a single positive integer.
     * @return integer if successful, -1 if input was invalid
     */
    public static int getInputAsInt() {
        String input = "";
        try {
            input = scanner.nextLine();
        } catch (NoSuchElementException e) {

        }

        int ret = -1;
        try {
            if ( (ret = Integer.parseInt(input)) >= 0 );
        } catch (NumberFormatException e) {
            ret = -1;
        }

        return ret;
    }

    public static void waitForUserInteraction(String message) {
        System.out.print(message);
        scanner.nextLine();
    }

    /**
     * Entry point for user to select an operation.
     */
    public static void displayMainMenu() {
        System.out.print(
                "\nPlease choose an option: \n" +
                "1) View your playlists\n" +
                "2) List your top songs\n" +
                "3) List your top artists\n" +
                "4) View your profile information\n" +
                "5) View featured playlists from Spotify\n" +
                "6) Exit program\n" +
                " > ");
    }

    public static int promptChoosePlaylistAction(int max) {

        System.out.print("\nChoose option:\n" +
                "0) Return to main menu\n");

        if (max > 0) {
            System.out.println("1-" + max +
                    ") View songs in playlist\n" +
                    (max + 1) + ") Export this list of playlists to a CSV");
        }

        System.out.print("> ");

        return waitForValidInput(max);
    }

    public static int promptChoosePlaylistTrackAction(int max) {

        System.out.print("\nChoose option:\n" +
                "0) Return to main menu\n" +
                "1) Write tracks to CSV\n");

        System.out.print("> ");

        return waitForValidInput(max);
    }


    // (5)
    public static int promptChooseTopPlaylistAction(int max) {

        System.out.print("\nChoose option:\n" +
                "0) Return to main menu\n");

        if (max > 0) {
            System.out.println("1-" + max +
                    ") View songs in playlist\n" +
                    (max + 1) + ") Export this list of playlists to a CSV\n" +
                    (max + 2) + ") Follow a playlist");
        }

        System.out.print("> ");

        return waitForValidInput(max + 2);
    }

    public static int promptChoosePlaylistToFollow(int max) {
        System.out.print("\nChoose option:\n" +
                "0) Return to main menu\n");

        if (max > 0) {
            System.out.println("1-" + max + ") Follow playlist\n");
        }

        System.out.print("> ");

        return waitForValidInput(max);
    }

    /**
     * Return a valid input > 0
     * @param max the largest integer the input can be
     * @return a valid input
     */
    public static int waitForValidInput(int max) {
        int index = 0;
        while (true) {
            index = InputManager.getInputAsInt();

            if (index == 0) {
                return 0;
            } else if (index > 0 && index <= max) {
                return index;
            } else if (index == max + 1) {
                return index;
            } else {
                System.out.print("Error: invalid input, try again\n> ");
            }
        }
    }

}
