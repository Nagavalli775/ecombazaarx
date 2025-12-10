// auth.js - shared for signin + register pages
document.addEventListener('DOMContentLoaded', () => {

  /* ---------------------
   SIGN IN behavior
   --------------------- */
  (function setupSignIn() {
    const signinForm = document.getElementById('signinForm');
    if (!signinForm) return;

    const signinInput = document.getElementById('signinInput');
    const signinPassword = document.getElementById('signinPassword');
    const signinErr = document.getElementById('signinErr');
    const signinPassErr = document.getElementById('signinPassErr');
    const showSignPassword = document.getElementById('showSignPassword');
    const forgotPassword = document.getElementById('forgotPassword');

    // Show/Hide password for sign in
    if (showSignPassword) {
      showSignPassword.addEventListener('change', () => {
        signinPassword.type = showSignPassword.checked ? 'text' : 'password';
      });
    }

    // Forgot password click
    if (forgotPassword) {
      forgotPassword.addEventListener('click', (e) => {
        e.preventDefault();
        alert("Forgot Password flow will be added with backend.");
      });
    }

    signinForm.addEventListener('submit', (e) => {
      e.preventDefault();
      signinErr.textContent = "";
      signinPassErr.textContent = "";

      const identifier = signinInput.value.trim();
      const password = signinPassword.value.trim();

      if (!identifier) {
        signinErr.textContent = "Please enter your mobile number or email.";
        return;
      }
      if (!password || password.length < 6) {
        signinPassErr.textContent = "Enter a valid password.";
        return;
      }

      // Demo output 
      console.log("Sign in demo:", { identifier, password });
      location.href = "landing.html";

      alert("Sign in successful (demo). Replace with backend API call.");
    });

  })();

  /* ---------------------
     REGISTER behavior
     --------------------- */
  const regForm = document.getElementById('registerForm');
  if (regForm) {
    const firstName = document.getElementById('firstName');
    const lastName = document.getElementById('lastName');
    const regEmail = document.getElementById('regEmail');
    const mobile = document.getElementById('mobile');
    const countryCode = document.getElementById('countryCode');
    const verifyBtn = document.getElementById('verifyBtn');
    const otpArea = document.getElementById('otpArea');
    const otpInput = document.getElementById('otpInput');
    const confirmOtp = document.getElementById('confirmOtp');
    const cancelOtp = document.getElementById('cancelOtp');
    const mobileStatus = document.getElementById('mobileStatus');
    const regPassword = document.getElementById('regPassword');
    const showPassword = document.getElementById('showPassword');
    const emailErr = document.getElementById('emailErr');
    const mobileErr = document.getElementById('mobileErr');
    const passErr = document.getElementById('passErr');
    const formMsg = document.getElementById('formMsg');
    const toSignIn = document.getElementById('toSignIn');

    let otpSent = false;
    let mobileVerified = false;

    // show password toggle
    if (showPassword) {
      showPassword.addEventListener('change', () => {
        regPassword.type = showPassword.checked ? 'text' : 'password';
      });
    }

    // enable verify only when name,email,password are valid and mobile present
    function updateVerifyState() {
      const nameOk = firstName.value.trim().length > 0 && lastName.value.trim().length > 0;
      const emailOk = validateEmail(regEmail.value.trim());
      const passOk = regPassword.value.length >= 6;
      const mobileOk = validateMobile(mobile.value.trim());

      if (!nameOk || !emailOk || !passOk) {
        verifyBtn.disabled = true;
        verifyBtn.title = 'Fill name, email and password first';
      } else {
        verifyBtn.disabled = mobileVerified ? true : (mobileOk ? false : true);
        verifyBtn.title = verifyBtn.disabled ? 'Enter a valid mobile number' : '';
      }
    }

    [firstName, lastName, regEmail, regPassword, mobile].forEach(el => {
      if (!el) return;
      el.addEventListener('input', () => {
        if (mobileVerified && el === mobile) {
          mobileVerified = false;
          mobileStatus.textContent = '';
          verifyBtn.disabled = false;
          verifyBtn.textContent = 'Verify';
          verifyBtn.removeAttribute('aria-pressed');
        }
        updateVerifyState();
      });
    });

    // when user clicks Verify -> show OTP area (demo OTP = 1234)
    verifyBtn.addEventListener('click', async () => {
      emailErr.textContent = mobileErr.textContent = passErr.textContent = formMsg.textContent = '';
      const emailVal = regEmail.value.trim();
      const passVal = regPassword.value;
      const mobileVal = mobile.value.trim();

      if (!validateEmail(emailVal)) { emailErr.textContent = 'Enter a valid email before verifying mobile.'; return; }
      if (passVal.length < 6) { passErr.textContent = 'Password must be at least 6 characters.'; return; }
      if (!validateMobile(mobileVal)) { mobileErr.textContent = 'Enter a valid mobile number.'; return; }

      otpArea.style.display = 'flex';
      otpSent = true;
      otpInput.value = '';
      mobileStatus.textContent = 'OTP sent (demo: 1234). Enter OTP to verify.';
      mobileStatus.style.color = '';
      verifyBtn.textContent = 'Resend';
      verifyBtn.setAttribute('aria-pressed', 'true');

      // TODO: replace with API call to send OTP
      // await fetch('/api/send-otp', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ countryCode: countryCode.value, mobile: mobileVal }) });
    });

    confirmOtp.addEventListener('click', async () => {
      if (!otpSent) return;
      const entered = (otpInput.value || '').trim();
      if (!entered) {
        mobileStatus.textContent = 'Enter OTP';
        mobileStatus.style.color = '#c23b3b';
        return;
      }
      if (entered === '1234') {
        mobileVerified = true;
        mobileStatus.textContent = 'Mobile verified ✓';
        mobileStatus.style.color = 'var(--green)';
        otpArea.style.display = 'none';
        verifyBtn.textContent = 'Verified';
        verifyBtn.disabled = true;
        verifyBtn.setAttribute('aria-pressed', 'true');
        updateVerifyState();
      } else {
        mobileStatus.textContent = 'Incorrect OTP. Try 1234 for demo.';
        mobileStatus.style.color = '#c23b3b';
      }

      // TODO: real verify call
      // const res = await fetch('/api/verify-otp', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ countryCode: countryCode.value, mobile: mobile.value, otp: entered })});
    });

    cancelOtp.addEventListener('click', () => {
      otpArea.style.display = 'none';
      mobileStatus.textContent = '';
      otpSent = false;
      otpInput.value = '';
    });

    // final submit logic
    regForm.addEventListener('submit', (e) => {
      e.preventDefault();
      emailErr.textContent = mobileErr.textContent = passErr.textContent = formMsg.textContent = '';

      if (!validateEmail(regEmail.value.trim())) { emailErr.textContent = 'Enter valid email.'; return; }
      if (!validateMobile(mobile.value.trim())) { mobileErr.textContent = 'Enter valid mobile.'; return; }
      if (regPassword.value.length < 6) { passErr.textContent = 'Password is too short.'; return; }
      if (!mobileVerified) { formMsg.textContent = 'Please verify your mobile number before creating account.'; return; }

      const payload = {
        firstName: firstName.value.trim(),
        lastName: lastName.value.trim(),
        email: regEmail.value.trim(),
        mobile: `${countryCode.value}${mobile.value.trim().replace(/\s+/g, '')}`,
        password: regPassword.value
      };

      // Demo: replace with real backend call to register endpoint
      // fetch('/api/auth/register', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(payload) })
      formMsg.style.color = 'var(--green)';
      formMsg.textContent = 'Account created (demo). Replace with backend API call.';
      setTimeout(() => { /* location.href = 'signin.html' */ }, 700);
    });

    if (toSignIn) toSignIn.addEventListener('click', () => location.href = 'signin.html');
    updateVerifyState();
  }

  /* ---------------------
     Helpers
     --------------------- */
  function validateEmail(email) {
    if (!email) return false;
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }
  function validateMobile(mobileVal) {
    if (!mobileVal) return false;
    const digits = mobileVal.replace(/\D/g, '');
    return digits.length >= 7 && digits.length <= 15;
  }
});
