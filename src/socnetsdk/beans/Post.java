package socnetsdk.beans;

import java.io.File;

/**
 *
 * @author @FgroupIndonesia.com
 */
public class Post {

    private String text;
    private File picture;

    public Post(String pesan){
        this.text = pesan;
    }
    
    public Post(String pesan, File gambar) {
        text = pesan;
        picture = gambar;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the picture
     */
    public File getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(File picture) {
        this.picture = picture;
    }

}
