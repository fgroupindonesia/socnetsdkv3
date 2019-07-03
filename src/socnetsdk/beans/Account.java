package socnetsdk.beans;

import java.util.ArrayList;

/**
 *
 * @author asus
 */
public class Account {

    private String username;
    private String pass;
    private String socmed;
    private int socmedcode;
    private boolean logged;
    private ArrayList<Post> posts;
    private int currentPostIndex;

    public Account(String username1, String passw, int jenisSocialMedia) {
        this.username = username1;
        this.pass = passw;
        this.setSocialMedia(jenisSocialMedia);
        this.posts = new ArrayList();
    }

    public Account(String username, String passw) {
        this.username = username;
        this.pass = passw;
        this.posts = new ArrayList();
    }

    public Account() {

    }

    public void setSocialMedia(int jenis) {
        switch (jenis) {
            case SocialMedia.FACEBOOK:
                this.socmed = "facebook";
                break;

            case SocialMedia.GOOGLE:
                this.socmed = "google";
                break;

            case SocialMedia.LINKEDIN:
                this.socmed = "linkedin";
                break;

            case SocialMedia.TWITTER:
                this.socmed = "twitter";
                break;
        }

        socmedcode = jenis;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the socmed
     */
    public String getSocialMedia() {
        return socmed;
    }

    public int getSocialMediaCode() {
        return socmedcode;
    }

    public void addPost(String pesan) {
        this.posts.add(new Post(pesan));
    }

    public void addPost(Post masuk) {
        this.posts.add(masuk);
    }

    public Post nextPost() {

        Post data = this.posts.get(currentPostIndex);
        currentPostIndex++;
        return data;
    }

    public ArrayList<Post> getAllPosts() {
        return posts;
    }

    /**
     * @return the logged
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * @param logged the logged to set
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

}
