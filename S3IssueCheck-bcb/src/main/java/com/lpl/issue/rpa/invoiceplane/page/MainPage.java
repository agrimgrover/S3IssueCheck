package com.lpl.issue.rpa.invoiceplane.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

import com.lpl.issue.rpa.RobotDriverWrapper;

public class MainPage extends RobotDriverWrapper {

    @FindBy(xpath = "//div[@id='panel-quick-actions']//a//span[text()='Add Client']/parent::*")
    private WebElement addClientButton;

    public MainPage(Logger logger) {
        super(logger);
    }

    public void addClient() {
        addClientButton.click();
    }

    public MenuNavigationBar getMenuNavigationBar() {
        return new MenuNavigationBar(logger);
    }

}