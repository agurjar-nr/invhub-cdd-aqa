package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$;

import com.smile.components.PageComponent;
import investigation.hub.common.web.components.tables.ContentTablePageComponent;
import io.qameta.allure.Step;
import java.io.File;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PageComponent
@Getter
public class ContentPageComponent extends PageGeneralComponent {

    ContentTablePageComponent contentTable = new ContentTablePageComponent();

    @Step("User uploads File")
    public ContentPageComponent uploadFile(String file) {
        $("div[class='dropzone file-upload'] input")
                .as("Browse Files input")
                .uploadFile(new File(file));
        log.info("Upload file");
        return this;
    }
}
