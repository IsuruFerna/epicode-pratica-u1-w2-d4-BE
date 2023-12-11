package epicode.entities;

import epicode.Interfaces.iProduct;

import java.util.StringJoiner;


public class Product implements iProduct {
    private final Long id;
    private String name;
    private String category;
    private double price;

    public Product (String name, String category, String price) {
        this.id = generateid();
        this.name = name;
        this.category = category;
        this.price = Double.parseDouble(price);
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("category='" + category + "'")
                .add("price=" + price)
                .toString();
    }
}
