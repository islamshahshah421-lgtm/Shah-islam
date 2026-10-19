package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ShopData
import com.example.model.CartItem
import com.example.model.Order
import com.example.model.PaymentMethod
import com.example.model.Product
import com.example.model.ScreenTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ShoppingViewModel : ViewModel() {

    private val _products = MutableStateFlow(ShopData.sampleProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _currentTab = MutableStateFlow(ScreenTab.SHOP)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _cart = MutableStateFlow<Map<String, CartItem>>(emptyMap())
    val cart: StateFlow<Map<String, CartItem>> = _cart.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<String?>(null)
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.asStateFlow()

    private val _couponDiscountRate = MutableStateFlow(0.0)
    val couponDiscountRate: StateFlow<Double> = _couponDiscountRate.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _showCheckoutDialog = MutableStateFlow(false)
    val showCheckoutDialog: StateFlow<Boolean> = _showCheckoutDialog.asStateFlow()

    private val _lastPlacedOrder = MutableStateFlow<Order?>(null)
    val lastPlacedOrder: StateFlow<Order?> = _lastPlacedOrder.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _products,
        _searchQuery,
        _selectedCategory
    ) { all, query, category ->
        all.filter { product ->
            val matchesCategory = category == "All" || product.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShopData.sampleProducts)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun switchTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun addToCart(product: Product) {
        val current = _cart.value.toMutableMap()
        val existing = current[product.id]
        if (existing != null) {
            current[product.id] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current[product.id] = CartItem(product = product, quantity = 1)
        }
        _cart.value = current
    }

    fun decrementFromCart(productId: String) {
        val current = _cart.value.toMutableMap()
        val existing = current[productId] ?: return
        if (existing.quantity > 1) {
            current[productId] = existing.copy(quantity = existing.quantity - 1)
        } else {
            current.remove(productId)
        }
        _cart.value = current
    }

    fun removeFromCart(productId: String) {
        val current = _cart.value.toMutableMap()
        current.remove(productId)
        _cart.value = current
    }

    fun clearCart() {
        _cart.value = emptyMap()
        _appliedCoupon.value = null
        _couponDiscountRate.value = 0.0
    }

    fun toggleFavorite(productId: String) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _favorites.value = current
    }

    fun applyCoupon(code: String): Boolean {
        val normalized = code.trim().uppercase(Locale.ROOT)
        val rate = ShopData.validCoupons[normalized]
        return if (rate != null) {
            _appliedCoupon.value = normalized
            _couponDiscountRate.value = rate
            true
        } else {
            false
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponDiscountRate.value = 0.0
    }

    fun openCheckout() {
        if (_cart.value.isNotEmpty()) {
            _showCheckoutDialog.value = true
        }
    }

    fun closeCheckout() {
        _showCheckoutDialog.value = false
    }

    fun placeOrder(
        customerName: String,
        phone: String,
        address: String,
        paymentMethod: PaymentMethod
    ): Order {
        val itemsList = _cart.value.values.toList()
        val subtotal = itemsList.sumOf { it.totalPrice }
        val deliveryFee = if (subtotal > 1000.0) 0.0 else 99.0
        val discount = subtotal * _couponDiscountRate.value
        val total = (subtotal - discount + deliveryFee).coerceAtLeast(0.0)

        val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateStr = formatter.format(Date())
        val orderId = "ORD-" + UUID.randomUUID().toString().take(6).uppercase(Locale.ROOT)

        val newOrder = Order(
            id = orderId,
            items = itemsList,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discount,
            total = total,
            customerName = customerName,
            phone = phone,
            deliveryAddress = address,
            paymentMethod = paymentMethod,
            dateFormatted = dateStr,
            status = "Order Confirmed"
        )

        _orders.value = listOf(newOrder) + _orders.value
        _lastPlacedOrder.value = newOrder
        _showCheckoutDialog.value = false
        clearCart()
        return newOrder
    }

    fun dismissLastPlacedOrder() {
        _lastPlacedOrder.value = null
    }
}
