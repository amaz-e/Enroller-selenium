package com.company.enroller.e2e.meetingsManagement;

import com.company.enroller.e2e.BasePage;
import com.company.enroller.e2e.Const;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

public class MeetingsPage extends BasePage {

    @FindBy(xpath = "//*[contains(text(), \"" + Const.NEW_MEETING_BTN_LABEL + "\")]")
    @CacheLookup
    private WebElement addNewMeetingBtn;

    @FindBy(css = "form > input")
    private WebElement meetingTitleInput;

    @FindBy(css = "form > textarea")
    private WebElement meetingDescInput;

    @FindBy(css = "form > button")
    private WebElement confirmMeetingBtn;


    public MeetingsPage(WebDriver driver) {
        super(driver);
    }

    public void addNewMeeting(String title, String desc) {
        this.click(this.addNewMeetingBtn);
        this.meetingTitleInput.sendKeys(title);
        this.meetingDescInput.sendKeys(desc);
        this.click(this.confirmMeetingBtn);

    }

    public void signUpForMeeting(String meetingTitle) {
        WebElement meetingRow = this.getMeetingByTitle(meetingTitle);
        WebElement signUpButton = meetingRow.findElement(By.xpath("//button[contains(text(),'Zapisz się')]"));

        this.click(signUpButton);
    }

    public void deleteMeeting(String meetingTitle) {
        WebElement meetingRow = this.getMeetingByTitle(meetingTitle);
        WebElement deleteMeetingButton = meetingRow.findElement(By.xpath("//button[contains(text(),'Usuń puste spotkanie')]"));
        this.click(deleteMeetingButton);
    }
}
