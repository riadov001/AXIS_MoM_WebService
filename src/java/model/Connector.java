/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.URI;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;

/**
 *
 * @author APP-Riad.belmahi
 */
public class Connector {

    public static void main(String args[]) {
        //test
        System.out.println("main");

        selectlod();
    }

    public static Model loadModels(String url) { //mélanoche
        //todo
        return null;
    }

    public static boolean executeQuery(String str) {

        //pas prio
        return true;
    }

    public static Model selectFromEntity(URI uri) { //loan
        //on construct toutes les propriétés et valeurs de l'URI passé en paramètre
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/query", "PREFIX axis: <http://titan.be/axis-csrm/datamodel/ontology/0.3#>"
                + "CONSTRUCT WHERE {<" + uri + "> ?p ?o}");

        Model constructModel = qe.execConstruct();

        return constructModel;
    }

    public static Model selectFromEntity(String pred, String obj) { //loan
        return null;
    }

    public static void selectlod() {
        //riad
        //on construct toutes les propriétés et valeurs de l'URI passé en paramètre
        // l'URI est externe, et fait donc référence à un lien dbpedia, freebase...

        String DBQueryString = "PREFIX dbont: <http://dbpedia.org/ontology/> "
                + "PREFIX dbp: <http://dbpedia.org/property/>"
                + "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>"
                + "PREFIX dbr: <http://dbpedia.org/resource/>"
                + // on ajoute  ?s owl:sameAs ?Entity" aprés le construct pour comparer avec les resultats locales
                " CONSTRUCT WHERE {"
                + " ?s dbont:museum dbr:Louvre ."
                + " }";

        Query DBquery = QueryFactory.create(DBQueryString);
        QueryExecution qDBexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", DBquery);

        ResultSet resultsDB = qDBexec.execSelect();
            System.out.println("DBPedia");
            ResultSetFormatter.out(System.out, resultsDB);

            String FBQueryString
                    = "prefix fb: <http://rdf.freebase.com/ns/>"
                    + "prefix fn: <http://www.w3.org/2005/xpath-functions#>"
                    + "construct where {"
                    + " ?s fb:type.object.type fb:visual_art.artwork ."
                    + " } ";

            Query FBquery = QueryFactory.create(FBQueryString);

            QueryExecution qFBexec = QueryExecutionFactory.sparqlService("http://www.freebase.com/query", FBquery);

            ResultSet resultsFB = qFBexec.execSelect();
            System.out.println("FreeBase");
            ResultSetFormatter.out(System.out, resultsFB);

            qDBexec.close();
            qFBexec.close();

        }

    public static int insert(String s, String p) { //robine

        int uid = 0;
        return uid;
    }

    public static boolean insert(String s, String p, String o) { //robine
        //insert en suivant la logique de sujet-prédicat-objet

        return true;
    }

    public static boolean update(String s, String p, String o) {
        //pas prio
        return true;
    }

    public static boolean delete(String s, String p, String o) {
        //pas prio
        return true;
    }

}
