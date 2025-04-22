package com.example.hotelas.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO {
    String concrete;
    String commune;
    String district;
    String city;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (concrete != null && !concrete.isEmpty()) {
            sb.append(concrete);
        }
        if (commune != null && !commune.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(commune);
        }
        if (district != null && !district.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(district);
        }
        if (city != null && !city.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }

        return sb.toString();
    }
}
