
Topic idea:
    - CLI Spotify client
        - control music playback on other devices
        - download playlist info into CSV
        - upload CSV into playlist

Languages:
    - Python for a Flask web server
  
    - Java for for CLI interface, writing to JSON, Spotify API interaction

    - R for converting JSON files to CSV files

The two inter-language communication methods:
    - REST API - Java sends an HTTP request to the Python Flask server to retrieve the Spotify API authentication code

    - Java runtime execution: run the R script within the Java VM


Deployment technology: Docker containers
