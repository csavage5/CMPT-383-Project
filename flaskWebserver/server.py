import flask
from flask import request

app = flask.Flask(__name__)
app.config["DEBUG"] = True

# Must be a dictionary, a String doesn't maintain state
code = {}

@app.route('/', methods=['GET'])
def testPage():
    return '''
    <h1>Test page<h1>
    '''

@app.route('/callback', methods=['GET'])
def spotifyRedirect():

    code["code"] = (request.args.get('code'))
    error = request.args.get('error')
    if error != None:
        # CASE: user denied access - return error page
        code["code"] = error
        print("received error: " + code["code"])
        return '''
        <h1>You've denied access to Spotify.</h1>
        <p>The application will not work if access is not given. 
        Please close this webpage and try again.</p>'''

    print("received code: " + code["code"])

    return '''
    <h1>Success: Access to Spotify granted.</h1>
    <p>You can close this webpage and return to the console.</p>'''

# Send code to Java app
@app.route('/getcode', methods=['GET'])
def getAccessCode():
    accessCode = code["code"]
    print("sending code: " + code["code"])
    code["code"] = ""
    return accessCode

app.run(host="0.0.0.0", port=8888)