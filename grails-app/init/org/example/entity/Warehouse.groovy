package org.example

import grails.gorm.annotation.Entity

@Entity
class Warehouse {
    String name

    static hasMany = [warehouseProducts: WarehouseProduct]

    static constraints = {
        name(blank: false)
    }

    static mapping = {
        name column: 'warehouse_name'
        storageProducts cascade: "all-delete-orphan"
    }
}