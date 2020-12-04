import flask
from flask import request

app = flask.Flask(__name__)
app.config["DEBUG"] = True
code = ""

@app.route('/callback', methods=['GET'])
def spotifyRedirect():

    code = request.args.get('code')
    error = request.args.get('error')
    print(code)
    if error != None:
        # CASE: user denied access - return error page
        return '''
        <h1>You've denied access to Spotify.</h1>
        <p>The application will not work if access is not given. 
        Please close this webpage and try again.</p>'''

    return '''
    <h1>Success: Access to Spotify granted.</h1>
    <p>You can now close this webpage and return to the console.</p>'''


# A route to return all of the available entries in our catalog.
@app.route('/getcode', methods=['GET'])
def api_all():
    return ""

app.run(host="localhost", port=8888)