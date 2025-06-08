package com.imdb.test.pages;

import com.codeborne.selenide.SelenideElement;

import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class ActorProfilePage {

    // Verifies the actor's profile header contains the expected name (case-insensitive, partial match allowed)
    public void verifyActorName(String expectedName) {
        SelenideElement actorHeader = $("h1").shouldBe(visible);
        String actualName = actorHeader.getText().trim();

        System.out.println("Expected actor name: " + expectedName);
        System.out.println("Actual profile page name: " + actualName);

        actorHeader.shouldHave(matchText("(?i).*" + Pattern.quote(expectedName) + ".*"));
    }
}
