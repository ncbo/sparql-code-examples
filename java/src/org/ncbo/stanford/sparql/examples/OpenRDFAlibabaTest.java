package org.ncbo.stanford.sparql.examples;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLConnection;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.openrdf.repository.sparql.config.SPARQLRepositoryConfig;
import org.openrdf.repository.sparql.config.SPARQLRepositoryFactory;

public class OpenRDFAlibabaTest {
	
	private String service = null;
	private String apikey = null;
	private SPARQLConnection conn = null;
	
	public OpenRDFAlibabaTest(String service, String apikey) throws Exception {
		this.service = service;
		this.apikey = apikey;
		
		SPARQLRepositoryConfig config = new SPARQLRepositoryConfig();
		config.setMethod(SPARQLRepositoryConfig.HTTP_GET);
		config.setURL(this.service);
		config.addParameter("apikey", this.apikey);
		
		SPARQLRepositoryFactory factory = new SPARQLRepositoryFactory();
		SPARQLRepository repo = factory.getRepository(config);
		this.conn  = (SPARQLConnection) repo.getConnection();
	}
	
	public TupleQueryResult executeQuery(String queryText) throws Exception {
		
		TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryText);
		return query.evaluate();
	}
	
	public void close() throws RepositoryException {
		this.conn.close();
	}
	public static void main(String[] args) throws Exception {
		
		String sparqlService = "http://localhost:8080/sparql";
		String apikey = "YOUR API KEY HERE";

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
		
		OpenRDFAlibabaTest test = new OpenRDFAlibabaTest(sparqlService, apikey);
		
		TupleQueryResult result = test.executeQuery(query);
		while(result.hasNext()) {
			BindingSet row = result.next();
			System.out.println(row.getBinding("ont").getValue() + " ---- " + 
			row.getBinding("name").getValue() + " ---- " + row.getBinding("acr").getValue());
		}
		test.close();
	}
}
