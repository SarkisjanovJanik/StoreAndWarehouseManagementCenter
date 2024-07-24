package org.example

import grails.gorm.transactions.Transactional
import org.example.entity.Product
import org.example.entity.Store
import org.example.entity.StoreProduct
import org.example.entity.Warehouse
import org.example.entity.WarehouseProduct

@Transactional
class WarehouseService {

    def addWarehouse(String name) {
        def warehouse = new Warehouse(name: name).save(failOnError: true)
        return warehouse
    }


    def addProductToWarehouse(Warehouse warehouse, Product product, int quantity) {
        if (!warehouse || !product) {
            throw new IllegalArgumentException("Warehouse or Product cannot be null")
        }

        WarehouseProduct existing = WarehouseProduct.findByWarehouseAndProduct(warehouse, product)

        if (existing) {
            existing.quantity += quantity
            if (!existing.save(flush: true)) {
                throw new RuntimeException("Failed to update existing WarehouseProduct")
            }
        } else {
            WarehouseProduct newWarehouseProduct = new WarehouseProduct(warehouse: warehouse, product: product, quantity: quantity)
            if (!newWarehouseProduct.save(flush: true)) {
                throw new RuntimeException("Failed to create new WarehouseProduct")
            }
        }
    }

    def removeProductFromWarehouse(Warehouse warehouse, Product product, int quantity) {
        if (!warehouse || !product) {
            throw new IllegalArgumentException("Warehouse or Product cannot be null")
        }

        WarehouseProduct existing = WarehouseProduct.findByWarehouseAndProduct(warehouse, product)

        if (existing) {
            if (existing.quantity < quantity) {
                if (!existing.delete(flush: true)) {
                    throw new RuntimeException("Failed to delete existing WarehouseProduct")
                }
            } else {
                existing.quantity -= quantity
                if (!existing.save(flush: true)) {
                    throw new RuntimeException("Failed to update existing WarehouseProduct")
                }
            }
        } else {
            throw new IllegalArgumentException("Product not found in the specified warehouse")
        }
    }


    def deliverProductToStore(Warehouse warehouse, Store store, Product product, int quantity) {
        if (!warehouse || !store || !product) {
            throw new IllegalArgumentException("Warehouse, Store, or Product cannot be null")
        }

        WarehouseProduct existingWarehouseProduct = WarehouseProduct.findByWarehouseAndProduct(warehouse, product)

        if (existingWarehouseProduct) {
            if (existingWarehouseProduct.quantity >= quantity) {
                existingWarehouseProduct.quantity -= quantity
                if (!existingWarehouseProduct.save(flush: true)) {
                    throw new RuntimeException("Failed to update existing WarehouseProduct")
                }

                StoreProduct existingStoreProduct = StoreProduct.findByStoreAndProduct(store, product)
                if (existingStoreProduct) {
                    existingStoreProduct.quantity += quantity
                    if (!existingStoreProduct.save(flush: true)) {
                        throw new RuntimeException("Failed to update existing StoreProduct")
                    }
                } else {
                    StoreProduct newStoreProduct = new StoreProduct(store: store, product: product, quantity: quantity, warehouse: warehouse)
                    if (!newStoreProduct.save(flush: true)) {
                        throw new RuntimeException("Failed to create new StoreProduct")
                    }
                }
            } else {
                throw new IllegalArgumentException("Not enough quantity of the product in the warehouse")
            }
        } else {
            throw new IllegalArgumentException("Product not found in the specified warehouse")
        }
    }
}