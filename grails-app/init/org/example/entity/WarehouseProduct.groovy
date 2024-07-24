package org.example

import grails.gorm.annotation.Entity

@Entity
class WarehouseProduct implements Serializable {

    Warehouse warehouse
    Product product
    int quantity

    static belongsTo = [warehouse: Warehouse, product: Product]

    static constraints = {
        quantity min: 0
    }

    static mapping = {
        id composite: ['warehouse', 'product']
        warehouse column: 'warehouse_id'
        product column: 'product_id'
        quantity column: 'quantity'
    }
}