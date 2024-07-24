package org.example

import grails.gorm.annotation.Entity

import java.time.LocalDate

@Entity
class Product {

    String name
    Double price
    LocalDate manufactureDate
    LocalDate expiryDate

    static constraints = {
        name(blank: false)
        price(nullable: false, min: 0.0d)
        manufactureDate(nullable: false, validator: { val, obj ->
            if (val != null) {
                if (val.isAfter(LocalDate.now())) {
                    return "Manufacture date cannot be in the future."
                }
                if (obj.expiryDate != null && val.isAfter(obj.expiryDate)) {
                    return "Expiry date cannot be before manufacture date."
                }
            }
        })
        expiryDate(nullable: true, validator: { val, obj ->
            if (val != null) {
                if (val.isBefore(LocalDate.now())) {
                    return "Expiry date cannot be in the past."
                }
            }
        })
    }

    static mapping = {
        name column: 'product_name'
        price column: 'product_price'
        manufactureDate column: 'manufacture_date'
        expiryDate column: 'expiry_date'
    }
}
