import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { users } from "../data/mockData"; // ✅ dummy data

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

  // ✅ UPDATED LOGIN (NO BACKEND)
  const handleLogin = (e) => {
    e.preventDefault();
    setError("");

    const user = users.find(
      (u) =>
        u.email === form.email &&
        u.password === form.password
    );

    if (user) {
      login(user.token); // store dummy token
      navigate("/dashboard");
    } else {
      setError("Invalid email or password");
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-[#E3F2FD]">
      <form
        onSubmit={handleLogin}
        className="bg-white p-8 rounded-2xl shadow-lg w-80"
      >
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-6 text-center">
          LOGIN
        </h2>

        <input
          name="email"
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          className="w-full p-2 mb-3 border rounded focus:ring-2 focus:ring-blue-300"
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300"
        />

        {error && (
          <p className="text-red-500 text-sm mb-2 text-center">
            {error}
          </p>
        )}

        <p
          onClick={() => navigate("/forgot-password")}
          className="text-sm text-blue-600 cursor-pointer text-right mb-4 hover:underline"
        >
          Forgot Password?
        </p>

        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded-lg hover:bg-blue-700">
          Login
        </button>

        <p className="text-sm text-center mt-4">
          Don’t have an account?{" "}
          <span
            onClick={() => navigate("/register")}
            className="text-blue-600 cursor-pointer hover:underline"
          >
            Register
          </span>
        </p>
      </form>
    </div>
  );
}