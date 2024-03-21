package investigation.hub.common.core.services.api.requests.services;

import investigation.hub.common.core.services.api.requests.authorization.Authorizer;
import investigation.hub.common.core.services.api.utils.RestAssuredWrapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

/**
 * Interactions with separate non-related endpoints
 * */
public class EndpointsRestRequestService extends AbstractRequestService {

    private static final String V1_UPLOAD_FILE = "/v1/upload/file";
    public static final String MULTPRT_FILE_KEY = "file";
    public static final String MULTPRT_XLSX_FILE_VAL = "file.xlsx";
    public static final String MULTPRT_MIME_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String FRM_PRM_FILE_KEY = "file";
    public static final String FRM_PRM_FILE_VAL = "demo.xlsx";
    public static final String FRM_PRM_BUCKET_KEY = "bucket";
    public static final String FRM_PRM_BUCKET_VAL = "alertsattachments";
    public static final String FRM_PRM_NAME_KEY = "name";
    public static final String FRM_PRM_NAME_VAL = "Test.xlsx";

    public EndpointsRestRequestService(@NonNull Authorizer authorizer) {
        super(authorizer);
    }

    @SneakyThrows
    public Response sendFileToEndpointV1UploadFile(File file) {
        RequestSpecification reqSpec = authorizer.produceRequestWithAuthData()
                .setBasePath(V1_UPLOAD_FILE)
                .addMultiPart(MULTPRT_FILE_KEY, MULTPRT_XLSX_FILE_VAL, Files.readAllBytes(file.toPath()),
                        MULTPRT_MIME_TYPE_XLSX)
                .addFormParam(FRM_PRM_FILE_KEY, FRM_PRM_FILE_VAL)
                .addFormParam(FRM_PRM_BUCKET_KEY, FRM_PRM_BUCKET_VAL)
                .addFormParam(FRM_PRM_NAME_KEY, FRM_PRM_NAME_VAL)
                .build();

        return RestAssuredWrapper.request().spec(reqSpec).post();
    }
}
