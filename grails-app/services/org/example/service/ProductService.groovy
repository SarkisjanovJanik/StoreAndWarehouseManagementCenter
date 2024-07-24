package org.example

import grails.gorm.transactions.Transactional
import grails.validation.ValidationException
import org.example.entity.Product

import java.time.LocalDate

@Transactional
class ProductService {

    List<Product> getAllProducts() {
        Product.list()
    }

    Product getProductById(Long id) {
        Product.get(id)
    }

    def addProduct(String name, Double price, LocalDate manufactureDate, LocalDate expiryDate) {
        def product
                = new Product(name: name, price: price, manufactureDate: manufactureDate, expiryDate: expiryDate).save(failOnError: true)
        return product
    }


    def updateProduct(Long id, Map<String, Object> updates) {
        def product = Product.findById(id)
        if (!product) {
            throw new IllegalArgumentException("Product not found with ID: $id")
        }

        if (updates.name != null) {
            product.name = updates.name
        }

        if (updates.price != null) {
            product.price = updates.price
        }

        if (updates.manufactureDate != null) {
            product.manufactureDate = updates.manufactureDate
        }

        if (updates.containsKey('expiryDate')) {
            product.expiryDate = updates.expiryDate
        }

        if (!product.validate()) {
            throw new ValidationException("Invalid product data")
        }

        product.save(flush: true)
        return product
    }


    def deleteProduct(Long id) {
        def product = Product.findById(id)
        if (!product) {
            throw new IllegalArgumentException("Product not found with ID: $id")
        }

        product.delete(flush: true)
        return product
    }
}



