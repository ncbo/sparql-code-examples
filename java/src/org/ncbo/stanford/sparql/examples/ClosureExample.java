package org.ncbo.stanford.sparql.examples;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

/**
 * This is an example built on top of the Jena ARQ library.
 * See: http://jena.sourceforge.net/ARQ/documentation.html
 */
public class ClosureExample {
	
	private String service = null;
	private String apikey = null;
	
	public ClosureExample(String service, String apikey) {
		this.service = service;
		this.apikey = apikey;
	}
	public ResultSet executeQuery(String queryString,String rules) throws Exception {
		 Query query = QueryFactory.create(queryString) ;

		 QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(this.service, query);
		 qexec.addParam("apikey", this.apikey);
		 if (rules != null)
			 qexec.addParam("rules", rules);
		 ResultSet results = qexec.execSelect() ;
		 return results;

	}
	public ResultSet executeQuery(String queryString) throws Exception {
		return executeQuery(queryString,null);

	}
	public static void main(String[] args) throws Exception {
		String sparqlService = "http://sparql.bioontology.org/sparql";
		String apikey = "YOUR API KEY";

		/*
		 * More query examples here:
		 * http://sparql.bioontology.org/examples
		 */
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				"SELECT DISTINCT * " +
				"FROM  <http://bioportal.bioontology.org/ontologies/SNOMEDCT> WHERE { " +
				"<http://purl.bioontology.org/ontology/SNOMEDCT/409019001> rdfs:subClassOf ?super . }";
		
		ClosureExample test = new ClosureExample(sparqlService,apikey);
		
		/**
		 * Direct super classes no rules are passed with the query
		 */
		ResultSet results = test.executeQuery(query);
		String textResults = ResultSetFormatter.asText(results);
		System.out.println(textResults);
		
		/**
		 * Same query with hierarchy closure.
		 * We need to add the parameter "rules" with the code SUBC
		 * SUBC enables rdfs:subClassOf closures to the SPARQL query.
		 */
		results = test.executeQuery(query,"SUBC");
		textResults = ResultSetFormatter.asText(results);
		System.out.println(textResults);
		
		/**
		 * prefNames and closures.
		 * This example uses the Nano Particle Ontology. 
		 * This ontology has its own property to record preferred names
		 * that property is subPropertyOf skos:prefLabel 
		 * 
		 * For this query we need to enable to rules:
		 * 		(1) SUBC for subClassOf hierarchies
		 * 		(2) SUBP for subPropertyOf
		 * Also we need to include the GLOBALS graph
		 * Since the subPropertyOf statements are contained in it.
		 */
		String queryLabelsAndClosure = 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" +
				"SELECT DISTINCT * " +
				"FROM  <http://bioportal.bioontology.org/ontologies/NPO>" +
				"FROM  <http://bioportal.bioontology.org/ontologies/globals>" +
				" WHERE { " +
				"<http://purl.bioontology.org/ontology/npo#NPO_446> rdfs:subClassOf ?super . " +
				"?super skos:prefLabel ?label }";
		results = test.executeQuery(queryLabelsAndClosure,"SUBC+SUBP");
		textResults = ResultSetFormatter.asText(results);
		System.out.println(textResults);
	}
}
