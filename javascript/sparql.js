SPARQL = function(o) {
  this.query = function(q) {
    return $.ajax({
      url: o.endpoint,
      accepts: {json: "application/sparql-results+json"},
      data: {query: q, apikey: o.apikey},
      dataType: "json"
    });
  };
};


