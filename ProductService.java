package com.example.demo.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.Model.Product;
import com.example.demo.Exception.ProductNotFoundException;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private Long nextId = 1L;

    // CREATE
    public Product addProduct(Product product) {

        validateProduct(product);

        product.setProductId(nextId++);

        Product saved = copyProduct(product);
        products.add(saved);

        return copyProduct(saved);
    }

    // READ ALL
    public List<Product> getAllProducts() {

        return products.stream()
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // READ BY ID
    public Product getProductById(Long id) {

        return copyProduct(findProduct(id));
    }

    // UPDATE
    public Product updateProduct(Long id, Product updatedProduct) {

        validateProduct(updatedProduct);

        Product existing = findProduct(id);

        existing.setProductName(updatedProduct.getProductName());
        existing.setCategory(updatedProduct.getCategory());
        existing.setStock(updatedProduct.getStock());
        existing.setPrice(updatedProduct.getPrice());
        existing.setBrand(updatedProduct.getBrand());

        return copyProduct(existing);
    }

    // DELETE
    public String deleteProduct(Long id) {

        Product product = findProduct(id);
        products.remove(product);

        return "Product deleted successfully with ID: " + id;
    }

    // PARTIAL NAME SEARCH
    public List<Product> searchByName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Search name cannot be blank");
        }

        String keyword = name.trim().toLowerCase(Locale.ROOT);

        return products.stream()
                .filter(p -> p.getProductName()
                        .toLowerCase(Locale.ROOT)
                        .contains(keyword))
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // FILTER BY CATEGORY
    public List<Product> filterByCategory(String category) {

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Category cannot be blank");
        }

        return products.stream()
                .filter(p -> p.getCategory()
                        .equalsIgnoreCase(category.trim()))
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // FILTER BY PRICE RANGE
    public List<Product> filterByPrice(
            BigDecimal minPrice, BigDecimal maxPrice) {

        if (minPrice == null || maxPrice == null
                || minPrice.compareTo(BigDecimal.ZERO) < 0
                || minPrice.compareTo(maxPrice) > 0) {

            throw new IllegalArgumentException(
                    "Provide a valid minimum and maximum price");
        }

        return products.stream()
                .filter(p -> p.getPrice().compareTo(minPrice) >= 0
                        && p.getPrice().compareTo(maxPrice) <= 0)
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // FIND LOW-STOCK PRODUCTS
    public List<Product> getLowStockProducts(Integer limit) {

        if (limit == null || limit < 0) {
            throw new IllegalArgumentException(
                    "Stock limit cannot be negative");
        }

        return products.stream()
                .filter(p -> p.getStock() <= limit)
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // SEARCH BY BRAND
    public List<Product> searchByBrand(String brand) {

        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException(
                    "Brand cannot be blank");
        }

        String keyword = brand.trim().toLowerCase(Locale.ROOT);

        return products.stream()
                .filter(p -> p.getBrand()
                        .toLowerCase(Locale.ROOT)
                        .contains(keyword))
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // SORT BY PRICE
    public List<Product> sortByPrice(String direction) {

        Comparator<Product> comparator =
                Comparator.comparing(Product::getPrice);

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        } else if (!"asc".equalsIgnoreCase(direction)) {
            throw new IllegalArgumentException(
                    "Direction must be asc or desc");
        }

        return products.stream()
                .sorted(comparator)
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // SORT BY NAME
    public List<Product> sortByName() {

        return products.stream()
                .sorted(Comparator.comparing(
                        Product::getProductName,
                        String.CASE_INSENSITIVE_ORDER))
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // SORT BY STOCK, HIGHEST FIRST
    public List<Product> sortByStock() {

        return products.stream()
                .sorted(Comparator.comparing(
                        Product::getStock).reversed())
                .map(this::copyProduct)
                .collect(Collectors.toList());
    }

    // COUNT PRODUCTS
    public int getProductCount() {

        return products.size();
    }

    // FIND PRODUCT BY ID
    private Product findProduct(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be greater than zero");
        }

        return products.stream()
                .filter(p -> Objects.equals(p.getProductId(), id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with ID: " + id));
    }

    // VALIDATE PRODUCT
    private void validateProduct(Product product) {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product details cannot be null");
        }

        if (product.getProductName() == null
                || product.getProductName().trim().length() < 3
                || product.getProductName().trim().length() > 50) {

            throw new IllegalArgumentException(
                    "Product name must contain 3 to 50 characters");
        }

        if (product.getCategory() == null
                || !product.getCategory().trim()
                        .matches("[a-zA-Z ]+")) {

            throw new IllegalArgumentException(
                    "Category must contain only letters and spaces");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot be negative or null");
        }

        if (product.getPrice() == null
                || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Price should be greater than zero");
        }

        if (product.getBrand() == null
                || product.getBrand().isBlank()
                || product.getBrand().trim().length() > 40) {

            throw new IllegalArgumentException(
                    "Brand is mandatory and cannot exceed 40 characters");
        }
    }

    // COPY PRODUCT
    private Product copyProduct(Product p) {

        return new Product(
                p.getProductId(),
                p.getProductName(),
                p.getCategory(),
                p.getStock(),
                p.getPrice(),
                p.getBrand()
        );
    }
}