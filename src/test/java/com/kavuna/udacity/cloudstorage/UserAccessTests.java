package com.kavuna.udacity.cloudstorage;

import com.kavuna.udacity.cloudstorage.pages.HomePage;
import com.kavuna.udacity.cloudstorage.pages.LoginPage;
import com.kavuna.udacity.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class UserAccessTests {
    @LocalServerPort
    public int port;

    public static WebDriver driver;
    public String baseURL;

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
    }

    @Test
    @DisplayName("Verifies that an unauthorized user can only access the login and signup pages")
    public void testUnauthorizedUserAccess(){
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals(driver.getTitle(), "Login");

        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        assertEquals(driver.getTitle(), "Sign Up");

        driver.get(baseURL + "/home");
        HomePage homePage = new HomePage(driver);
        assertEquals(driver.getTitle(), "Login");
    }


    @Test
    @DisplayName("Verifies user signup, login, home page access, logout out and ensures home page is no longer accessible.")
    public void testSignUpLoginLogout() {

        String username = "pzastoup";
        String password = "whatabadpassword";

        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("Peter", "Zastoupil", username, password);

        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        driver.get(baseURL + "/home");
        HomePage homePage = new HomePage(driver);
        assertEquals(driver.getTitle(), "Home");

        driver.get(baseURL + "/logout");
        HomePage logoutPage = new HomePage(driver);
        assertEquals(driver.getTitle(), "Login");

        driver.get(baseURL + "/home");
        assertEquals(driver.getTitle(), "Login");
    }
}
