package com.neopragma.legacy.screen;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Tests implementation details of ZipCodeSearch as a sort of contract test for cityAndStateFrom webService
 *
 * @author andy.lawrence@gmail.com
 * @version 1.0.0
 * @since 1.7
 */

public class ZipCodeSearchTest {

	@Test
	public void parsesAZipCodeResponseCorrectly() throws URISyntaxException, IOException {
		String response = "<...><meta name=\"description\" content=\"Zip Code 85658 - Marana AZ Arizona, USA - Pima County\"></...>";
		ZipCodeSearch zipCodeSearch = new ZipCodeSearch();

		CityState result = zipCodeSearch.cityAndStateFrom(response);

		assertEquals("Marana", result.getCity());
		assertEquals("AZ", result.getState());
	}

	@Test
	public void buildCorrectUri() throws URISyntaxException {
		ZipCodeSearch zipCodeSearch = new ZipCodeSearch();
		URI result = zipCodeSearch.buildZipCodeUri("45202");

		assertNotNull(result);
		assertEquals("http://www.zip-codes.com/search.asp?fld-zip=45202&selectTab=0&srch-type=city", result.toASCIIString());
	}
}
