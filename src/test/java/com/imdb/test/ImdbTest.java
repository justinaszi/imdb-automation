package com.imdb.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.imdb.test.pages.ActorProfilePage;
import com.imdb.test.pages.HomePage;
import com.imdb.test.pages.TitlePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

@Epic("IMDb Regression Suite")
@Feature("Search and Cast Profile Navigation")
public class ImdbTest {

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 8000;
    }

    @Test(description = "Verify IMDb search and navigation to cast profile")
    @Story("Navigate from title search to actor profile")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks that a user can search for 'QA', select the first title, view cast, and navigate to the 3rd actor's profile.")
    public void testImdbQAFlow() {

        HomePage homePage = new HomePage();
        TitlePage titlePage = new TitlePage();
        ActorProfilePage actorPage = new ActorProfilePage();

        Allure.step("Open IMDb homepage", homePage::open);
        Allure.step("Accept cookies if visible", homePage::acceptCookiesIfVisible);

        Allure.step("Search for 'QA'", () -> homePage.searchFor("QA"));

        List<SelenideElement> titleSuggestions = Allure.step("Get title suggestions", homePage::getTitleSuggestions);

        SelenideElement firstTitle = titleSuggestions.get(0);
        String firstTitleText = firstTitle.$(".searchResult__constTitle").getText().trim();
        Allure.step("Selected title from search: " + firstTitleText);
        firstTitle.click();

        Allure.step("Verify that title page matches selected title", () ->
                titlePage.verifyTitle(firstTitleText));

        SelenideElement thirdProfile = Allure.step("Get 3rd cast member", () -> titlePage.getCastMember(2));
        String thirdActorName = titlePage.getActorName(thirdProfile);
        Allure.step("3rd cast member name: " + thirdActorName);

        Allure.step("Click on 3rd cast member", () -> titlePage.clickActor(thirdProfile));

        Allure.step("Verify actor profile page is correct", () ->
                actorPage.verifyActorName(thirdActorName));
    }
}
