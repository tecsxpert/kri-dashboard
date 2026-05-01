import { useNavigate, useLocation } from "react-router-dom";
import { useState } from "react";
import { users } from "../data/mockData";

export default function ResetPassword() {
  const navigate = useNavigate();
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const email = queryParams.get("email");

  const [form, setForm] = useState({
    password: "",
    confirmPassword: ""
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (!form.password || !form.confirmPassword) {
      setError("All fields required");
      return;
    }

    if (form.password !== form.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    // 🔥 Dummy update
    const user = users.find((u) => u.email === email);
    if (user) {
      user.password = form.password;
    }

    alert("Password reset successful 💙");
    navigate("/");
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-[#E3F2FD]">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded-2xl shadow-lg w-80"
      >
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-6 text-center">
          Reset Password
        </h2>

        <input
          type="password"
          name="password"
          placeholder="New Password"
          onChange={handleChange}
          className="w-full p-2 mb-3 border rounded focus:ring-2 focus:ring-blue-300"
        />

        <input
          type="password"
          name="confirmPassword"
          placeholder="Confirm Password"
          onChange={handleChange}
          className="w-full p-2 mb-3 border rounded focus:ring-2 focus:ring-blue-300"
        />

        {error && (
          <p className="text-red-500 text-sm mb-2 text-center">
            {error}
          </p>
        )}

        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded-lg hover:bg-blue-700">
          Reset Password
        </button>
      </form>
    </div>
  );
}