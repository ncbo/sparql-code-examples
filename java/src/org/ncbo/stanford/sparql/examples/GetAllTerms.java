package org.ncbo.stanford.sparql.examples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.engine.http.HttpQuery;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class GetAllTerms {
	
	 
	
	private static String KEY = "YOUR KEY";
	private static String SERVICE = "http://sparql.bioontology.org/sparql/";
	
	/* warning: do not increment PAGE size until POST is supported in the SPARQL endpoint
	 * if > 65 it can hit URL lenght limits.
	 */
	private static int PAGE_SIZE = 65; /* WARNING KEEP UNDER 65 UNTIL FURTHER NOTICE */
	
	private static String PREFIXES = "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n" + 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
			"PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n" + 
			"PREFIX meta: <http://bioportal.bioontology.org/metadata/def/> \n" + 
			"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" + 
			"PREFIX umls: <http://bioportal.bioontology.org/ontologies/umls/>\n";
	
	private static String ALL_URIS = "SELECT DISTINCT ?uri \n" + 
			"WHERE { GRAPH <$GRAPH> {\n" + 
			"    ?uri a owl:Class .\n" + 
			"}}"; /* To play with a smaller extraction include 'LIMIT 500' */
	
	
	public static ResultSet executeQuery(String queryString) throws Exception {
		 Query query = QueryFactory.create(queryString) ;
		 HttpQuery.urlLimit = 100000;
		 QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(SERVICE, query);
		 qexec.addParam("apikey", KEY);
		 ResultSet results = qexec.execSelect() ;
		 return results;
	}
	
	public static List<Resource> getAllOntologyIRIs(String ontologyGraph) throws Exception {
		String query = PREFIXES + ALL_URIS.replace("$GRAPH", ontologyGraph);
		ResultSet rs = executeQuery(query);
		List<Resource> uris = new ArrayList<Resource>();
		while (rs.hasNext()) {
			QuerySolution sol = rs.next();
			uris.add(sol.get("uri").asResource());
		}
		return uris;
	}
	
	
	public static String getQueryTermBatch(List<Resource> resources, String[] properties) {
		
		StringBuilder query = new StringBuilder();
		query.append(PREFIXES);
		query.append("SELECT * WHERE {\n");
		
		
		/* property binding for the query */
		for (String prop : properties) {
			String varName = prop.split(":")[1]; /* lets use the fragment after ':' as the var name */ 
			query.append("?uri " + prop + " ?"+varName + " .\n");
		}

		/* we limit the query to the resources in the batch. For that we use FILTER and || */
		query.append("FILTER(\n");
		for (int i = 0; i < resources.size(); i++) {
			query.append("?uri = <" + resources.get(i).toString() + "> ");
			if (i+1 < resources.size())
				query.append(" ||\n");
		}
		query.append(") }\n");

		return query.toString();
	}
	
	
	public static void getTermBatch(List<Resource> resources, String[] properties) throws Exception {
		String queryBatch = getQueryTermBatch(resources,properties);
		
		ResultSet rs = executeQuery(queryBatch);
		while (rs.hasNext()) {
			QuerySolution sol = rs.next();
			
			/**
			 * TODO: your code here ... do something with this term.
			 * 
			 * The sample code below just prints the term information.
			 */
			/* Remove comments to print resultset

			Iterator<String> varnames = sol.varNames();
			System.out.print("Term Data : ");
			while (varnames.hasNext()) {
				String var = varnames.next();
				if (!sol.get(var).isLiteral())
					System.out.print(var+"="+sol.get(var).toString()+"   ");
				else
					System.out.print(var+"="+sol.get(var).asLiteral().getValue().toString()+"   ");
				
			} 
			System.out.println();
			*/
		}
	}	
	
	
	public static void traverseAllTerms(List<Resource> uris,String[] properties) throws Exception {
		List<Resource> batch = new ArrayList<Resource>();
		Queue<Resource> queueResources = new LinkedList<Resource>(uris);
		while(queueResources.size() > 0) {
			batch.add(queueResources.poll());
			if (batch.size() % PAGE_SIZE == 0) {
				getTermBatch(batch,properties);
				batch = new ArrayList<Resource>();
			}
			if (queueResources.size() % 1000 == 0) {
				System.out.println("Count Down " + queueResources.size());
			}
		}
		if (batch.size() > 0) {
			getTermBatch(batch,properties);
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		long t0 = System.currentTimeMillis();
		/**
		 * This is the graph where SNOMEDCT is placed in the triple store.
		 * The first example in http://sparql.bioontology.org/examples
		 * shows a query that retrieves all the different graphs with their ontology IDs. 
		 */
		String snomedGraph = "http://bioportal.bioontology.org/ontologies/SNOMEDCT";
		
		/**
		 * These are the two predicates we want to bind when we 
		 * traverse over all the terms in the ontology.
		 * 
		 * Other properties can be included i.e: skos:prefLabel, 
		 * skos:altLabel, rdfs:subClassOf
		 */
		String[] properties = { "umls:cui", "skos:notation" };
		
		
		/**
		 *  Step 1: Get all the ontology Term IRIs.
		 */
		List<Resource> allURIs = getAllOntologyIRIs(snomedGraph);
		
		/**
		 * Step 2: Get the property data for the term IRIs in batches of PAGE_SIZE 
		 */
		traverseAllTerms(allURIs,properties);

		System.out.printf("%d terms retrieved in %.2f s\n",allURIs.size(),(System.currentTimeMillis() - t0)/1000.0);
	}
}
