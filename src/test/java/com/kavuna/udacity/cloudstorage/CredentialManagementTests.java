package com.kavuna.udacity.cloudstorage;

import com.kavuna.udacity.cloudstorage.pages.HomePage;
import com.kavuna.udacity.cloudstorage.pages.LoginPage;
import com.kavuna.udacity.cloudstorage.pages.SignupPage;
import com.kavuna.udacity.cloudstorage.model.Credential;
import com.kavuna.udacity.cloudstorage.service.CredentialService;
import com.kavuna.udacity.cloudstorage.service.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialManagementTests {

    @LocalServerPort
    public int port;

    //pulling in the credentialService
    @Resource(name = "credentialService")
    public CredentialService credentialService;

    public static WebDriver driver;
    public static String baseURL;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll() {
        driver.quit();
        driver = null;
    }

    @BeforeEach
    public void beforeEach() {
        baseURL = "http://localhost:" + port;
        String username = "pzastoup";
        String password = "whatabadpassword";

        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("Peter", "Zastoupil", username, password);

        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }

    @Test
    @DisplayName("Testing credential creation, and verifies newly added credential is displayed.")
    public void testAddCredential() {

        String credentialUrl = "http://kavuna.net";
        String credentialUsername = "someusername";
        String credentialPassword = "badpassword";

        //navigate to the notes tab to fill the add note form and submit
        HomePage homePage = new HomePage(driver);
        homePage.clickCredentialsTab();
        homePage.clickAddCredentialButton();
        homePage.fillAndSubmitAddCredentialForm(credentialUrl, credentialUsername, credentialPassword);

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        //checks if the newly added url is found on the page
        assertTrue(homePage.getCredentialUrls().contains(credentialUrl));

        //checks if the newly added username is found on the page
        assertTrue(homePage.getCredentialUsernames().contains(credentialUsername));

        //ensures that the plain text  password is found on the page
        assertTrue(homePage.getCredentialPasswords().contains(credentialPassword));

        /**
         * For each credential on the home page find the corresponding record in the db and ensures that the urls and
         * usernames are the same. This also ensures that the corresponding db password is encrypted
         */
        for (Credential credential : homePage.getCredentials()) {
            Credential dbCredential = credentialService.getCredential(credential.getCredentialId());
            assertEquals(dbCredential.getUsername(), credentialUsername); //usernames match
            assertEquals(dbCredential.getUrl(), credentialUrl); //urls match
            assertTrue(Base64.isBase64(dbCredential.getPassword())); //password in the db is encrypted
            assertFalse(Base64.isBase64(credential.getPassword())); //password on the page is plain text
        }
    }

    @Test
    @DisplayName("Verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.")
    public void testUpdateCredential(){

        //begin by adding a credential record
        String credentialUrl = "http://kavuna.net";
        String credentialUsername = "someusername";
        String credentialPassword = "badpassword";

        //navigate to the notes tab to fill the add note form and submit
        HomePage homePage = new HomePage(driver);
        homePage.clickCredentialsTab();
        homePage.clickAddCredentialButton();
        homePage.fillAndSubmitAddCredentialForm(credentialUrl, credentialUsername, credentialPassword);

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        String credentialUrlUpdated = "http://kavuna.com";
        String credentialUsernameUpdated = "goodusername";
        String credentialPasswordUpdated = "g00dP@ssw0d123";

        homePage.clickCredentialsTab();
        homePage.clickCredentialEditButton();
        homePage.fillAndSubmitAddCredentialForm(credentialUrlUpdated, credentialUsernameUpdated, credentialPasswordUpdated);

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        //checks if the newly added url is found on the page
        assertTrue(homePage.getCredentialUrls().contains(credentialUrlUpdated));

        //checks if the newly added username is found on the page
        assertTrue(homePage.getCredentialUsernames().contains(credentialUsernameUpdated));

        //ensures that the plain text  password is found on the page
        assertTrue(homePage.getCredentialPasswords().contains(credentialPasswordUpdated));
    }
    @DisplayName("Test deletes an existing a credential and verifies that it is no longer displayed.")
    @Test
    public void testDeleteCredential(){

        //begin by adding a credential record
        String credentialUrl = "http://kavuna.net";
        String credentialUsername = "someusername";
        String credentialPassword = "badpassword";

        //navigate to the notes tab to fill the add note form and submit
        HomePage homePage = new HomePage(driver);
        homePage.clickCredentialsTab();
        homePage.clickAddCredentialButton();
        homePage.fillAndSubmitAddCredentialForm(credentialUrl, credentialUsername, credentialPassword);

        //go to the home page and navigate to credentials tab
        driver.get(baseURL + "/home");
        homePage.clickCredentialsTab();

        //checks if the newly added url is found on the page
        assertTrue(homePage.getCredentialUrls().contains(credentialUrl));

        //checks if the newly added username is found on the page
        assertTrue(homePage.getCredentialUsernames().contains(credentialUsername));

        //ensures that the plain text  password is found on the page
        assertTrue(homePage.getCredentialPasswords().contains(credentialPassword));

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        //go to the home page and navigate to credentials tab
        driver.get(baseURL + "/home");
        homePage.clickCredentialsTab();

        homePage.clickCredentialDeleteButton();

        //back to credentials tab
        driver.get(baseURL + "/home");
        homePage.clickCredentialsTab();

        //checks that the url of the just deleted url is no longer on the page
        assertFalse(homePage.getCredentialUrls().contains(credentialUrl));

        //checks if the username of the just deleted credential is no longer on the page
        assertFalse(homePage.getCredentialUsernames().contains(credentialUsername));

        //ensures that the plain text  password is found on the page
        assertFalse(homePage.getCredentialPasswords().contains(credentialPassword));
    }
}
