package com.kavuna.udacity.cloudstorage;

import com.kavuna.udacity.cloudstorage.pages.HomePage;
import com.kavuna.udacity.cloudstorage.pages.LoginPage;
import com.kavuna.udacity.cloudstorage.pages.ResultPage;
import com.kavuna.udacity.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class NoteManagementTests {
    @LocalServerPort
    public int port;

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
    @Order(1)
    @DisplayName("Testing note creation, and verifies newly added note is displayed.")
    public void testAddNote() {

        String noteTitle = "Some testing note";
        String noteDescription = "This is a testing note to ensure that adding notes works.";

        //navigate to the notes tab to fill the add note form and submit
        HomePage homePage = new HomePage(driver);
        homePage.clickNotesTab();
        homePage.clickAddNoteButton();
        homePage.fillAndSubmitAddNoteForm(noteTitle, noteDescription);

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        //move back to the home page since adding a new note redirects to /result
        driver.get(baseURL+"/home");

        //activate notes tab
        homePage.clickNotesTab();

        //checks if the newly added title is found on the page
        assertTrue(homePage.getNoteTitles().contains(noteTitle));

    }

    @Order(2)
    @Test
    @DisplayName("Testing editing an existing note and verifying that the changes are displayed")
    public void testNoteEdit(){

        String noteTitle = "Some testing note";
        String noteDescription = "This is a testing note to ensure that adding notes works.";

        String noteTitleUpdated = "updated testing note";
        String noteDescriptionUpdated = "This note has been updated for testing purposes.";

        HomePage homePage = new HomePage(driver);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //activate notes tab
        homePage.clickNotesTab();

        //navigate to the notes tab to fill the add note form and submit
        homePage.clickNotesTab();
        homePage.clickAddNoteButton();
        homePage.fillAndSubmitAddNoteForm(noteTitle, noteDescription);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //back to home page
        driver.get(baseURL+"/home");

        //activate notes tab
        homePage.clickNotesTab();

        //trigger edit form
        homePage.clickNoteEditButton();

        //make updates and submit
        homePage.fillAndSubmitAddNoteForm(noteTitleUpdated, noteDescriptionUpdated);

        //back to the home page again
        driver.get(baseURL+"/home");

        //activate notes tab
        homePage.clickNotesTab();

        //the updated title must be on the page
        assertTrue(homePage.getNoteTitles().contains(noteTitleUpdated));
    }
    @Order(3)
    @Test
    @DisplayName("Testing deleting an existing note and verifying that the changes are displayed")
    public void testNoteDelete(){

        String noteTitle = "Some testing note";
        String noteDescription = "This is a testing note to ensure that adding notes works.";

        HomePage homePage = new HomePage(driver);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //activate notes tab
        homePage.clickNotesTab();

        //navigate to the notes tab to fill the add note form and submit
        homePage.clickNotesTab();
        homePage.clickAddNoteButton();
        homePage.fillAndSubmitAddNoteForm(noteTitle, noteDescription);

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        //move back to the home page since adding a new note redirects to /result
        driver.get(baseURL+"/home");

        //activate notes tab
        homePage.clickNotesTab();

        //there should be 1 note displayed on the home page
        assertEquals(1, homePage.getNumberOfNotes());

        //click the delete button
        homePage.clickNoteDeleteButton();

        //move back to the home page since deleting a note redirects to /result
        driver.get(baseURL+"/home");

        //activate notes tab
        homePage.clickNotesTab();

        //there should be 0 note displayed on the home page after the one note available has been deleted
        assertEquals(0, homePage.getNumberOfNotes());
    }
}
