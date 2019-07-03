package socnetsdk.inner;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
//import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.events.FailLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FrameLoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.LoadEvent;
import com.teamdev.jxbrowser.chromium.events.NetError;
import com.teamdev.jxbrowser.chromium.events.ProvisionalLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import static org.openqa.grid.common.SeleniumProtocol.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import socnetsdk.Application;

//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//
//import org.openqa.selenium.chrome.ChromeDriverService;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;
import socnetsdk.URLSources;
import socnetsdk.beans.Account;
import socnetsdk.beans.Node;
import socnetsdk.beans.Post;
import socnetsdk.beans.SocialMedia;
import socnetsdk.inner.patcher.JxBrowserHackUtil;
import socnetsdk.inner.patcher.JxVersion;

/**
 *
 * @author @FgroupIndonesia.com
 */
public class Engine {

    WebDriver driver = null;
    DesiredCapabilities capabilities = null;
    ChromeDriverService service = null;
    final int WAITING_TIME = 10000; // 10seconds
    final int WAITING_CLICK_TIME = 4000; // 4seconds
    int currentWaitingTime = 0;
    boolean lagiNunggu = true;
    boolean killOnceDone = false;
    String remoteDebuggingAddress = "localhost:9222";
    String fileChromeDriver = "chromedriver.exe";
    int minRange, maxRange;
    ArrayList<Account> dataUsers;

    public Engine() {

        prepareStuff();
        visit("http://google.com");
        //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
    }

    private void prepareStuff() {

        //killAllChromeDrivers();
        dataUsers = new ArrayList();

        BrowserPreferences.setChromiumSwitches("--remote-debugging-port=9222");
        JxBrowserHackUtil.hack(JxVersion.V6_22);
        String identity = UUID.randomUUID().toString();

        BrowserContextParams params = new BrowserContextParams("temp/browser/" + identity);
        BrowserContext context1 = new BrowserContext(params);

        browser = new Browser(context1);
        browserView = new BrowserView(browser);
        prepareBrowserListener(browser);
        browserDocument = browser.getDocument();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(browserView, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        runningService();

    }

    private void prepareBrowserListener(Browser instance) {

        instance.addLoadListener(new LoadAdapter() {
            @Override
            public void onStartLoadingFrame(StartLoadingEvent event) {
                if (event.isMainFrame()) {
                    setCompleteLoaded(false);
                    //System.out.println("Main frame has started loading");
                }
            }

            @Override
            public void onProvisionalLoadingFrame(ProvisionalLoadingEvent event) {
                if (event.isMainFrame()) {
                    //System.out.println("Provisional load was committed for a frame");
                }
            }

            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                    setCompleteLoaded(true);
                    //System.out.println("Main frame has finished loading");
                }
            }

            @Override
            public void onFailLoadingFrame(FailLoadingEvent event) {
                NetError errorCode = event.getErrorCode();
                if (event.isMainFrame()) {
                    setCompleteLoaded(true);
                    //System.out.println("Main frame has failed loading: " + errorCode);
                }
            }

            @Override
            public void onDocumentLoadedInFrame(FrameLoadEvent event) {
                //setCompleteLoaded(true);
                //System.out.println("Frame document is loaded.");
            }

            @Override
            public void onDocumentLoadedInMainFrame(LoadEvent event) {
                //setCompleteLoaded(true);
                //System.out.println("Main frame document is loaded.");
            }
        });

    }

    private void runningService() {

        try {

            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File(Application.Path(fileChromeDriver)))
                    .usingAnyFreePort()
                    .build();
            service.start();

            System.out.println("Chromedriver berlokasi di " + Application.Path(fileChromeDriver));
        } catch (Exception ex) {
            Logger.writeError("Error at Engine constructor!");
        }

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", remoteDebuggingAddress);
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        //options.addArguments("--proxy-server='direct://'");
        //options.addArguments("--proxy-bypass-list=*");

        //options.addExtensions(new File("modifyheaders206.crx"));
        capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new RemoteWebDriver(service.getUrl(), capabilities);

        System.out.println("Debugging Browser on " + remoteDebuggingAddress);

    }

    public void addUser(Account masuk) {
        this.dataUsers.add(masuk);
    }

    public void addUsers(Account[] masuk) {
        for (Account satuan : masuk) {
            this.dataUsers.add(satuan);
        }

    }

    private Node createNewData(String key, String val) {
        return new Node(key, val);
    }

    private void killAllChromeDrivers() {

        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("Taskkill /IM chromedriver.exe /F");
        } catch (Exception ex) {
            Logger.writeError("error at killAllChromeDrivers!");
        }

    }

    private WebElement getElementByName(String nama) {
//    private DOMElement getElementByName(String nama) {

        // let say we're waiting
        lagiNunggu = true;
        WebElement element = null;
//        DOMElement element = null;

        while (lagiNunggu) {

            try {
                waiting(1000);
                //element = driver.findElement(By.name(nama));
                element = (new WebDriverWait(driver, WAITING_TIME / 1000))
                        .until(ExpectedConditions.elementToBeClickable(By.name(nama)));
                //element = browserDocument.findElement(By.name(nama));
            } catch (Exception ex) {
                // when found nothing let's wait another time
                currentWaitingTime += 1000;
                System.err.println("Waiting..." + currentWaitingTime + " ms.");
            }

            if (currentWaitingTime == WAITING_TIME) {
                // exit from waiting task
                lagiNunggu = false;
                currentWaitingTime = 0;
            }

            if (element != null) {
                currentWaitingTime = 0;
                lagiNunggu = false;
            }

        }

        return element;
    }

    private WebElement getElementById(String nama) {
        //private DOMElement getElementById(String nama) {
        // let say we're waiting
        lagiNunggu = true;
        WebElement element = null;
        //DOMElement element = null;

        while (lagiNunggu) {

            try {
                waiting(1000);
                //element = driver.findElement(By.id(nama));
                // element = browserDocument.findElement(By.id(nama));
                element = (new WebDriverWait(driver, WAITING_TIME / 1000))
                        .until(ExpectedConditions.elementToBeClickable(By.id(nama)));
            } catch (Exception ex) {
                // when found nothing let's wait another time
                currentWaitingTime += 1000;
                System.err.println("Waiting..." + currentWaitingTime + " ms.");
            }

            if (currentWaitingTime == WAITING_TIME) {
                // exit from waiting task
                lagiNunggu = false;
                currentWaitingTime = 0;
            }

            if (element != null) {
                currentWaitingTime = 0;
                lagiNunggu = false;
            }

        }

        return element;
    }

    private WebElement getElementByXPath(String nama) {
        //private DOMElement getElementByXPath(String nama) {
        // let say we're waiting
        lagiNunggu = true;
        WebElement element = null;
        //DOMElement element = null;

        while (lagiNunggu) {

            try {
                waiting(1000);
                //element = driver.findElement(By.xpath(nama));
                element = (new WebDriverWait(driver, WAITING_TIME / 1000))
                        .until(ExpectedConditions.elementToBeClickable(By.xpath(nama)));
                //element = browserDocument.findElement(By.xpath(nama));
            } catch (Exception ex) {
                // when found nothing let's wait another time
                currentWaitingTime += 1000;
                System.err.println("Waiting..." + currentWaitingTime + " ms.");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }

            if (currentWaitingTime == WAITING_TIME) {
                // exit from waiting task
                lagiNunggu = false;
                currentWaitingTime = 0;
            }

            if (element != null) {
                currentWaitingTime = 0;
                lagiNunggu = false;
            }

        }

        return element;
    }

    private WebElement getElementByCSS(String nama) {
        //private DOMElement getElementByCSS(String nama) {
        // let say we're waiting
        lagiNunggu = true;
        WebElement element = null;
        //DOMElement element = null;

        while (lagiNunggu) {

            try {
                waiting(1000);
                element = (new WebDriverWait(driver, WAITING_TIME / 1000))
                        .until(ExpectedConditions.elementToBeClickable(By.cssSelector(nama)));
                //element = driver.findElement(By.cssSelector(nama));
                //element = browserDocument.findElement(By.cssSelector(nama));
            } catch (Exception ex) {
                // when found nothing let's wait another time
                currentWaitingTime += 1000;
                System.err.println("Waiting..." + currentWaitingTime + " ms.");
                System.err.println(ex.getMessage());
            }

            if (currentWaitingTime == WAITING_TIME) {
                // exit from waiting task
                lagiNunggu = false;
                currentWaitingTime = 0;
            }

            if (element != null) {
                currentWaitingTime = 0;
                lagiNunggu = false;
            }

        }

        return element;
    }

    private void waiting(int miliseconds) {

        try {
            Thread.sleep(miliseconds);
        } catch (Exception ex) {
            Logger.writeError("error when Waiting()");
        }

    }

    public void postTwitter(Account akun) {
        if (driver.getCurrentUrl().equals(URLSources.URL_TWITTER_POST) == false) {

            // visit first
            driver.get(URLSources.URL_TWITTER_POST);

        }
        WebElement textbox = getElementByCSS("div.notranslate.public-DraftEditor-content");
        textbox.click();

        textbox.sendKeys(akun.nextPost().getText());

        WebElement button = getElementByXPath("//span[contains(text(),'Tweet')]");
        button.click();

        waiting(WAITING_CLICK_TIME);
    }

    public void postLinkedin(Account akun) {
        if (driver.getCurrentUrl().equals(URLSources.URL_LINKEDIN_HOME) == false) {

            // visit first
            driver.get(URLSources.URL_LINKEDIN_HOME);

        }
        WebElement textbox = getElementByXPath("//span[contains(text(),'on your mind?')]");
        textbox.click();

        waiting(WAITING_CLICK_TIME);
    }

    public void postFacebook(Account akun) {
        if (driver.getCurrentUrl().equals(URLSources.URL_FACEBOOK_MOBILE) == false) {

            // visit first
            driver.get(URLSources.URL_FACEBOOK_MOBILE);

        }
        WebElement textbox = getElementByXPath("//div[contains(text(),'on your mind?')]");
        textbox.click();
        
        
        WebElement textboxPesan = getElementByXPath("//textarea[contains(@aria-label,'on your mind?')]");
        textboxPesan.sendKeys(akun.nextPost().getText());
        
        WebElement buttonPost = getElementByXPath("//button[contains(@value,'Post')]");
        buttonPost.click();

        waiting(WAITING_CLICK_TIME);
    }

    public void postGoogle(Account data) {
        //System.out.println(driver.getCurrentUrl());

        if (driver.getCurrentUrl().equals(URLSources.URL_GOOGLEPLUS_HOME) == false) {

            // visit first
            driver.get(URLSources.URL_GOOGLEPLUS_HOME);

        }

        // clicking the textbox
        WebElement textbox = getElementByXPath("//span[contains(text(),'with you')]");
        //DOMElement textbox = getElementByXPath("//span[contains(text(),'with you')]");
        textbox.click();
        waiting(WAITING_CLICK_TIME);

        WebElement textArea = getElementByCSS("textarea[placeholder*='with you']");
        //DOMElement textArea = getElementByCSS("textarea[placeholder*='with you']");
        //textArea.clear();
        //textArea.sendKeys(data.getText());
        waiting(WAITING_CLICK_TIME);

        for (Post dataPosting : data.getAllPosts()) {

            if (dataPosting.getPicture() != null) {

                WebElement gambarIcon = getElementByCSS("div[data-tooltip='Add photos']");
                //DOMElement gambarIcon = getElementByCSS("div[data-tooltip='Add photos']");
                gambarIcon.click();
                waiting(WAITING_CLICK_TIME);

                WebElement gambarUpload = getElementByXPath("//div/input[@aria-label=\"Upload photo\"]");
                //DOMElement gambarUpload = getElementByXPath("//div/input[@aria-label=\"Upload photo\"]");
                //gambarUpload.sendKeys(data.getPicture().getAbsolutePath());
                // wait until the loading completed
                loadingDiv();

            }

            //DOMElement postButton = getElementByXPath("//span[text()='Post']");
            WebElement postButton = getElementByXPath("//span[text()='Post']");
            postButton.click();

        }

        waiting(WAITING_CLICK_TIME);

    }

    
    public void loginGoogle(Account akun) {

        driver.get(URLSources.URL_GOOGLEPLUS_LOGIN);
        //visit(URLSources.URL_GOOGLEPLUS_LOGIN);

        WebElement username = getElementById("identifierId");
        //DOMElement username = getElementById("identifierId");

        username.sendKeys(akun.getUsername());

        WebElement nextButton = getElementById("identifierNext");
        //DOMElement nextButton = getElementById("identifierNext");
        nextButton.click();

        waiting(WAITING_CLICK_TIME);

        WebElement pass = getElementByCSS("input[name='password']");
        //DOMElement pass = getElementByCSS("input[name='password']");

        //pass.sendKeys(akun.getPass());
        nextButton = getElementById("passwordNext");
        nextButton.click();

        waiting(WAITING_CLICK_TIME);

    }

    public void loginTwitter(Account akun) {

        driver.get(URLSources.URL_TWITTER_LOGIN);
        //visit(URLSources.URL_TWITTER_LOGIN);

        WebElement username = getElementByCSS("input[name='session[username_or_email]']");
        //DOMElement username = getElementByXPath("//input[@name, ‘session[username_or_email]’]");
        username.sendKeys(akun.getUsername());

        WebElement pass = getElementByName("session[password]");
        //DOMElement pass = getElementByXPath("//input[@name, ‘session[password]");

        pass.sendKeys(akun.getPass());
        // enter key
        pass.submit();

        waiting(WAITING_CLICK_TIME);

    }

    public void loginFacebook(Account akun) {

        driver.get(URLSources.URL_FACEBOOK_LOGIN);
        //visit(URLSources.URL_FACEBOOK_LOGIN);

        WebElement username = getElementById("email");
        //DOMElement username = getElementById("email");
        username.sendKeys(akun.getUsername());

        WebElement pass = getElementById("pass");
        //DOMElement pass = getElementById("pass");
        pass.sendKeys(akun.getPass());

        // enter key
        pass.submit();
        //pressEnter();

        waiting(WAITING_CLICK_TIME);

    }

    public void loginLinkedin(Account akun) {

        driver.get(URLSources.URL_LINKEDIN_LOGIN);
        //visit(URLSources.URL_LINKEDIN_LOGIN);

        WebElement username = getElementById("username");
        //DOMElement username = getElementById("username");
        username.sendKeys(akun.getUsername());

        WebElement pass = getElementById("password");
        //DOMElement pass = getElementById("password");
        pass.sendKeys(akun.getPass());

        // enter key
        //pass.submit();
        pressEnter();

        waiting(WAITING_CLICK_TIME);

    }

    private boolean isPageLoggingPassed(Account akun) {

//        if (akun.getSocialMediaCode() == SocialMedia.FACEBOOK && driver.getCurrentUrl().equalsIgnoreCase(URLSources.URL_FACEBOOK_LOGIN)) {
//            return false;
//        } else if (akun.getSocialMediaCode() == SocialMedia.TWITTER && driver.getCurrentUrl().equalsIgnoreCase(URLSources.URL_TWITTER_LOGIN)) {
//            return false;
//        } else if (akun.getSocialMediaCode() == SocialMedia.LINKEDIN && driver.getCurrentUrl().equalsIgnoreCase(URLSources.URL_LINKEDIN_LOGIN)) {
//            return false;
//        } else if (akun.getSocialMediaCode() == SocialMedia.GOOGLE && driver.getCurrentUrl().equalsIgnoreCase(URLSources.URL_GOOGLEPLUS_LOGIN)) {
//            return false;
//        }
        if (akun.getSocialMediaCode() == SocialMedia.FACEBOOK && getURL().equalsIgnoreCase(URLSources.URL_FACEBOOK_LOGIN)) {
            return false;
        } else if (akun.getSocialMediaCode() == SocialMedia.TWITTER && getURL().equalsIgnoreCase(URLSources.URL_TWITTER_LOGIN)) {
            return false;
        } else if (akun.getSocialMediaCode() == SocialMedia.LINKEDIN && getURL().equalsIgnoreCase(URLSources.URL_LINKEDIN_LOGIN)) {
            return false;
        } else if (akun.getSocialMediaCode() == SocialMedia.GOOGLE && getURL().equalsIgnoreCase(URLSources.URL_GOOGLEPLUS_LOGIN)) {
            return false;
        }

        return true;
    }

    private void loadingDiv() {

        WebElement divLoading = null;
        //DOMElement divLoading = null;
        boolean tunggu = true;

        while (tunggu) {

            // keep waiting
            // when the element is still there
            try {

                divLoading = getElementByCSS("div[data-loadingmessage=\"Loading...\"");
                System.out.println("image belum terupload!");
            } catch (Exception ex) {
                System.out.println("image sudah terupload!");
                tunggu = false;
            }

            waiting(3000);

        }

    }

    private void posting(Account dataAkun) {
        switch (dataAkun.getSocialMediaCode()) {
            case SocialMedia.ALL:
                postFacebook(dataAkun);
                postGoogle(dataAkun);
                postTwitter(dataAkun);
                postLinkedin(dataAkun);
                break;
            case SocialMedia.FACEBOOK:
                postFacebook(dataAkun);
                break;
            case SocialMedia.TWITTER:
                postTwitter(dataAkun);
                break;
            case SocialMedia.GOOGLE:
                postGoogle(dataAkun);
                break;
            case SocialMedia.LINKEDIN:
                postLinkedin(dataAkun);
                break;
        }

        System.out.println("Posting akun " + dataAkun.getUsername() + " di " + dataAkun.getSocialMedia() + " sukses!");
    }

    private boolean loggingFirst(Account dataUser) {
        if (dataUser.getSocialMediaCode() == SocialMedia.ALL) {
            loginGoogle(dataUser);
            loginFacebook(dataUser);
            loginTwitter(dataUser);
            loginLinkedin(dataUser);
            System.out.println("all detected.");
        } else if (dataUser.getSocialMediaCode() == SocialMedia.FACEBOOK) {
            loginFacebook(dataUser);
            System.out.println("fb detected.");

        } else if (dataUser.getSocialMediaCode() == SocialMedia.TWITTER) {
            loginTwitter(dataUser);
            System.out.println("twitter detected.");
        } else if (dataUser.getSocialMediaCode() == SocialMedia.GOOGLE) {
            loginGoogle(dataUser);
            System.out.println("google detected.");
        } else if (dataUser.getSocialMediaCode() == SocialMedia.LINKEDIN) {
            loginLinkedin(dataUser);
            System.out.println("linkedin detected.");
        }

        System.out.println("Trying to loggin as..." + dataUser.getUsername());
        dataUser.setLogged(isPageLoggingPassed(dataUser));
        return dataUser.isLogged();
    }

    private boolean isLogged(Account dataUser) {
        return dataUser.isLogged();
    }

    public void start() {
        // waiting the browser fully loaded
        while (!isCompleteLoaded()) {
            try {
                // keep looping
                System.out.println("waiting for browser...");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.writeError("error while starting Engine before Browser came up!");
            }
        }

        System.out.println("Browser is ready! Now trying for executing everything...");

        // connecting Selenium with JxBrowser
        // runningService();
        //driver = new ChromeDriver(capabilities);
        //connectToWebDriver();
        System.out.println("Selenium is now Connected to JXBrowser!");

        // execute everything!
        // lakukan loggin untuk masing-masing akun
        for (Account dataAkun : dataUsers) {
            if (isLogged(dataAkun) == false) {
                if (loggingFirst(dataAkun) == true) {
                    posting(dataAkun);
                }
            }
        }

        if (killOnceDone) {
            System.out.println("Exitting web driver services...done!");
            //driver.quit();
            //service.stop();
        }
    }

    public void start(boolean autoExit) {
        killOnceDone = autoExit;
        this.start();

    }

    public void start(boolean autoExit, int minInterval, int maxInterval) {
        killOnceDone = autoExit;
        this.minRange = minInterval;
        this.maxRange = maxInterval;

        this.start();

    }

    public void start(int minInterval, int maxInterval) {
        this.minRange = minInterval;
        this.maxRange = maxInterval;

        this.start();

    }

    public void setTimeInterval(int minimalRange, int maximumRange) {

        // set a number in between this numbers
        // as minutes
        this.minRange = minimalRange;
        this.maxRange = maximumRange;

    }

    private int getRandomNumber(int a, int b) {

        double nilaiAcak = Math.random() * b + a;
        return (int) nilaiAcak;

    }

    private int getTimeInterval() {

        // known as miliseconds
        int nilaiAcakBulat = getRandomNumber(this.minRange, this.maxRange) * 1000;

        // multiplication of random number quarter miliseconds
        int pengacakLain = getRandomNumber(minRange, minRange) * 250;

        return (nilaiAcakBulat + pengacakLain);
    }

    /**
     * @return the completeLoaded
     */
    public boolean isCompleteLoaded() {
        return completeLoaded;
    }

    /**
     * @param completeLoaded the completeLoaded to set
     */
    public void setCompleteLoaded(boolean completeLoaded) {
        this.completeLoaded = completeLoaded;

    }

    public void setBrowserDocument(DOMDocument doc) {
        browserDocument = doc;
    }

    public DOMDocument getBrowserDocument() {
        return browserDocument;
    }

    /**
     * Creates new form Browser
     */
    static Browser browser = null;
    static BrowserView browserView = null;
    private DOMDocument browserDocument = null;

    private boolean completeLoaded;

    public void visit(String anURL) {
        browser.loadURL(anURL);

    }

    public String getURL() {
        return browser.getURL();
    }

    public void pressEnter() {
        browser.forwardKeyEvent(new BrowserKeyEvent(BrowserKeyEvent.KeyEventType.PRESSED, BrowserKeyEvent.KeyCode.VK_RETURN));
        browser.forwardKeyEvent(new BrowserKeyEvent(BrowserKeyEvent.KeyEventType.TYPED, BrowserKeyEvent.KeyCode.VK_RETURN));
        browser.forwardKeyEvent(new BrowserKeyEvent(BrowserKeyEvent.KeyEventType.RELEASED, BrowserKeyEvent.KeyCode.VK_RETURN));
    }

}
