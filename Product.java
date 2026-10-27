package com.example.demo.Model;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long productId;

    @NotBlank(message = "Product name is mandatory")
    @Size(min = 3, max = 50,
          message = "Product name must contain 3 to 50 characters")
    private String productName;

    @NotBlank(message = "Category is mandatory")
    @Pattern(regexp = "^[a-zA-Z ]+$",
             message = "Category must contain only letters and spaces")
    private String category;

    @NotNull(message = "Stock is mandatory")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price should be greater than zero")
    @DecimalMin(value = "0.01",
                message = "Price must be at least 0.01")
    private BigDecimal price;

    @NotBlank(message = "Brand is mandatory")
    @Size(max = 40, message = "Brand cannot exceed 40 characters")
    private String brand;
}