package Dialog;

public class PropertyAdmin {

    private String name;
    private String value_locale;
    private String value_dbpedia;
    private Entity entity_locale;
    private Entity entity_dbpedia;
    private String type;

    public PropertyAdmin() {
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getValue_locale() {
	return value_locale;
    }

    public void setValue_locale(String value_locale) {
	this.value_locale = value_locale;
    }

    public String getValue_dbpedia() {
	return value_dbpedia;
    }

    public void setValue_dbpedia(String value_dbpedia) {
	this.value_dbpedia = value_dbpedia;
    }

    public Entity getEntity_locale() {
	return entity_locale;
    }

    public void setEntity_locale(Entity entity_locale) {
	this.entity_locale = entity_locale;
    }

    public Entity getEntity_dbpedia() {
	return entity_dbpedia;
    }

    public void setEntity_dbpedia(Entity entity_dbpedia) {
	this.entity_dbpedia = entity_dbpedia;
    }

}