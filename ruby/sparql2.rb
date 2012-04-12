# Ruby querying of the BioPortal triplestore using standard the RDF.rb family of libraries
# Documentation for working with the RDF.rb SPARQL client can be found here: http://sparql.rubyforge.org/client/
# The RDF.rb SPARQL client currently requires a monkeypatch to preserve query string parameters.

require 'rubygems'
require 'sparql/client'

apikey = ""

# Monkeypatch to preserve query string parameters
module SPARQL
	class Client
		attr_accessor :url

    def get(query, headers = {}, &block)
      url = self.url.dup
      query_values = url.query_values.nil? || url.query_values.empty? ? {:query => query.to_s} : url.query_values.merge({:query => query.to_s})
      url.query_values = query_values

      request = Net::HTTP::Get.new(url.request_uri, @headers.merge(headers))
      response = @http.request url, request
      if block_given?
				block.call(response)
      else
				response
      end
    end
	end
end

endpoint = SPARQL::Client.new("http://sparql.bioontology.org/sparql/?apikey=#{apikey}")

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
