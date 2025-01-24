package com.resma.facma.entity

class Product private constructor(val name: String, val description: String?, price: Double){
    var price: Double = price
        set(value){
            field = value
            updatePriceWithTax()
        }
    private var priceWithTax: Double = 0.0
    private var taxes: ArrayList<Tax> = ArrayList(0)

    companion object {
        fun create(name: String, description: String = "", price: Double): Product {
            if (name.isBlank()) throw Exception("The name of the product cannot be blank")
            if (price <= 0) throw Exception("The price of the product cannot be 0 or negative")

            val product = Product(name, description, price)
            product.applyTax(Tax.IVA)

            return product
        }
    }

    fun applyTax(tax: Tax){
        if (!taxes.contains(tax)) {
            taxes.add(tax)
            updatePriceWithTax()
        }
    }

    private fun updatePriceWithTax(){
        var plus = 0.0
        taxes.forEach{ tax -> plus += (price * tax.rate / 100) }

        priceWithTax = price + plus
    }

    override fun toString(): String {
        return "$name ($description): $price; taxes: $priceWithTax"
    }
}