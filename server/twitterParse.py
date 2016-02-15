File Edit Options Buffers Tools Python Help                                                                          
from __future__ import print_function
from alchemyapi import AlchemyAPI
import json
import requests
import sys

alchemyapi = AlchemyAPI()

def parseTwitter(jsonFile)
	response = alchemyapi.sentiment('text', jsonFile)

	if response['status'] == 'OK':                                                            
        	if 'score' in response['docSentiment']:
                	scorestr = str(response['docSentiment']['score'])
                	req.append(scorestr)                                                        
	else:
        	req.append("0.0")

       	return (json.dumps(req))




