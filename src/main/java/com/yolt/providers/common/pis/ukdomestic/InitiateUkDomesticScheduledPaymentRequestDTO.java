package com.yolt.providers.common.pis.ukdomestic;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Value
@EqualsAndHashCode(callSuper = true)
public class InitiateUkDomesticScheduledPaymentRequestDTO extends InitiateUkDomesticPaymentRequestDTO {

    @Nullable
    OffsetDateTime executionDate;

    @ConstructorProperties({"endToEndIdentification", "currencyCode", "amount", "creditorAccount", "debtorAccount", "remittanceInformationUnstructured", "dynamicFields", "executionDate"})
    public InitiateUkDomesticScheduledPaymentRequestDTO(@NotNull String endToEndIdentification,
                                                        @NotNull String currencyCode,
                                                        @NotNull BigDecimal amount,
                                                        @NotNull UkAccountDTO creditorAccount,
                                                        @Valid UkAccountDTO debtorAccount,
                                                        String remittanceInformationUnstructured,
                                                        Map<String, String> dynamicFields,
                                                        @NotNull OffsetDateTime executionDate) {
        super(endToEndIdentification, currencyCode, amount, creditorAccount, debtorAccount, remittanceInformationUnstructured, dynamicFields);
        this.executionDate = executionDate;
    }
}