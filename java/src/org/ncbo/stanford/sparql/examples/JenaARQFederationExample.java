package org.ncbo.stanford.sparql.examples;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.DatasetImpl;

/**
 * This is an example built on top of the Jena ARQ library.
 * See: http://jena.sourceforge.net/ARQ/documentation.html
 */
public class JenaARQFederationExample {
	
	public JenaARQFederationExample() {
	}
	public ResultSet executeQuery(String queryString) throws Exception {

		 QueryExecution exec =  QueryExecutionFactory.create(QueryFactory.create(queryString), new
				 DatasetImpl(ModelFactory.createDefaultModel()));
		 return exec.execSelect();

	}
	public static void main(String[] args) throws Exception {
		
		/**
		 * For documentation please read "Federated SPARQL queries"
		 *  at http://www.bioontology.org/wiki/index.php/SPARQL_BioPortal 
		 */
		JenaARQFederationExample test = new JenaARQFederationExample();

		String query = "PREFIX map: <http://protege.stanford.edu/ontologies/mappings/mappings.rdfs#> \n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
				"SELECT DISTINCT ?mappedParent WHERE {\n" + 
				"    SERVICE <http://sparql.bioontology.org/mappings/sparql/?apikey=YOUR API KEY HERE> {\n" + 
				"        ?mapping map:target <http://purl.bioontology.org/ontology/CSP/0468-5952> .\n" + 
				"        ?mapping map:source ?source .\n" + 
				"    }\n" + 
				"    SERVICE <http://sparql.bioontology.org/ontologies/sparql/?apikey=YOUR API KEY HERE> {\n" + 
				"        ?source rdfs:subClassOf ?mappedParent .\n" +
				"    }\n" +
				"}";
		
		ResultSet results = test.executeQuery(query);
	    for ( ; results.hasNext() ; ) {
	      QuerySolution soln = results.nextSolution() ;
	      System.out.println(soln);
	    }
	}
}
