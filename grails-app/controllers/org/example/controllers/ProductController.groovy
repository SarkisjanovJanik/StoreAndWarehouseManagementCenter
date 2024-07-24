package org.example

import grails.gorm.transactions.Transactional
import grails.rest.RestfulController
import grails.validation.ValidationException
import org.example.entity.Product
import org.springframework.http.HttpStatus

import java.time.LocalDate

@Transactional (readOnly = true)
class ProductController extends RestfulController<Product> {
    static responseFormats = ['json', 'xml']

    ProductService productService

    ProductController() {
        super(Product)
    }

    def readAll() {
        List<Product> products = productService.getAllProducts()
        respond products, [status: HttpStatus.OK]
    }

    def read(Long id) {
        if (!id) {
            respond([message: 'Product ID is required'], status: HttpStatus.BAD_REQUEST)
            return
        }

            def product = productService.getProductById(id)
            if (product) {
                respond(product, status: HttpStatus.OK)
            } else {
                respond([message: 'Product not found'], status: HttpStatus.NOT_FOUND)
        }
    }
    @Transactional
    def createProduct() {
        def requestBody = request.JSON
        def name = requestBody.name
        def price = requestBody.price as Double
        def manufactureDate = requestBody.manufactureDate ? LocalDate.parse(requestBody.manufactureDate) : null
        def expiryDate = requestBody.expiryDate ? LocalDate.parse(requestBody.expiryDate) : null

        if (!name || !price || !manufactureDate) {
            respond([message: 'Invalid input. Please provide name, price, and manufactureDate.'], status: HttpStatus.BAD_REQUEST)
            return
        }
            productService.addProduct(name, price, manufactureDate, expiryDate)
            respond([message: "Product created successfully"], status: HttpStatus.CREATED)

        }
    @Transactional
    def updateProduct(Long id) {
        def requestBody = request.JSON
        def updates = [:]

        if (requestBody.name) {
            updates.name = requestBody.name
        }

        if (requestBody.price) {
            updates.price = requestBody.price as Double
        }

        if (requestBody.manufactureDate) {
            updates.manufactureDate = LocalDate.parse(requestBody.manufactureDate)
        }

        if (requestBody.containsKey('expiryDate')) {
            updates.expiryDate = requestBody.expiryDate ? LocalDate.parse(requestBody.expiryDate) : null
        }

        if (!updates) {
            respond([message: 'No fields to update'], status: HttpStatus.BAD_REQUEST)
            return
        }

        try {
            def updatedProduct = productService.updateProduct(id, updates)
            respond(updatedProduct, status: HttpStatus.OK)
        } catch (ValidationException e) {
            respond([message: "Validation error"], status: HttpStatus.BAD_REQUEST)
        } catch (IllegalArgumentException e) {
            respond([message: "Product not found"], status: HttpStatus.NOT_FOUND)
        }
    }
    @Transactional
    def deleteProduct(Long id) {
        def requestBody = request.JSON

        if (!id) {
            respond([message: 'Invalid input. Please provide product ID.'], status: HttpStatus.BAD_REQUEST)
            return
        }

        try {
            def deletedProduct = productService.deleteProduct(id)
            respond([message: "Product deleted successfully", product: deletedProduct], status: HttpStatus.OK)
        } catch (IllegalArgumentException e) {
            respond([message: "Product not found"], status: HttpStatus.NOT_FOUND)
        }
   }
}