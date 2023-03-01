package de.quandoo.recruitment.registry.model;

import java.util.Objects;

public class Customer {
    private final String uuid;

    public Customer(final String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return uuid.equals(customer.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
