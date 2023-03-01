package de.quandoo.recruitment.registry;

import de.quandoo.recruitment.registry.builder.CuisineBuider;
import de.quandoo.recruitment.registry.builder.CustomerBuilder;
import de.quandoo.recruitment.registry.model.Cuisine;
import de.quandoo.recruitment.registry.model.Customer;
import de.quandoo.recruitment.registry.model.DataBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataBaseTest {

    @Test
    @DisplayName("It should return a not null instance")
    public void whenGetCustomerCuisines_thenReturnInstance() {
        //action
        ConcurrentHashMap<String, List<Cuisine>> customerCuisines = DataBase.getCustomerCuisines();
        //assertion
        Assertions.assertNotNull(customerCuisines);
    }

    @Test
    @DisplayName("It should return the same instance when #getCustomerCuisines is called")
    public void whenGetCustomerCuisines_thenReturnSameInstance() {
        //action
        ConcurrentHashMap<String, List<Cuisine>> customerCuisines = DataBase.getCustomerCuisines();
        ConcurrentHashMap<String, List<Cuisine>> customerCuisines2 = DataBase.getCustomerCuisines();

        customerCuisines.put(CustomerBuilder.createCustomer().getUuid(),
                Arrays.asList(CuisineBuider.createCuisine("Berliner"), CuisineBuider.createCuisine("Italian")));
        //assertions
        Assertions.assertSame(customerCuisines, customerCuisines2);
    }

    @Test
    @DisplayName("It should return a valid instance")
    public void whenGetCuisineCustomers_thenReturnInstance() {
        //action
        ConcurrentHashMap<Cuisine, List<Customer>> cuisineCustomers = DataBase.getCuisineCustomers();
        //assertion
        Assertions.assertNotNull(cuisineCustomers);
    }

    @Test
    @DisplayName("It should return the same instance when #getCuisineCustomers is called")
    public void whenGetCuisineCustomers_thenReturnSameInstance() {
        //action
        ConcurrentHashMap<Cuisine, List<Customer>> cuisineCustomers = DataBase.getCuisineCustomers();
        ConcurrentHashMap<Cuisine, List<Customer>> cuisineCustomers2 = DataBase.getCuisineCustomers();

        cuisineCustomers2.put(CuisineBuider.createCuisine("Berliner"),
                Arrays.asList(CustomerBuilder.createCustomer(), CustomerBuilder.createCustomer()));
        //assertions
        Assertions.assertSame(cuisineCustomers, cuisineCustomers2);
    }
}
