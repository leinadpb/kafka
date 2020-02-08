package com.leinad;

import com.github.javafaker.Faker;
import com.leinad.enums.Color;
import com.leinad.enums.DesignType;
import com.leinad.enums.ProductType;
import com.leinad.enums.UserId;
import com.leinad.models.Event;
import com.leinad.models.InternalProduct;
import com.leinad.models.InternalUser;

public class EventGenerator {

    private Faker faker = new Faker();

    public Event generateEvent() {
        return Event.builder()
                .user(generateRandomUser())
                .product(generateRandomProduct())
                .build();
    }

    private InternalUser generateRandomUser() {
        return InternalUser.builder()
                .userId(faker.options().option(UserId.class))
                .username(faker.name().lastName())
                .dateOfBirth(faker.date().birthday())
                .build();
    }

    private InternalProduct generateRandomProduct() {
        return InternalProduct.builder()
                .color(faker.options().option(Color.class))
                .type(faker.options().option(ProductType.class))
                .designType(faker.options().option(DesignType.class))
                .build();
    }
}
