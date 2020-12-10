# CMPT 383 Final Project

## Overall Goals

This project interfaces with the Spotify Web API to display information about a Spotify account in a command-line interface. The user can view their Spotify playlists and tracks within, top artists and tracks, etc and then export that data to JSON and CSV file formats. Users can also view the top Spotify playlists and add them to their library.

The files will be saved to ./spotifyCLIOutput.

## Utilized Languages

Java: interfaces with Spotify web API, interactive component for user

Python: runs a Flask web server to capture the authentication code that Spotify sends once the user approves this application for use with their account

R: converts a JSON outputted by Java to a CSV.

# Inter-language Communication

Java -> Python: called via an HTTP GET request to retrieve the Spotify authentication code

Java -> R: called via a command execution in the Java runtime to convert a JSON to a CSV

## Starting the Project

To build the project and download dependencies:

    docker compose build

This project runs two containers: one is a Flask web server, and another is an interactive Java CLI. I couldn't get the docker-compose command to allow STDIN for the Java container, so starting up requires two docker-compose run commands:

   docker-compose run --publish 8888:8888 --detach flask_webserver
   docker-compose run spotify_cli_client

The second command will send the user to an interactive Java CLI, where they will be asked to copy a link into their web browser (the web browser won't launch when the Java application is run inside a Docker container). From here, the user can approve the app with a Spotify account. If the user doesn't have a Spotify account, I have made a demo account:

    username: cmpt383project@gmail.com
    password: demoAccount

It will authenticate with whichever account is currently logged into Spotify on your web browser.

In case Spotify wants extra verification when you log in, the linked email credentials:

    email:    cmpt383project@gmail.com
    password: demoAccount

## Using Project

Once the user has allowed access, they will be presented with a success webpage. This webpage sends a POST request to the local webserver, capturing an authentication code from the Spotify web API.

The user will be asked to return to the Java application, which will wait until you press Enter before attempting to retrieve the code from the webserver. This code is then exchanged with Spotify for a refresh token and temporary access token.

The user must follow the Spotify authorization link each time the Java app is restarted, since the authentication code and tokens aren't written to disk. If the account being used is the same across restarts, the link will instantly send the user to the success page since the application has already been approved to be used with that account.

## Project Features

The user can view information about their account:
    - List of followed playlists
      - List of tracks in each playlist
    - Top songs the user listens to
    - Top artists the user listens to
    - Basic profile information
    - Spotify's featured playlists
      - Can follow any listed featured playlist

Now the user can select options to view information in these categories. The user can choose to write out the information they are presented with to a CSV file, which will be saved to the ./spotifyCLIOutput directory. Each time a new CSV is written out, it will overwrite any file with the name "spotifyCLI_output.csv", or create a new file if one doesn't exist.



<!-- Topic idea:
    - CLI Spotify client
        - view playlist, track, other info
        - download playlist info into CSV
        - upload CSV into playlist

Languages:
    - Python for a Flask web server
  
    - Java for for CLI interface, writing to JSON, Spotify API interaction

    - R for converting JSON files to CSV files

The two inter-language communication methods:
    - REST API - Java sends an HTTP request to the Python Flask server to retrieve the Spotify API authentication code
    - Java runtime execution: run the R script within the Java VM


Deployment technology: Docker containers -->
