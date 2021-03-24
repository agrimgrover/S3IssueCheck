package com.lpl.issue.rpa.invoiceplane.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

import com.lpl.issue.rpa.RobotDriverWrapper;
import com.lpl.issue.rpa.invoiceplane.to.ProductTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductsPage extends RobotDriverWrapper {

    @FindBy(xpath = "//div[@id='content']//table/tbody/tr")
    private List<WebElement> products;

    @FindBy(xpath = "//div[@id='headerbar']/div[@class='pull-right']//a[@title='First' and not(contains(@class,'disabled'))]")
    private WebElement firstPage;

    @FindBy(xpath = "//div[@id='headerbar']/div[@class='pull-right']//a[@title='Last' and not(contains(@class,'disabled'))]")
    private WebElement lastPage;

    @FindBy(xpath = "//div[@id='headerbar']/div[@class='pull-right']//a[@title='Next' and not(contains(@class,'disabled'))]")
    private WebElement nextPage;

    @FindBy(xpath = "//div[@id='headerbar']/div[@class='pull-right']//a[@title='Prev' and not(contains(@class,'disabled'))]")
    private WebElement prevPage;

    public ProductsPage(Logger logger) {
        super(logger);
    }

    public List<ProductTO> getProducts() {

        final List<ProductTO> result;
        if (products != null) {
            result = IntStream
                    .range(0, products.size())
                    .mapToObj((int index) -> mapWebElementToProduct(products.get(index), index))
                    .collect(Collectors.toList());
        } else {
            result = new ArrayList<>();
        }

        return result;
    }

    private ProductTO mapWebElementToProduct(WebElement product, long index) {
        Document doc = Jsoup.parse(product.getAttribute("outerHTML"), "", Parser.xmlParser());

        ProductTO productTO = new ProductTO();
        productTO.setIndex(index);
        productTO.setFamily(doc.select("td:nth-child(1)").text());
        productTO.setSku(doc.select("td:nth-child(2)").text());
        productTO.setProductName(doc.select("td:nth-child(3)").text());
        productTO.setProductDescription(doc.select("td:nth-child(4)").text());
        productTO.setPrice(doc.select("td:nth-child(5)").text());
        productTO.setTaxRate(doc.select("td:nth-child(6)").text());
        return productTO;
    }

    public boolean firstPage() {
        try {
            firstPage.click();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public boolean lastPage() {
        try {
            lastPage.click();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public boolean nextPage() {
        try {
            nextPage.click();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public boolean prevPage() {
        try {
            prevPage.click();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

}
