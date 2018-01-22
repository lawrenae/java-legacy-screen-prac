package com.neopragma.legacy.screen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;


/**
 * Automated unit checks for the base version of the JobApplicant application.
 * This version of the code contains a number of code smells that may point to
 * potential improvements in the design of the code.
 *
 * @author neopragma
 * @version 1.0.0
 * @since 1.7
 */

public class JobApplicantTest {

    private JobApplicant jobApplicant;

    @Before
    public void beforeEach() {
        jobApplicant = new JobApplicant();
    }

    @Test
    public void completeNameProvided() {
        jobApplicant.setName("First", "Middle", "Last");
        assertTrue(jobApplicant.validateName());
    }

    @Test
    public void firstAndLastNamesProvided() {
        jobApplicant.setName("First", null, "Last");
        assertTrue(jobApplicant.validateName());
    }

    @Test
    public void missingFirstName() {
        jobApplicant.setName(null, null, "Last");
        assertFalse(jobApplicant.validateName());
    }

    @Test
    public void missingLastName() {
        jobApplicant.setName("First", null, null);
        assertFalse(jobApplicant.validateName());
    }

    @Test
    public void completeSpanishNameProvided() {
        jobApplicant.setSpanishName("PrimerNombre", "SegundoNombre", "PrimerApellido", "SegundoApellido");
        assertTrue(jobApplicant.validateName());
    }

    @Test
    public void spanishNameWithOneFirstNameProvided() {
        jobApplicant.setSpanishName("PrimerNombre", null, "PrimerApellido", "SegundoApellido");
        assertTrue(jobApplicant.validateName());
    }

    @Test
    public void spanishNameWithOneLastNameProvided() {
        jobApplicant.setSpanishName("PrimerNombre", null, "PrimerApellido", null);
        assertTrue(jobApplicant.validateName());
    }

    @Test
    public void spanishNameWithNoFirstNameProvided() {
        jobApplicant.setSpanishName(null, null, "PrimerApellido", null);
        assertFalse(jobApplicant.validateName());
    }

    @Test
    public void spanishNameWithNoLastNameProvided() {
        jobApplicant.setSpanishName("PrimerNombre", "SegundoNombre", null, null);
        assertFalse(jobApplicant.validateName());
    }

    @Test
    public void formatEnglishNameLastNameFirst() {
        jobApplicant.setName("First", "Middle", "Last");
        assertEquals("Last, First Middle", jobApplicant.formatLastNameFirst());
    }

    @Test
    public void ssnFormattingTest() {
        jobApplicant.setSsn("123456789");
        assertEquals("123-45-6789", jobApplicant.formatSsn());
    }

    @Test
    public void itValidatesSsnWithNoDashes() {
        jobApplicant.setSsn("123456789");
        assertTrue(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsWithSsnWithDashesInWrongPlaces() {
        jobApplicant.setSsn("12-3456-789");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itValidatesSsnWithDashes() {
        jobApplicant.setSsn("123-45-6789");
        assertTrue(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsWhenSsnIsTooShort() {
        jobApplicant.setSsn("12345678");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsWhenSsnIsTooLong() {
        jobApplicant.setSsn("1234567890");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsWhenSsnAreaNumberIs000() {
        jobApplicant.setSsn("000223333");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsWhenSsnAreaNumberIs666() {
        jobApplicant.setSsn("666223333");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsSsnAreaNumberStartsWith9() {
        jobApplicant.setSsn("900223333");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsSsnSerialNumberIs0000() {
        jobApplicant.setSsn("111220000");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsSpecialCase078051120() {
        jobApplicant.setSsn("078051120");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itRejectsSpecialCase219099999() {
        jobApplicant.setSsn("219099999");
        assertFalse(jobApplicant.validateSsn());
    }

    @Test
    public void itFindsAddisonTexasBy5DigitZipCode() throws URISyntaxException, IOException {
        ZipCodeSearch findCityState = mock(ZipCodeSearch.class);
        when(findCityState.find("75001")).thenReturn(new CityState("Addison", "TX"));
        jobApplicant = new JobApplicant(findCityState);
        jobApplicant.setZipCode("75001");
        assertEquals("Addison", jobApplicant.getCity());
        assertEquals("TX", jobApplicant.getState());
        assertEquals("75001", jobApplicant.getZipCode());
    }
}
