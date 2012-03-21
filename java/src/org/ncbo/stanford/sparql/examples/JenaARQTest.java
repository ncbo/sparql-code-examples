package org.ncbo.stanford.sparql.examples;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

/**
 * This is an example built on top of the Jena ARQ library.
 * See: http://jena.sourceforge.net/ARQ/documentation.html
 */
public class JenaARQTest {
	
	private String service = null;
	private String apikey = null;
	
	public JenaARQTest(String service, String apikey) {
		this.service = service;
		this.apikey = apikey;
	}
	public ResultSet executeQuery(String queryString) throws Exception {
		 Query query = QueryFactory.create(queryString) ;

		 QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(this.service, query);
		 qexec.addParam("apikey", this.apikey);
		 ResultSet results = qexec.execSelect() ;
		 return results;

	}
	public static void main(String[] args) throws Exception {
		String sparqlService = "http://sparql.bioontology.org/sparql";
		String apikey = "YOUR API KEY";

		/*
		 * More query examples here:
		 * http://alphasparql.bioontology.org/examples
		 */
		String query = "PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#> " +
					   "SELECT ?ont ?name ?acr " +
					   "WHERE { ?ont a omv:Ontology; " +
					   "omv:acronym ?acr; " +
					   "omv:name ?name . " +
					   "}";
		
		JenaARQTest test = new JenaARQTest(sparqlService,apikey);
		ResultSet results = test.executeQuery(query);
		    for ( ; results.hasNext() ; ) {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode ontUri = soln.get("ont") ;
		      Literal name = soln.getLiteral("name") ;
		      Literal acr = soln.getLiteral("acr") ;
		      System.out.println(ontUri + " ---- " + name + " ---- " + acr);
		    }
	}
}
