package com.resma.facma.entity

import com.resma.facma.entity.interfaces.Emisor
import com.resma.facma.entity.interfaces.Receptor
import java.util.Date

class Invoice {
    lateinit var invoiceNumber: String
    lateinit var issueDate: Date
    lateinit var dueDate: Date
    lateinit var emisor: Emisor
    lateinit var receptor: Receptor
    lateinit var products: Array<Product>
    var amount: Double = 0.0
    lateinit var createdAt: Date
    lateinit var updatedAt: Date

    fun addTax(product: Product, tax: Tax){

    }
}