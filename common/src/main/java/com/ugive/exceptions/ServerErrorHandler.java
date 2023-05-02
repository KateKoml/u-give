//package com.ugive.exceptions;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.util.StreamUtils;
//import org.springframework.web.client.DefaultResponseErrorHandler;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//
//public class ServerErrorHandler extends DefaultResponseErrorHandler {
//    private final Logger log = LoggerFactory.getLogger(ServerErrorHandler.class);
//
//    @Override
//    public void handleError(ClientHttpResponse response) throws IOException {
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            response.getBody();
//            String responseBody = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
//            if (log.isErrorEnabled()) {
//                log.error(String.format("%s%n%s", response.getStatusCode().toString(), responseBody),
//                        new ThrowableSupplier<String>() {
//                            @Override
//                            public String get() {
//                                return responseBody;
//                            }
//                        });
//            }
//        }
//    }
//}
