package org.example

import grails.gorm.transactions.Transactional
import grails.rest.RestfulController
import org.example.entity.Product
import org.springframework.http.HttpStatus
import org.example.entity.Store


@Transactional (readOnly = true)
class StoreController extends RestfulController<Store> {
    static responseFormats = ['json', 'xml']

    StoreService storeService

    StoreController() {
        super(Store)
    }

    @Transactional
    def createStore() {
        def name = params.name
        def address = params.address

        storeService.addStore(name, address)

        respond([message: "Store created successfully"], status: HttpStatus.CREATED)
    }

    def sellProduct(Long storeId, Long productId, int quantity) {
        Store store = Store.get(storeId)
        Product product = Product.get(productId)
        if (store && product) {
            try {
                storeService.sellProduct(store, product, quantity)
                respond([message: "Product sold"], status: HttpStatus.OK)
            } catch (IllegalArgumentException e) {
                respond([message: e.message], status: HttpStatus.BAD_REQUEST)
            }
        } else {
            respond([message: "Store or Product not found"] as CharSequence, status: HttpStatus.NOT_FOUND)
        }
    }

    def returnProductToWarehouse(Long storeId, Long productId, int quantity) {
        Store store = Store.get(storeId)
        Product product = Product.get(productId)
        if (store && product) {
            try {
                storeService.returnProductToWarehouse(store, product, quantity)
                respond([message: "Product returned to warehouse"], status: HttpStatus.OK)
            } catch (IllegalArgumentException e) {
                respond([message: e.message], status: HttpStatus.BAD_REQUEST)
            }
        } else {
            respond([message: "Store or Product not found"] as CharSequence, status: HttpStatus.NOT_FOUND)
        }
    }
}
//    def returnExpiredProductsToWarehouse(Long storeId) {
//        Store store = Store.get(storeId)
//        if (store) {
//            storeService.returnExpiredProductsToWarehouse(store)
//            respond ([message: "Expired products returned to warehouse"], status: HttpStatus.OK)
//        } else {
//            respond ([message: "Store not found"] as CharSequence,status: HttpStatus.NOT_FOUND)
//        }
//    }

