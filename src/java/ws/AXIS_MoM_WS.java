
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import Dialog.Comment;
import Dialog.Entity;
import Dialog.Property;
import Dialog.PropertyAdmin;
import control.semantics;
import java.util.ArrayList;
import javax.jws.WebService;
import static model.Connector.selectAllEntitiesURI;
import static model.Connector.selectRegOfEntity;
import static model.Connector.selectAllComments;
import model.Person;
import model.Object;
import model.Place;


@WebService(serviceName = "AXIS_MoM_WS", endpointInterface = "ws.AXIS_MoM_WSInterface")
public class AXIS_MoM_WS implements AXIS_MoM_WSInterface {
    


    @Override
    public Entity AddEntity(Entity e) {
	return e.AddEntity();
    }

    @Override
    public Boolean RemoveEntity(Entity e) {
	return true;
    }

    @Override
    public Boolean SetEntityProperty(Entity e, Property p, Entity valueEntity) {
	Object obj = new Object();
        Person pers = new Person();
        Place pla = new Place();
        e.constructEntity();
        
        Entity[] tab = new Entity[1];
        tab[0] = valueEntity;
        p.setEnt(tab);
        
        boolean ret = false;
        switch (p.getName()) {
	    case "author":
                obj.setURI(e.getURI());
//                obj.constructEntity();
//                obj.constructObject();
                obj.insertAuthor(p);
                ret = true;
                break;
            case "sameas":
                e.insertSameAs(p);
                ret = true;
                break;
            case "description":
                e.insertDescription(p);
                ret = true;
                break;
            case "location":
                obj.setURI(e.getURI());
//                obj.constructEntity();
//                obj.constructObject();
                obj.insertLocation(p);
                ret = true;
                break;
            case "image":
                e.insertImage(p);
                ret = true;
                break;
            case "name":
                e.insertName(p);
                ret = true;
                break;
            case "birthdate":
                pers.setURI(e.getURI());
//                pers.constructEntity();
//                pers.constructPerson();
                pers.insertBirthDate(p);
                ret = true;
                break;
            case "deathdate":
                pers.setURI(e.getURI());
//                pers.constructEntity();
//                pers.constructPerson();
                pers.insertDeathDate(p);
                ret = true;
                break;
            case "restinplace":
                pers.setURI(e.getURI());
//                pers.constructEntity();
//                pers.constructPerson();
                pers.insertRestInPlace(p);
                ret = true;
                break;
            case "mother":
                pers.setURI(e.getURI());
//                pers.constructEntity();
//                pers.constructPerson();
                pers.insertMother(p);
                ret = true;
                break;
            case "father":
                pers.setURI(e.getURI());
//                pers.constructEntity();
//                pers.constructPerson();
                pers.insertFather(p);
                ret = true;
                break;
            case "isauthorof":
                pers.setURI(e.getURI());
//                pers.constructEntity();
//                pers.constructPerson();
                pers.insertIsAuthorOf(p);
                ret = true;
                break;
            case "birthplace":
                pers.setURI(e.getURI());
//                pers.constructEntity();
//                pers.constructPerson();
                pers.insertPlaceOfBirth(p);
                ret = true;
                break;
            case "birthplaceof":
                pla.setURI(e.getURI());
//                pla.constructEntity();
//                pla.constructPlace();
                pla.insertBirthPlaceOf(p);
                ret = true;
                break;
            case "postalcode":
                pla.setURI(e.getURI());
//                pla.constructEntity();
//                pla.constructPlace();
                pla.insertPostalCode(p);
                ret = true;
                break;
            case "region":
                pla.setURI(e.getURI());
//                pla.constructEntity();
//                pla.constructPlace();
                pla.insertRegion(p);
                ret = true;
                break;
            case "country":
                pla.setURI(e.getURI());
//                pla.constructEntity();
//                pla.constructPlace();
                pla.insertCountry(p);
                ret = true;
                break;
            case "locationof":
                pla.setURI(e.getURI());
//                pla.constructEntity();
//                pla.constructPlace();
                pla.insertLocationOf(p);
                ret = true;
                break;
            default:
                return false;
                
        }
        
	return ret;
    }

    @Override
    public Boolean RemoveEntityProperty(Entity e, Property p, Entity valueEntity) {
        
        boolean ret = false;
        String property = "null";
        String regof = "null";
        
        switch (p.getName()) {
	    case "author":
                property = "axis-datamodel:performs";
                regof = "EmbodimentOfObject";
                break;
            case "sameas":
                e.delete(e.getURI(), "owl:sameAs", "<"+valueEntity.getURI()+">");
                break;
            case "description":
                property = "rdf:Description";
                regof = "Document";
                break;
            case "location":
                property = "axis-datamodel:takePlaceIn";
                regof = "EmbodimentOfObject";
                break;
            case "birthdate":
                property = "schema:birthDate";
                regof = "RegOfAgent";
                break;
            case "deathdate":
                property = "schema:deathDate";
                regof = "RegOfAgent";
                break;
            case "restinplace":
                property = "dbont:restInPlace";
                regof = "RegOfAgent";
                break;
            case "mother":
                property = "dbont:mother";
                regof = "RegOfAgent";
                break;
            case "father":
                property = "dbont:father";
                regof = "RegOfAgent";
                break;
            case "isauthorof":
                property = "axis-datamodel:isPerformedBy";
                regof = "RegOfAgent";
                break;
            case "birthplace":
                property = "dbont:birthPlace";
                regof = "RegOfAgent";
                break;
            case "postalcode":
                property = "dbont:postalCode";
                regof = "RegOfPlace";
                break;
            case "region":
                property = "dbont:region";
                regof = "RegOfPlace";
                break;
            case "country":
                property = "dbont:country";
                regof = "RegOfPlace";
                break;
            case "locationof":
                property = "axis-datamodel:isAPlaceOfObject";
                regof = "RegOfPlace";
                break;
            default:
                return false;
        }
        
        if(valueEntity.getURI().isEmpty())
            e.delete(selectRegOfEntity(e.getURI(), "Document"), property, "?o");
        else
            e.delete(selectRegOfEntity(e.getURI(), regof), property, "<"+valueEntity.getURI()+">");
        
	return ret;
    }

    @Override
    public Boolean RemoveEntityObjectPropertyWithObject(Entity e, Property p, Entity valueEntity) {
	return true;
    }

    @Override
    public Property[] LoadEntityProperties(Entity e) {
        
        
        e.constructEntity();
        System.out.println("e:"+e);
//        Object obj = new Object();
//        obj.setURI(e.getURI());
//        obj.constructObject();
//        System.out.println(obj);
        
        semantics ctrl = new semantics();
        
        Property[] tab = ctrl.getAllPropertiesFromEntity(e);
        return tab;

    }

    @Override
    public Entity[] SearchOurEntitiesFromText(String needle) {

	Entity[] tabEntities = selectAllEntitiesURI();
        ArrayList<Entity> tab = new ArrayList<Entity>();

        needle = needle.toLowerCase().replaceAll(" ", "");
        for(int i =0; i<tabEntities.length; i++) {
//            Entity e = new Entity();
//            e.setURI(tabEntities[i]);
//            e.constructEntity();
//            e.setName("leo");
            if(tabEntities[i].getName() != null) {
                if(tabEntities[i].getName().toLowerCase().replaceAll(" ", "").contains(needle)) {
                    Entity e = new Entity();
                    e.setURI(tabEntities[i].getURI());
                    e.constructEntity();
                    tab.add(e);
                }
            }
        }

        Entity[] ret = new Entity[tab.size()];
	return (Entity[]) tab.toArray(ret);
    }

    @Override
    public Entity[] SearchAllEntitiesFromText(String needle) {
	ArrayList<Entity> list = new ArrayList<>();

	Entity e1 = new Entity();
	e1.setImage("http://1.1.1.2/bmi/static.ladepeche.fr/content/media/image/zoom/2011/03/07/603056.jpg");
	e1.setName("Canette de coca");
	e1.setURI("coca");
	e1.setType("object");
	list.add(e1);

	Entity e2 = new Entity();
	e2.setImage("http://1.1.1.1/bmi/cp91279.biography.com/1000509261001/1000509261001_1891997649001_History-Bill-Clinton-on-MLK-SF.jpg");
	e2.setName("Martin Luther King");
	e2.setURI("MLK");
	e2.setType("person");
	list.add(e2);

	Entity[] ret = new Entity[list.size()];
	return (Entity[]) list.toArray(ret);
    }

    @Override
    public Comment AddComment(Comment c, Entity e) {
        c.setEntity(e);
        c = c.insertComment();
	return c;
    }

    @Override
    public Boolean GrantComment(Comment c) {
        c.changeValided(true);
	return true;
    }

    @Override
    public Boolean RemoveComment(Comment c, Entity e) {
        c.setEntity(e);
        return c.deleteComment();
    }

    @Override
    public Boolean DenyComment(Comment c) {
        c.changeValided(false);
	return true;
    }
    
    @Override
    public Comment[] LoadComment(Entity e) {
        
        Comment[] tab;
        if(e == null)
            tab = selectAllComments();
        else
            tab = selectAllComments(e);
	
        return tab;
    }

    @Override
    public Entity[] GetAllEntities() {
        
        return this.SearchOurEntitiesFromText("");
    }

    @Override
    public PropertyAdmin[] GetAllPropertiesAdmin(Entity e) {
        
        e.constructEntity();
        semantics ctrl = new semantics();
        PropertyAdmin[] tab = ctrl.getAllPropertiesAdminFromEntity(e);

        return tab;

    }

    @Override
    public Entity GetEntity(Entity e) {
	e.constructEntity();        
        return e;
    }

}
