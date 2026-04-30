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
      <form
        onSubmit={handleLogin}
        className="bg-white p-8 rounded-xl shadow-lg w-80"
      >
        <h2 className="text-2xl font-bold mb-5 text-[#1B4F8A] text-center">
          KRI Dashboard Login
        </h2>

        <input
          type="text"
          placeholder="Username"
          className="w-full p-2 mb-3 border rounded focus:outline-none focus:ring-2 focus:ring-blue-300"
        />

        <input
          type="password"
          placeholder="Password"
          className="w-full p-2 mb-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-300"
        />

        <p
          onClick={() => navigate("/forgot-password")}
          className="text-sm text-blue-600 cursor-pointer text-right mb-4 hover:underline"
        >
          Forgot Password?
        </p>

        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded hover:bg-blue-700 transition">
          Login
        </button>
      </form>
    </div>
  );
}