/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.github.jsonldjava.core.RDFDataset.Quad;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import static java.lang.System.in;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.Lock;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;
import org.apache.jena.tdb.sys.TDBInternal;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.util.FileManager;

/**
 *
 * @author APP-Riad.Belmahi
 */
public class OpenOWL {

    public static void main(String args[]) {


        //TestOpenOwl();
        testLoan();
        

    }

    static String s;

    // Open a connection to MonFichierOwl.OWL
    static OntModel OpenConnectOWL() {

        //OntModel mode = null;
        String pathOntologie = "/Users/loannguyen/Downloads/AXIS_MoM_WebService-master/sources/model/axis-csrm-datamodel-MoM.owl";
        OntModel mode = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
        java.io.InputStream in = FileManager.get().open(pathOntologie);  //MyFile
        

        //test
        if (in == null) {
            throw new IllegalArgumentException("Pas de base de connaissance");  // there i no file to connect
        }
        return (OntModel) mode.read(in, "");
    }

    // jena.query.ResultSet  return
    static ResultSet ExecSparQl(String Query) {

        Query query = QueryFactory.create(Query);

        QueryExecution qe = QueryExecutionFactory.create(query, OpenConnectOWL());
        ResultSet results = qe.execSelect();

        return results;  // Return jena.query.ResultSet 

    }

    // String return (convert jena.query.ResultSet  to String)
    static String ExecSparQlString(String Query) {
        try {
            Query query = QueryFactory.create(Query);

            QueryExecution qe = QueryExecutionFactory.create(query, OpenConnectOWL());

            ResultSet results = qe.execSelect();

            // Test
            if (results.hasNext()) {

                // if iS good 
                ByteArrayOutputStream go = new ByteArrayOutputStream();
                ResultSetFormatter.out((OutputStream) go, results, query);
                //  String s = go.toString();
                s = new String(go.toByteArray(), "UTF-8");
            } // not okay
            else {

                s = "rien";
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OpenOWL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;   // return  jena.query.ResultSet  as string 
    }

    // example using  jena.query.ResultSet  
    public static void TestOpenOwl() {  // method

       //OntModel model = OpenOWL.OpenConnectOWL();
      // System.out.println("");  // get the activity from my File OWL 
       String queryString;
       
       queryString = "PREFIX axis: <http://titan.be/axis-csrm/datamodel/ontology/0.3#>" +
                    "CONSTRUCT WHERE { ?s axis:uses ?o}";
        /* queryString = "PREFIX axis: <http://titan.be/axis-csrm/datamodel/ontology/0.1#>"
                + "SELECT  ?hasRepresentation "
                + "where { ?y axis:aperture ?hasRepresentation.}";
          */      
        // call method ExecSparQl from class OpenOWL
        // ExecSparQl return a Resultset 
        String results = OpenOWL.ExecSparQlString(queryString);
        
        System.out.println("le resultat"+ results);

    }
    
    
    private static final String UPDATE_TEMPLATE = 
            "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
            + "INSERT DATA"
            + "{ <http://example/%s>    dc:title    \"A new book\" ;"
            + "                         dc:creator  \"A.N.Other\" ." + "}   ";
    
    public static void test() {
        String id = UUID.randomUUID().toString();
        System.out.println(String.format("Adding %s", id));
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(
                UpdateFactory.create(String.format(UPDATE_TEMPLATE, id)), 
                "http://localhost:3030/ds/update");
        upp.execute();
        //Query the collection, dump output
        
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/query", "PREFIX axis: <http://titan.be/axis-csrm/datamodel/ontology/0.3#>"+
                        "SELECT * WHERE {?x axis:uses ?y}");
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results);
        
        
        
        qe.close();
    }
    
    
    public static void testSelectDBPedia() {
        String sparqlQueryString1 = "PREFIX dbont: <http://dbpedia.org/ontology/> " +
                "PREFIX dbp: <http://dbpedia.org/property/>" +
                "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
                "   SELECT ?musician  ?place" +
                "   FROM<http://dbpedia.org/resource/Daphne_Oram>" +
                "   WHERE { " +
                "       ?musician dbont:birthPlace ?place ." +
                "   }";

        Query query = QueryFactory.create(sparqlQueryString1);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

        ResultSet results = qexec.execSelect();
        ResultSetFormatter.out(System.out, results);      

        qexec.close() ;
    }
    
    public static void testSelect() {
        String id = UUID.randomUUID().toString();
        System.out.println(String.format("Adding %s", id));
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(
                UpdateFactory.create(String.format(UPDATE_TEMPLATE, id)), 
                "http://localhost:3030/ds/update");
        upp.execute();
        //Query the collection, dump output
        
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/query", "PREFIX axis: <http://titan.be/axis-csrm/datamodel/ontology/0.3#>"+
                        "SELECT * WHERE {? axis:uses ?y}");
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results);
        
        
        
        qe.close();
    }
    
    public static void testConstruct() {
        String id = UUID.randomUUID().toString();
        System.out.println(String.format("Adding %s", id));
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(
                UpdateFactory.create(String.format(UPDATE_TEMPLATE, id)), 
                "http://localhost:3030/ds/update");
        upp.execute();
        //Query the collection, dump output
        
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/query", "PREFIX axis: <http://titan.be/axis-csrm/datamodel/ontology/0.3#>"+
                        "CONSTRUCT WHERE {?s axis:uses ?o}");

       Model constructModel = qe.execConstruct();
       System.out.println("Construct result = " + constructModel.toString());

        
        qe.close();
    }
    
    public static void testInsert() {
        String id = UUID.randomUUID().toString();
        System.out.println(String.format("Adding %s", id));
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(
                UpdateFactory.create(String.format(UPDATE_TEMPLATE, id)), 
                "http://localhost:3030/ds/update");
        upp.execute();
        //Query the collection, dump output
        
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/update", "PREFIX axis: <http://titan.be/axis-csrm/datamodel/ontology/0.3#> "
            + "INSERT DATA "
            + "{ <http://example/%s>    axis:uses    \"objet\" .}");
         
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results);

        qe.close();
    }
    
    public static void testLoan() {
        
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/query", "PREFIX axis: <http://titan.be/axis-csrm/datamodel/ontology/0.3#>"+
                        "CONSTRUCT WHERE {<http://titan.be/axis-poc2015/Entity_TheMuseumObjects> ?p ?o}");

       Model constructModel = qe.execConstruct();
       System.out.println("Construct result = " + constructModel.toString());
    }



}
