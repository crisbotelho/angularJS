package de.quandoo.recruitment.registry.builder;

import de.quandoo.recruitment.registry.model.Cuisine;

import java.util.UUID;

public class CuisineBuider {

    private CuisineBuider(){}

    public static Cuisine createCuisine(String name) {
        return new Cuisine(UUID.randomUUID().toString(), name);
    }

}
