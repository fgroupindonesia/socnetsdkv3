package socnetsdk.test;

import socnetsdk.beans.Account;
import socnetsdk.beans.SocialMedia;
import socnetsdk.inner.Engine;

/**
 *
 * @author asus
 */
public class SimulasiSederhana {

    public static void main(String[] args) {

        Engine myBot = new Engine();

        Account myUser = new Account("gumuruh@gmail.com", "g54saNWh", SocialMedia.FACEBOOK);
        myUser.addPost("Mau coba SocNetSDKv3? Skarang sudah ada di github.");
        
        myBot.addUser(myUser);
        myBot.start(true);
        
        
        
    }

}
