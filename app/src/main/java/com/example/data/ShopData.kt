package com.example.data

import com.example.model.Product

object ShopData {
    val categories = listOf(
        "All",
        "Groceries",
        "Electronics",
        "Fashion",
        "Home & Kitchen",
        "Beverages",
        "Personal Care"
    )

    val sampleProducts = listOf(
        Product(
            id = "p1",
            name = "Fresh Organic Bananas",
            description = "Naturally ripened sweet and potassium-rich organic bananas sourced from local farms.",
            price = 120.0,
            originalPrice = 150.0,
            category = "Groceries",
            rating = 4.8f,
            reviewCount = 340,
            imageUrl = "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=500&auto=format&fit=crop&q=80",
            unit = "1 Dozen",
            badge = "20% OFF",
            isFeatured = true
        ),
        Product(
            id = "p2",
            name = "Wireless Bluetooth Earbuds",
            description = "Noise cancelling wireless earbuds with deep bass, fast charging, and up to 36 hours playtime.",
            price = 2499.0,
            originalPrice = 3499.0,
            category = "Electronics",
            rating = 4.6f,
            reviewCount = 520,
            imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=500&auto=format&fit=crop&q=80",
            unit = "1 Box",
            badge = "Best Seller",
            isFeatured = true
        ),
        Product(
            id = "p3",
            name = "Pure Basmati Rice (Premium)",
            description = "Long grain aromatic basmati rice, naturally aged for exceptional taste and fragrance.",
            price = 450.0,
            originalPrice = 520.0,
            category = "Groceries",
            rating = 4.9f,
            reviewCount = 210,
            imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=500&auto=format&fit=crop&q=80",
            unit = "2 Kg Bag",
            badge = "Top Rated"
        ),
        Product(
            id = "p4",
            name = "Classic Cotton Crewneck T-Shirt",
            description = "100% breathable combed organic cotton everyday casual t-shirt in navy blue.",
            price = 899.0,
            originalPrice = 1199.0,
            category = "Fashion",
            rating = 4.5f,
            reviewCount = 180,
            imageUrl = "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=500&auto=format&fit=crop&q=80",
            unit = "Size M / L",
            badge = "Popular"
        ),
        Product(
            id = "p5",
            name = "Stainless Steel Thermal Bottle",
            description = "Double-wall vacuum insulated water bottle keeping beverages cold for 24h or hot for 12h.",
            price = 799.0,
            originalPrice = 999.0,
            category = "Home & Kitchen",
            rating = 4.7f,
            reviewCount = 95,
            imageUrl = "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=500&auto=format&fit=crop&q=80",
            unit = "750 ml",
            badge = null
        ),
        Product(
            id = "p6",
            name = "Artisan Dark Roast Coffee Beans",
            description = "Rich, aromatic Arabica coffee beans freshly roasted with notes of dark chocolate and caramel.",
            price = 950.0,
            originalPrice = 1200.0,
            category = "Beverages",
            rating = 4.9f,
            reviewCount = 410,
            imageUrl = "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=500&auto=format&fit=crop&q=80",
            unit = "500g Pack",
            badge = "Staff Pick",
            isFeatured = true
        ),
        Product(
            id = "p7",
            name = "Natural Aloe Vera Gentle Face Wash",
            description = "Hydrating facial cleanser enriched with organic aloe vera, tea tree extract, and vitamin E.",
            price = 390.0,
            originalPrice = 450.0,
            category = "Personal Care",
            rating = 4.6f,
            reviewCount = 145,
            imageUrl = "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500&auto=format&fit=crop&q=80",
            unit = "150 ml Tube",
            badge = null
        ),
        Product(
            id = "p8",
            name = "Smart Fitness Band with SpO2",
            description = "Track steps, sleep, heart rate, workouts, and receive smartphone notifications on a vibrant AMOLED screen.",
            price = 3200.0,
            originalPrice = 4200.0,
            category = "Electronics",
            rating = 4.7f,
            reviewCount = 680,
            imageUrl = "https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=500&auto=format&fit=crop&q=80",
            unit = "1 Unit",
            badge = "24% OFF",
            isFeatured = true
        ),
        Product(
            id = "p9",
            name = "Extra Virgin Cold Pressed Olive Oil",
            description = "First cold press olive oil with low acidity, ideal for cooking, dressing, and culinary dishes.",
            price = 1450.0,
            originalPrice = 1700.0,
            category = "Groceries",
            rating = 4.8f,
            reviewCount = 160,
            imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&auto=format&fit=crop&q=80",
            unit = "1 Liter Bottle",
            badge = "Pure Quality"
        ),
        Product(
            id = "p10",
            name = "Ceramic Pour-Over Coffee Mug Set",
            description = "Handcrafted matte ceramic coffee mugs, microwave and dishwasher safe, set of 2.",
            price = 680.0,
            originalPrice = 850.0,
            category = "Home & Kitchen",
            rating = 4.4f,
            reviewCount = 88,
            imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=500&auto=format&fit=crop&q=80",
            unit = "Set of 2",
            badge = null
        ),
        Product(
            id = "p11",
            name = "Organic Green Tea with Jasmine",
            description = "Antioxidant-rich whole leaf green tea scented with fragrant jasmine blossoms.",
            price = 320.0,
            originalPrice = 380.0,
            category = "Beverages",
            rating = 4.8f,
            reviewCount = 195,
            imageUrl = "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=500&auto=format&fit=crop&q=80",
            unit = "25 Tea Bags",
            badge = "Healthy"
        ),
        Product(
            id = "p12",
            name = "Casual Canvas Slip-On Sneakers",
            description = "Lightweight comfortable walking sneakers with cushioned memory foam insole.",
            price = 1850.0,
            originalPrice = 2400.0,
            category = "Fashion",
            rating = 4.6f,
            reviewCount = 310,
            imageUrl = "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=500&auto=format&fit=crop&q=80",
            unit = "Pair",
            badge = "Sale"
        )
    )

    val validCoupons = mapOf(
        "DUKAAN10" to 0.10,
        "WELCOME20" to 0.20,
        "SAVE50" to 0.05
    )
}
