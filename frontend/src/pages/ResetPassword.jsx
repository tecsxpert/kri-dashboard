import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function ResetPassword() {
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    // 👉 Call backend API with token later
    console.log("New password:", password);

    alert("Password reset successful (demo)");
    navigate("/");
  };

  return (
    <div className="flex items-center justify-center h-screen bg-[#E3F2FD]">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded shadow w-80"
      >
        <h2 className="text-xl font-bold mb-4 text-[#1B4F8A]">
          Reset Password
        </h2>

        <input
          type="password"
          placeholder="New Password"
          className="w-full p-2 mb-4 border rounded"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded hover:bg-blue-700">
          Reset Password
        </button>
      </form>
    </div>
  );
}