package eu.railplanner.railplanner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Country {
    ALBANIA,
    ANDORRA,
    ARMENIA,
    AUSTRIA("AT"),
    AZERBAIJAN,
    BELARUS,
    BELGIUM("BE"),
    BOSNIA_AND_HERZEGOVINA,
    BULGARIA,
    CROATIA,
    CYPRUS,
    CZECHIA,
    DENMARK,
    ESTONIA,
    FINLAND,
    FRANCE("FR"),
    GEORGIA,
    GERMANY("DE"),
    GREECE,
    HUNGARY,
    ICELAND,
    IRELAND,
    ITALY,
    KAZAKHSTAN,
    KOSOVO,
    LATVIA,
    LIECHTENSTEIN,
    LITHUANIA,
    LUXEMBOURG,
    MALTA,
    MOLDOVA,
    MONACO,
    MONTENEGRO,
    NETHERLANDS("NL"),
    NORTH_MACEDONIA,
    NORWAY,
    POLAND,
    PORTUGAL,
    ROMANIA,
    RUSSIA,
    SAN_MARINO,
    SERBIA,
    SLOVAKIA,
    SLOVENIA,
    SPAIN,
    SWEDEN,
    SWITZERLAND("CH"),
    TURKEY,
    UKRAINE,
    UNITED_KINGDOM("GB"),
    VATICAN_CITY;

    /**
     * ISO code of this country.
     */
    private String code;

    @Nullable
    public static Country byCode(@Nonnull String code) {
        for (Country country : values()) {
            if (Objects.equals(country.getCode(), code)) {
                return country;
            }
        }
        return null;
    }
}
