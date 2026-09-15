package com.jimmy.portal.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    private static final String REQUEST_ID = "requestId";
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    private static final String REDACTED = "*****";

    private static final int MAX_PAYLOAD_SIZE = 10_000;

    private final ObjectMapper objectMapper;

    public RequestLoggingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestId = getRequestId(request);

        MDC.put(REQUEST_ID, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        long startTime = System.nanoTime();

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(
                        request,
                        MAX_PAYLOAD_SIZE
                );

        log.info(
                "HTTP_REQUEST_STARTED method={} uri={}",
                request.getMethod(),
                request.getRequestURI()
        );

        try {

            filterChain.doFilter(wrappedRequest, response);

        } finally {

            logRequestBody(wrappedRequest);

            long durationMs =
                    (System.nanoTime() - startTime) / 1_000_000;

            int status = response.getStatus();

            if (status >= 500) {

                log.error(
                        "HTTP_REQUEST_COMPLETED method={} uri={} status={} durationMs={}",
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        durationMs
                );

            } else if (status >= 400) {

                log.warn(
                        "HTTP_REQUEST_COMPLETED method={} uri={} status={} durationMs={}",
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        durationMs
                );

            } else {

                log.info(
                        "HTTP_REQUEST_COMPLETED method={} uri={} status={} durationMs={}",
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        durationMs
                );
            }

            MDC.remove(REQUEST_ID);
        }
    }

    private void logRequestBody(
            ContentCachingRequestWrapper request
    ) {

        // We only want JSON request bodies.
        String contentType = request.getContentType();

        if (contentType == null ||
                !contentType.toLowerCase().contains("application/json")) {
            return;
        }

        byte[] content = request.getContentAsByteArray();

        if (content.length == 0) {
            return;
        }

        String body =
                new String(content, StandardCharsets.UTF_8);

        String sanitizedBody = sanitizeBody(body);

        if (content.length >= MAX_PAYLOAD_SIZE) {

            log.info(
                    "HTTP_REQUEST_BODY payload={} truncated=true",
                    sanitizedBody
            );

        } else {

            log.info(
                    "HTTP_REQUEST_BODY payload={}",
                    sanitizedBody
            );
        }
    }

    private String sanitizeBody(String body) {

        try {

            JsonNode root =
                    objectMapper.readTree(body);

            redactPasswords(root);

            return objectMapper.writeValueAsString(root);

        } catch (Exception exception) {

            return "[NON_JSON_BODY]";
        }
    }

    private void redactPasswords(JsonNode node) {

        if (node.isObject()) {

            ObjectNode objectNode =
                    (ObjectNode) node;

            Iterator<Map.Entry<String, JsonNode>> fields =
                    objectNode.fields();

            while (fields.hasNext()) {

                Map.Entry<String, JsonNode> field =
                        fields.next();

                String fieldName = field.getKey();

                if (fieldName.equalsIgnoreCase("password")) {

                    objectNode.put(
                            fieldName,
                            REDACTED
                    );

                } else {

                    redactPasswords(field.getValue());
                }
            }

        } else if (node.isArray()) {

            for (JsonNode child : node) {
                redactPasswords(child);
            }
        }
    }

    private String getRequestId(
            HttpServletRequest request
    ) {

        String requestId =
                request.getHeader(REQUEST_ID_HEADER);

        if (requestId != null
                && !requestId.isBlank()
                && requestId.length() <= 100
                && requestId.matches(
                "[a-zA-Z0-9\\-_.]+"
        )) {

            return requestId;
        }

        return UUID.randomUUID().toString();
    }
}