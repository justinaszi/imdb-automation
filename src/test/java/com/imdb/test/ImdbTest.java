package com.imdb.test;

import com.codeborne.selenide.Configuration;
import com.imdb.test.pages.HomePage;
import com.imdb.test.pages.TitlePage;
import com.imdb.test.pages.ActorProfilePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.codeborne.selenide.SelenideElement;

import java.util.List;

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
        HomePage homePage = new HomePage();
        homePage.open();
        homePage.acceptCookiesIfVisible();
        homePage.searchFor("QA");

        List<SelenideElement> titleSuggestions = homePage.getTitleSuggestions();
        SelenideElement firstTitle = titleSuggestions.get(0);
        String firstTitleText = firstTitle.$(".searchResult__constTitle").getText().trim();
        System.out.println("Selected title from search: " + firstTitleText);
        firstTitle.click();

        TitlePage titlePage = new TitlePage();
        titlePage.verifyTitle(firstTitleText);

        SelenideElement thirdProfile = titlePage.getCastMember(2); // 3rd cast member (0-based index)
        String thirdActorName = titlePage.getActorName(thirdProfile);
        System.out.println("Expected actor name (from cast): " + thirdActorName);
        titlePage.clickActor(thirdProfile);

        ActorProfilePage actorPage = new ActorProfilePage();
        actorPage.verifyActorName(thirdActorName);
    }
}
