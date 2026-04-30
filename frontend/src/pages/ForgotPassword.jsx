import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const navigate = useNavigate();

  return (
    <div className="flex items-center justify-center h-screen bg-[#E3F2FD]">
      <form className="bg-white p-8 rounded shadow w-80">
        <h2 className="text-xl font-bold text-[#1B4F8A] mb-4 text-center">Forgot Password</h2>

        <input
          type="email"
          placeholder="Enter Email"
          className="w-full p-2 mb-4 border rounded"
          onChange={(e) => setEmail(e.target.value)}
        />

        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded">
          Send Link
        </button>

        <p onClick={() => navigate("/")} className="text-sm text-blue-600 mt-3 cursor-pointer text-center">
          Back to Login
        </p>
      </form>
    </div>
  );
}