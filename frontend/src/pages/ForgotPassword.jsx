import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { users } from "../data/mockData";

export default function ForgotPassword() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (!email) {
      setError("Email is required");
      return;
    }

    setLoading(true);

    // 🔥 Dummy check (simulate backend)
    setTimeout(() => {
      const user = users.find((u) => u.email === email);

      if (!user) {
        setError("Email not found");
        setLoading(false);
        return;
      }

      alert("Reset link sent (demo) 💙");

      // 👉 Navigate to reset page with email
      navigate(`/reset-password?email=${email}`);

      setLoading(false);
    }, 800);
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-[#E3F2FD]">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded-2xl shadow-lg w-80"
      >
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-6 text-center">
          FORGOT PASSWORD
        </h2>

        <input
          type="email"
          placeholder="Enter Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full p-2 mb-3 border rounded focus:ring-2 focus:ring-blue-300"
        />

        {error && (
          <p className="text-red-500 text-sm mb-2 text-center">
            {error}
          </p>
        )}

        <button
          disabled={loading}
          className="w-full bg-[#1B4F8A] text-white p-2 rounded-lg hover:bg-blue-700 transition"
        >
          {loading ? "Sending..." : "Send Link"}
        </button>

        <p
          onClick={() => navigate("/")}
          className="text-sm text-blue-600 mt-4 cursor-pointer text-center hover:underline"
        >
          Back to Login
        </p>
      </form>
    </div>
  );
}