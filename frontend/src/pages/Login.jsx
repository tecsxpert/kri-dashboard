import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import API from "../services/api";

export default function Login() {
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);

  const [form, setForm] = useState({
    email: "",
    password: ""
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      // 🔐 Replace with your backend API
      const res = await API.post("/auth/login", form);

      const token = res.data.token;
      login(token);

      navigate("/dashboard");
    } catch (err) {
      setError("Invalid email or password");
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-[#E3F2FD]">
      <form
        onSubmit={handleLogin}
        className="bg-white p-8 rounded-2xl shadow-lg w-80"
      >
        {/* Title */}
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-6 text-center">
          LOGIN
        </h2>

        {/* Email */}
        <input
          name="email"
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          className="w-full p-2 mb-3 border rounded focus:ring-2 focus:ring-blue-300 outline-none"
        />

        {/* Password */}
        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300 outline-none"
        />

        {/* Error */}
        {error && (
          <p className="text-red-500 text-sm mb-2 text-center">
            {error}
          </p>
        )}

        {/* Forgot Password */}
        <p
          onClick={() => navigate("/forgot-password")}
          className="text-sm text-blue-600 cursor-pointer text-right mb-4 hover:underline"
        >
          Forgot Password?
        </p>

        {/* Login Button */}
        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded-lg hover:bg-blue-700 transition">
          Login
        </button>

        {/* Register Link */}
        <p className="text-sm text-center mt-4">
          Don’t have an account?{" "}
          <span
            onClick={() => navigate("/register")}
            className="text-blue-600 cursor-pointer hover:underline font-medium"
          >
            Register
          </span>
        </p>
      </form>
    </div>
  );
}