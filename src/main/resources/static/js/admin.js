const API = "http://localhost:9090/api/admin";

        function authHeaders() {
            return {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            };
        }

        const contentArea = document.getElementById("contentArea");
        const topbarTitle = document.getElementById("topbarTitle");

        const overviewBtn = document.getElementById("overviewBtn");
        const verifySellerBtn = document.getElementById("verifySellerBtn");
        const usersBtn = document.getElementById("usersBtn");

        function setActive(btn) {
            [overviewBtn, verifySellerBtn, usersBtn].forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
        }

        async function loadOverview() {
            setActive(overviewBtn);
            topbarTitle.innerText = "Admin Dashboard";

            contentArea.innerHTML = `
                <h3>Welcome Admin 👋</h3>
                <p style="margin-top:10px;color:#64748b">
                    Platform statistics overview
                </p>

                <!-- STATS -->
                <div style="margin-top:20px;display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:16px">
                    <div class="card">
                        <h4 style="color:#64748b">Total Users</h4>
                        <strong id="totalUsers" style="font-size:28px;color:#22c55e">0</strong>
                    </div>

                    <div class="card">
                        <h4 style="color:#64748b">Total Sellers</h4>
                        <strong id="totalSellers" style="font-size:28px;color:#2563eb">0</strong>
                    </div>
                </div>

                <!-- USERS vs SELLERS CHART -->
                <div class="card" style="margin-top:30px">
                    <h3 style="margin-bottom:14px">Users vs Sellers</h3>
                    <canvas id="userSellerChart" height="120"></canvas>
                </div>

                <!-- CARBON CHART -->
                <div class="card" style="margin-top:30px">
                    <h3 style="margin-bottom:14px">Monthly Carbon Footprint</h3>
                    <canvas id="carbonChart" height="120"></canvas>
                </div>

                <div class="card" style="margin-top:30px">
                    <h3 style="margin-bottom:14px">Top Eco-Friendly Products</h3>
                    <canvas id="ecoProductChart" height="120"></canvas>
                </div>

                <div class="card" style="margin-top:30px;text-align:center">
                    <h3>Download Eco Report</h3>
                    <button class="logout-btn" onclick="downloadReport()">
                        Download PDF
                    </button>
                </div>

            `;

            // Load numbers
            loadAdminStats();

            // Render charts AFTER DOM is ready
            renderUserSellerChart();
            renderCarbonChart();
            loadTopEcoProducts();


        }

        async function loadAdminStats() {
            try {
                const res = await fetch(`${API}/stats`, {
                    headers: authHeaders()
                });

                if (!res.ok) return;

                const data = await res.json();

                document.getElementById("totalUsers").innerText = data.totalUsers;
                document.getElementById("totalSellers").innerText = data.totalSellers;

                renderUserSellerChart(data.totalUsers, data.totalSellers);

            } catch (err) {
                console.error("Stats error:", err);
            }
        }

        async function renderUserSellerChart() {
            const usersRes = await fetch(`${API}/users`, { headers: authHeaders() });
            const sellersRes = await fetch(`${API}/sellers`, { headers: authHeaders() });

            const users = await usersRes.json();
            const sellers = await sellersRes.json();

            new Chart(document.getElementById("userSellerChart"), {
                type: "bar",
                data: {
                    labels: ["Users", "Sellers"],
                    datasets: [{
                        label: "Count",
                        data: [users.length, sellers.length],
                        backgroundColor: ["#2563eb", "#22c55e"]
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: { beginAtZero: true, ticks: { stepSize: 1 } }
                    }
                }
            });
        }

        function renderCarbonChart() {
            new Chart(document.getElementById("carbonChart"), {
                type: "line",
                data: {
                    labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun"],
                    datasets: [{
                        label: "CO₂ Saved (kg)",
                        data: [120, 180, 260, 300, 420, 510],
                        borderColor: "#22c55e",
                        backgroundColor: "rgba(34,197,94,0.2)",
                        fill: true,
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true
                }
            });
        }

        function loadTopEcoProducts() {
            const labels = ["Cloth Bag", "Steel Bottle", "Bamboo Brush"];
            const data = [18, 25, 12];

            new Chart(document.getElementById("ecoProductChart"), {
                type: "bar",
                data: {
                    labels,
                    datasets: [{
                        label: "Carbon Impact Saved (kg)",
                        data,
                        backgroundColor: "#22c55e"
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: { beginAtZero: true }
                    }
                }
            });
        }

        async function loadVerifySeller() {
            setActive(verifySellerBtn);
            topbarTitle.innerText = "Verify Sellers";

            const res = await fetch(`${API}/sellers`, { headers: authHeaders() });
            const sellers = await res.json();

            let rows = "";
            sellers.forEach(s => {
                rows += `
                    <tr>
                        <td>${s.username}</td>
                        <td>${s.email}</td>
                        <td>${s.phone}</td>
                        <td>${s.username} Store</td>
                        <td>Retail</td>
                        <td>NA</td>
                        <td><button class="verify-btn" onclick="approve(this)">Verify</button></td>
                    </tr>
                `;
            });

            contentArea.innerHTML = `
                <table>
                    <thead>
                        <tr>
                            <th>Name</th><th>Email</th><th>Phone</th>
                            <th>Shop</th><th>Business</th><th>GST</th><th>Status</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        }

        async function loadUsers() {
            setActive(usersBtn);
            topbarTitle.innerText = "Users";

            const res = await fetch(`${API}/users`, { headers: authHeaders() });
            const users = await res.json();

            let rows = "";
            users.forEach(u => {
                rows += `
            <tr>
                <td>${u.username}</td>
                <td>${u.phone}</td>
                <td>${u.email}</td>
            </tr>
        `;
            });

            contentArea.innerHTML = `
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Phone</th>
                            <th>Email</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        }


        function downloadReport() {
            fetch("http://localhost:9090/api/admin/eco-report", {
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("token")
                }
            })
                .then(res => res.blob())
                .then(blob => {
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement("a");
                    a.href = url;
                    a.download = "EcoBasaarX_Report.pdf";
                    a.click();
                    window.URL.revokeObjectURL(url);
                });
        }

        function approve(btn) {
            btn.parentElement.innerHTML = `<span class="approved">Approved</span>`;
        }

        function logout() {
            localStorage.clear();
            window.location.href = "/landing";
        }

        loadOverview();