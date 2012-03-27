package org.ncbo.stanford.sparql.examples;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * This is a standalone test that uses BioPortal SPARQL Endpoint without any extra libraries. 
 * The result set format is CSV.
 */
public class SimpleTest {
	
	private String service = null;
	private String apikey = null;
	
	public SimpleTest(String service, String apikey) {
		this.service = service;
		this.apikey = apikey;
	}
	
	public String executeQuery(String queryText, String acceptFormat) throws Exception {
		String httpQueryString = String.format("query=%s&apikey=%s", 
			     URLEncoder.encode(queryText, "UTF-8"), 
			     URLEncoder.encode(this.apikey, "UTF-8"));
		
		URL url = new URL(this.service + "?" + httpQueryString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", acceptFormat);

		conn.connect();
		InputStream in = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder buff = new StringBuilder();
		String line = null;
		while ((line = reader.readLine())!=null) {
			buff.append(line);
			buff.append("\n");
		}
		conn.disconnect();
		return buff.toString();
	}
	
	public static void main(String[] args) throws Exception {
		
		String sparqlService = "http://sparql.bioontology.org/sparql";
		String apikey = "YOUR API KEY HERE";

		/*
		 * More query examples here:
		 * http://sparql.bioontology.org/examples
		 */
		String query = "PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#> " +
					   "SELECT ?ont ?name ?acr " +
					   "WHERE { ?ont a omv:Ontology; " +
					   "omv:acronym ?acr; " +
					   "omv:name ?name . " +
					   "}";
		
		SimpleTest test = new SimpleTest(sparqlService, apikey);
		
		//Accept formats can be: "text/plain", "application/json", 
		// "application/rdfxml", "text/csv", text/tab-separated-values
		String response = test.executeQuery(query,"text/tab-separated-values");
		System.out.println(response);
	}
}
