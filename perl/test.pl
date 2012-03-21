#!/usr/bin/perl

use sparql;
use strict;

my $endpoint = "http://sparql.bioontology.org/sparql";
my $apikey = "YOUR API KEY HERE";

my $query_string = <<END;
PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#>
SELECT ?ont ?name ?acr
WHERE { 
	?ont a omv:Ontology .
	?ont omv:acronym ?acr .
	?ont omv:name ?name . 
} 
END

my $sparql = sparql->new();
my $res = $sparql->query($endpoint.'?apikey='.$apikey,$query_string);

for my $row (@{$res}) {
    for my $col (keys %{$row}) {
        print('?'.$col.' = '.$row->{$col}."\n");
    }
    print("\n");
}
