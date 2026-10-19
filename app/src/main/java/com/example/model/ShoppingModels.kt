package com.example.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null,
    val category: String,
    val rating: Float = 4.5f,
    val reviewCount: Int = 120,
    val imageUrl: String,
    val unit: String = "1 pc",
    val badge: String? = null,
    val isFeatured: Boolean = false
) {
    val discountPercent: Int?
        get() = originalPrice?.let {
            if (it > price) (((it - price) / it) * 100).toInt() else null
        }
}

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Double
        get() = product.price * quantity
}

enum class PaymentMethod(val label: String, val subtitle: String) {
    CASH_ON_DELIVERY("Cash on Delivery (COD)", "Pay with cash upon delivery"),
    CARD("Debit / Credit Card", "Visa, Mastercard, PayPak"),
    ONLINE_WALLET("Mobile Wallet / UPI", "Easypaisa, JazzCash, or GPay")
}

data class Order(
    val id: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val total: Double,
    val customerName: String,
    val phone: String,
    val deliveryAddress: String,
    val paymentMethod: PaymentMethod,
    val dateFormatted: String,
    val status: String = "In Delivery"
)

enum class ScreenTab {
    SHOP,
    CATEGORIES,
    CART,
    ORDERS
}
