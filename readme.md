# 🎬 IMDb Test Automation – Luminor Bank Assignment

This project is a UI test automation solution for IMDb, implemented as a job assignment for Luminor Bank. It automates a real-world regression test scenario using a modern tech stack and best practices like the Page Object Model (POM).

📁 GitHub Repository:
**[https://github.com/justinaszi/imdb-automation](https://github.com/justinaszi/imdb-automation)**

---

## ✅ Task Overview

> Automate the following test steps as a regression test case:

- Open [imdb.com](https://www.imdb.com)
- Search for "QA"
- Save the first title from the dropdown
- Click on the first title
- Verify the title matches
- Verify there are more than 3 cast members
- Click on the 3rd cast member
- Verify the correct actor profile page opens

---

## 🛠 Tech Stack

- **Java 17**
- **Gradle**
- **Selenide**
- **TestNG**
- **Allure Reporting**
- **Page Object Model (POM)** for structure and reusability

---

## 🚀 How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/justinaszi/imdb-automation.git
   cd imdb-automation
   ```
   
2. **Run Tests**
    ```bash
   ./gradlew clean test
    ```

3. **Generate and view Allure report:**
    ```bash
   ./gradlew allureReport
     ```
   ```bash
   ./gradlew allureServe
    ```