### README.md

---

# Grails Application for Product, Warehouse, and Store Management

## Overview

This Grails application provides a comprehensive solution for managing products, warehouses, and stores. It offers a RESTful API for creating, updating, reading, and deleting products, as well as handling product inventory across warehouses and stores. Additionally, the application includes a scheduled task that automatically returns expired products to their respective warehouses.

## URL Mappings

The URL mappings define the endpoints available in the application. These mappings are configured in the `UrlMappings` class.

### Products

- `GET /products/read/{id}`: Retrieve product details.
- `PUT /products/update/{id}`: Update an existing product.
- `DELETE /products/remove/{id}`: Remove a product.
- `POST /products/create`: Create a new product.

### Warehouses

- `POST /warehouses/create`: Create a new warehouse.
- `POST /warehouses/addProduct`: Add a product to a warehouse.
- `DELETE /warehouses/removeProduct`: Remove a product from a warehouse.
- `POST /warehouses/deliverProduct`: Deliver a product to a store.

### Stores

- `POST /stores/create`: Create a new store.
- `POST /stores/sellProduct`: Sell a product from the store.
- `POST /stores/returnProduct`: Return a product to the warehouse.

## Scheduled Task for Expired Products

The application includes a scheduled task that runs daily to return expired products from stores to their original warehouses. This helps in maintaining the integrity of the inventory.

## Domain Classes

The application uses several domain classes to represent the entities in the system:

- **Product**: Represents a product with details such as name, price, manufacture date, and expiry date.
- **Store**: Represents a store with a name and address, and manages multiple store products.
- **StoreProduct**: Associates products with stores and tracks quantities. Each store product belongs to a specific store and warehouse.
- **Warehouse**: Represents a warehouse that stores multiple products.
- **WarehouseProduct**: Associates products with warehouses and tracks quantities.

## Unit Testing

Unit tests are provided to ensure the correctness of service methods and scheduled tasks. An example test for the `StoreService` class is shown below.


