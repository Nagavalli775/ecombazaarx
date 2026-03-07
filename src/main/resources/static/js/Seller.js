(function () {
            const PROD_KEY = 'ecobasaarx_products_v2';

            /* ---------- helpers ---------- */
            const readJSON = (k, fallback) => {
                try {
                    const r = localStorage.getItem(k);
                    return r ? JSON.parse(r) : fallback;
                } catch {
                    return fallback;
                }
            };

            const writeJSON = (k, v) => {
                try {
                    localStorage.setItem(k, JSON.stringify(v));
                } catch (e) {
                    console.error(e);
                }
            };

            const esc = s =>
                String(s || '').replace(/[&<>"']/g, m =>
                    ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[m])
                );

            const placeholderDataUrl = () =>
                'data:image/svg+xml;utf8,' +
                encodeURIComponent(
                    '<svg xmlns="http://www.w3.org/2000/svg" width="600" height="400"><rect width="100%" height="100%" fill="#eef7f0"/><text x="50%" y="50%" fill="#7fae9a" text-anchor="middle" dy=".35em">No image</text></svg>'
                );

            /* ---------- products ---------- */
            let products = (function () {
                const stored = readJSON(PROD_KEY, null);
                if (Array.isArray(stored) && stored.length) return stored;

                return [
                    { id: 'p01', title: 'Reusable Bamboo Toothbrush', img: 'assets/products/bamboo_toothbrush.jpg', price: 129, ecoScore: 92, co2: 0.12, seller: 'GreenStore', qty: 10, available: true },
                    { id: 'p02', title: 'Stainless Steel Bottle 500ml', img: 'assets/products/stainless_bottle.jpg', price: 499, ecoScore: 95, co2: 0.45, seller: 'PureSip', qty: 8, available: true },
                    { id: 'p03', title: 'Zero-Waste Shampoo Bar', img: 'assets/products/shampoo_bar.jpg', price: 219, ecoScore: 90, co2: 0.25, seller: 'CleanBar', qty: 6, available: true },
                    { id: 'p04', title: 'Compostable Garbage Bags', img: 'assets/products/compost_bag.jpg', price: 179, ecoScore: 71, co2: 1.2, seller: 'EcoWrap', qty: 50, available: true }
                ];
            })();

            /* ---------- DOM ---------- */
            const totalProductsEl = document.getElementById('totalProducts');
            const totalStockEl = document.getElementById('totalStock');
            const productGrid = document.getElementById('productGrid');
            const quickList = document.getElementById('quickList');
            const ordersTableBody = document.querySelector('#ordersTable tbody');

            const inp_title = document.getElementById('inp_title');
            const inp_price = document.getElementById('inp_price');
            const inp_qty = document.getElementById('inp_qty');
            const inp_imgurl = document.getElementById('inp_imgurl');
            const inp_imgfile = document.getElementById('inp_imgfile');
            const inp_eco = document.getElementById('inp_eco');
            const inp_co2 = document.getElementById('inp_co2');
            const btnAdd = document.getElementById('btnAdd');
            const btnClear = document.getElementById('btnClear');

            const panels = {
                dashboard: document.querySelector('.summary'),
                add: document.getElementById('panelAdd'),
                products: document.getElementById('panelProducts'),
                inventory: document.getElementById('panelInventory'),
                orders: document.getElementById('panelOrders'),
                profile: document.getElementById('panelProfile'),
                settings: document.getElementById('panelSettings')
            };

            /* ---------- navigation ---------- */
            document.querySelectorAll('.nav-link').forEach(a => {
                a.addEventListener('click', e => {
                    e.preventDefault();
                    openSection(a.dataset.section);
                });
            });

            document.getElementById('btnViewProducts').addEventListener('click', () => openSection('products'));
            document.getElementById('btnOpenAdd').addEventListener('click', () => openSection('add'));

            function openSection(section) {
                document.querySelectorAll('.nav-link').forEach(x =>
                    x.classList.toggle('active', x.dataset.section === section)
                );

                Object.values(panels).forEach(el => el && (el.style.display = 'none'));

                if (section === 'dashboard') {
                    panels.dashboard.style.display = 'flex';
                    document.getElementById('pageTitle').textContent = 'Seller Dashboard';
                    updateCounters();
                    return;
                }

                panels.dashboard.style.display = 'none';
                document.getElementById('pageTitle').textContent =
                    section.charAt(0).toUpperCase() + section.slice(1);

                if (panels[section]) panels[section].style.display = 'block';
                if (section === 'products') renderProducts();
                if (section === 'orders') renderOrders();
            }

            /* ---------- counters ---------- */
            function updateCounters() {
                totalProductsEl.textContent = products.length;
                totalStockEl.textContent = products.reduce((s, p) => s + (Number(p.qty) || 0), 0);
            }

            // Milestone 4 (demo insights – derived from products)
            function updateCarbonInsights() {
                const totalCarbonSaved = products.reduce((s, p) => s + (p.co2 || 0), 0);
                console.log("Total carbon (demo):", totalCarbonSaved);
            }

            /* ---------- render ---------- */
            function renderProducts() {
                productGrid.innerHTML = '';
                products.forEach(p => {
                    const el = document.createElement('article');
                    el.className = 'product-card';
                    el.innerHTML = `
                <div class="product-media">
                    <img src="${esc(p.img || placeholderDataUrl())}">
                </div>
                <div class="product-body">
                    <div class="product-title">${esc(p.title)}</div>
                    <div class="product-meta">
                        <span>${esc(p.seller)}</span>
                        <span class="badge">${p.ecoScore}</span>
                    </div>
                    <div>
                        <strong>₹${p.price}</strong><br>
                        <span class="muted">Stock: ${p.qty}</span>
                    </div>
                </div>
            `;
                    productGrid.appendChild(el);
                });
                renderQuick();
                updateCounters();
            }

            function renderQuick() {
                quickList.innerHTML = '';
                products.forEach((p, i) => {
                    quickList.innerHTML += `<tr><td>${i + 1}</td><td>${p.title}</td><td>${p.qty}</td></tr>`;
                });
            }

            function renderOrders() {
                ordersTableBody.innerHTML = `
            <tr><td>ORD001</td><td>Alice</td><td>2</td><td>Delivered</td></tr>
            <tr><td>ORD002</td><td>Bob</td><td>1</td><td>Pending</td></tr>
        `;
            }

            async function loadSellerProfile() {
                try {
                    const token = localStorage.getItem("token");

                    const response = await fetch("/api/seller/profile", {
                        headers: {
                            "Authorization": "Bearer " + token
                        }
                    });

                    if (!response.ok) {
                        throw new Error("Unauthorized");
                    }

                    const seller = await response.json();

                    document.getElementById("sellerName").textContent = seller.username;
                    document.getElementById("sellerEmail").textContent = seller.shopName;
                    document.getElementById("sellerPhone").textContent = seller.shopAddress;
                    document.getElementById("sellerCompany").textContent = seller.businessType;

                } catch (error) {
                    console.error("Error loading seller profile", error);
                }
            }


            window.toggleProfileSidebar = function () {
                const sidebar = document.getElementById("profileSidebar");
                const opened = sidebar.classList.toggle("active");

                if (opened) {
                    loadSellerProfile();
                }
            };

            window.logout = function () {
                localStorage.clear();
                window.location.href = "/landing";
            };

    /* ---------- add product ---------- */
    btnAdd.addEventListener('click', async e => {
    e.preventDefault();

    if (!inp_title.value) 
        return alert('Enter product name');

    if (!inp_imgfile.files[0]) 
        return alert('Please upload a product image');

    // Convert image file to Base64
    const img = await new Promise(res => {
        const reader = new FileReader();
        reader.onload = e => res(e.target.result);
        reader.readAsDataURL(inp_imgfile.files[0]);
    });

    products.unshift({
        id: Date.now().toString(),
        title: inp_title.value,
        price: Number(inp_price.value),
        qty: Number(inp_qty.value),
        ecoScore: Number(inp_eco.value),
        co2: Number(inp_co2.value),
        img: img,
        seller: 'You',
        available: true
    });

    writeJSON(PROD_KEY, products);
    openSection('products');
});

/* ---------- image preview ---------- */

const previewImg = document.getElementById('previewImg');
const previewPlaceholder = document.getElementById('previewPlaceholder');

inp_imgfile.addEventListener('change', () => {

    const file = inp_imgfile.files[0];

    if (!file) {
        // If no file selected
        previewImg.style.display = "none";
        previewPlaceholder.style.display = "block";
        previewImg.src = "";
        return;
    }

    // Only allow image files
    if (!file.type.startsWith("image/")) {
        alert("Please select a valid image file");
        inp_imgfile.value = "";
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


btnClear.addEventListener('click', () => {
    inp_title.value = '';
    inp_price.value = '';
    inp_qty.value = '';
    inp_eco.value = '';
    inp_co2.value = '';
    inp_imgfile.value = '';   

    previewImg.src = "";
    previewImg.style.display = "none";
    previewPlaceholder.style.display = "block";

});
})();
