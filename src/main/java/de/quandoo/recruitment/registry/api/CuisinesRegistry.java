package de.quandoo.recruitment.registry.api;

import de.quandoo.recruitment.registry.model.Cuisine;
import de.quandoo.recruitment.registry.model.Customer;
import java.util.List;

public interface CuisinesRegistry {

    void register(Customer customer, Cuisine cuisine);

    List<Cuisine> customerCuisines(Customer customer);

    List<Cuisine> topCuisines(int n);

    List<Customer> cuisineCustomers(Cuisine cuisine);
}
