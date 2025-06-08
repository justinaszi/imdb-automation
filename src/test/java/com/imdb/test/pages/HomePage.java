package com.imdb.test.pages;

import com.codeborne.selenide.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    // Opens IMDb homepage
    public void open() {
        Selenide.open("https://www.imdb.com");
    }

    // Accept cookies if the popup appears
    public void acceptCookiesIfVisible() {
        SelenideElement acceptButton = $x("//button[text()='Accept']");
        if (acceptButton.exists()) {
            acceptButton.click();
        }
    }

    // Enters a query in the search bar
    public void searchFor(String query) {
        $("[name='q']").setValue(query);
    }

    // Gets filtered search suggestions that link to titles
    public List<SelenideElement> getTitleSuggestions() {
        ElementsCollection allSuggestions = $$(".react-autosuggest__suggestions-list li a");
        allSuggestions.shouldHave(CollectionCondition.sizeGreaterThan(1));

        return allSuggestions.asFixedIterable().stream()
                .filter(el -> {
                    String href = el.getAttribute("href");
                    return href != null && href.contains("/title/");
                })
                .collect(Collectors.toList());
    }
}
