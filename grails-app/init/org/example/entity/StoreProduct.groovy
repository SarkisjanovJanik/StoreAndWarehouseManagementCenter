package org.example


import grails.gorm.annotation.Entity

@Entity
class StoreProduct implements Serializable {

    Store store
    Product product
    int quantity
    Warehouse warehouse // Add this line to track the source warehouse

    static belongsTo = [store: Store, product: Product]

    static constraints = {
        quantity min: 0
        warehouse nullable: false // Add this constraint
    }

    static mapping = {
        id composite: ['store', 'product']
        store column: 'store_id'
        product column: 'product_id'
        quantity column: 'quantity'
        warehouse column: 'warehouse_id'
    }
}

