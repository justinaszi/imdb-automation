package com.imdb.test.pages;

import com.codeborne.selenide.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class TitlePage {

    // Verifies that the page's main title matches the expected title
    public void verifyTitle(String expectedTitle) {
        $("h1").shouldBe(visible).shouldHave(text(expectedTitle));
    }

    // Returns a List of visible cast members (minimum 4 required)
    public List<SelenideElement> getVisibleCast() {
        ElementsCollection castList = $$("[data-testid='title-cast-item']").filter(visible);
        castList.shouldHave(CollectionCondition.sizeGreaterThan(3));
        return castList.asFixedIterable().stream().collect(Collectors.toList()); // ✅ Compatible with Java 8+
    }

    // Retrieves the cast member at a given index (0-based)
    public SelenideElement getCastMember(int index) {
        List<SelenideElement> cast = getVisibleCast();
        if (index >= cast.size()) {
            throw new IllegalArgumentException("Cast index " + index + " is out of bounds. Found " + cast.size() + " members.");
        }
        return cast.get(index);
    }

    // Extracts the actor's name from a cast element
    public String getActorName(SelenideElement castMember) {
        SelenideElement actorLink = castMember.$("a").shouldBe(visible);
        return actorLink.find("span").exists()
                ? actorLink.find("span").text().trim()
                : actorLink.text().trim();
    }

    // Scrolls to and clicks the actor's profile link
    public void clickActor(SelenideElement castMember) {
        castMember.$("a").shouldBe(visible).scrollIntoView(true).click();
    }
}
