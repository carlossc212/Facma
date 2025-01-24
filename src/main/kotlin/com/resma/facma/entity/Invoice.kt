package com.resma.facma.entity

import com.resma.facma.entity.interfaces.Emisor
import com.resma.facma.entity.interfaces.Receptor
import java.util.Date

class Invoice {
    private lateinit var invoiceNumber: String
    private lateinit var issueDate: Date
    private lateinit var dueDate: Date
    private lateinit var emisor: Emisor
    private lateinit var receptor: Receptor
    private lateinit var products: Array<Product>
    private var amount: Double = 0.0
    private lateinit var createdAt: Date
    private lateinit var updatedAt: Date
}