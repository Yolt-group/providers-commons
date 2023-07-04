package com.yolt.providers.common.domain.dynamic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.yolt.providers.common.ais.form.EncryptionDetails;
import com.yolt.providers.common.domain.TestObjectMapper;
import com.yolt.providers.common.domain.dynamic.step.FormStep;
import com.yolt.providers.common.domain.dynamic.step.RedirectStep;
import nl.ing.lovebird.providershared.AccessMeansDTO;
import nl.ing.lovebird.providershared.form.ExplanationField;
import nl.ing.lovebird.providershared.form.Form;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessMeansOrStepDTOTest {

    private Validator validator;

    private ObjectMapper objectMapper = TestObjectMapper.INSTANCE;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldSerializeAccessMeansOrStepDTOWithOnlyAccessMeans() throws JsonProcessingException {
        // given
        Date date = Date.from(Instant.parse("2018-12-18T10:15:30.00Z"));
        AccessMeansDTO accessMeansDTO = new AccessMeansDTO(UUID.randomUUID(), "accessMeans", date, date);
        AccessMeansOrStepDTO accessMeansOrStepDTO = new AccessMeansOrStepDTO(accessMeansDTO);

        // when
        String serialized = objectMapper.writeValueAsString(accessMeansOrStepDTO);

        // then
        ReadContext readJsonContext = JsonPath.parse(serialized);
        assertThat((String) readJsonContext.read("$.step")).isNull();
        assertThat((String) readJsonContext.read("$.accessMeans.userId")).isEqualTo(accessMeansDTO.getUserId().toString());
        assertThat((String) readJsonContext.read("$.accessMeans.accessMeansBlob")).isEqualTo("accessMeans");
        assertThat((String) readJsonContext.read("$.accessMeans.updated")).isEqualTo("2018-12-18T10:15:30.000+0000");
        assertThat((String) readJsonContext.read("$.accessMeans.expireTime")).isEqualTo("2018-12-18T10:15:30.000+0000");
    }

    @Test
    public void shouldSerializeAccessMeansOrStepDTOWithOnlyRedirectStep() throws JsonProcessingException {
        // given
        RedirectStep step = new RedirectStep("url", "externalId", "state");
        AccessMeansOrStepDTO accessMeansOrStepDTO = new AccessMeansOrStepDTO(step);

        // when
        String serialized = objectMapper.writeValueAsString(accessMeansOrStepDTO);

        // then
        ReadContext readJsonContext = JsonPath.parse(serialized);
        assertThat((String) readJsonContext.read("$.accessMeans")).isNull();
        assertThat((String) readJsonContext.read("$.step.type")).isEqualTo("REDIRECT_URL");
        assertThat((String) readJsonContext.read("$.step.redirectUrl")).isEqualTo("url");
        assertThat((String) readJsonContext.read("$.step.externalConsentId")).isEqualTo("externalId");
        assertThat((String) readJsonContext.read("$.step.providerState")).isEqualTo("state");
    }

    @Test
    public void shouldSerializeAccessMeansOrStepDTOWithOnlyFormStep() throws JsonProcessingException {
        // given
        Date date = Date.from(Instant.parse("2018-12-18T10:15:30.00Z"));
        Form form = new Form(Collections.emptyList(), new ExplanationField(), Collections.emptyMap());
        EncryptionDetails encryptionDetails = EncryptionDetails.noEncryption();
        FormStep step = new FormStep(form, encryptionDetails, date.toInstant(), "state");

        // when
        AccessMeansOrStepDTO accessMeansOrStepDTO = new AccessMeansOrStepDTO(step);

        // then
        String serialized = objectMapper.writeValueAsString(accessMeansOrStepDTO);
        ReadContext readJsonContext = JsonPath.parse(serialized);
        assertThat((String) readJsonContext.read("$.accessMeans")).isNull();
        assertThat((String) readJsonContext.read("$.step.type")).isEqualTo("FORM");

        assertThat((List<Object>) readJsonContext.read("$.step.form.formComponents")).isEmpty();
        assertThat((String) readJsonContext.read("$.step.form.explanationField.type")).isEqualTo("EXPLANATION");
        assertThat((String) readJsonContext.read("$.step.form.explanationField.id")).isNull();
        assertThat((String) readJsonContext.read("$.step.form.explanationField.displayName")).isNull();
        assertThat((String) readJsonContext.read("$.step.form.explanationField.explanation")).isNull();
        assertThat((String) readJsonContext.read("$.step.form.explanationField.componentType")).isEqualTo("FIELD");
        assertThat((String) readJsonContext.read("$.step.form.explanationField.fieldType")).isEqualTo("EXPLANATION");
        assertThat((Map<Object, Object>) readJsonContext.read("$.step.form.hiddenComponents")).isEmpty();

        assertThat((String) readJsonContext.read("$.step.timeoutTime")).isEqualTo("2018-12-18T10:15:30Z");
        assertThat((String) readJsonContext.read("$.step.providerState")).isEqualTo("state");
    }

    @Test
    public void shouldSerializeRedirectStepWithoutExternalConsentId() throws JsonProcessingException {
        // given
        RedirectStep step = new RedirectStep("url", null, "state");

        // when
        AccessMeansOrStepDTO accessMeansOrStepDTO = new AccessMeansOrStepDTO(step);

        // then
        String serialized = objectMapper.writeValueAsString(accessMeansOrStepDTO);

        ReadContext readJsonContext = JsonPath.parse(serialized);
        assertThat((String) readJsonContext.read("$.accessMeans")).isNull();
        assertThat((String) readJsonContext.read("$.step.type")).isEqualTo("REDIRECT_URL");
        assertThat((String) readJsonContext.read("$.step.redirectUrl")).isEqualTo("url");
        assertThat((String) readJsonContext.read("$.step.externalConsentId")).isNull();
        assertThat((String) readJsonContext.read("$.step.providerState")).isEqualTo("state");
    }

    @Test
    public void shouldReturnViolationWhenUrlIsMalformed() {
        // given
        RedirectStep step = new RedirectStep("url", null, "state");

        // when
        Set<ConstraintViolation<RedirectStep>> violations = validator.validate(step);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation violation = (ConstraintViolation) violations.toArray()[0];
        assertThat(violation.getMessageTemplate()).isEqualTo("{org.hibernate.validator.constraints.URL.message}");
    }

    @Test
    public void shouldNotReturnViolationsWhenStepWithAProperUrl () {
        // given
        RedirectStep step = new RedirectStep("https://url.com", null, "state");

        // when
        Set<ConstraintViolation<RedirectStep>> violations = validator.validate(step);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldSerializeAccessMeansOrStepDTOWithEncryptionDetails() throws JsonProcessingException {
        // given
        Date date = Date.from(Instant.parse("2018-12-18T10:15:30.00Z"));
        Form form = new Form(Collections.emptyList(), new ExplanationField(), Collections.emptyMap());
        var alghoritm = "RSA-OAEP-256";
        var encryptionMethod = "A256GCM";
        var publicJSONWebKey = "{ \"kty\": \"RSA\", \"n\": \"tixrPVyrHFwff8Tv6dppJ6Pw1CJdMhWzMr7Qq<ESCAPED JWK JSON STRING>BzDRrbn5TTBNo0So5G22g4l6cYJw\", \"e\": \"AQAB\" }";
        var kty = "RSA";
        var n = "tixrPVyrHFwff8Tv6dppJ6Pw....RQCxBzDRrbn5TTBNo0So5G22g4l6cYJw";
        var e = "AQAB";
        var jweDetails = new EncryptionDetails.JWEDetails(alghoritm, encryptionMethod, publicJSONWebKey,
                new EncryptionDetails.RsaPublicJWK(alghoritm, kty, n, e)
        );
        EncryptionDetails encryptionDetails = EncryptionDetails.of(jweDetails);
        FormStep step = new FormStep(form, encryptionDetails, date.toInstant(), "state");

        // when
        AccessMeansOrStepDTO accessMeansOrStepDTO = new AccessMeansOrStepDTO(step);

        // then
        String serialized = objectMapper.writeValueAsString(accessMeansOrStepDTO);
        ReadContext readJsonContext = JsonPath.parse(serialized);
        assertThat((String) readJsonContext.read("$.accessMeans")).isNull();
        assertThat((String) readJsonContext.read("$.step.type")).isEqualTo("FORM");

        assertThat((Object) readJsonContext.read("$.step.encryptionDetails")).isNotNull();
        assertThat((Object) readJsonContext.read("$.step.encryptionDetails.jweDetails")).isNotNull();
        assertThat((String) readJsonContext.read("$.step.encryptionDetails.jweDetails.algorithm")).isEqualTo(alghoritm);
        assertThat((String) readJsonContext.read("$.step.encryptionDetails.jweDetails.encryptionMethod")).isEqualTo(encryptionMethod);
        assertThat((String) readJsonContext.read("$.step.encryptionDetails.jweDetails.publicJSONWebKey")).isEqualTo(publicJSONWebKey);
        assertThat((Object) readJsonContext.read("$.step.encryptionDetails.jweDetails.rsaPublicJwk")).isNotNull();
        assertThat((String) readJsonContext.read("$.step.encryptionDetails.jweDetails.rsaPublicJwk.alg")).isEqualTo(alghoritm);
        assertThat((String) readJsonContext.read("$.step.encryptionDetails.jweDetails.rsaPublicJwk.kty")).isEqualTo(kty);
        assertThat((String) readJsonContext.read("$.step.encryptionDetails.jweDetails.rsaPublicJwk.n")).isEqualTo(n);
        assertThat((String) readJsonContext.read("$.step.encryptionDetails.jweDetails.rsaPublicJwk.e")).isEqualTo(e);
    }
}