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


		 //qexec.addParam("apikey", this.apikey);


	}
	public static void main(String[] args) throws Exception {

		String query = "PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#> " +
					   "SELECT DISTINCT ?ont " +
					   "WHERE { " +
					   "SERVICE <http://stagesparql.bioontology.org/ontologies/sparql/?apikey=73f776e6-e21b-4bce-8420-24f9a3670dbb> { " +
					   "?ont a omv:Ontology; . }" +
					   "" +
					   "}";
		
		JenaARQFederationExample test = new JenaARQFederationExample();
		ResultSet results = test.executeQuery(query);
		    for ( ; results.hasNext() ; ) {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode type = soln.get("ont") ;
		      System.out.println(type);
		    }
	}
}
