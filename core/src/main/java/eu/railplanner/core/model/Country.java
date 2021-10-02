package eu.railplanner.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.util.Objects;

/**
 * List of countries in Europe, along with ISO codes and timezones.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Country {
    ALBANIA("AL", "ALB", ZoneId.of("Europe/Amsterdam")),
    ANDORRA("AD", "AND", ZoneId.of("Europe/Andorra")),
    ARMENIA,
    AUSTRIA("AT", "AUT", ZoneId.of("Europe/Amsterdam")),
    AZERBAIJAN,
    BELARUS("BY", "BLR", ZoneId.of("Europe/Minsk")),
    BELGIUM("BE", "BEL", ZoneId.of("Europe/Brussels")),
    BOSNIA_AND_HERZEGOVINA,
    BULGARIA,
    CROATIA("HR", "HRV", ZoneId.of("Europe/Zagreb")),
    CYPRUS,
    CZECHIA("CZ", "CZE", ZoneId.of("Europe/Prague")),
    DENMARK("DK", "DNK", ZoneId.of("Europe/Copenhagen")),
    ESTONIA("EE", "EST", ZoneId.of("Europe/Tallinn")),
    FINLAND("FI", "FIN", ZoneId.of("Europe/Helsinki")),
    FRANCE("FR", "FRA", ZoneId.of("Europe/Paris")),
    GEORGIA,
    GERMANY("DE", "DEU", ZoneId.of("Europe/Berlin")),
    GREECE,
    HUNGARY("HU", "HUN", ZoneId.of("Europe/Budapest")),
    ICELAND,
    IRELAND("IE", "IRL", ZoneId.of("Europe/Dublin")),
    ITALY("IT", "ITA", ZoneId.of("Europe/Rome")),
    KAZAKHSTAN,
    KOSOVO,
    LATVIA,
    LIECHTENSTEIN,
    LITHUANIA,
    LUXEMBOURG("LU", "LUX", ZoneId.of("Europe/Luxembourg")),
    MALTA,
    MOLDOVA,
    MONACO,
    MONTENEGRO,
    NETHERLANDS("NL", "NLD", ZoneId.of("Europe/Amsterdam")),
    NORTH_MACEDONIA,
    NORWAY("NO", "NOR", ZoneId.of("Europe/Oslo")),
    POLAND("PL", "POL", ZoneId.of("Europe/Warsaw")),
    PORTUGAL("PT", "PRT", ZoneId.of("Europe/Lisbon")),
    ROMANIA("RO", "ROU", ZoneId.of("Europe/Bucharest")),
    RUSSIA("RU", "RUS", ZoneId.of("Europe/Moscow")),
    SAN_MARINO,
    SERBIA,
    SLOVAKIA("SK", "SVK", ZoneId.of("Europe/Bratislava")),
    SLOVENIA("SI", "SVN", ZoneId.of("Europe/Ljubljana")),
    SPAIN("ES", "ESP", ZoneId.of("Europe/Madrid")),
    SWEDEN("SE", "SWE", ZoneId.of("Europe/Stockholm")),
    SWITZERLAND("CH", "CHE", ZoneId.of("Europe/Zurich")),
    TURKEY,
    UKRAINE,
    UNITED_KINGDOM("GB", "GBR", ZoneId.of("Europe/London")),
    VATICAN_CITY;

    /**
     * ISO code of this country in 2 letters
     */
    private String code2;

    /**
     * ISO code of this country in 3 letters
     */
    private String code3;

    /**
     * Timezone of the country, if it has a single one.
     */
    private ZoneId timezone;

    @Nullable
    public static Country byCode(@Nullable String code) {
        if (code != null) {
            for (Country country : values()) {
                if (Objects.equals(country.getCode2(), code) || Objects.equals(country.getCode3(), code)) {
                    return country;
                }
            }
        }

        return null;
    }
}
