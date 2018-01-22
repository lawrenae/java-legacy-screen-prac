package com.neopragma.legacy.screen;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

class ZipCodeSearch {

    public CityState find(String zipCode) throws URISyntaxException, IOException {
        CityState result;
        CloseableHttpResponse response = null;
        try {
            response = searchZipCodes(zipCode);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                StringBuffer responseString = readResponse(response);
                result = cityAndStateFrom(responseString.toString());
            } else {
                result = new CityState("","");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return result;
    }

    CityState cityAndStateFrom(String resultString) {
        int contentOffset = getContentOffset(resultString);

        int stateOffset = resultString.indexOf(" ", contentOffset);
        String city = resultString.substring(contentOffset, stateOffset);

        stateOffset += 1;
        String state = resultString.substring(stateOffset, stateOffset + 2);

        return new CityState(city, state);
    }

    private int getContentOffset(String result) {
        int metaOffset = result.indexOf("<meta ");

        String zipCodeContent = " content=\"Zip Code ";
        int contentOffset =
                result.indexOf(zipCodeContent, metaOffset) +
                zipCodeContent.length();

        String dash = " - ";
        contentOffset = result.indexOf(dash, contentOffset) + dash.length();

        return contentOffset;
    }

    private StringBuffer readResponse(CloseableHttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result;
    }

    URI buildZipCodeUri(String zipCode) throws URISyntaxException {
        return new URIBuilder()
                .setScheme("http")
                .setHost("www.zip-codes.com")
                .setPath("/search.asp")
                .setParameter("fld-zip", zipCode)
                .setParameter("selectTab", "0")
                .setParameter("srch-type", "city")
                .build();
    }

    private CloseableHttpResponse searchZipCodes(String zipCode) throws URISyntaxException, IOException {
        // Use a service to look up cityAndStateFrom city and state based on zip code.
        // Save cityAndStateFrom returned city and state if content length is greater than zero.
        HttpGet request = new HttpGet(buildZipCodeUri(zipCode));

        CloseableHttpClient httpclient = buildHttpClientWithProxy();

        return httpclient.execute(request);
    }

    private CloseableHttpClient buildHttpClientWithProxy() {
        HttpHost proxy = new HttpHost("127.0.0.1", 3128, "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        return HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();
    }
}
