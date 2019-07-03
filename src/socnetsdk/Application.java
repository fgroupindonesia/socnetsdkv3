package socnetsdk;

/**
 *
 * @author asus
 */
public class Application {

    static String appName = "SocNetSDKv3";
    static String systemPath = System.getenv("APPDATA");
    static boolean changed = false;

    public static void changePath(String newLocation) {
        changed = true;
        systemPath = newLocation;
        
    }

    public static String Path() {
        if (changed == false) {
            return systemPath + "\\" + appName;
        }
        return systemPath;
    }

    public static String Path(String fileName) {
        return Path() + "\\" + fileName;
    }
}
