import requests
import urllib
import json
from flask import Flask
from flask import request
from alchemyapi import AlchemyAPI

alchemyapi = AlchemyAPI()

def parseTwitter(jsonFile):
        response = alchemyapi.sentiment('text', jsonFile)

        if response['status'] == 'OK':
                if 'score' in response['docSentiment']:
                        scorestr = str(response['docSentiment']['score'])
                        jsonFile.append(scorestr)
                else:
                        jsonFile.append("0.0")
        else:
                jsonFile.append("0.0")

        return (json.dumps(jsonFile))

app = Flask(__name__)

headers = {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8', "Authorization":"Basic Nk5RdWRJb1Y1SmNaY3NmRFp4MmdZUVhSNjprVmRjMXFoTFBrQVhjbk5PdWxHQWcwNmV6QjFBNkRZemxNODhhNzFnSnpDOWw3bEZkTg=="}
r = requests.post('https://api.twitter.com/oauth2/token', headers = headers, data={"grant_type":"client_credentials"})

x = r.json()['access_token']


@app.route("/api")
def hello():
    global x
    query = urllib.quote_plus(request.args.get('q'))
    url = 'https://api.twitter.com/1.1/search/tweets.json?count=20&q=' + query
    headers2 = {"Authorization":"Bearer " + x}
    r2 = requests.get(url, headers=headers2)
    j = r2.json()
    statuses = j['statuses']
    response = [];
    for el in statuses:
 
        response.append(el['text'])

    return parseTwitter(response);

@app.route("/")
def home():
    return "This is the site for our barcode scanner. Thanks Twitter."


if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True)







