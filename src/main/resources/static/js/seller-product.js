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
   LOAD PRODUCTS
====================================================== */
async function loadProducts() {
    try {
        const res = await fetch(`${API_BASE}/products`, {
            headers: authHeaders()
        });

        if (!res.ok) throw new Error("HTTP " + res.status);

        const products = await res.json();

        renderProducts(products);
        renderQuickList(products);
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

    console.log(products);


    products.forEach(p => {
    grid.innerHTML += `
        <article class="product-card">

            <div class="product-media" 
                 style="height:160px; overflow:hidden; border-radius:8px; background:#f8f8f8; display:flex; align-items:center; justify-content:center;">

                <img src="${p.imageUrl}" 
                     style="max-width:100%; max-height:100%; object-fit:cover;">

            </div>

            <div class="product-body">
                <div class="product-title">${p.name}</div>

                <div class="product-meta">
                    <span>₹${p.price}</span>
                    <span class="badge">
                        ${p.ecoVerified ? "Eco Verified" : "Standard"}
                    </span>
                </div>

                <div class="muted">Stock: ${p.stockQuantity}</div>
                <div class="muted">CO₂: ${p.carbonImpactKg} kg</div>
            </div>

        </article>
    `;
});

}

/* ======================================================
   QUICK LIST
====================================================== */
function renderQuickList(products) {
    const quickList = document.getElementById("quickList");
    quickList.innerHTML = "";

    products.forEach((p, i) => {
        quickList.innerHTML += `
            <tr>
                <td>${i + 1}</td>
                <td>${p.name}</td>
                <td>${p.stockQuantity}</td>
            </tr>
        `;
    });
}

/* ======================================================
   CREATE PRODUCT 
====================================================== */
async function createProduct() {

    const token = localStorage.getItem("token");

    const name = document.getElementById("inp_title").value;
    const price = Number(document.getElementById("inp_price").value);
    const qty = Number(document.getElementById("inp_qty").value);
    const co2 = Number(document.getElementById("inp_co2").value);
    const ecoScore = Number(document.getElementById("inp_eco").value);
    const file = document.getElementById("inp_imgfile").files[0];

    if (!name || !price || !qty || !co2) {
        alert("Please fill all required fields");
        return;
    }

    if (!file) {
        alert("Please upload an image");
        return;
    }

    try {

        /* ---- Upload Image ---- */
        const formData = new FormData();
        formData.append("file", file);

        const uploadRes = await fetch(`${API_BASE}/upload`, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token
            },
            body: formData
        });

        if (!uploadRes.ok) throw new Error("Image upload failed");

        const uploadData = await uploadRes.json();

        /* ---- Create Product ---- */
        const product = {
            name: name,
            description: "",
            price: price,
            stockQuantity: qty,
            carbonImpactKg: co2,
            ecoVerified: ecoScore >= 70,
            category: "GENERAL",
            imageUrl: uploadData.imageUrl
        };

        const res = await fetch(`${API_BASE}/seller/products`, {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify(product)
        });

        if (!res.ok) throw new Error("Product creation failed");

        alert("Product added successfully ✔");

        clearForm();
        openSection("products");
        loadProducts();

    } catch (e) {
        console.error(e);
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
   DASHBOARD COUNTERS
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

    Object.values(panels).forEach(p => {
        if (p) p.style.display = "none";
    });

    document.querySelectorAll(".nav-link").forEach(link => {
        link.classList.toggle("active", link.dataset.section === section);
    });

    if (section === "dashboard") {
        panels.dashboard.style.display = "flex";
        loadProducts();
    } else if (panels[section]) {
        panels[section].style.display = "block";
    }

    document.getElementById("pageTitle").innerText =
        section.charAt(0).toUpperCase() + section.slice(1);
}

/* ======================================================
   CLEAR FORM
====================================================== */
function clearForm() {
    document.getElementById("inp_title").value = "";
    document.getElementById("inp_price").value = "";
    document.getElementById("inp_qty").value = "";
    document.getElementById("inp_co2").value = "";
    document.getElementById("inp_eco").value = "";
    document.getElementById("inp_imgfile").value = "";

    const previewImg = document.getElementById("previewImg");
    const previewPlaceholder = document.getElementById("previewPlaceholder");

    previewImg.src = "";
    previewImg.style.display = "none";
    previewPlaceholder.style.display = "block";
}

/* ======================================================
   LOGOUT
====================================================== */
window.logout = function () {
    localStorage.clear();
    window.location.href = "/landing";
};

/* ======================================================
   EVENT BINDINGS
====================================================== */
document.addEventListener("DOMContentLoaded", () => {

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
    document.getElementById("btnViewProducts").onclick = () => {
        openSection("products");
        loadProducts();
    };

    /* ================= IMAGE PREVIEW ================= */

const previewImg = document.getElementById("previewImg");
const previewPlaceholder = document.getElementById("previewPlaceholder");
const imgInput = document.getElementById("inp_imgfile");

imgInput.addEventListener("change", function () {

    const file = this.files[0];

    if (!file) {
        previewImg.style.display = "none";
        previewPlaceholder.style.display = "block";
        previewImg.src = "";
        return;
    }

    if (!file.type.startsWith("image/")) {
        alert("Please select a valid image file");
        this.value = "";
        return;
    }

    const reader = new FileReader();

    reader.onload = function (e) {
        previewImg.src = e.target.result;
        previewImg.style.display = "block";
        previewPlaceholder.style.display = "none";
    };

    reader.readAsDataURL(file);
});


    openSection("dashboard");
    loadProducts();
});
