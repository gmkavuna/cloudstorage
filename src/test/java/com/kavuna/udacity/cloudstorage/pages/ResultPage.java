package com.kavuna.udacity.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ResultPage {

    private final WebDriver webDriver;

    @FindBy(tagName = "a")
    private WebElement backHomeLink;

    @Autowired
    public ResultPage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }
    public void clickBackHomeLink(){
       backHomeLink.click();
    }
}
