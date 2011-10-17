var http = require('http');

var query_string = "PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#>\n" +
"SELECT ?ont ?name ?acr " +
"WHERE { " +
"	?ont a omv:Ontology .  " +
"	?ont omv:acronym ?acr ." +
"	?ont omv:name ?name . " +
" }";
var apikey =  "Your API KEY!";

var options = {
  host: 'alphasparql.bioontology.org',
  port: 8080,
  path: '/sparql?query=' +encodeURIComponent(query_string) + "&apikey=" + encodeURIComponent(apikey),
  headers: {
      'Accept': 'application/json',
  },
  method: 'GET'
};

var req = http.request(options, function(res) {
  
  console.log("Got response: " + res.statusCode);
  console.log('HEADERS: ' + JSON.stringify(res.headers));
  
  var data = "";

  res.on('data', function (chunk) {
    data += chunk; 
  });

  res.on('end', function () {
        var json_res = JSON.parse(data);
        var vars = json_res.head.vars;
        for (i in json_res.results.bindings) {
            var b = json_res.results.bindings[i];
            console.log("\nrow "+i+" :");
            for (j in vars) {
                var v = vars[j];
                console.log(v+"="+b[v].value);
            }
        }
    });

}).on('error', function(e) {
  console.log("Got error: " + e.message);
});
req.end();
