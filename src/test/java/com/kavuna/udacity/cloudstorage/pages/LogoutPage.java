package com.kavuna.udacity.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogoutPage {

    public LogoutPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

}