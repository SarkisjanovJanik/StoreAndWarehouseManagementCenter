package org.example

import grails.gorm.annotation.Entity

@Entity
class Store {

    String name
    String address
    static hasMany = [storeProducts: StoreProduct]

    static constraints = {
        name(nullable: false)
        address(nullable: false)
    }

    static mapping = {
        name column: 'store_name'
        address column: 'store_address'
        storeProducts cascade: 'all-delete-orphan'
    }
}