package com.example.hotelas.model.common;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class DiscountDTOForDiscountActivity extends DiscountDTO {
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public DiscountDTOForDiscountActivity() {
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
