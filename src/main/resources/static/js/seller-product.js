/* ======================================================
   CONFIG
====================================================== */
const API_BASE = "http://localhost:9090/api";

/* ======================================================
   AUTH HEADER
====================================================== */
function authHeaders() {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };
}

/* ======================================================
   LOAD PRODUCTS (SELLER)
====================================================== */
async function loadProducts() {
    try {
        const res = await fetch(`${API_BASE}/products`, {
            headers: authHeaders()
        });

        if (!res.ok) {
            throw new Error("HTTP Error " + res.status);
        }

        const products = await res.json();
        renderProducts(products);
        updateCounters(products);

    } catch (e) {
        console.error("Failed to load products", e);
    }
}

/* ======================================================
   RENDER PRODUCTS
====================================================== */
function renderProducts(products) {
    const grid = document.getElementById("productGrid");
    grid.innerHTML = "";

    if (!products || products.length === 0) {
        grid.innerHTML = "<p class='muted'>No products found</p>";
        return;
    }

    products.forEach(p => {
        grid.innerHTML += `
        <article class="product-card">
            <div class="product-body">
                <div class="product-title">${p.name}</div>

                <div class="product-meta">
                    <span>₹${p.price}</span>
                    <span class="badge">${p.ecoVerified ? "Eco" : "Normal"}</span>
                </div>

                <div class="muted">Stock: ${p.stockQuantity}</div>
                <div class="muted">CO₂: ${p.carbonImpactKg} kg</div>

                <div class="product-actions">
                    <button class="small-btn edit-btn" onclick="editProduct(${p.id})">Edit</button>
                    <button class="small-btn del-btn" onclick="deleteProduct(${p.id})">Delete</button>
                </div>
            </div>
        </article>
        `;
    });
}

/* ======================================================
   CREATE PRODUCT
====================================================== */
async function createProduct() {
    const product = {
        name: document.getElementById("inp_title").value,
        price: Number(document.getElementById("inp_price").value),
        stockQuantity: Number(document.getElementById("inp_qty").value),
        carbonImpactKg: Number(document.getElementById("inp_co2").value),
        ecoVerified: true,
        category: "GENERAL"
    };

    try {
        const res = await fetch(`${API_BASE}/seller/products`, {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify(product)
        });

        if (!res.ok) {
            throw new Error("Failed to create product");
        }

        alert("Product added successfully ✔");
        clearForm();
        openSection("products");
        loadProducts();

    } catch (e) {
        console.error("Create product failed", e);
        alert("Failed to add product");
    }
}

/* ======================================================
   DELETE PRODUCT
====================================================== */
async function deleteProduct(id) {
    if (!confirm("Delete product?")) return;

    try {
        await fetch(`${API_BASE}/seller/products/${id}`, {
            method: "DELETE",
            headers: authHeaders()
        });

        loadProducts();

    } catch (e) {
        console.error("Delete failed", e);
    }
}

/* ======================================================
   UPDATE STOCK
====================================================== */
async function editProduct(id) {
    const qty = prompt("Enter new stock quantity");
    if (qty === null) return;

    try {
        await fetch(`${API_BASE}/seller/products/${id}`, {
            method: "PUT",
            headers: authHeaders(),
            body: JSON.stringify({
                stockQuantity: Number(qty)
            })
        });

        loadProducts();

    } catch (e) {
        console.error("Update failed", e);
    }
}

/* ======================================================
   COUNTERS
====================================================== */
function updateCounters(products) {
    document.getElementById("totalProducts").textContent = products.length;
    document.getElementById("totalStock").textContent =
        products.reduce((sum, p) => sum + (p.stockQuantity || 0), 0);
}

/* ======================================================
   UI SECTION HANDLING
====================================================== */
function openSection(section) {

    const panels = {
        dashboard: document.querySelector('.summary'),
        add: document.getElementById('panelAdd'),
        products: document.getElementById('panelProducts'),
        inventory: document.getElementById('panelInventory'),
        orders: document.getElementById('panelOrders'),
        profile: document.getElementById('panelProfile'),
        settings: document.getElementById('panelSettings')
    };

    // Hide everything first
    Object.values(panels).forEach(p => {
        if (p) p.style.display = "none";
    });

    // Sidebar active state
    document.querySelectorAll(".nav-link").forEach(link => {
        link.classList.toggle("active", link.dataset.section === section);
    });

    // Show selected section
    if (section === "dashboard") {
        panels.dashboard.style.display = "flex";
    } else if (panels[section]) {
        panels[section].style.display = "block";
    }

    document.getElementById("pageTitle").innerText =
        section.charAt(0).toUpperCase() + section.slice(1);
}

/* ======================================================
   FORM HELPERS
====================================================== */
function clearForm() {
    document.getElementById("inp_title").value = "";
    document.getElementById("inp_price").value = "";
    document.getElementById("inp_qty").value = "";
    document.getElementById("inp_co2").value = "";
}

/* ======================================================
   EVENT BINDINGS
====================================================== */
document.getElementById("btnAdd").addEventListener("click", e => {
    e.preventDefault();
    createProduct();
});

document.querySelectorAll(".nav-link").forEach(link => {
    link.addEventListener("click", e => {
        e.preventDefault();
        openSection(link.dataset.section);
    });
});

document.getElementById("btnOpenAdd").onclick = () => openSection("add");
document.getElementById("btnViewProducts").onclick = () => openSection("products");

/* ======================================================
   INIT
====================================================== */
document.addEventListener("DOMContentLoaded", () => {
    openSection("dashboard");
    loadProducts();
});
