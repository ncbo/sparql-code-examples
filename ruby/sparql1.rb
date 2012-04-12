# Ruby querying of the BioPortal triplestore using standard Ruby libraries and the JSON rubygem

require 'rubygems'
require 'json'
require 'open-uri'
require 'cgi'

apikey = ""
endpoint = "http://sparql.bioontology.org/sparql/"
query = <<-EOS
PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#>

SELECT ?ont ?name ?acr
WHERE { ?ont a omv:Ontology;
             omv:acronym ?acr;
             omv:name ?name .
} LIMIT 100
EOS

# Returns a JSON object with the results of a SPARQL query
def query(query, apikey, endpoint, accept = "application/json")
	json = open("#{endpoint}?query=#{CGI.escape(query)}&apikey=#{apikey}", "Accept" => accept).read
	JSON.parse(json)

end

puts JSON.pretty_generate(query(query, apikey, endpoint))
