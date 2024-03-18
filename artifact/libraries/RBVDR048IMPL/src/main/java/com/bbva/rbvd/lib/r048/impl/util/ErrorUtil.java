package com.bbva.rbvd.lib.r048.impl.util;

import com.bbva.rbvd.dto.insuranceroyal.error.DetailsErrorDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorRequestDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ErrorUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorUtil.class);

    public static ErrorRequestDTO prepareRequestToHandlerError(RestClientException exception) {
        ErrorRequestDTO error = new ErrorRequestDTO();
        if(exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) exception;
            String requestBody = httpClientErrorException.getResponseBodyAsString();
            LOGGER.info("HttpClientErrorException - Response body: {}", requestBody);

            if(StringUtils.isEmpty(requestBody) || requestBody.contains("html")) {
                LOGGER.info("*** Null or empty error responseBody ***");
                return error;
            }

            JsonObject jsonResponseObject = new JsonParser().parse(requestBody).getAsJsonObject();
            JsonObject jsonErrorObject = jsonResponseObject.getAsJsonObject("error");
            if (Objects.nonNull(jsonErrorObject)){
                JsonObject jsonDetailsObject = jsonErrorObject.getAsJsonObject("details");
                JsonElement httpCode = jsonErrorObject.get("httpStatus");
                if (Objects.nonNull(jsonDetailsObject) && Objects.nonNull(httpCode)){
                    LOGGER.info("HttpClientErrorException - Details arrays: {}", jsonDetailsObject);
                    LOGGER.info("HttpClientErrorException - httpCode: {}", httpCode.getAsInt());
                    List<DetailsErrorDTO> detailsListr = new ArrayList<>();
                    for (Map.Entry<String, JsonElement> entry : jsonDetailsObject.entrySet()) {
                        DetailsErrorDTO errorDetail = new DetailsErrorDTO();
                        String code = entry.getKey();
                        String message = entry.getValue().getAsString();
                        errorDetail.setCode(code);
                        errorDetail.setValue(message);
                        detailsListr.add(errorDetail);
                    }
                    error.setDetails(detailsListr);
                    error.setHttpCode((long) httpCode.getAsInt());
                }
            }

            LOGGER.info("HttpClientErrorException - error -> {}", error);

            return error;
        }
        LOGGER.info("*** Non null or empty error responseBody with wrong structure ***");
        return error;
    }

    private ErrorUtil() {
    }
}
