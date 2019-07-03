
package socnetsdk.beans;

/**
 *
 * @author @FgroupIndonesia.com
 */
public class Node {
    private String key;
    private String values;

    public Node(String aKey, String aVal){
        key = aKey;
        values = aVal;
    }
    
    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the values
     */
    public String getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(String values) {
        this.values = values;
    }
}
