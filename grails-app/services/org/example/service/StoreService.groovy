package org.example

import grails.gorm.transactions.Transactional
import org.example.entity.Product
import org.example.entity.Store
import org.example.entity.StoreProduct
import org.example.entity.Warehouse

@Transactional
class StoreService {

    WarehouseService warehouseService


    def addStore(String name, String address) {
        def store =
                new Store(name: name, address: address).save(failOnError: true)
        return store
    }

    def sellProduct(Store store, Product product, int quantity) {
        if (!store || !product) {
            throw new IllegalArgumentException("Store or Product cannot be null")
        }

        StoreProduct storeProduct = StoreProduct.findByStoreAndProduct(store, product)
        if (storeProduct) {
            if (storeProduct.quantity >= quantity) {
                storeProduct.quantity -= quantity
                if (!storeProduct.save(flush: true)) {
                    throw new RuntimeException("Failed to update existing StoreProduct")
                }
                return true
            } else {
                throw new IllegalArgumentException("The product is either not found or there is not enough of it")
            }
        }
    }

    def returnProductToWarehouse(Store store, Product product, int quantity) {
        if (!store || !product) {
            throw new IllegalArgumentException("Store or Product cannot be null")
        }

        StoreProduct storeProduct = StoreProduct.findByStoreAndProduct(store, product)
        if (storeProduct && storeProduct.quantity >= quantity) {
            storeProduct.quantity -= quantity
            Warehouse warehouse = storeProduct.warehouse
            if (storeProduct.quantity == 0) {
                storeProduct.delete(flush: true)
            } else {
                storeProduct.save(flush: true)
            }
            warehouseService.addProductToWarehouse(warehouse, product, quantity)
            return true
        } else {
            return false
        }
    }
}
//    def returnExpiredProductsToWarehouse(Store store) {
//        store.products.each { StoreProduct storeProduct ->
//            if (storeProduct.product.expiryDate?.isBefore(LocalDate.now())) {
//                returnProductToWarehouse(store, storeProduct.product, storeProduct.quantity)
//            }
//        }

