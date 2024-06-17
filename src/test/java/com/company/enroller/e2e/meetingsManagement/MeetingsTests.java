package com.company.enroller.e2e.meetingsManagement;

import com.company.enroller.e2e.BasePage;
import com.company.enroller.e2e.BaseTests;
import com.company.enroller.e2e.Const;
import com.company.enroller.e2e.authentication.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class MeetingsTests extends BaseTests {

    WebDriver driver;
    MeetingsPage page;
    LoginPage loginPage;

    @BeforeEach
    void setup() {
        this.dbInit();
        this.driver = WebDriverManager.chromedriver().create();
        this.page = new MeetingsPage(driver);
        this.loginPage = new LoginPage(driver);
        this.page.get(Const.HOME_PAGE);
//        this.page.sleep(10000);
    }


    @Test
    @DisplayName("[SPOTKANIA.1] The meeting should be added to your meeting list. It should contain a title and description.")
    void addNewMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.addNewMeeting(Const.MEETING_II_TITLE, Const.MEETING_DESC);
        // Asserts
        assertThat(this.page.getMeetingByTitle(Const.MEETING_II_TITLE)).isNotNull();
        // TODO: Dodaj sprawdzenie czy poprawnie został dodany opis.
        assertThat(this.page.getMeetingByTitle(Const.MEETING_II_TITLE).getText()).contains(Const.MEETING_DESC);
        // TODO: Dodaj sprawdzenie czy zgadza się aktualna liczba spotkań.
        assertThat(this.page.getMeetings().size()).isEqualTo(Const.EXPECTED_MEETING_COUNT);
    }

    // TODO: Sprawdź czy użytkownik może dodać spotkanie bez nazwy. Załóż że nie ma takiej możliwości a warunkiem
    //  jest nieaktywny przycisk "Dodaj".

    @Test
    @DisplayName("[SPOTKANIA.2] The user should not be able to add a meeting without a title. The 'Add' button should be inactive.")
    void addMeetingWithoutTitle() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.addNewMeeting("", "");
        this.page.sleep(1); // checking without any cooldown caused to return that test failed
        WebElement addButton = driver.findElement(By.xpath("//button[contains(text(),'Dodaj')]"));
        assertThat(addButton.isEnabled()).isFalse();
    }


    // TODO: Sprawdź czy użytkownik może poprawnie zapisać się do spotkania.
    @Test
    @DisplayName("[SPOTKANIA.3] The user should be able to sign up for a meeting correctly.")
    void signUpForMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.addNewMeeting(Const.MEETING_III_TITLE, Const.MEETING_DESC);

        this.page.signUpForMeeting(Const.MEETING_III_TITLE);

        this.page.sleep(1); //  without any cooldown caused to return test failed
        assertThat(this.page.getParticipantsListForMeeting(Const.MEETING_III_TITLE)).contains(Const.USER_I_NAME);

    }

    // TODO: Sprawdź czy użytkownik może usunąć puste spotkanie.
    @Test
    @DisplayName("[SPOTKANIA.4] The user should be able to delete an empty meeting.")
    void deleteEmptyMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.addNewMeeting(Const.MEETING_IV_TITLE, "");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'Usuń puste spotkanie')]")));

        this.page.deleteMeeting(Const.MEETING_IV_TITLE);

        this.page.sleep(1);
        // Assert that the meeting row is no longer present
        assertThat(this.page.getMeetings()).doesNotContain(this.page.getMeetingByTitle(Const.MEETING_IV_TITLE));
    }

    @AfterEach
    void exit() {
        this.page.quit();
        this.removeAllMeeting();
    }

}
