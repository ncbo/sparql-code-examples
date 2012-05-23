""" Simple Python script to query "http://sparql.bioontology.org/sparql/"
    This script uses the SPARQLWrapper Python lib. Download and install from:
        http://sparql-wrapper.sourceforge.net/
"""

from SPARQLWrapper import SPARQLWrapper, JSON, XML, N3, RDF
import pdb

if __name__ == "__main__":
    sparql_service = "http://sparql.bioontology.org/sparql/"

    #To get your API key register at http://bioportal.bioontology.org/accounts/new
    api_key = "YOUR API KEY"

    #Some sample query.
    query_string = """ 
PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#>

SELECT ?ont ?name ?acr
WHERE { ?ont a omv:Ontology;
             omv:acronym ?acr;
             omv:name ?name .
} 
"""
    sparql = SPARQLWrapper(sparql_service)
    sparql.addCustomParameter("apikey",api_key)
    sparql.setQuery(query_string)
    sparql.setReturnFormat(JSON)
    results = sparql.query().convert()
    for result in results["results"]["bindings"]:
        print result["ont"]["value"], result["name"]["value"], result["acr"]["value"]
