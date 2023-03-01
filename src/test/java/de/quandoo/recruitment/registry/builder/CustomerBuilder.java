package de.quandoo.recruitment.registry.builder;

import de.quandoo.recruitment.registry.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerBuilder {

    private CustomerBuilder(){}

    public static List<Customer> createCustomers(final int quantity) {
        List<Customer> customers = new ArrayList<>(quantity);
        for(int i = 0; i < quantity; i++) {
            customers.add(createCustomer());
        }
        return customers;
    }

    public static Customer createCustomer() {
        return new Customer(UUID.randomUUID().toString());
    }
}
