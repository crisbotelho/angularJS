package de.quandoo.recruitment.registry;

import de.quandoo.recruitment.registry.api.CuisinesRegistry;
import de.quandoo.recruitment.registry.model.Cuisine;
import de.quandoo.recruitment.registry.model.Customer;
import de.quandoo.recruitment.registry.model.DataBase;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class InMemoryCuisinesRegistry implements CuisinesRegistry {

    private ConcurrentHashMap<String, List<Cuisine>> customerCuisines = DataBase.getCustomerCuisines();
    private ConcurrentHashMap<Cuisine, List<Customer>> cuisineCustomers = DataBase.getCuisineCustomers();

    @Override
    public void register(final Customer customer, final Cuisine cuisine) {
        registerCustomerCuisines(customer, cuisine);
        registerCuisineCustomers(customer, cuisine);
    }

    private void registerCuisineCustomers(Customer customer, Cuisine cuisine) {
        if(!cuisineCustomers.containsKey(cuisine)) {
            List<Customer> customers = new ArrayList<>();
            customers.add(customer);
            cuisineCustomers.put(cuisine, customers);
        } else {
            List<Customer> customers = cuisineCustomers.get(cuisine);
            if(!customers.contains(customer)) {
                customers.add(customer);
            }
        }
    }

    private void registerCustomerCuisines(Customer customer, Cuisine cuisine) {
        if(!customerCuisines.containsKey(customer.getUuid())) {
            List<Cuisine> cuisines = new ArrayList<>();
            cuisines.add(cuisine);
            customerCuisines.put(customer.getUuid(), cuisines);
        } else {
            List<Cuisine> cuisines = customerCuisines.get(customer.getUuid());
            if(!cuisines.contains(cuisine)) {
                cuisines.add(cuisine);
            }
        }
    }

    @Override
    public List<Customer> cuisineCustomers(final Cuisine cuisine) {
        return cuisineCustomers.get(cuisine);
    }

    @Override
    public List<Cuisine> customerCuisines(final Customer customer) {
        return customerCuisines.get(customer.getUuid());
    }

    @Override
    public List<Cuisine> topCuisines(final int n) {
        return cuisineCustomers.entrySet().stream()
                .sorted(Collections.reverseOrder(comparingByValue(Comparator.comparingInt(List::size))))
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
