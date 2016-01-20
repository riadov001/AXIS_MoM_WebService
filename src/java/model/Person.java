/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Dialog.Entity;
import Dialog.Property;
import Dialog.PropertyAdmin;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static model.Connector.*;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

/**
 *
 * @author loannguyen
 */
public class Person extends Entity {
    public PropertyAdmin birthDate;
    public PropertyAdmin deathDate;
    public PropertyAdmin placeOfBirth;
    public PropertyAdmin mother;
    public PropertyAdmin father;
    public PropertyAdmin isAuthorOf;
    public PropertyAdmin restInPlace;

    public Property[] getPropertiesPerson() {
        ArrayList<Property> list = new ArrayList<Property>();

	list.add(new Property(this.birthDate.getName(), this.birthDate.getValue_locale(), this.birthDate.getType(), this.birthDate.getEntity_locale()));
        list.add(new Property(this.deathDate.getName(), this.deathDate.getValue_locale(), this.deathDate.getType(), this.deathDate.getEntity_locale()));
        list.add(new Property(this.placeOfBirth.getName(), this.placeOfBirth.getValue_locale(), this.placeOfBirth.getType(), this.placeOfBirth.getEntity_locale()));
	list.add(new Property(this.mother.getName(), this.mother.getValue_locale(), this.mother.getType(), this.mother.getEntity_locale()));
        list.add(new Property(this.father.getName(), this.father.getValue_locale(), this.father.getType(), this.father.getEntity_locale()));
        list.add(new Property(this.isAuthorOf.getName(), this.isAuthorOf.getValue_locale(), this.isAuthorOf.getType(), this.isAuthorOf.getEntity_locale()));
        list.add(new Property(this.restInPlace.getName(), this.restInPlace.getValue_locale(), this.restInPlace.getType(), this.restInPlace.getEntity_locale()));
	Property[] ret = new Property[list.size()];
	return (Property[]) list.toArray(ret);
    }
        
    public PropertyAdmin[] getPropertiesAdminPerson() {
        ArrayList<PropertyAdmin> list = new ArrayList<PropertyAdmin>();

	list.add(this.birthDate);
        list.add(this.deathDate);
        list.add(this.placeOfBirth);
	
	PropertyAdmin[] ret = new PropertyAdmin[list.size()];
	return (PropertyAdmin[]) list.toArray(ret);
    }
    
    public void constructPerson() {
        this.birthDate = getPersonPropertyAdmin("birthdate");
        this.deathDate = getPersonPropertyAdmin("deathdate");
        this.placeOfBirth = getPersonPropertyAdmin("birthplace");
        this.mother = getPersonPropertyAdmin("mother");
        this.father = getPersonPropertyAdmin("father");
        this.isAuthorOf = getPersonPropertyAdmin("isauthorof");
        this.restInPlace = getPersonPropertyAdmin("restinplace");
        
        String testuri = this.getURI();
        ArrayList<Property> p = getPropertiesMapFromLod(this.getURI());
        if(p != null){
        Iterator<Property> it = p.iterator();
        while(it.hasNext()){
            Property n = it.next();
            switch (n.getName()) {
                case "birthdate":
                    this.birthDate.setEntity_dbpedia(n.getEnt());
                    this.birthDate.setValue_dbpedia(n.getValue());
                    break;
                case "deathdate":
                    this.deathDate.setEntity_dbpedia(n.getEnt());
                    this.deathDate.setValue_dbpedia(n.getValue());
                    break;
                case "birthplace":
                    this.placeOfBirth.setEntity_dbpedia(n.getEnt());
                    this.placeOfBirth.setValue_dbpedia(n.getValue());
                    break;
                    
            }
            
        }}
    }
    
    public PropertyAdmin getPersonPropertyAdmin(String propertyName){
          
        PropertyAdmin pa = new PropertyAdmin();
        pa.setName(propertyName);
        switch (propertyName) {
            case "birthdate":
                pa = getPropertyAdmin("birthDate", "literal");
                pa.setName(propertyName);
                break;
            case "deathdate":
                pa = getPropertyAdmin("deathDate", "literal");
                pa.setName(propertyName);
                break;
            case "birthplace":
                pa = getPropertyAdmin("birthPlace", "entity");
                pa.setName(propertyName);
                break;
            case "mother":
                pa = getPropertyAdmin("mother", "entity");
                pa.setName(propertyName);
                break;
            case "father":
                pa = getPropertyAdmin("father", "entity");
                pa.setName(propertyName);
                break;
            case "isauthorof":
                pa = getPropertyAdmin("isAuthorOf", "entity");
                pa.setName(propertyName);
                break;
            case "restinplace":
                pa = getPropertyAdmin("restInPlace", "entity");
                pa.setName(propertyName);
                break;
        }
        
        return pa;
    }
    
    public void insertBirthDate(Property p) {
        insert(this.getURI(), "schema:birthDate", p.getValue(), p.getType());
    }
    
    public void insertDeathDate(Property p) {
        insert(this.getURI(), "schema:deathDate", p.getValue(), p.getType());
    }
    
    public void insertPlaceOfBirth(Property p) {
        String uri1 = null;
        switch (this.getTypeProperty(p)) {
	    case "dbpedia":
                uri1 = insert("rdf:type", "axis-datamodel:Place");
                insert(this.getURI(), "dbont:birthPlace", uri1);
                insert(uri1, "owl:sameAs", p.getEnt().getURI());
                break;
                
            case "our":
                insert(this.getURI(), "dbont:birthPlace", p.getEnt().getURI());
                break;
                
            case "literal":
                uri1 = insert("rdf:type", "axis-datamodel:Place");
                insert(this.getURI(), "dbont:birthPlace", uri1);
                insert(uri1, "rdfs:label", p.getValue(), p.getType());
                break;
        }
    }

    public void insertMother(Property p) {
        String uri1 = null;
        switch (this.getTypeProperty(p)) {
	    case "dbpedia":
                uri1 = insert("rdf:type", "axis-datamodel:Person");
                insert(this.getURI(), "dbont:mother", uri1);
                insert(uri1, "dbont:child", this.getURI());
                insert(uri1, "owl:sameAs", p.getEnt().getURI());
                break;
                
            case "our":
                insert(this.getURI(), "dbont:mother", p.getEnt().getURI());
                break;
                
            case "literal":
                uri1 = insert("rdf:type", "axis-datamodel:Person");
                insert(this.getURI(), "dbont:mother", uri1);
                insert(uri1, "dbont:child", this.getURI());
                insert(uri1, "rdfs:label", p.getValue(), p.getType());
                break;
        }
    }
    
    public void insertFather(Property p) {
        String uri1 = null;
        switch (this.getTypeProperty(p)) {
	    case "dbpedia":
                uri1 = insert("rdf:type", "axis-datamodel:Person");
                insert(this.getURI(), "dbont:father", uri1);
                insert(uri1, "dbont:child", this.getURI());
                insert(uri1, "owl:sameAs", p.getEnt().getURI());
                break;
                
            case "our":
                insert(this.getURI(), "dbont:father", p.getEnt().getURI());
                break;
                
            case "literal":
                uri1 = insert("rdf:type", "axis-datamodel:Person");
                insert(this.getURI(), "dbont:father", uri1);
                insert(uri1, "dbont:child", this.getURI());
                insert(uri1, "rdfs:label", p.getValue(), p.getType());
                break;
        }
    }
    
    public void insertRestInPlace(Property p) {
        String uri1 = null;
        switch (this.getTypeProperty(p)) {
	    case "dbpedia":
                uri1 = insert("rdf:type", "axis-datamodel:Place");
                insert(this.getURI(), "dbont:restInPlace", uri1);
                insert(uri1, "owl:sameAs", p.getEnt().getURI());
                break;
                
            case "our":
                insert(this.getURI(), "dbont:restInPlace", p.getEnt().getURI());
                break;
                
            case "literal":
                uri1 = insert("rdf:type", "axis-datamodel:Place");
                insert(this.getURI(), "dbont:restInPlace", uri1);
                insert(uri1, "rdfs:label", p.getValue(), p.getType());
                break;
        }
    }
    
    public void insertIsAuthorOf(Property p) {
        String uri1 = null;
        switch (this.getTypeProperty(p)) {
	    case "dbpedia":
                uri1 = insert("rdf:type", "axis-datamodel:PhysicalObject");
                insert(this.getURI(), "axis-datamodel:performs", uri1);
                insert(uri1, "axis-datamodel:isPerformedBy", this.getURI());
                insert(uri1, "owl:sameAs", p.getEnt().getURI());
                break;
                
            case "our":
                insert(this.getURI(), "axis-datamodel:performs", p.getEnt().getURI());
                insert(p.getEnt().getURI(), "axis-datamodel:isPerformedBy", this.getURI());
                break;
                
            case "literal":
                uri1 = insert("rdf:type", "axis-datamodel:PhysicalObject");
                insert(this.getURI(), "axis-datamodel:performs", uri1);
                insert(uri1, "axis-datamodel:isPerformedBy", this.getURI());
                insert(uri1, "rdfs:label", p.getValue(), p.getType());
                break;
        }
    }

    @Override
    public String toString() {
        return "Person{" + "birthDate=" + birthDate + ",\n deathDate=" + deathDate + ",\n placeOfBirth=" + placeOfBirth + ",\n mother=" + mother + ",\n father=" + father + ",\n isAuthorOf=" + isAuthorOf + ",\n restInPlace=" + restInPlace + '}';
    }
    
    
    
}
