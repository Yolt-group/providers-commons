package com.yolt.providers.common.mapper.currency;

import nl.ing.lovebird.extendeddata.common.CurrencyCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class NumericCodeCurrencyCodeMapperTest {

    private NumericCodeCurrencyCodeMapper numericCodeCurrencyCodeMapper = new NumericCodeCurrencyCodeMapper();

    private static Stream<Arguments> getCurrencyCodeMap() {
        return Stream.of(
                Arguments.of("191", CurrencyCode.HRK),
                Arguments.of("192", CurrencyCode.CUP),
                Arguments.of("072", CurrencyCode.BWP),
                Arguments.of("590", CurrencyCode.PAB),
                Arguments.of("230", CurrencyCode.ETB),
                Arguments.of("990", CurrencyCode.CLF),
                Arguments.of("352", CurrencyCode.ISK),
                Arguments.of("232", CurrencyCode.ERN),
                Arguments.of("356", CurrencyCode.INR),
                Arguments.of("598", CurrencyCode.PGK),
                Arguments.of("752", CurrencyCode.SEK),
                Arguments.of("478", CurrencyCode.MRO),
                Arguments.of("116", CurrencyCode.KHR),
                Arguments.of("512", CurrencyCode.OMR),
                Arguments.of("238", CurrencyCode.FKP),
                Arguments.of("634", CurrencyCode.QAR),
                Arguments.of("997", CurrencyCode.USN),
                Arguments.of("756", CurrencyCode.CHF),
                Arguments.of("999", CurrencyCode.XXX),
                Arguments.of("516", CurrencyCode.NAD),
                Arguments.of("084", CurrencyCode.BZD),
                Arguments.of("480", CurrencyCode.MUR),
                Arguments.of("360", CurrencyCode.IDR),
                Arguments.of("242", CurrencyCode.FJD),
                Arguments.of("484", CurrencyCode.MXN),
                Arguments.of("364", CurrencyCode.IRR),
                Arguments.of("760", CurrencyCode.SYP),
                Arguments.of("882", CurrencyCode.WST),
                Arguments.of("124", CurrencyCode.CAD),
                Arguments.of("400", CurrencyCode.JOD),
                Arguments.of("368", CurrencyCode.IQD),
                Arguments.of("643", CurrencyCode.RUB),
                Arguments.of("764", CurrencyCode.THB),
                Arguments.of("886", CurrencyCode.YER),
                Arguments.of("524", CurrencyCode.NPR),
                Arguments.of("008", CurrencyCode.ALL),
                Arguments.of("404", CurrencyCode.KES),
                Arguments.of("646", CurrencyCode.RWF),
                Arguments.of("800", CurrencyCode.UGX),
                Arguments.of("408", CurrencyCode.KPW),
                Arguments.of("807", CurrencyCode.MKD),
                Arguments.of("090", CurrencyCode.SBD),
                Arguments.of("096", CurrencyCode.BND),
                Arguments.of("132", CurrencyCode.CVE),
                Arguments.of("012", CurrencyCode.DZD),
                Arguments.of("496", CurrencyCode.MNT),
                Arguments.of("376", CurrencyCode.ILS),
                Arguments.of("410", CurrencyCode.KRW),
                Arguments.of("498", CurrencyCode.MDL),
                Arguments.of("532", CurrencyCode.ANG),
                Arguments.of("136", CurrencyCode.KYD),
                Arguments.of("533", CurrencyCode.AWG),
                Arguments.of("654", CurrencyCode.SHP),
                Arguments.of("776", CurrencyCode.TOP),
                Arguments.of("931", CurrencyCode.CUC),
                Arguments.of("414", CurrencyCode.KWD),
                Arguments.of("932", CurrencyCode.ZWL),
                Arguments.of("933", CurrencyCode.BYN),
                Arguments.of("417", CurrencyCode.KGS),
                Arguments.of("934", CurrencyCode.TMT),
                Arguments.of("418", CurrencyCode.LAK),
                Arguments.of("936", CurrencyCode.GHS),
                Arguments.of("937", CurrencyCode.VEF),
                Arguments.of("938", CurrencyCode.SDG),
                Arguments.of("818", CurrencyCode.EGP),
                Arguments.of("262", CurrencyCode.DJF),
                Arguments.of("780", CurrencyCode.TTD),
                Arguments.of("144", CurrencyCode.LKR),
                Arguments.of("784", CurrencyCode.AED),
                Arguments.of("388", CurrencyCode.JMD),
                Arguments.of("422", CurrencyCode.LBP),
                Arguments.of("940", CurrencyCode.UYI),
                Arguments.of("941", CurrencyCode.RSD),
                Arguments.of("788", CurrencyCode.TND),
                Arguments.of("426", CurrencyCode.LSL),
                Arguments.of("943", CurrencyCode.MZN),
                Arguments.of("944", CurrencyCode.AZN),
                Arguments.of("702", CurrencyCode.SGD),
                Arguments.of("548", CurrencyCode.VUV),
                Arguments.of("946", CurrencyCode.RON),
                Arguments.of("704", CurrencyCode.VND),
                Arguments.of("947", CurrencyCode.CHE),
                Arguments.of("826", CurrencyCode.GBP),
                Arguments.of("948", CurrencyCode.CHW),
                Arguments.of("706", CurrencyCode.SOS),
                Arguments.of("949", CurrencyCode.TRY),
                Arguments.of("270", CurrencyCode.GMD),
                Arguments.of("392", CurrencyCode.JPY),
                Arguments.of("152", CurrencyCode.CLP),
                Arguments.of("032", CurrencyCode.ARS),
                Arguments.of("430", CurrencyCode.LRD),
                Arguments.of("156", CurrencyCode.CNY),
                Arguments.of("398", CurrencyCode.KZT),
                Arguments.of("036", CurrencyCode.AUD),
                Arguments.of("554", CurrencyCode.NZD),
                Arguments.of("950", CurrencyCode.XAF),
                Arguments.of("434", CurrencyCode.LYD),
                Arguments.of("951", CurrencyCode.XCD),
                Arguments.of("952", CurrencyCode.XOF),
                Arguments.of("710", CurrencyCode.ZAR),
                Arguments.of("678", CurrencyCode.STD),
                Arguments.of("558", CurrencyCode.NIO),
                Arguments.of("834", CurrencyCode.TZS),
                Arguments.of("044", CurrencyCode.BSD),
                Arguments.of("682", CurrencyCode.SAR),
                Arguments.of("320", CurrencyCode.GTQ),
                Arguments.of("960", CurrencyCode.XDR),
                Arguments.of("048", CurrencyCode.BHD),
                Arguments.of("840", CurrencyCode.USD),
                Arguments.of("203", CurrencyCode.CZK),
                Arguments.of("324", CurrencyCode.GNF),
                Arguments.of("566", CurrencyCode.NGN),
                Arguments.of("446", CurrencyCode.MOP),
                Arguments.of("600", CurrencyCode.PYG),
                Arguments.of("328", CurrencyCode.GYD),
                Arguments.of("208", CurrencyCode.DKK),
                Arguments.of("604", CurrencyCode.PEN),
                Arguments.of("967", CurrencyCode.ZMW),
                Arguments.of("968", CurrencyCode.SRD),
                Arguments.of("969", CurrencyCode.MGA),
                Arguments.of("728", CurrencyCode.SSP),
                Arguments.of("608", CurrencyCode.PHP),
                Arguments.of("170", CurrencyCode.COP),
                Arguments.of("050", CurrencyCode.BDT),
                Arguments.of("292", CurrencyCode.GIP),
                Arguments.of("051", CurrencyCode.AMD),
                Arguments.of("052", CurrencyCode.BBD),
                Arguments.of("690", CurrencyCode.SCR),
                Arguments.of("174", CurrencyCode.KMF),
                Arguments.of("694", CurrencyCode.SLL),
                Arguments.of("970", CurrencyCode.COU),
                Arguments.of("332", CurrencyCode.HTG),
                Arguments.of("971", CurrencyCode.AFN),
                Arguments.of("454", CurrencyCode.MWK),
                Arguments.of("972", CurrencyCode.TJS),
                Arguments.of("973", CurrencyCode.AOA),
                Arguments.of("214", CurrencyCode.DOP),
                Arguments.of("578", CurrencyCode.NOK),
                Arguments.of("975", CurrencyCode.BGN),
                Arguments.of("458", CurrencyCode.MYR),
                Arguments.of("976", CurrencyCode.CDF),
                Arguments.of("977", CurrencyCode.BAM),
                Arguments.of("978", CurrencyCode.EUR),
                Arguments.of("979", CurrencyCode.MXV),
                Arguments.of("858", CurrencyCode.UYU),
                Arguments.of("060", CurrencyCode.BMD),
                Arguments.of("064", CurrencyCode.BTN),
                Arguments.of("340", CurrencyCode.HNL),
                Arguments.of("462", CurrencyCode.MVR),
                Arguments.of("188", CurrencyCode.CRC),
                Arguments.of("980", CurrencyCode.UAH),
                Arguments.of("068", CurrencyCode.BOB),
                Arguments.of("981", CurrencyCode.GEL),
                Arguments.of("222", CurrencyCode.SVC),
                Arguments.of("860", CurrencyCode.UZS),
                Arguments.of("344", CurrencyCode.HKD),
                Arguments.of("586", CurrencyCode.PKR),
                Arguments.of("984", CurrencyCode.BOV),
                Arguments.of("104", CurrencyCode.MMK),
                Arguments.of("985", CurrencyCode.PLN),
                Arguments.of("986", CurrencyCode.BRL),
                Arguments.of("348", CurrencyCode.HUF),
                Arguments.of("108", CurrencyCode.BIF),
                Arguments.of("504", CurrencyCode.MAD),
                Arguments.of("901", CurrencyCode.TWD),
                Arguments.of("748", CurrencyCode.SZL)
        );
    }

    @ParameterizedTest
    @MethodSource("getCurrencyCodeMap")
    void shouldMapProperly(String numericCurrencyCode, CurrencyCode expectedCurrency) {
        //when
        CurrencyCode returnedCurrency = numericCodeCurrencyCodeMapper.mapToCurrencyCode(numericCurrencyCode);
        //then
        assertThat(returnedCurrency).isEqualTo(expectedCurrency);
    }

    @Test
    void shouldReturnNullIfThereIsNoCurrencyWithSpecificCode() {
        //when
        CurrencyCode returnedCurrency = numericCodeCurrencyCodeMapper.mapToCurrencyCode("000");
        //then
        assertThat(returnedCurrency).isNull();
    }
}