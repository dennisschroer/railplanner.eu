package eu.railplanner.railplanner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Country {
    ALBANIA("AL", "ALB"),
    ANDORRA("AD", "AND"),
    ARMENIA,
    AUSTRIA("AT", "AUT"),
    AZERBAIJAN,
    BELARUS("BY", "BLR"),
    BELGIUM("BE", "BEL"),
    BOSNIA_AND_HERZEGOVINA,
    BULGARIA,
    CROATIA("HR", "HRV"),
    CYPRUS,
    CZECHIA("CZ", "CZE"),
    DENMARK("DK", "DNK"),
    ESTONIA("EE", "EST"),
    FINLAND("FI", "FIN"),
    FRANCE("FR", "FRA"),
    GEORGIA,
    GERMANY("DE", "DEU"),
    GREECE,
    HUNGARY("HU", "HUN"),
    ICELAND,
    IRELAND("IE", "IRL"),
    ITALY("IT", "ITA"),
    KAZAKHSTAN,
    KOSOVO,
    LATVIA,
    LIECHTENSTEIN,
    LITHUANIA,
    LUXEMBOURG("LU", "LUX"),
    MALTA,
    MOLDOVA,
    MONACO,
    MONTENEGRO,
    NETHERLANDS("NL", "NLD"),
    NORTH_MACEDONIA,
    NORWAY("NO", "NOR"),
    POLAND("PL", "POL"),
    PORTUGAL("PT", "PRT"),
    ROMANIA("RO", "ROU"),
    RUSSIA("RU", "RUS"),
    SAN_MARINO,
    SERBIA,
    SLOVAKIA("SK", "SVK"),
    SLOVENIA("SI", "SVN"),
    SPAIN("ES", "ESP"),
    SWEDEN("SE", "SWE"),
    SWITZERLAND("CH", "CHE"),
    TURKEY,
    UKRAINE,
    UNITED_KINGDOM("GB", "GBR"),
    VATICAN_CITY;

    /**
     * ISO code of this country in 2 letters
     */
    private String code2;

    /**
     * ISO code of this country in 3 letters
     */
    private String code3;

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
