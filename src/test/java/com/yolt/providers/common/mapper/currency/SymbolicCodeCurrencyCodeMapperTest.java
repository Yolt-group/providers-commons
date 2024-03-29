package com.yolt.providers.common.mapper.currency;

import nl.ing.lovebird.extendeddata.common.CurrencyCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SymbolicCodeCurrencyCodeMapperTest {

    private SymbolicCurrencyCodeMapper symbolicCurrencyCodeMapper = new SymbolicCurrencyCodeMapper();


    private static Stream<Arguments> getCurrencyCodeMap() {
        return Stream.of(
                Arguments.of("FJD", CurrencyCode.FJD),
                Arguments.of("MXN", CurrencyCode.MXN),
                Arguments.of("STD", CurrencyCode.STD),
                Arguments.of("SCR", CurrencyCode.SCR),
                Arguments.of("CDF", CurrencyCode.CDF),
                Arguments.of("BBD", CurrencyCode.BBD),
                Arguments.of("GTQ", CurrencyCode.GTQ),
                Arguments.of("CLP", CurrencyCode.CLP),
                Arguments.of("HNL", CurrencyCode.HNL),
                Arguments.of("UGX", CurrencyCode.UGX),
                Arguments.of("MXV", CurrencyCode.MXV),
                Arguments.of("ZAR", CurrencyCode.ZAR),
                Arguments.of("TND", CurrencyCode.TND),
                Arguments.of("CUC", CurrencyCode.CUC),
                Arguments.of("BSD", CurrencyCode.BSD),
                Arguments.of("SLL", CurrencyCode.SLL),
                Arguments.of("SDG", CurrencyCode.SDG),
                Arguments.of("IQD", CurrencyCode.IQD),
                Arguments.of("CUP", CurrencyCode.CUP),
                Arguments.of("GMD", CurrencyCode.GMD),
                Arguments.of("TWD", CurrencyCode.TWD),
                Arguments.of("RSD", CurrencyCode.RSD),
                Arguments.of("DOP", CurrencyCode.DOP),
                Arguments.of("UYI", CurrencyCode.UYI),
                Arguments.of("KMF", CurrencyCode.KMF),
                Arguments.of("MYR", CurrencyCode.MYR),
                Arguments.of("FKP", CurrencyCode.FKP),
                Arguments.of("XOF", CurrencyCode.XOF),
                Arguments.of("GEL", CurrencyCode.GEL),
                Arguments.of("UYU", CurrencyCode.UYU),
                Arguments.of("MAD", CurrencyCode.MAD),
                Arguments.of("CVE", CurrencyCode.CVE),
                Arguments.of("TOP", CurrencyCode.TOP),
                Arguments.of("AZN", CurrencyCode.AZN),
                Arguments.of("OMR", CurrencyCode.OMR),
                Arguments.of("PGK", CurrencyCode.PGK),
                Arguments.of("KES", CurrencyCode.KES),
                Arguments.of("SEK", CurrencyCode.SEK),
                Arguments.of("BTN", CurrencyCode.BTN),
                Arguments.of("UAH", CurrencyCode.UAH),
                Arguments.of("GNF", CurrencyCode.GNF),
                Arguments.of("ERN", CurrencyCode.ERN),
                Arguments.of("MZN", CurrencyCode.MZN),
                Arguments.of("SVC", CurrencyCode.SVC),
                Arguments.of("ARS", CurrencyCode.ARS),
                Arguments.of("QAR", CurrencyCode.QAR),
                Arguments.of("IRR", CurrencyCode.IRR),
                Arguments.of("MRO", CurrencyCode.MRO),
                Arguments.of("CNY", CurrencyCode.CNY),
                Arguments.of("THB", CurrencyCode.THB),
                Arguments.of("UZS", CurrencyCode.UZS),
                Arguments.of("BDT", CurrencyCode.BDT),
                Arguments.of("LYD", CurrencyCode.LYD),
                Arguments.of("BMD", CurrencyCode.BMD),
                Arguments.of("KWD", CurrencyCode.KWD),
                Arguments.of("PHP", CurrencyCode.PHP),
                Arguments.of("XXX", CurrencyCode.XXX),
                Arguments.of("RUB", CurrencyCode.RUB),
                Arguments.of("PYG", CurrencyCode.PYG),
                Arguments.of("ISK", CurrencyCode.ISK),
                Arguments.of("JMD", CurrencyCode.JMD),
                Arguments.of("COP", CurrencyCode.COP),
                Arguments.of("MKD", CurrencyCode.MKD),
                Arguments.of("USD", CurrencyCode.USD),
                Arguments.of("COU", CurrencyCode.COU),
                Arguments.of("DZD", CurrencyCode.DZD),
                Arguments.of("PAB", CurrencyCode.PAB),
                Arguments.of("SGD", CurrencyCode.SGD),
                Arguments.of("USN", CurrencyCode.USN),
                Arguments.of("ETB", CurrencyCode.ETB),
                Arguments.of("KGS", CurrencyCode.KGS),
                Arguments.of("SOS", CurrencyCode.SOS),
                Arguments.of("VEF", CurrencyCode.VEF),
                Arguments.of("VUV", CurrencyCode.VUV),
                Arguments.of("LAK", CurrencyCode.LAK),
                Arguments.of("BND", CurrencyCode.BND),
                Arguments.of("XAF", CurrencyCode.XAF),
                Arguments.of("LRD", CurrencyCode.LRD),
                Arguments.of("CHF", CurrencyCode.CHF),
                Arguments.of("HRK", CurrencyCode.HRK),
                Arguments.of("ALL", CurrencyCode.ALL),
                Arguments.of("CHE", CurrencyCode.CHE),
                Arguments.of("DJF", CurrencyCode.DJF),
                Arguments.of("ZMW", CurrencyCode.ZMW),
                Arguments.of("TZS", CurrencyCode.TZS),
                Arguments.of("VND", CurrencyCode.VND),
                Arguments.of("AUD", CurrencyCode.AUD),
                Arguments.of("ILS", CurrencyCode.ILS),
                Arguments.of("CHW", CurrencyCode.CHW),
                Arguments.of("GHS", CurrencyCode.GHS),
                Arguments.of("GYD", CurrencyCode.GYD),
                Arguments.of("KPW", CurrencyCode.KPW),
                Arguments.of("BOB", CurrencyCode.BOB),
                Arguments.of("KHR", CurrencyCode.KHR),
                Arguments.of("MDL", CurrencyCode.MDL),
                Arguments.of("IDR", CurrencyCode.IDR),
                Arguments.of("KYD", CurrencyCode.KYD),
                Arguments.of("AMD", CurrencyCode.AMD),
                Arguments.of("BWP", CurrencyCode.BWP),
                Arguments.of("SHP", CurrencyCode.SHP),
                Arguments.of("TRY", CurrencyCode.TRY),
                Arguments.of("LBP", CurrencyCode.LBP),
                Arguments.of("TJS", CurrencyCode.TJS),
                Arguments.of("JOD", CurrencyCode.JOD),
                Arguments.of("AED", CurrencyCode.AED),
                Arguments.of("HKD", CurrencyCode.HKD),
                Arguments.of("RWF", CurrencyCode.RWF),
                Arguments.of("EUR", CurrencyCode.EUR),
                Arguments.of("LSL", CurrencyCode.LSL),
                Arguments.of("DKK", CurrencyCode.DKK),
                Arguments.of("CAD", CurrencyCode.CAD),
                Arguments.of("BGN", CurrencyCode.BGN),
                Arguments.of("BOV", CurrencyCode.BOV),
                Arguments.of("MMK", CurrencyCode.MMK),
                Arguments.of("MUR", CurrencyCode.MUR),
                Arguments.of("NOK", CurrencyCode.NOK),
                Arguments.of("SYP", CurrencyCode.SYP),
                Arguments.of("ZWL", CurrencyCode.ZWL),
                Arguments.of("GIP", CurrencyCode.GIP),
                Arguments.of("RON", CurrencyCode.RON),
                Arguments.of("LKR", CurrencyCode.LKR),
                Arguments.of("NGN", CurrencyCode.NGN),
                Arguments.of("CRC", CurrencyCode.CRC),
                Arguments.of("CZK", CurrencyCode.CZK),
                Arguments.of("PKR", CurrencyCode.PKR),
                Arguments.of("XCD", CurrencyCode.XCD),
                Arguments.of("ANG", CurrencyCode.ANG),
                Arguments.of("HTG", CurrencyCode.HTG),
                Arguments.of("BHD", CurrencyCode.BHD),
                Arguments.of("KZT", CurrencyCode.KZT),
                Arguments.of("SRD", CurrencyCode.SRD),
                Arguments.of("SZL", CurrencyCode.SZL),
                Arguments.of("SAR", CurrencyCode.SAR),
                Arguments.of("TTD", CurrencyCode.TTD),
                Arguments.of("YER", CurrencyCode.YER),
                Arguments.of("MVR", CurrencyCode.MVR),
                Arguments.of("AFN", CurrencyCode.AFN),
                Arguments.of("INR", CurrencyCode.INR),
                Arguments.of("AWG", CurrencyCode.AWG),
                Arguments.of("KRW", CurrencyCode.KRW),
                Arguments.of("NPR", CurrencyCode.NPR),
                Arguments.of("JPY", CurrencyCode.JPY),
                Arguments.of("MNT", CurrencyCode.MNT),
                Arguments.of("AOA", CurrencyCode.AOA),
                Arguments.of("PLN", CurrencyCode.PLN),
                Arguments.of("GBP", CurrencyCode.GBP),
                Arguments.of("SBD", CurrencyCode.SBD),
                Arguments.of("BYN", CurrencyCode.BYN),
                Arguments.of("HUF", CurrencyCode.HUF),
                Arguments.of("BIF", CurrencyCode.BIF),
                Arguments.of("MWK", CurrencyCode.MWK),
                Arguments.of("MGA", CurrencyCode.MGA),
                Arguments.of("XDR", CurrencyCode.XDR),
                Arguments.of("BZD", CurrencyCode.BZD),
                Arguments.of("BAM", CurrencyCode.BAM),
                Arguments.of("EGP", CurrencyCode.EGP),
                Arguments.of("MOP", CurrencyCode.MOP),
                Arguments.of("NAD", CurrencyCode.NAD),
                Arguments.of("SSP", CurrencyCode.SSP),
                Arguments.of("NIO", CurrencyCode.NIO),
                Arguments.of("PEN", CurrencyCode.PEN),
                Arguments.of("NZD", CurrencyCode.NZD),
                Arguments.of("WST", CurrencyCode.WST),
                Arguments.of("TMT", CurrencyCode.TMT),
                Arguments.of("CLF", CurrencyCode.CLF),
                Arguments.of("BRL", CurrencyCode.BRL)
        );
    }

    @ParameterizedTest
    @MethodSource("getCurrencyCodeMap")
    void shouldMapProperly(String currencyCode, CurrencyCode expectedCurrency) {
        //when
        CurrencyCode returnedCurrency = symbolicCurrencyCodeMapper.mapToCurrencyCode(currencyCode);
        //then
        assertThat(returnedCurrency).isEqualTo(expectedCurrency);
    }

    @Test
    void shouldReturnNullIfThereIsNoCurrencyWithSpecificCode() {
        //when
        CurrencyCode returnedCurrency = symbolicCurrencyCodeMapper.mapToCurrencyCode("ABC");
        //then
        assertThat(returnedCurrency).isNull();
    }
}
