package com.yolt.providers.common.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Represents provider's documentation domain object, that contains encoded content.
 * It contains also strategies for its validation, reading and encoding.
 */
@Value
@RequiredArgsConstructor
public class ProviderInternalDocumentation {

    private static final String ALLOWED_EXTENSION = "md";
    private static final char LINE_SEPARATOR = '\n';

    String encodedContent;

    public static ProviderInternalDocumentation readDocumentationByPath(String documentationPath) {
        validateDocumentationPath(documentationPath);

        String documentation = readDocumentation(documentationPath);
        return new ProviderInternalDocumentation(encodeDocumentation(documentation));
    }

    private static void validateDocumentationPath(String documentationPath) {
        if (documentationPath == null) {
            throw new IllegalArgumentException("Provider documentation path can't be null");
        }

        String fileExtension = documentationPath.substring(documentationPath.lastIndexOf(".") + 1);
        if (!ALLOWED_EXTENSION.equalsIgnoreCase(fileExtension)) {
            throw new IllegalArgumentException("Provider documentation is only allowed as markdown, found documentation with extension: " + fileExtension);
        }
    }

    private static String readDocumentation(String documentationPath) {
        try {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(documentationPath)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append(LINE_SEPARATOR);
                }
            }
            return resultStringBuilder.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException("Found unexpected error when reading documentation", e);
        }
    }

    private static String encodeDocumentation(String documentation) {
        byte[] encode = Base64.getEncoder().encode(documentation.getBytes(StandardCharsets.UTF_8));
        return new String(encode, StandardCharsets.UTF_8);
    }
}
