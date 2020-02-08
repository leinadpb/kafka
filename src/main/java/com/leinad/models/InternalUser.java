package com.leinad.models;

import com.leinad.enums.Color;
import com.leinad.enums.DesignType;
import com.leinad.enums.ProductType;
import com.leinad.enums.UserId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class InternalUser {
    private UserId userId;
    private String username;
    private Date dateOfBirth;
}
