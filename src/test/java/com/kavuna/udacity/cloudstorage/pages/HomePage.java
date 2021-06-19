package com.kavuna.udacity.cloudstorage.pages;

import com.kavuna.udacity.cloudstorage.model.Credential;
import com.kavuna.udacity.cloudstorage.service.CredentialService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class HomePage {

    // driver;
    private final WebDriver webDriver;

    //notes fields
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(css = "#note-description")
    private WebElement noteDescriptionField;

    @FindBy(css = "#noteSubmit")
    private WebElement noteSubmitButton;

    @FindBy(className = "noteTitle")
    private WebElement noteTitles;

    @FindBy(name = "note-edit-button")
    private WebElement noteEditButton;

    @FindBy(name = "note-delete-button")
    private WebElement noteDeleteButton;


    //credentials fields
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id="add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(css = "#credential-url")
    private WebElement credentialUrlField;

    @FindBy(css = "#credential-username")
    private WebElement credentialUsernameField;

    @FindBy(css = "#credential-password")
    private WebElement credentialPasswordField;

    @FindBy(css = "#credentialSubmit")
    private WebElement credentialSubmitButton;

    @FindBy(name = "credential-edit-button")
    private WebElement credentialEditButton;

    @FindBy(name = "credential-delete-button")
    private WebElement credentialDeleteButton;

    //constructor
    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;}

    //clicks Notes tab link
    public void clickNotesTab() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.notesTab);
    }

    //fires + Add a New Note
    public void clickAddNoteButton() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.addNoteButton);
    }

    public void clickNoteEditButton() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteEditButton);
    }

    public void clickNoteDeleteButton() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteDeleteButton);
    }

    //contains actions for adding a note, starting with clicking on the Notes tab link
    public void fillAndSubmitAddNoteForm(String noteTitle, String noteDescription) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + noteTitle + "';", this.noteTitleField);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + noteDescription + "';", this.noteDescriptionField);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteSubmitButton);
    }

    public int getNumberOfNotes() {
        return (webDriver.findElements(By.className("noteTitle"))).size();
    }

    public List<String> getNoteTitles() {

        List<String> titles = new ArrayList<>();
        for (WebElement element : webDriver.findElements(By.className("noteTitle"))) {
            System.out.println(element.getAttribute("innerHTML"));
            titles.add(element.getAttribute("innerHTML"));
        }
        return titles;
    }

    /**
     * methods related to credential management
     */
    public void clickCredentialsTab() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.credentialsTab);
    }
    //fires the button that pulls up the form to submit a new credential
    public void clickAddCredentialButton() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.addCredentialButton);
    }

    public void clickCredentialEditButton() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.credentialEditButton);
    }

    public void clickCredentialDeleteButton() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.credentialDeleteButton);
    }

    //contains actions for adding a note, starting with clicking on the Notes tab link
    public void fillAndSubmitAddCredentialForm(String url, String username, String password) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + url + "';", this.credentialUrlField);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + username + "';", this.credentialUsernameField);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + password + "';", this.credentialPasswordField);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.credentialSubmitButton);
    }

    public int getNumberOfCredentials() {
        return (webDriver.findElements(By.className("credentialUrl"))).size();
    }
    public List<String> getCredentialUrls() {
        List<String> urls = new ArrayList<>();
        for (WebElement element : webDriver.findElements(By.className("credentialUrl"))) {
            urls.add(element.getAttribute("innerHTML"));
        }
        return urls;
    }
    public List<String> getCredentialUsernames() {
        List<String> usernames = new ArrayList<>();
        for (WebElement element : webDriver.findElements(By.className("credentialUsername"))) {
            usernames.add(element.getAttribute("innerHTML"));
        }
        return usernames;
    }

    public List<String> getCredentialPasswords() {
        List<String> passwords = new ArrayList<>();
        for (WebElement element : webDriver.findElements(By.className("credentialPassword"))) {
            passwords.add(element.getAttribute("innerHTML"));
        }
        return passwords;
    }

    //returns a List of all the credentials displayed on the page
    public List<Credential> getCredentials() {
        List<Credential> credentials = new ArrayList<>();
        for (WebElement element : webDriver.findElements(By.className("credential-row"))) {
            Credential credential = new Credential();
            credential.setCredentialId(Integer.parseInt(element.getAttribute("name")));
            String url = element.findElement(By.className("credentialUrl")).getAttribute("innerHTML");
            String username = element.findElement(By.className("credentialUsername")).getAttribute("innerHTML");
            String password = element.findElement(By.className("credentialPassword")).getAttribute("innerHTML");
        }
        return credentials;
    }
}