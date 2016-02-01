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
import static model.Connector.*;
import java.util.Iterator;

/**
 *
 * @author APP-Riad.Belmahi
 */
public class Object extends Entity {

    public PropertyAdmin location;
    public PropertyAdmin author;
    public PropertyAdmin sameAs;
    public PropertyAdmin description;

    public Property[] getPropertiesObject() {
        ArrayList<Property> list = new ArrayList<Property>();
//        entityBrowser(this.getURI()
        if (!this.author.getName().isEmpty()) {
            list.add(new Property(this.author.getName(), this.author.getValue_locale(), this.author.getType(), this.author.getEntity_locale()));
        }
        if (!this.location.getName().isEmpty()) {
            list.add(new Property(this.location.getName(), this.location.getValue_locale(), this.location.getType(), this.location.getEntity_locale()));
        }
        if (!this.description.getName().isEmpty()) {
            list.add(new Property(this.description.getName(), this.description.getValue_locale(), this.description.getType(), this.description.getEntity_locale()));
        }
        //list.add(new Property(this.dateCreation.getName(), this.dateCreation.getValue_locale(), this.dateCreation.getType(), this.dateCreation.getEntity_locale()));
        Property[] ret = new Property[list.size()];
        return (Property[]) list.toArray(ret);
    }

    public PropertyAdmin[] getPropertiesAdminObject() {
        ArrayList<PropertyAdmin> list = new ArrayList<PropertyAdmin>();

        list.add(this.author);
        list.add(this.location);
        //list.add(this.sameAs);
        list.add(this.description);
        //list.add(this.dateCreation);

        PropertyAdmin[] ret = new PropertyAdmin[list.size()];
        return (PropertyAdmin[]) list.toArray(ret);
    }

    public void constructObject(boolean getdbpedia) {
        if (!this.getURI().contains("dbpedia")) {
//            this.author = getObjectPropertyAdmin("author");
//            this.location = getObjectPropertyAdmin("location");
//            this.description = getObjectPropertyAdmin("description");
            this.author = getPropertyAdmin("author", "axis-datamodel:isPerformedBy");
            this.location = getPropertyAdmin("location", "axis-datamodel:takePlaceIn");
            this.description = getPropertyAdmin("description", "rdf:Description");
            this.sameAs = getPropertyAdmin("description", "owl:sameAs");
        } else {
            this.author = new PropertyAdmin();
            this.author.setName("author");
            this.location = new PropertyAdmin();
            this.location.setName("location");
            this.description = new PropertyAdmin();
            this.description.setName("description");
        }
        if (this.getURI().contains("dbpedia") || getdbpedia == true) {
            ArrayList<Property> p = getPropertiesMapFromLod(this);
            if (p != null) {
                Iterator<Property> it = p.iterator();
                while (it.hasNext()) {
                    Property n = it.next();
                    switch (n.getName()) {
                        case "author":
                            this.author.setType(n.getType());
                            if (this.getURI().contains("dbpedia")) {
                                this.author.setEntity_locale(n.getEnt());
                                this.author.setValue_locale(n.getValue());
                            } else {
                                this.author.setEntity_dbpedia(n.getEnt());
                                this.author.setValue_dbpedia(n.getValue());
                            }
                            break;
                        case "location":
                            this.location.setType(n.getType());
                            if (this.getURI().contains("dbpedia")) {
                                this.location.setEntity_locale(n.getEnt());
                                this.location.setValue_locale(n.getValue());
                            } else {
                                this.location.setEntity_dbpedia(n.getEnt());
                                this.location.setValue_dbpedia(n.getValue());
                            }
                            break;
                        case "description":
                            this.description.setType(n.getType());
                            if (this.getURI().contains("dbpedia")) {
                                this.description.setEntity_locale(n.getEnt());
                                this.description.setValue_locale(n.getValue());
                            } else {
                                this.description.setEntity_dbpedia(n.getEnt());
                                this.description.setValue_dbpedia(n.getValue());
                            }
                            break;
                    }
                }
            }
        }

    }

//    public PropertyAdmin getObjectPropertyAdmin(String propertyName) {
//        PropertyAdmin pa = new PropertyAdmin();
//        switch (propertyName) {
//            case "author":
//                pa = getPropertyAdmin("isPerformedBy", "entity");
//                pa.setName(propertyName);
//                break;
//            case "location":
//                pa = getPropertyAdmin("takesPlaceIn", "entity");
//                pa.setName(propertyName);
//                break;
//            case "description":
//                pa = getPropertyAdmin("Description", "literal");
//                pa.setName(propertyName);
//                break;
//        }
//        return pa;
//    }

    public void insertDateCreation(Property p) {

    }

    public void insertLocation(Property p) {

        String uri1 = null;
        switch (this.getTypeProperty(p)) {
            case "dbpedia":
                insert(selectRegOfEntity(this.getURI(), "RegOfObjectItem"), "axis-datamodel:takePlaceIn", p.getEnt()[0].getURI());
                insert(p.getEnt()[0].getURI(), "axis-datamodel:isAPlaceOfObject", this.getURI());
//                uri1 = insert("rdf:type", "axis-datamodel:Place");
//                insert(this.getURI(), "axis-datamodel:takePlaceIn", uri1);
//                insert(uri1, "axis-datamodel:isAPlaceOfObject", this.getURI());
//                insert(uri1, "owl:sameAs", p.getEnt()[0].getURI());
                break;

            case "our":
                insert(selectRegOfEntity(this.getURI(), "RegOfObjectItem"), "axis-datamodel:takePlaceIn", p.getEnt()[0].getURI());
                insert(p.getEnt()[0].getURI(), "axis-datamodel:isAPlaceOfObject", this.getURI());
                break;

            case "literal":
                uri1 = insert("rdf:type", "axis-datamodel:Place");
                insert(selectRegOfEntity(this.getURI(), "RegOfObjectItem"), "axis-datamodel:takePlaceIn", uri1);
                insert(uri1, "axis-datamodel:isAPlaceOfObject", this.getURI());
                insert(uri1, "rdfs:label", p.getValue(), p.getType());
                break;
        }
    }

    public void insertAuthor(Property p) {

        String uri1 = null;

        switch (this.getTypeProperty(p)) {
            case "dbpedia":
                insert(selectRegOfEntity(this.getURI(), "RegOfObjectItem"), "axis-datamodel:isPerformedBy", p.getEnt()[0].getURI());
                insert(p.getEnt()[0].getURI(), "axis-datamodel:performs", this.getURI());
//                uri1 = insert("rdf:type", "axis-datamodel:PhysicalPerson");
//                insert(this.getURI(), "axis-datamodel:isPerformedBy", uri1);
//                insert(uri1, "axis-datamodel:performs", this.getURI());
//                insert(uri1, "owl:sameAs", p.getEnt()[0].getURI());
                break;

            case "our":
                insert(selectRegOfEntity(this.getURI(), "RegOfObjectItem"), "axis-datamodel:isPerformedBy", p.getEnt()[0].getURI());
                insert(p.getEnt()[0].getURI(), "axis-datamodel:performs", this.getURI());
                break;

            case "literal":
                uri1 = insert("rdf:type", "axis-datamodel:PhysicalPerson");
                insert(selectRegOfEntity(this.getURI(), "RegOfObjectItem"), "axis-datamodel:isPerformedBy", uri1);
                insert(uri1, "axis-datamodel:performs", this.getURI());
                insert(uri1, "rdfs:label", p.getValue(), p.getType());
                break;
        }

    }

    @Override
    public String toString() {
        return "Object{" + "location=" + location + ", author=" + author + ", sameAs=" + sameAs + ", description=" + description + '}';
    }

    public String superString() {
        return super.toString();
    }

}
