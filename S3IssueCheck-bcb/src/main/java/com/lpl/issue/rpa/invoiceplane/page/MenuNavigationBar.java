package com.lpl.issue.rpa.invoiceplane.page;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

import com.lpl.issue.rpa.RobotDriverWrapper;

public class MenuNavigationBar extends RobotDriverWrapper {

    @FindBy(xpath = "//a[contains(@class,'logout')]")
    private WebElement logoutButton;

    @FindBy(xpath = "//*[@id='ip-navbar-collapse']//li/a[text()='Dashboard']")
    private WebElement dashboardMenu;

    @FindBy(xpath = "//*[@id='ip-navbar-collapse']//li/a//span[text()='Products']/parent::*")
    private WebElement productsMenu;

    @FindBy(xpath = "//*[@id='ip-navbar-collapse']//li/ul/li/a[text()='View products']")
    private WebElement viewProductsMenuItem;

    public MenuNavigationBar(Logger logger) {
        super(logger);
    }

    public ProductsPage openProducts() {
        productsMenu.click();
        viewProductsMenuItem.click();

        return new ProductsPage(logger);
    }

    public void openDashboard() {
        dashboardMenu.click();
    }

    // If it is necessary to logout from Invoice Plane explicitly
    public void logout() {
        try {
            logoutButton.click();
        } catch (TimeoutException ex) {
            logger.info("Timed out on waiting logout button");
        }
    }

}