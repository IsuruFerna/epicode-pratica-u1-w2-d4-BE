package epicode;

import com.github.javafaker.Faker;
import epicode.entities.Customer;
import epicode.entities.Order;
import epicode.entities.Product;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {
        Supplier<String> randomCategory = () -> {
            String[] genre = {"Books", "Baby", "Toys", "Clothes", "Boys"};
            Random rnd = new Random();
            return genre[rnd.nextInt(0, genre.length)];
        };

        Faker faker = new Faker(Locale.ITALY);

        Supplier<Product> item = () -> new Product(faker.commerce().productName(), randomCategory.get(), faker.commerce().price());

        // creating product list
        List<Product> productList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            productList.add(item.get());
        }

//        productList.forEach(System.out::println);

        Supplier<Integer> randomTier = () -> {
            Random rnd = new Random();
            return rnd.nextInt(1, 4);
        };

        Supplier<LocalDate> randomOrderdDate = () -> {
            Random rnd = new Random();
            LocalDate today = LocalDate.now();
            return today.minusDays(rnd.nextInt(180, 360));
        };

        Supplier<LocalDate> randomDeliveryDate = () -> {
            Random rnd = new Random();
            LocalDate today = LocalDate.now();
            return today.plusDays(rnd.nextInt(0, 31));
        };

        Supplier<Product> randomProduct = () -> {
            Random rnd = new Random();
            return productList.get(rnd.nextInt(0, productList.size()));
        };

        Supplier<Customer> customer = () -> new Customer(faker.name().firstName(), randomTier.get());

        // crates 10 customers
        List<Customer> listCustomers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listCustomers.add(customer.get());
        }

        Supplier<Customer> randomCustomer = () -> {
            Random rnd = new Random();
            return listCustomers.get(rnd.nextInt(0, listCustomers.size()));
        };

        System.out.println("list customers: " + listCustomers);

        Supplier<Order> order = () -> new Order("status", randomOrderdDate.get(), randomDeliveryDate.get(), randomProduct.get(), randomCustomer.get());

        // creates orders
        List<Order> listOrder = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            listOrder.add(order.get());
        }

        // es1
        System.out.println("------------------------ Es1 --------------------------");

        Map<String, List<Order>> clientOrders = listOrder.stream().collect(Collectors.groupingBy(Order::getName));
        clientOrders.forEach((client, orders) -> System.out.println("customer: " + client + " , orders=" + orders));

        // es2
        System.out.println("------------------------ Es2 --------------------------");
        Map<String, Double> aquisiti = listOrder.stream().collect(Collectors.groupingBy(Order::getName, Collectors.summingDouble(clientOrder -> clientOrder.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum())));
        aquisiti.forEach((client, totalPrice) -> System.out.println("Customer: " + client + " , total: " + totalPrice));

        // es3
        System.out.println("------------------------ Es3 --------------------------");
/*        double maxPrice = productList.stream().mapToDouble(Product::getPrice).max().getAsDouble();
        System.out.println(maxPrice);
        Map<String, List<Product>> highestPriceProducts = productList.stream()
                .filter(price -> price.getPrice() == maxPrice)
                .collect(Collectors.groupingBy(Product::getName));
        System.out.println(highestPriceProducts);*/

        Map<String, List<Product>> highestPriceProducts = productList.stream()
                .filter(price -> price.getPrice() == productList.stream().mapToDouble(Product::getPrice).max().getAsDouble())
                .collect(Collectors.groupingBy(Product::getName));
        System.out.println(highestPriceProducts);

        // es4
        System.out.println("------------------------ Es4 --------------------------");
        Map<String, Integer> numOfOrders = listOrder.stream().collect(Collectors.groupingBy(Order::getName,
                Collectors.summingInt(clientOrder -> clientOrder.getProducts().size())));
        OptionalDouble averageOrders = numOfOrders.values().stream().mapToInt(Integer::intValue).average();
        System.out.println(numOfOrders);
        System.out.println("Average orders: " + averageOrders);

        // es5
        System.out.println("------------------------ Es5 --------------------------");
        Map<String, Double> categoryworth = productList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));
        System.out.println(categoryworth);

        // es6
        System.out.println("------------------------ Es6 --------------------------");
        for(int i = 0; i< productList.size(); i++) {
            System.out.println(productList.get(i));
        }

        File file = new File("src/output.txt");
        try{
            for(int i = 0; i< productList.size(); i++) {
                String row = productList.get(i).getName() + "@" + productList.get(i).getCategory() + "@" + productList.get(i).getPrice() + "#";
                FileUtils.writeStringToFile(file, row + System.lineSeparator(), StandardCharsets.UTF_8, true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // es7
        System.out.println("------------------------ Es7 --------------------------");
        try {
            String contenuto = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            System.out.println("trovato contenuto del file: " + contenuto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
