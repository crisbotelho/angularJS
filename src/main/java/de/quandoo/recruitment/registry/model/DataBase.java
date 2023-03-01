package de.quandoo.recruitment.registry.model;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    private static ConcurrentHashMap<String, List<Cuisine>> customerCuisines;
    private static ConcurrentHashMap<Cuisine, List<Customer>> cuisineCustomers;

    private DataBase(){}

    public static ConcurrentHashMap<String, List<Cuisine>> getCustomerCuisines() {
        if(customerCuisines == null) {
            synchronized (DataBase.class) {
                if(customerCuisines == null) {
                    customerCuisines = new ConcurrentHashMap<>();
                }
            }
        }
        return customerCuisines;
    }

    public static ConcurrentHashMap<Cuisine, List<Customer>> getCuisineCustomers() {
        if(cuisineCustomers == null) {
            synchronized (DataBase.class) {
                if(cuisineCustomers == null) {
                    cuisineCustomers = new ConcurrentHashMap<>();
                }
            }
        }
        return cuisineCustomers;
    }
}
