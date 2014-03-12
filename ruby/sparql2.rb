# Ruby querying of the BioPortal triplestore using standard the RDF.rb family of libraries
# Documentation for working with the RDF.rb SPARQL client can be found here: https://github.com/ruby-rdf/sparql-client

require 'rubygems'
require 'sparql/client'

apikey = ""

endpoint = SPARQL::Client.new("http://sparql.bioontology.org/sparql/?apikey=#{apikey}", {method: :get})

query = <<-EOS
PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#>

SELECT ?ont ?name ?acr
WHERE { ?ont a omv:Ontology;
             omv:acronym ?acr;
             omv:name ?name .
} LIMIT 100
EOS

results = endpoint.query(query)

results.each do |statement|
	puts statement.inspect
end
