package com.leinad.models;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Event {
    private InternalUser user;
    private InternalProduct product;
}
