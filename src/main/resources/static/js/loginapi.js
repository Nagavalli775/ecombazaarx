// Role based login API call

const loginForm = document.getElementById("signinForm");
loginForm.addEventListener('submit', (e) => {
     e.preventDefault();
     console.log("it's working, ready to write login api");
     login();
});

async function login() {
    const payload = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

     const response = await fetch("http://localhost:9090/login", {
        method: "POST",
        headers: {"Content-Type": "application/json" },
        body:JSON.stringify(payload)
   });

   if(!response.ok) {
    alert("Login Failed");
    return;
   }
   
   console.log("hello");

   const data = await response.json();
   console.log(data, "wwwwwwwwwww");

   localStorage.setItem("token", data.token);
   localStorage.setItem("role", data.role);
   console.log(data.role, "roleeeee");

   if(data.role === "USER") {
    window.location.href = "/landing";
   }
   else if(data.role === "SELLER") {
    window.location.href = "/seller/dashboard";
   }
   else if(data.role == "ADMIN") {
    window.location.href = "/admin/dashboard";
   }
}

