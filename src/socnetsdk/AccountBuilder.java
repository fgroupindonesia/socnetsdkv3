package socnetsdk;

import socnetsdk.beans.Account;

/**
 *
 * @author asus
 */
public class AccountBuilder {

    public static Account[] create(String username, String pass, String pesan, int... jenis) {

        int ukuran = jenis.length;

        Account data[] = new Account[ukuran];
        int i = 0;

        for (int socMed : jenis) {
            Account tunggal = new Account(username, pass, socMed);
            tunggal.addPost(pesan);
            data[i] = tunggal;
            i++;
        }
        return data;

    }
}
