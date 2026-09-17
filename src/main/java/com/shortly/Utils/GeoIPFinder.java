package com.shortly.Utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Component
public class GeoIPFinder {
    @Value("${GEOIP_DIR}")
    private String GeoIPDir;

    private DatabaseReader countryDBReader;
    private DatabaseReader cityDBReader;

    public GeoIPFinder() throws IOException {
        if (GeoIPDir == null) GeoIPDir = "/app/geoip/";
        try {
            File database_country = new File(
                    GeoIPDir + "GeoLite2-City.mmdb"
            );
            File database_city = new File(
                    GeoIPDir + "GeoLite2-City.mmdb"
            );

            this.countryDBReader = new DatabaseReader.Builder(database_country).build();
            this.cityDBReader = new DatabaseReader.Builder(database_city).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getCountry(String IpAddr) {
        try {
            InetAddress ipAddress = InetAddress.getByName(IpAddr);

            CountryResponse response =
                    countryDBReader.country(ipAddress);

            return response.country().isoCode();

        } catch (Exception e) {
            System.out.println("Country not found, setting default IN.");
            return "IN";
        }
    }

    public String getCity(String IpAddr) {
        try {
            InetAddress ipAddress = InetAddress.getByName(IpAddr);

            CityResponse response =
                    cityDBReader.city(ipAddress);

            return response.city().name();
        } catch (Exception e) {
            System.out.println("City not found, setting default.");
            return "Other";
        }
    }
}