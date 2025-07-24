import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import './css/Register.css';

const schema = yup.object().shape({
  name: yup.string().required("Name is required"),
  email: yup.string().email("Invalid email").required("Email is required"),
  username: yup.string().min(4, "Username must be at least 4 characters").required("Username is required"),
  password: yup.string().min(6, "Password must be at least 6 characters").required("Password is required"),
  otp: yup.string().length(6, "OTP must be 6 digits").required("OTP is required")
});

function Register() {
  const [otpSent, setOtpSent] = useState(false);
  const [otpVerified, setOtpVerified] = useState(false);
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    setError,
    getValues,
    formState: { errors }
  } = useForm({
    resolver: yupResolver(schema),
    mode:"onChange"
  });

  const handleSendOtp = async () => {
    const email = getValues("email");
    if (!email) {
      setError("email", { message: "Please enter a valid email before sending OTP." });
      return;
    }
    try {
      await axios.post("http://localhost:8080/api/users/send-otp", { email });
      setOtpSent(true);
      alert("OTP sent to your email!");
    } catch (err) {
      alert("Failed to send OTP");
    }
  };

  const handleVerifyOtp = async () => {
    const { email, otp } = getValues();
    if (!otp || !email) {
      return;
    }
    try {
      await axios.post("http://localhost:8080/api/users/verify-otp", { email:email, otp:otp });
      setOtpVerified(true);
      alert("OTP verified successfully!");
    } catch (err) {
      setOtpVerified(false);
      alert("Invalid OTP! Please try again.");
      setError("otp", { message: "Invalid OTP" });
    }
  };

  const onSubmit = async (data) => {
    if (!otpVerified) {
      alert("Please verify the OTP before registering.");
      return;
    }
    try {
      await axios.post("http://localhost:8080/api/users/sign-up", data);
      alert("Registration successful!");
      navigate("/");
    } catch (err) {
    if (err.response && err.response.status === 409) {
      const responseData = err.response.data;
      if(responseData.includes("email")) {
        alert(err.response.data || "Username or email already exists.");
        localStorage.removeItem("otpVerified");
        navigate("/login");
    } else if(responseData.includes("Username")){
        alert(err.response.data || "Username already exists.");
    } else {
       alert("Registration failed. Please try again.");
    }
    }
  };
  }
  return (
    <>
      <div className="register-bg"></div>
      <div className="register-container">
        <h2 className="register-title">Register</h2>

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="register-field">
            <input className="register-input" placeholder="Name" {...register("name")} />
            <p className="error-msg">{errors.name?.message}</p>
          </div>

          <div className="register-field email-field">
              <div style={{ display: "flex", flexDirection: "column", flex: 1 }}>
            <input className="register-input" placeholder="Email" {...register("email")} />
                        <p className="error-msg">{errors.email?.message}</p>
              </div>
            <button
              className="register-btn otp-btn"
              type="button"
              onClick={handleSendOtp}
              disabled={otpSent}
            >
              Send OTP
            </button>
          </div>

          <div className="register-field email-field">
            <input className="register-input" placeholder="OTP" {...register("otp")} />
            <button
              className="register-btn otp-btn"
              type="button"
              onClick={handleVerifyOtp}
              disabled={!getValues("otp")||otpVerified}
            >
              Verify OTP
            </button>
            <p className="error-msg">{errors.otp?.message}</p>
          </div>

          <div className="register-field">
            <input className="register-input" placeholder="Username" {...register("username")} />
            <p className="error-msg">{errors.username?.message}</p>
          </div>

          <div className="register-field">
            <input
              className="register-input"
              type="password"
              placeholder="Password"
              {...register("password")}
            />
            <p className="error-msg">{errors.password?.message}</p>
          </div>

          <button className="register-btn main-btn" type="submit" disabled={!otpVerified}>
            Register
          </button>
        </form>
      </div>
    </>
  );
}

export default Register;
