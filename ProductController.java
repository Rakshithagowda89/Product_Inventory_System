package com.example.demo.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.Product;
import com.example.demo.Service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Product> addProduct(
            @Valid @RequestBody Product product) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(product));
    }

    // READ ALL
    @GetMapping
    public List<Product> getAllProducts() {

        return productService.getAllProducts();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {

        return productService.getProductById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {

        return productService.updateProduct(id, product);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        return productService.deleteProduct(id);
    }

    // PARTIAL NAME SEARCH
    @GetMapping("/search")
    public List<Product> searchByName(
            @RequestParam String name) {

        return productService.searchByName(name);
    }

    // FILTER BY CATEGORY
    @GetMapping("/category/{category}")
    public List<Product> filterByCategory(
            @PathVariable String category) {

        return productService.filterByCategory(category);
    }

    // FILTER BY PRICE RANGE
    @GetMapping("/filter/price")
    public List<Product> filterByPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        return productService.filterByPrice(min, max);
    }

    // LOW STOCK
    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(
            @RequestParam(defaultValue = "5") Integer limit) {

        return productService.getLowStockProducts(limit);
    }

    // SEARCH BY BRAND
    @GetMapping("/brand")
    public List<Product> searchByBrand(
            @RequestParam String name) {

        return productService.searchByBrand(name);
    }

    // SORT BY PRICE
    @GetMapping("/sort/price")
    public List<Product> sortByPrice(
            @RequestParam(defaultValue = "asc") String direction) {

        return productService.sortByPrice(direction);
    }

    // SORT BY NAME
    @GetMapping("/sort/name")
    public List<Product> sortByName() {

        return productService.sortByName();
    }

    // SORT BY STOCK
    @GetMapping("/sort/stock")
    public List<Product> sortByStock() {

        return productService.sortByStock();
    }

    // COUNT PRODUCTS
    @GetMapping("/count")
    public int getProductCount() {

        return productService.getProductCount();
    }
}