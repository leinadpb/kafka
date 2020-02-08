package com.leinad.models;

import com.leinad.enums.Color;
import com.leinad.enums.DesignType;
import com.leinad.enums.ProductType;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class InternalProduct {
    private Color color;
    private ProductType type;
    private DesignType designType;
}
