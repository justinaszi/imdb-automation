package com.imdb.test;

import com.codeborne.selenide.SelenideElement;
import com.imdb.test.pages.ActorProfilePage;
import com.imdb.test.pages.HomePage;
import com.imdb.test.pages.TitlePage;
import io.qameta.allure.*;

import org.testng.annotations.Test;

import java.util.List;

@Epic("IMDb Regression Suite")
@Feature("Search and Cast Profile Navigation")
public class ImdbTest extends BaseTest {

    private static final String SEARCH_QUERY = "QA";
    private static final int CAST_INDEX = 2; // 0-based index for 3rd cast member

    @Test(description = "Verify IMDb search and navigation to cast profile")
    @Story("Navigate from title search to actor profile")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks that a user can search for '" + SEARCH_QUERY + "', select the first title, view cast, and navigate to the 3rd actor's profile.")
    public void testImdbQAFlow() {

        HomePage homePage = new HomePage();
        TitlePage titlePage = new TitlePage();
        ActorProfilePage actorPage = new ActorProfilePage();

        Allure.step("Open IMDb homepage", homePage::open);
        Allure.step("Accept cookies if visible", homePage::acceptCookiesIfVisible);

        Allure.step("Search for '" + SEARCH_QUERY + "'", () -> homePage.searchFor(SEARCH_QUERY));

        List<SelenideElement> titleSuggestions = Allure.step("Get title suggestions", homePage::getTitleSuggestions);

        SelenideElement firstTitle = titleSuggestions.get(0);
        String firstTitleText = firstTitle.$(".searchResult__constTitle").getText().trim();
        Allure.step("Selected title from search: " + firstTitleText);
        firstTitle.click();

        Allure.step("Verify that title page matches selected title", () ->
                titlePage.verifyTitle(firstTitleText));

        SelenideElement thirdProfile = Allure.step("Get cast member #" + (CAST_INDEX + 1), () -> titlePage.getCastMember(CAST_INDEX));
        String thirdActorName = titlePage.getActorName(thirdProfile);
        Allure.step("Cast member #" + (CAST_INDEX + 1) + " name: " + thirdActorName);

        Allure.step("Click on cast member #" + (CAST_INDEX + 1), () -> titlePage.clickActor(thirdProfile));

        Allure.step("Verify actor profile page is correct", () ->
                actorPage.verifyActorName(thirdActorName));
    }
}
