package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ShopData
import com.example.model.ScreenTab
import com.example.ui.components.CheckoutDialog
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.components.ProductDetailSheet
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ShoppingViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DukaanApp()
            }
        }
    }
}

@Composable
fun DukaanApp(shoppingViewModel: ShoppingViewModel = viewModel()) {
    val currentTab by shoppingViewModel.currentTab.collectAsState()
    val products by shoppingViewModel.products.collectAsState()
    val filteredProducts by shoppingViewModel.filteredProducts.collectAsState()
    val selectedCategory by shoppingViewModel.selectedCategory.collectAsState()
    val searchQuery by shoppingViewModel.searchQuery.collectAsState()
    val cart by shoppingViewModel.cart.collectAsState()
    val favorites by shoppingViewModel.favorites.collectAsState()
    val appliedCoupon by shoppingViewModel.appliedCoupon.collectAsState()
    val couponDiscountRate by shoppingViewModel.couponDiscountRate.collectAsState()
    val selectedProduct by shoppingViewModel.selectedProduct.collectAsState()
    val showCheckoutDialog by shoppingViewModel.showCheckoutDialog.collectAsState()
    val lastPlacedOrder by shoppingViewModel.lastPlacedOrder.collectAsState()
    val orders by shoppingViewModel.orders.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Handle back button to return to Shop if currently on another tab
    BackHandler(enabled = currentTab != ScreenTab.SHOP) {
        shoppingViewModel.switchTab(ScreenTab.SHOP)
    }

    // Totals calculation
    val cartList = remember(cart) { cart.values.toList() }
    val subtotal = remember(cartList) { cartList.sumOf { it.totalPrice } }
    val deliveryFee = remember(subtotal) { if (subtotal > 1000.0 || subtotal == 0.0) 0.0 else 99.0 }
    val discount = remember(subtotal, couponDiscountRate) { subtotal * couponDiscountRate }
    val total = remember(subtotal, deliveryFee, discount) { (subtotal - discount + deliveryFee).coerceAtLeast(0.0) }
    val totalCartCount = remember(cartList) { cartList.sumOf { it.quantity } }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                // Shop Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.SHOP,
                    onClick = { shoppingViewModel.switchTab(ScreenTab.SHOP) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.SHOP) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                            contentDescription = "Shop"
                        )
                    },
                    label = { Text("Shop", fontSize = 12.sp, fontWeight = if (currentTab == ScreenTab.SHOP) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_shop")
                )

                // Categories Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.CATEGORIES,
                    onClick = { shoppingViewModel.switchTab(ScreenTab.CATEGORIES) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.CATEGORIES) Icons.Filled.Category else Icons.Outlined.Category,
                            contentDescription = "Categories"
                        )
                    },
                    label = { Text("Categories", fontSize = 12.sp, fontWeight = if (currentTab == ScreenTab.CATEGORIES) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_categories")
                )

                // Cart Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.CART,
                    onClick = { shoppingViewModel.switchTab(ScreenTab.CART) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (totalCartCount > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ) {
                                        Text("$totalCartCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.CART) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                                contentDescription = "Cart"
                            )
                        }
                    },
                    label = { Text("Cart", fontSize = 12.sp, fontWeight = if (currentTab == ScreenTab.CART) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_cart")
                )

                // Orders Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.ORDERS,
                    onClick = { shoppingViewModel.switchTab(ScreenTab.ORDERS) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.ORDERS) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                            contentDescription = "Orders"
                        )
                    },
                    label = { Text("Orders", fontSize = 12.sp, fontWeight = if (currentTab == ScreenTab.ORDERS) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_orders")
                )
            }
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (currentTab) {
            ScreenTab.SHOP -> {
                CatalogScreen(
                    products = filteredProducts,
                    categories = ShopData.categories,
                    selectedCategory = selectedCategory,
                    searchQuery = searchQuery,
                    cartMap = cart,
                    favorites = favorites,
                    onSearchChange = shoppingViewModel::onSearchQueryChange,
                    onCategoryChange = shoppingViewModel::onCategorySelect,
                    onProductClick = shoppingViewModel::selectProduct,
                    onAddToCart = { product ->
                        shoppingViewModel.addToCart(product)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Added ${product.name} to cart")
                        }
                    },
                    onIncrement = { id ->
                        val item = cart[id]
                        if (item != null) shoppingViewModel.addToCart(item.product)
                    },
                    onDecrement = shoppingViewModel::decrementFromCart,
                    onToggleFavorite = shoppingViewModel::toggleFavorite,
                    onOpenCart = { shoppingViewModel.switchTab(ScreenTab.CART) },
                    modifier = screenModifier
                )
            }

            ScreenTab.CATEGORIES -> {
                CategoriesScreen(
                    categories = ShopData.categories,
                    products = products,
                    onCategorySelected = { category ->
                        shoppingViewModel.onCategorySelect(category)
                        shoppingViewModel.switchTab(ScreenTab.SHOP)
                    },
                    modifier = screenModifier
                )
            }

            ScreenTab.CART -> {
                CartScreen(
                    cartItems = cartList,
                    subtotal = subtotal,
                    deliveryFee = deliveryFee,
                    discount = discount,
                    total = total,
                    appliedCoupon = appliedCoupon,
                    onApplyCoupon = shoppingViewModel::applyCoupon,
                    onRemoveCoupon = shoppingViewModel::removeCoupon,
                    onIncrement = { id ->
                        val item = cart[id]
                        if (item != null) shoppingViewModel.addToCart(item.product)
                    },
                    onDecrement = shoppingViewModel::decrementFromCart,
                    onRemoveItem = shoppingViewModel::removeFromCart,
                    onClearCart = shoppingViewModel::clearCart,
                    onProceedCheckout = shoppingViewModel::openCheckout,
                    onStartShopping = { shoppingViewModel.switchTab(ScreenTab.SHOP) },
                    modifier = screenModifier
                )
            }

            ScreenTab.ORDERS -> {
                OrdersScreen(
                    orders = orders,
                    onStartShopping = { shoppingViewModel.switchTab(ScreenTab.SHOP) },
                    modifier = screenModifier
                )
            }
        }

        // Product Detail Bottom Sheet
        selectedProduct?.let { product ->
            val qty = cart[product.id]?.quantity ?: 0
            val isFav = favorites.contains(product.id)
            ProductDetailSheet(
                product = product,
                quantityInCart = qty,
                isFavorite = isFav,
                onDismiss = { shoppingViewModel.selectProduct(null) },
                onAddToCart = {
                    shoppingViewModel.addToCart(product)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Added ${product.name} to cart")
                    }
                },
                onIncrement = { shoppingViewModel.addToCart(product) },
                onDecrement = { shoppingViewModel.decrementFromCart(product.id) },
                onToggleFavorite = { shoppingViewModel.toggleFavorite(product.id) }
            )
        }

        // Checkout Dialog
        CheckoutDialog(
            isOpen = showCheckoutDialog,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discount,
            total = total,
            onDismiss = shoppingViewModel::closeCheckout,
            onConfirmOrder = { name, phone, address, payment ->
                shoppingViewModel.placeOrder(name, phone, address, payment)
            }
        )

        // Order Success Celebration Dialog
        OrderSuccessDialog(
            order = lastPlacedOrder,
            onDismiss = shoppingViewModel::dismissLastPlacedOrder,
            onViewOrders = {
                shoppingViewModel.dismissLastPlacedOrder()
                shoppingViewModel.switchTab(ScreenTab.ORDERS)
            }
        )
    }
}
