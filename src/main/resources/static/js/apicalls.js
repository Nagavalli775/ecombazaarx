// User SignUp API call

const regForm = document.getElementById("registerForm")
regForm.addEventListener('submit', (e) => {
   e.preventDefault();
   console.log("it's working, ready to call the api");
    userSignup();
});

async function userSignup() {
   const payload = {
      username: document.getElementById("username").value,
      email: document.getElementById("email").value,
      phone: document.getElementById("phone").value,
      password: document.getElementById("password").value
   };

   const response = await fetch("http://localhost:9090/signup/user", {
        method: "POST",
        headers: {"Content-Type": "application/json" },
        body:JSON.stringify(payload)
   });

   if(response.ok) {
      window.location.href = "/landing";
   } else {
      alert("User Signup failed");
   }
}

