package de.quandoo.recruitment.registry;

import de.quandoo.recruitment.registry.builder.CuisineBuider;
import de.quandoo.recruitment.registry.builder.CustomerBuilder;
import de.quandoo.recruitment.registry.model.Cuisine;
import de.quandoo.recruitment.registry.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InMemoryCuisinesRegistryTest {

    @Mock
    private ConcurrentHashMap<String, List<Cuisine>> customerCuisines;
    @Mock
    private ConcurrentHashMap<Cuisine, List<Customer>> cuisineCustomers;
    @InjectMocks
    private InMemoryCuisinesRegistry cuisinesRegistry;

    @Test
    @DisplayName("It should register the customer to the cuisine")
    public void givenCustomerAndCuisine_whenCuisinesRegistry_thenOneCustomerAddedToCuisine() {
        final int expectedSize = 1;
        final String expectedCuisineName = "Japanese";

        ArgumentCaptor<Cuisine> capturedCuisine = ArgumentCaptor.forClass(Cuisine.class);
        ArgumentCaptor<List<Customer>> capturedCustomerList = ArgumentCaptor.forClass(List.class);

        //action
        Cuisine japaneseCuisine = CuisineBuider.createCuisine("Japanese");
        cuisinesRegistry.register(CustomerBuilder.createCustomer(), japaneseCuisine);

        //Assert
        verify(cuisineCustomers).put(capturedCuisine.capture(), capturedCustomerList.capture());
        Cuisine capturedCuisineValue = capturedCuisine.getValue();
        List<Customer> capturedCustomerListValue = capturedCustomerList.getValue();
        assertEquals(expectedCuisineName, capturedCuisineValue.getName());
        assertEquals(expectedSize, capturedCustomerListValue.size());

    }

    @Test
    @DisplayName("It should register the cuisine to the customer")
    public void givenCustomerAndCuisine_whenCuisinesRegistry_thenOneCuisineAddedToCustomer() {
        final int expectedSize = 1;

        ArgumentCaptor<String> capturedCustomer = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<Cuisine>> capturedCuisineList = ArgumentCaptor.forClass(List.class);

        //action
        Cuisine japaneseCuisine = CuisineBuider.createCuisine("Japanese");
        Customer customer = CustomerBuilder.createCustomer();
        cuisinesRegistry.register(customer, japaneseCuisine);

        //Assert
        verify(customerCuisines).put(capturedCustomer.capture(), capturedCuisineList.capture());
        String capturedCustomerValue = capturedCustomer.getValue();
        List<Cuisine> capturedCuisineListValue = capturedCuisineList.getValue();
        assertEquals(customer.getUuid(), capturedCustomerValue);
        assertEquals(expectedSize, capturedCuisineListValue.size());

    }

    @Test
    @DisplayName("It should add the cuisine to the cuisine list related to the customer")
    public void givenCustomersAndCuisines_whenCuisinesRegistry_thenCuisinesAddedToCustomer() {
        final int expectedSizeThree = 3;
        //arrange
        Cuisine japaneseCuisine = CuisineBuider.createCuisine("Japanese");
        Cuisine brazilianCuisine = CuisineBuider.createCuisine("Brazilian");
        Cuisine berlinerCuisine = CuisineBuider.createCuisine("Berliner");
        List<Cuisine> cuisines = new ArrayList<>();
        cuisines.add(japaneseCuisine);
        cuisines.add(brazilianCuisine);
        Customer customer = CustomerBuilder.createCustomer();

        when(customerCuisines.containsKey(customer.getUuid())).thenReturn(Boolean.TRUE);
        when(customerCuisines.get(customer.getUuid())).thenReturn(cuisines);
        //action
        cuisinesRegistry.register(customer, berlinerCuisine);

        //Assert
        assertEquals(expectedSizeThree, cuisines.size());
    }

    @Test
    @DisplayName("It should add the customer to the customer list related to the cuisine")
    public void givenCustomersAndCuisines_whenCuisinesRegistry_thenCustomersAddedToCuisine() {
        final int expectedSizeFive = 5;
        //arrange
        Cuisine japaneseCuisine = CuisineBuider.createCuisine("Japanese");
        List<Customer> customerList = CustomerBuilder.createCustomers(4);
        Customer customerToAdd = CustomerBuilder.createCustomer();

        when(cuisineCustomers.containsKey(japaneseCuisine)).thenReturn(Boolean.TRUE);
        when(cuisineCustomers.get(japaneseCuisine)).thenReturn(customerList);
        //action
        cuisinesRegistry.register(customerToAdd, japaneseCuisine);

        //Assert
        assertEquals(expectedSizeFive, customerList.size());
    }

    @Test
    @DisplayName("It should list the four customers registered with the german cuisine")
    public void givenGermanCuisine_whenCuisineCustomers_thenFourCustomers() {
        final Integer expected = 4;
        //arrange
        Cuisine germanCuisine = CuisineBuider.createCuisine("German");
        List<Customer> customerList = CustomerBuilder.createCustomers(4);
        when(cuisineCustomers.get(germanCuisine)).thenReturn(customerList);
        //action
        List<Customer> customersResponse = cuisinesRegistry.cuisineCustomers(germanCuisine);
        //assertions
        assertEquals(expected, customersResponse.size());
    }

    @Test
    @DisplayName("It should list the three cuisines registered with the specific customer")
    public void givenCustomer_whenCustomerCuisines_thenThreeCuisines() {
        final Integer expected = 3;
        //arrange
        Customer customer = CustomerBuilder.createCustomer();
        Cuisine koreanCuisine = CuisineBuider.createCuisine( "Korean");
        Cuisine japaneseCuisine = CuisineBuider.createCuisine( "Japanese");
        Cuisine brazilianCuisine = CuisineBuider.createCuisine( "Brazilian");
        List<Cuisine> cuisineList = Arrays.asList(koreanCuisine, japaneseCuisine, brazilianCuisine);
        when(customerCuisines.get(customer.getUuid())).thenReturn(cuisineList);
        //action
        List<Cuisine> cuisinesResponse = cuisinesRegistry.customerCuisines(customer);
        //assert
        assertEquals(expected, cuisinesResponse.size());

    }


    @Test
    @DisplayName("It should list the three top cuisines (Japanese, Brazilian and Korean)")
    public void whenTopCuisines_thenReturnJapaneseBrazilianAndKorenCuisines() {
        final Integer expectedSize = 3;
        //Arrange
        List<Customer> customerList = CustomerBuilder.createCustomers(12);

        Cuisine germanCuisine = CuisineBuider.createCuisine("German");
        Cuisine italianCuisine = CuisineBuider.createCuisine("Italian");
        Cuisine frenchCuisine = CuisineBuider.createCuisine("French");
        Cuisine korenCuisine = CuisineBuider.createCuisine("Korean");
        Cuisine japaneseCuisine = CuisineBuider.createCuisine("Japanese");
        Cuisine brazilianCuisine = CuisineBuider.createCuisine("Brazilian");

        List<Cuisine> cuisines = new ArrayList<>(3);
        cuisines.add(japaneseCuisine);
        cuisines.add(brazilianCuisine);
        cuisines.add(korenCuisine);

        Set<Map.Entry<Cuisine, List<Customer>>> entries = new HashSet<>();
        entries.add(new AbstractMap.SimpleEntry<>(germanCuisine, Arrays.asList(customerList.get(0), customerList.get(1))));
        entries.add(new AbstractMap.SimpleEntry<>(italianCuisine, Arrays.asList(customerList.get(2))));
        entries.add(new AbstractMap.SimpleEntry<>(frenchCuisine, Arrays.asList(customerList.get(3))));
        entries.add(new AbstractMap.SimpleEntry<>(korenCuisine, Arrays.asList(customerList.get(4), customerList.get(5),
                customerList.get(3))));
        entries.add(new AbstractMap.SimpleEntry<>(japaneseCuisine, Arrays.asList(customerList.get(6), customerList.get(7),
                customerList.get(8), customerList.get(9))));
        entries.add(new AbstractMap.SimpleEntry<>(brazilianCuisine,
                Arrays.asList(customerList.get(10), customerList.get(11), customerList.get(0), customerList.get(1))));

        when(cuisineCustomers.entrySet()).thenReturn(entries);
        //Action
        List<Cuisine> cuisineListResponse = cuisinesRegistry.topCuisines(3);
        //Assertions
        assertThat(cuisines, containsInAnyOrder(cuisineListResponse.get(0),
                cuisineListResponse.get(1), cuisineListResponse.get(2)));
        assertEquals(expectedSize, cuisineListResponse.size());
    }

    @Test
    @DisplayName("It should return Japanese cuisine as the top one cuisine")
    public void whenTopCuisines_thenReturnJapaneseCuisine() {
        final Integer expectedSize = 1;

        //arrange
        List<Customer> customerList = CustomerBuilder.createCustomers(12);

        Cuisine germanCuisine = CuisineBuider.createCuisine("German");
        Cuisine italianCuisine = CuisineBuider.createCuisine("Italian");
        Cuisine frenchCuisine = CuisineBuider.createCuisine("French");
        Cuisine korenCuisine = CuisineBuider.createCuisine("Korean");
        Cuisine japaneseCuisine = CuisineBuider.createCuisine("Japanese");
        Cuisine brazilianCuisine = CuisineBuider.createCuisine("Brazilian");

        List<Cuisine> cuisines = new ArrayList<>(1);
        cuisines.add(japaneseCuisine);

        Set<Map.Entry<Cuisine, List<Customer>>> entries = new HashSet<>();
        entries.add(new AbstractMap.SimpleEntry<>(germanCuisine, Arrays.asList(customerList.get(0), customerList.get(1))));
        entries.add(new AbstractMap.SimpleEntry<>(italianCuisine, Arrays.asList(customerList.get(2))));
        entries.add(new AbstractMap.SimpleEntry<>(frenchCuisine, Arrays.asList(customerList.get(3))));
        entries.add(new AbstractMap.SimpleEntry<>(korenCuisine, Arrays.asList(customerList.get(4), customerList.get(5))));
        entries.add(new AbstractMap.SimpleEntry<>(japaneseCuisine, Arrays.asList(customerList.get(6), customerList.get(7),
                customerList.get(8), customerList.get(9))));
        entries.add(new AbstractMap.SimpleEntry<>(brazilianCuisine,
                Arrays.asList(customerList.get(10), customerList.get(11))));

        when(cuisineCustomers.entrySet()).thenReturn(entries);
        //action
        List<Cuisine> cuisineListResponse = cuisinesRegistry.topCuisines(1);
        //assertions
        assertThat(cuisines, containsInAnyOrder(cuisineListResponse.get(0)));
        assertEquals(expectedSize, cuisineListResponse.size());
    }

    @Test
    @DisplayName("It should return an empty list")
    public void whenTopCuisines_thenReturnEmptyList() {
        final Integer expectedSize = 0;
        //arrange
        Set<Map.Entry<Cuisine, List<Customer>>> entries = new HashSet<>();
        when(cuisineCustomers.entrySet()).thenReturn(entries);
        //action
        List<Cuisine> cuisineListResponse = cuisinesRegistry.topCuisines(3);
        //assert
        assertEquals(expectedSize, cuisineListResponse.size());
    }

    @Test
    @DisplayName("It should return all cuisine as the top cuisines")
    public void whenTopCuisines_thenReturnAllCuisines() {
        final Integer expectedSize = 6;

        //arrange
        List<Customer> customerList = CustomerBuilder.createCustomers(12);

        Cuisine germanCuisine = CuisineBuider.createCuisine("German");
        Cuisine italianCuisine = CuisineBuider.createCuisine("Italian");
        Cuisine frenchCuisine = CuisineBuider.createCuisine("French");
        Cuisine korenCuisine = CuisineBuider.createCuisine("Korean");
        Cuisine japaneseCuisine = CuisineBuider.createCuisine("Japanese");
        Cuisine brazilianCuisine = CuisineBuider.createCuisine("Brazilian");

        Set<Map.Entry<Cuisine, List<Customer>>> entries = new HashSet<>();
        entries.add(new AbstractMap.SimpleEntry<>(germanCuisine, Arrays.asList(customerList.get(0), customerList.get(1))));
        entries.add(new AbstractMap.SimpleEntry<>(italianCuisine, Arrays.asList(customerList.get(2))));
        entries.add(new AbstractMap.SimpleEntry<>(frenchCuisine, Arrays.asList(customerList.get(3))));
        entries.add(new AbstractMap.SimpleEntry<>(korenCuisine, Arrays.asList(customerList.get(4), customerList.get(5))));
        entries.add(new AbstractMap.SimpleEntry<>(japaneseCuisine, Arrays.asList(customerList.get(6), customerList.get(7),
                customerList.get(8), customerList.get(9))));
        entries.add(new AbstractMap.SimpleEntry<>(brazilianCuisine,
                Arrays.asList(customerList.get(10), customerList.get(11))));

        when(cuisineCustomers.entrySet()).thenReturn(entries);

        //action
        List<Cuisine> cuisineListResponse = cuisinesRegistry.topCuisines(10);
        //assertions
        assertEquals(expectedSize, cuisineListResponse.size());
    }

    @Test
    @DisplayName("It should return two cuisines")
    public void givenMoreThanThreeTopCuisines_whenTopCuisines_thenReturnTwoTopCuisines() {
        final Integer expectedSize = 2;

        //arrange
        List<Customer> customerList = CustomerBuilder.createCustomers(12);

        Cuisine germanCuisine = CuisineBuider.createCuisine("German");
        Cuisine italianCuisine = CuisineBuider.createCuisine("Italian");
        Cuisine frenchCuisine = CuisineBuider.createCuisine("French");
        Cuisine korenCuisine = CuisineBuider.createCuisine("Korean");
        Cuisine japaneseCuisine = CuisineBuider.createCuisine("Japanese");
        Cuisine brazilianCuisine = CuisineBuider.createCuisine("Brazilian");

        Set<Map.Entry<Cuisine, List<Customer>>> entries = new HashSet<>();
        entries.add(new AbstractMap.SimpleEntry<>(germanCuisine, Arrays.asList(customerList.get(0), customerList.get(1))));
        entries.add(new AbstractMap.SimpleEntry<>(italianCuisine, Arrays.asList(customerList.get(2))));
        entries.add(new AbstractMap.SimpleEntry<>(frenchCuisine, Arrays.asList(customerList.get(3))));
        entries.add(new AbstractMap.SimpleEntry<>(korenCuisine, Arrays.asList(customerList.get(4), customerList.get(5))));
        entries.add(new AbstractMap.SimpleEntry<>(japaneseCuisine, Arrays.asList(customerList.get(6), customerList.get(7),
                customerList.get(8), customerList.get(9))));
        entries.add(new AbstractMap.SimpleEntry<>(brazilianCuisine,
                Arrays.asList(customerList.get(10), customerList.get(11))));

        when(cuisineCustomers.entrySet()).thenReturn(entries);

        //action
        List<Cuisine> cuisineListResponse = cuisinesRegistry.topCuisines(2);
        //assertions
        assertEquals(expectedSize, cuisineListResponse.size());
    }


}