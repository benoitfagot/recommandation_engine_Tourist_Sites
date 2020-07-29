package lucene;

public class Couple {
    Float key;
    String value;


    public Couple(Float key, String value){
        this.key=key;
        this.value=value;
    }

    public Float getKey() {
        return key;
    }

    public void setKey(Float key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}