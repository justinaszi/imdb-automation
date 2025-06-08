package com.imdb.test;

import com.codeborne.selenide.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class ImdbTest {

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 8000;
    }

    @Test(description = "Verify IMDb search and navigation to cast profile")
    public void testImdbQAFlow() {
        open("https://www.imdb.com");

        // Accept cookies if the prompt appears
        if ($x("//button[text()='Accept']").exists()) {
            $x("//button[text()='Accept']").click();
        }

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

        // Click first valid title and extract actual title text only
        SelenideElement firstTitle = titleSuggestions.get(0);
        String firstTitleText = firstTitle.$(".searchResult__constTitle").getText().trim();

        System.out.println("Selected title from search: " + firstTitleText);

        firstTitle.click();

        // Validate title on detail page (strict match only on title)
        $("h1").shouldBe(visible).shouldHave(text(firstTitleText));

        // Validate at least 4 visible cast members
        ElementsCollection cast = $$("[data-testid='title-cast-item']").filter(visible);
        cast.shouldHave(CollectionCondition.sizeGreaterThan(3));

// Click 3rd cast member and save name
        SelenideElement thirdProfile = cast.get(2);
        SelenideElement actorLink = thirdProfile.$("a").shouldBe(visible);

// Try to get nested <span> text if present
        String thirdActorName = actorLink.find("span").exists()
                ? actorLink.find("span").text().trim()
                : actorLink.text().trim();

        System.out.println("Expected actor name (from cast): " + thirdActorName);

// Scroll and click
        actorLink.scrollIntoView(true).click();

// Validate actor profile page
        SelenideElement actorPageHeader = $("h1").shouldBe(visible);
        String actualProfileName = actorPageHeader.getText().trim();
        System.out.println("Actual profile page name: " + actualProfileName);

        actorPageHeader.shouldHave(matchText("(?i).*" + Pattern.quote(thirdActorName) + ".*"));
    }
}
