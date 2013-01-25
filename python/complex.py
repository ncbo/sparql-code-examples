""" Simple Python script to query "http://alphasparql.bioontology.org/sparql/"
    No extra libraries required.
"""

import json
import urllib2
import urllib
import traceback
import sys 

def query(q,apikey,epr,f='application/json'):
    """Function that uses urllib/urllib2 to issue a SPARQL query.
       By default it requests json as data format for the SPARQL resultset"""

    try:
        params = {'query': q, 'apikey': apikey}
        params = urllib.urlencode(params)
        opener = urllib2.build_opener(urllib2.HTTPHandler)
        request = urllib2.Request(epr+'?'+params)
        request.add_header('Accept', f)
        request.get_method = lambda: 'GET'
        url = opener.open(request)
        return url.read()
    except Exception, e:
        traceback.print_exc(file=sys.stdout)
        raise e

if __name__ == "__main__":
    #Eventually alphasparql will be moved to http://sparql.bioontology.org/sparql/
    sparql_service = "http://alphasparql.bioontology.org/sparql/"

    #To get your API key register at http://bioportal.bioontology.org/accounts/new
    api_key = "73f776e6-e21b-4bce-8420-24f9a3670dbb"


    query_string = """SELECT DISTINCT ?p WHERE { ?s ?p ?o }"""

    #defaults
#PREFERED NAME PROPERTY 
#default: http://www.w3.org/2000/01/rdf-schema#label	
#SYNONYM PROPERTY 
#default: http://www.w3.org/2004/02/skos/core#altLabel	
#DEFINITION PROPERTY 
#default: http://www.w3.org/2004/02/skos/core#definition	
#AUTHOR PROPERTY 
#default: http://purl.org/dc/elements/1.1/creator

    synonymProperty = "synonymProperty"
    documentationProperty = "documentationProperty"
    authorProperty = "authorProperty"
    preferredNameProperty = "preferredNameProperty"
    
    query_string_p = """SELECT DISTINCT ?p WHERE {
    GRAPH <http://bioportal.bioontology.org/ontologies/1132> {
            ?s ?p ?o
        }
    }
    """
    query_string = """SELECT DISTINCT * WHERE {
    GRAPH <http://bioportal.bioontology.org/ontologies/1132> {
            ?s <http://www.w3.org/2000/01/rdf-schema#label> ?o
        }
    }
    """
    query_string = """SELECT DISTINCT * WHERE {
            ?s ?p <http://bioportal.bioontology.org/ontologies/1132> .
    }
    """

    query_string_d = "DESCRIBE <http://purl.obolibrary.org/obo/NCBITaxon_288460>" 

    query_string = """
PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX bio:  <http://bioportal.bioontology.org/metadata/def/> 

SELECT *
WHERE { 
	?ont a omv:Ontology .
    ?ont bio:isVersionOfVirtualOntology ?vrt .
    GRAPH ?vrt {
        ?s a owl:Class .
    }
} LIMIT 10
    """
    json_string = query(query_string, api_key, sparql_service,f="text/plain")
    print json_string
    #resultset=json.loads(json_string)
    #print json.dumps(resultset,indent=1)
