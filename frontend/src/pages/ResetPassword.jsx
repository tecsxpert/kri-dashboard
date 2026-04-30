import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function ResetPassword() {
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  return (
    <div className="flex items-center justify-center h-screen bg-[#E3F2FD]">
      <form className="bg-white p-8 rounded shadow w-80">
        <h2 className="text-xl font-bold text-[#1B4F8A] mb-4 text-center">Reset Password</h2>

        <input
          type="password"
          placeholder="New Password"
          className="w-full p-2 mb-4 border rounded"
          onChange={(e) => setPassword(e.target.value)}
        />

        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded">
          Reset Password
        </button>
      </form>
    </div>
  );
}