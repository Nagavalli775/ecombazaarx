// Seller Signup API call

const sellerForm = document.getElementById("sellerForm");
sellerForm.addEventListener('submit', (e) => {
      e.preventDefault();
      console.log("seller signup working ready to goooo");
      sellerSignup();
});

async function sellerSignup() {
     const payload = {
      username: document.getElementById("username").value,
      email: document.getElementById("email").value,
      phone: document.getElementById("phone").value,
      password: document.getElementById("password").value,
      shopName: document.getElementById("shopName").value,
      shopAddress: document.getElementById("shopAddress").value,
      businessType: document.getElementById("businessType").value,
      gstNumber: document.getElementById("gstNumber").value,
      bankAccountNumber: document.getElementById("bankAccountNumber").value,
      ifscCode: document.getElementById("ifscCode").value,
      termsAccepted: document.getElementById("termsAccepted").value
   };

   const res = await fetch("http://localhost:9090/signup/seller", {
        method: "POST",
        headers: {"Content-Type": "application/json" },
        body:JSON.stringify(payload)
   });

   if(res.ok) {
      window.location.href = "/landing";
   } else {
      alert("Seller Signup failed");
   }
}