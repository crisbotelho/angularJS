package de.quandoo.recruitment.registry.model;

import java.util.Objects;

public class Cuisine {

    private final String uuid;
    private final String name;

    public Cuisine(String uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuisine cuisine = (Cuisine) o;
        return uuid.equals(cuisine.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
