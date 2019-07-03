package socnetsdk.test;

import socnetsdk.AccountBuilder;
import socnetsdk.beans.Account;
import socnetsdk.beans.Post;
import socnetsdk.beans.SocialMedia;
import socnetsdk.inner.Engine;

/**
 *
 * @author asus
 */
public class SimulasiMenantang {

    public static void main(String[] args) {

        Engine myBot = new Engine();
        String pesanMenantang = "hayo coba socnetSDK now!";
        Account myUsers [] = AccountBuilder.create("one@gmail.com", "konci", pesanMenantang, SocialMedia.FACEBOOK, SocialMedia.TWITTER);
        
        myBot.addUsers(myUsers);
        myBot.start(true, 3, 5);
        
    }

}
