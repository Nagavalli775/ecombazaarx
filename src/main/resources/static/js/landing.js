 /* --- PRODUCT SOURCE (seller sync + fallback demo products) --- */
        // const PROD_KEY = "ecobasaarx_products_v2";

        /* Your 12 demo products (fallback) */
        // const defaultProducts = [
        //     { id: 'p01', title: "Reusable Bamboo Toothbrush", img: "/assets/products/bamboo_toothbrush.jpg", price: 129, ecoScore: 92, co2: 0.12, delta: "35% less CO₂", seller: "GreenStore" },
        //     { id: 'p02', title: "Stainless Steel Bottle 500ml", img: "/assets/products/stainless_bottle.jpg", price: 499, ecoScore: 95, co2: 0.45, delta: "60% less CO₂", seller: "PureSip" },
        //     { id: 'p03', title: "Zero-Waste Shampoo Bar", img: "/assets/products/shampoo_bar.jpg", price: 219, ecoScore: 90, co2: 0.25, delta: "40% less CO₂", seller: "CleanBar" },
        //     { id: 'p04', title: "Compostable Garbage Bags", img: "/assets/products/compost_bag.jpg", price: 179, ecoScore: 71, co2: 1.20, delta: "20% less CO₂", seller: "EcoWrap" },
        //     { id: 'p05', title: "Bamboo Cutlery Set", img: "/assets/products/bamboo_cutlery.jpg", price: 149, ecoScore: 94, co2: 0.18, delta: "50% less CO₂", seller: "GreenWare" },
        //     { id: 'p06', title: "Biodegradable Drinking Straws", img: "/assets/products/biodegradable_straws.jpg", price: 89, ecoScore: 87, co2: 0.05, delta: "70% less CO₂", seller: "StrawCo" },
        //     { id: 'p07', title: "Eco-friendly Detergent", img: "/assets/products/eco_detergent.jpg", price: 249, ecoScore: 82, co2: 0.90, delta: "28% less CO₂", seller: "CleanChem" },
        //     { id: 'p08', title: "Eco Dish Washing Brush", img: "/assets/products/eco_dish_brush.jpg", price: 199, ecoScore: 93, co2: 0.22, delta: "55% less CO₂", seller: "GreenWare" },
        //     { id: 'p09', title: "Metal Straw Set (Pack of 4)", img: "/assets/products/metal_straw_set.jpg", price: 159, ecoScore: 98, co2: 0.03, delta: "85% less CO₂", seller: "PureSip" },
        //     { id: 'p10', title: "Organic Coffee Beans", img: "/assets/products/organic_coffee.jpg", price: 349, ecoScore: 78, co2: 2.80, delta: "18% less CO₂", seller: "BeanKind" },
        //     { id: 'p11', title: "Recycled Paper Towels", img: "/assets/products/recycled_papertowel.jpg", price: 129, ecoScore: 88, co2: 0.40, delta: "32% less CO₂", seller: "PaperAgain" },
        //     { id: 'p12', title: "Reusable Grocery Tote Bag", img: "/assets/products/reusable_grocery_bag.jpg", price: 99, ecoScore: 96, co2: 0.06, delta: "65% less CO₂", seller: "BagIt" }
        // ];

       const API_BASE = "http://localhost:9090/api";
let products = [];

async function loadProductsFromBackend() {
    try {
        const res = await fetch(`${API_BASE}/products`);
        if (!res.ok) throw new Error("Failed to fetch products");

        const data = await res.json();

        products = data.map(p => ({
            id: String(p.id),
            title: p.name,
            img: p.imageUrl,
            price: p.price,
            ecoScore: p.ecoVerified ? 90 : 60,
            ecoCertified: p.ecoVerified,
            co2: p.carbonImpactKg,
            delta: "",
            seller: p.sellerName,
            description: p.description || ""
        }));

        renderCatalog();

    } catch (e) {
        console.error("Backend load failed.", e);
        products = [];
        renderCatalog();
    }
}

        /* Load seller products if present */
        // let products = (function () {
        //     try {
        //         const raw = localStorage.getItem(PROD_KEY);
        //         if (!raw) return defaultProducts;
        //         const data = JSON.parse(raw);

        //         if (!Array.isArray(data) || data.length === 0) return defaultProducts;

        //         return data;
        //     } catch (e) {
        //         console.error("Error reading seller products:", e);
        //         return defaultProducts;
        //     }
        // })();

        /* cart in memory */
        let cart = [];

        /* shortcut DOM nodes (cart badge & offcanvas) */
        const cartBadge = document.getElementById('cartBadge');
        const offcanvas = document.getElementById('offcanvasCart');
        const offcanvasItems = document.getElementById('offcanvasItems');
        const modalRoot = document.getElementById('modalRoot');

        function esc(s) { return String(s || '').replace(/[&<>"']/g, m => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[m])); }

        /* render catalog */
        function renderCatalog() {
            const grid = document.getElementById('ecoGrid');
            const q = (document.getElementById('searchBox').value || '').toLowerCase();
            const ecoMin = Number(document.getElementById('ecoMin').value || 0);
            const ecoCert = document.getElementById('ecoCertified').checked;
            const sortBy = document.getElementById('sortBy').value || '';

            let list = products.filter(p => {
                if (q && !(p.title + ' ' + p.seller).toLowerCase().includes(q)) return false;
                if (p.ecoScore < ecoMin) return false;
                if (ecoCert && !p.ecoCertified) return false;
                return true;
            });

            if (sortBy === 'price_asc') list.sort((a, b) => a.price - b.price);
            if (sortBy === 'price_desc') list.sort((a, b) => b.price - a.price);
            if (sortBy === 'carbon_asc') list.sort((a, b) => a.co2 - b.co2);
            if (sortBy === 'carbon_desc') list.sort((a, b) => b.co2 - a.co2);

            grid.innerHTML = list.map(p => `
            <article class="eco-card">
            <div class="eco-img"><img src="${esc(p.img)}" alt="${esc(p.title)}"></div>
            <div class="eco-body">
                <div style="display:flex;justify-content:space-between;align-items:flex-start;">
                <div style="max-width:70%"><div class="eco-title">${esc(p.title)}</div><div class="small-note" style="margin-top:6px">Seller: ${esc(p.seller)}</div></div>
                <div style="text-align:right"><div class="eco-price">₹${p.price}</div><div class="eco-score-badge" style="margin-top:8px">${p.ecoScore}</div></div>
                </div>

                <div style="margin-top:10px">
                <div class="eco-co2">${p.co2.toFixed(3)} kg CO₂</div>
                <div class="eco-delta small-note">${esc(p.delta)}</div>
                </div>

                <div class="card-actions">
                <button 
                    class="btn primary"
                    id="btn-${p.id}"
                    onclick="handleAddToCart('${p.id}')">
                    Add to cart
                </button>
                <button class="btn-ghost" onclick="showDetails('${p.id}')">Details</button>
                </div>
            </div>
            </article>
        `).join('');
        }

        /* Live sync: if seller updates products, refresh landing page */
        // window.addEventListener("storage", (e) => {
        //     if (e.key === PROD_KEY) {
        //         try {
        //             products = JSON.parse(e.newValue) || defaultProducts;
        //         } catch {
        //             products = defaultProducts;
        //         }
        //         renderEcoCards();
        //     }
        // });

        /* cart rendering updates badge + offcanvas content */
        function renderCartUI() {
            // badge count = total items
            const count = cart.reduce((s, i) => s + i.qty, 0);
            cartBadge.textContent = count;
            cartBadge.style.display = count ? 'inline-block' : 'none';

            // offcanvas items
            if (!cart.length) {
                offcanvasItems.innerHTML = '<div class="small-note">No items yet</div>';
            } else {
                offcanvasItems.innerHTML = cart.map(it => `
                    <div class="cart-item-row">
                    <div class="cart-item-left">
                        <div class="cart-item-title">${esc(it.title)}</div>
                        <div class="cart-item-meta">₹${it.price} × ${it.qty}</div>
                    </div>

                    <div class="cart-item-right">
                        <div class="cart-item-co2">${(it.co2 * it.qty).toFixed(3)} kg</div>
                        <div class="cart-item-actions">
                        <button class="qty-btn" onclick="changeQty('${it.id}',-1)">−</button>
                        <button class="qty-btn" onclick="changeQty('${it.id}',1)">+</button>
                        <button class="remove-btn" onclick="removeItem('${it.id}')">🗑</button>
                        </div>
                    </div>
                    </div>
                `).join('');
            }

            // totals
            const subtotal = cart.reduce((s, i) => s + i.price * i.qty, 0);
            const totalCO2 = cart.reduce((s, i) => s + i.co2 * i.qty, 0);
            document.getElementById('subtotal').textContent = subtotal.toFixed(2);
            document.getElementById('totalCO2').textContent = totalCO2.toFixed(3);

            // rating
            const items = cart.reduce((s, i) => s + i.qty, 0);
            const ratingEl = document.getElementById('orderRating');
            if (!items) { ratingEl.innerHTML = ''; return; }
            const avg = totalCO2 / items;
            if (avg <= 0.5) ratingEl.innerHTML = '<span class="eco-score-badge" style="background:#eaf9ed;color:#0a7a2b">Eco Hero</span>';
            else if (avg <= 2.0) ratingEl.innerHTML = '<span class="eco-score-badge" style="background:#fff8e6;color:#b36b00">Moderate</span>';
            else ratingEl.innerHTML = '<span class="eco-score-badge" style="background:#fff0f0;color:#b00000">High</span>';
        }

        /* cart functions */
        function addToCart(id) {
            const p = products.find(x => x.id === id);
            if (!p) return;
            const inCart = cart.find(x => x.id === id);
            if (inCart) inCart.qty++;
            else cart.push({ id: p.id, title: p.title, price: p.price, co2: p.co2, qty: 1 });
            renderCartUI();
        }
        function handleAddToCart(id) {
            addToCart(id); // existing logic stays

            const btn = document.getElementById(`btn-${id}`);
            if (!btn) return;

            btn.textContent = "Added to cart";
            btn.disabled = true;
            btn.classList.remove("primary");
            btn.classList.add("btn-added");
        }

        function changeQty(id, delta) {
            const it = cart.find(x => x.id === id);
            if (!it) return;
            it.qty += delta;
            if (it.qty <= 0) cart = cart.filter(x => x.id !== id);
            renderCartUI();
        }
        function removeItem(id) { cart = cart.filter(x => x.id !== id); renderCartUI(); }

        /* modal details */
        function showDetails(id) {
            const p = products.find(x => x.id === id);
            modalRoot.innerHTML = `<div class="modal-backdrop"><div class="modal">
    <h3>${esc(p.title)}</h3>
    <p><strong>Price:</strong> ₹${p.price}</p>
    <p><strong>CO₂e:</strong> ${p.co2.toFixed(3)} kg</p>
    <p><strong>Seller:</strong> ${esc(p.seller)}</p>
    <p>${esc(p.description)}</p>
    <div style="display:flex;gap:8px;margin-top:12px">
      <button class="btn primary" onclick="addToCart('${p.id}'); closeModal();">Add to cart</button>
      <button class="btn-ghost" onclick="closeModal()">Close</button>
    </div>
  </div></div>`;
        }
        function closeModal() { modalRoot.innerHTML = ''; }

        /* offcanvas toggle */
        document.getElementById('cartToggleBtn').addEventListener('click', () => {
            offcanvas.classList.add('show'); offcanvas.setAttribute('aria-hidden', 'false'); renderCartUI();
        });
        document.getElementById('offcanvasClose').addEventListener('click', () => {
            offcanvas.classList.remove('show'); offcanvas.setAttribute('aria-hidden', 'true');
        });

        /* checkout & clear */
        document.getElementById('checkoutBtn').addEventListener('click', () => {
            if (!cart.length) return alert('Cart is empty');
            alert(`Checkout demo\nTotal CO₂e: ${document.getElementById('totalCO2').textContent} kg`);
            cart = []; renderCartUI(); offcanvas.classList.remove('show');
        });
        document.getElementById('clearCart').addEventListener('click', () => {
            cart = [];
            renderCartUI();
            offcanvas.classList.remove('show');
            offcanvas.setAttribute('aria-hidden', 'true');
        });

        /* filter wiring */
        ['searchBox', 'ecoMin', 'ecoCertified', 'sortBy'].forEach(id => {
            const el = document.getElementById(id);
            if (!el) return;
            el.addEventListener('input', renderCatalog);
            el.addEventListener('change', renderCatalog);
        });

        /* init */
        // renderCatalog();
        // renderCartUI();
        loadProductsFromBackend();
        renderCartUI();


  
        (function () {
            const items = document.querySelectorAll('.about-item');
            if (!items.length) return;

            // stagger effect (optional)
            items.forEach((it, idx) => it.style.transitionDelay = (idx * 120) + 'ms');

            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        // Reveal the item but DO NOT hide earlier revealed ones
                        entry.target.classList.add('in-view');
                        observer.unobserve(entry.target); // reveal only once
                    }
                });
            }, {
                threshold: 0.25
            });

            items.forEach(it => observer.observe(it));
        })();