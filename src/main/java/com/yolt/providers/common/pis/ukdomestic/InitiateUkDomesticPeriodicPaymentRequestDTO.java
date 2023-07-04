package com.yolt.providers.common.pis.ukdomestic;

import com.yolt.providers.common.pis.common.UkPeriodicPaymentInfo;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.Map;

@Value
@EqualsAndHashCode(callSuper = true)
public class InitiateUkDomesticPeriodicPaymentRequestDTO extends InitiateUkDomesticPaymentRequestDTO {

    @NotNull
    UkPeriodicPaymentInfo periodicPaymentInfo;

    @ConstructorProperties({"endToEndIdentification", "currencyCode", "amount", "creditorAccount", "debtorAccount", "remittanceInformationUnstructured", "dynamicFields", "periodicPaymentInfo"})
    public InitiateUkDomesticPeriodicPaymentRequestDTO(@NotNull String endToEndIdentification,
                                                       @NotNull String currencyCode,
                                                       @NotNull BigDecimal amount,
                                                       @NotNull UkAccountDTO creditorAccount,
                                                       @Valid UkAccountDTO debtorAccount,
                                                       String remittanceInformationUnstructured,
                                                       Map<String, String> dynamicFields,
                                                       @NotNull UkPeriodicPaymentInfo periodicPaymentInfo) {
        super(endToEndIdentification, currencyCode, amount, creditorAccount, debtorAccount, remittanceInformationUnstructured, dynamicFields);
        this.periodicPaymentInfo = periodicPaymentInfo;
    }
}
