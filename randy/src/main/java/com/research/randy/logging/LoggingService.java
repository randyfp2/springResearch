package com.research.randy.logging;
import com.research.randy.model.logging.LoggingModel;
import com.research.randy.repository.LoggingDBRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LoggingService {

    private static final Logger loggerTransactional = LogManager.getLogger("com.research.randy.transactional");
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Autowired
    private LoggingDBRepository researchLogRepository;
    public void logTransaction(String serviceUrl,String reffNo, String partnerReferenceNo, String responseCode, String responseMessage,
                               String incomingRequestTime, String incomingRequest,String apiExternalURL, String apiExternalRequest,
                               String apiExternalResponse, String apiExternalHttpStatus, String incomingResponse,
                               String incomingResponseTime) {

        String localIP = getLocalIpAddress();
        String logMessage = String.format(
                "ServiceUrl: %s | PartnerReferenceNo: %s | ResponseCode: %s | ResponseMessage: %s | IncomingRequestTime: %s | IncomingRequest: %s | apiExternalURL: %s | " +
                        "ApiExternalRequest: %s | ApiExternalResponse: %s | ApiExternalHttpStatus: %s | " +
                        "IncomingResponse: %s | IncomingResponseTime: %s | localIP: %s",
                serviceUrl,partnerReferenceNo, responseCode, responseMessage, incomingRequestTime, incomingRequest,apiExternalURL,
                apiExternalRequest, apiExternalResponse, apiExternalHttpStatus, incomingResponse, incomingResponseTime, localIP
        );
        loggerTransactional.info(logMessage);
        LoggingModel researchLog = new LoggingModel();
        researchLog.setServiceUrl(serviceUrl);
        researchLog.setReferenceNo(reffNo);
        researchLog.setPartnerReferenceNo(partnerReferenceNo);
        researchLog.setResponseCode(responseCode);
        researchLog.setResponseMessage(responseMessage);
        researchLog.setIncomingRequestTime(incomingRequestTime);
        researchLog.setIncomingRequest(incomingRequest);
        researchLog.setApiExternalURL(apiExternalURL);
        researchLog.setApiExternalRequest(apiExternalRequest);
        researchLog.setApiExternalResponse(apiExternalResponse);
        researchLog.setApiExternalHttpStatus(apiExternalHttpStatus);
        researchLog.setIncomingResponse(incomingResponse);
        researchLog.setIncomingResponseTime(incomingResponseTime);
        researchLog.setLocalIp(localIP);
        researchLogRepository.save(researchLog);
    /*
        loggerTransactional.info("in main class");
        loggerTransactional.info("info logging is printed");
        loggerTransactional.debug("logger debud is worked");
        loggerTransactional.warn("logging warn is worked");
    }

 */
    }

    private String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            loggerTransactional.error("Unable to get local IP address", e);
            return "Unknown IP";
        }
    }

    // Menyimpan log ke database

}
