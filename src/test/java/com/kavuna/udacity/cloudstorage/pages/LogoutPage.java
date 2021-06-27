package com.kavuna.udacity.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LogoutPage {

    @Autowired
    public LogoutPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

}