import { useNavigate } from "react-router-dom";

export default function Login() {
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    localStorage.setItem("token", "demo");
    navigate("/dashboard");
  };

  return (
    <div className="flex items-center justify-center h-screen bg-[#E3F2FD]">
      <form onSubmit={handleLogin} className="bg-white p-8 rounded shadow w-80">
        <h2 className="text-xl font-bold text-[#1B4F8A] mb-4 text-center">Login</h2>

        <input className="w-full p-2 mb-3 border rounded" placeholder="Email" />
        <input className="w-full p-2 mb-2 border rounded" placeholder="Password" type="password" />

        <p onClick={() => navigate("/forgot-password")} className="text-sm text-blue-600 cursor-pointer text-right mb-3">
          Forgot Password?
        </p>

        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded">Login</button>

        <p onClick={() => navigate("/register")} className="text-sm text-blue-600 mt-3 cursor-pointer text-center">
          New user? Register
        </p>
      </form>
    </div>
  );
}