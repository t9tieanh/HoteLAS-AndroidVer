package com.example.hotelas.model.common;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AmenityDTO {
    @SerializedName("name")         private String name;
    @SerializedName("category")     private String categoryUrl;
    @SerializedName("categoryName") private String categoryName;
}
