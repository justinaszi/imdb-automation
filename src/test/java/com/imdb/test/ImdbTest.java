package com.imdb.test;

import com.codeborne.selenide.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class ImdbTest {

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    @Test(description = "Verify IMDb search and navigation to cast profile")
    public void testImdbQAFlow() {
        open("https://www.imdb.com");

        // Type search query
        $("[name='q']").setValue("QA");

        // Wait for suggestions to appear
        ElementsCollection allSuggestions = $$(".react-autosuggest__suggestions-list li a");
        allSuggestions.shouldHave(CollectionCondition.sizeGreaterThan(1));

        // Manually filter suggestions with "/title/" in href
        List<SelenideElement> titleSuggestions = new ArrayList<>();
        for (SelenideElement el : allSuggestions) {
            String href = el.getAttribute("href");
            if (href != null && href.contains("/title/")) {
                titleSuggestions.add(el);
            }
        }

        if (titleSuggestions.isEmpty()) {
            throw new AssertionError("No valid title suggestions found.");
        }

        // Click first valid title
        SelenideElement firstTitle = titleSuggestions.get(0);
        String firstTitleText = firstTitle.getText();
        firstTitle.click();

        // Validate title on detail page
        $("h1").shouldBe(visible).shouldHave(text(firstTitleText));

        // Validate at least 4 cast members
        ElementsCollection cast = $$(".ipc-metadata-list-summary-item__c").filter(visible);
        cast.shouldHave(CollectionCondition.sizeGreaterThan(3));

        // Click 3rd cast member and save name
        SelenideElement thirdProfile = cast.get(2);
        String thirdActorName = thirdProfile.$("a").getText();
        thirdProfile.$("a").click();

        // Validate actor profile page
        $("h1").shouldBe(visible).shouldHave(text(thirdActorName));
    }
}
