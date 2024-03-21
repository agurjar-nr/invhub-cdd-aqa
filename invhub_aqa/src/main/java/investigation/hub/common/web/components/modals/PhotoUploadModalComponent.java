package investigation.hub.common.web.components.modals;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.components.CreateUserPageComponent;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

@Log4j2
@ModalComponent
public class PhotoUploadModalComponent {

    private final SelenideElement container = $(".uppy-Dashboard-inner");

    @Step("User uploads photo")
    public PhotoUploadModalComponent uploadPhoto(String photo) {
        container
                .$x(".//input[@name='files[]']")
                .as("Upload photo area")
                .uploadFile(new File(photo));
        log.info("Upload photo");
        return this;
    }

    @Step("User clicks upload 1 file button")
    public PhotoUploadModalComponent clickUpload1FileButton() {
        container
                .$x(".//button[@aria-label = 'Upload 1 file']")
                .as("Upload 1 file button")
                .click();
        log.info("Click 'Upload 1 file' button");
        return this;
    }

    @Step("User clicks done button")
    public CreateUserPageComponent clickDoneButton() {
        container
                .$x(".//button[contains(text(),'Done')]")
                .as("Done button")
                .click();
        log.info("Click 'Done' button");
        return new CreateUserPageComponent();
    }
}
