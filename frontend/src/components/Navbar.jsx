import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <div className="bg-[#1B4F8A] text-white px-6 py-4 flex justify-between items-center shadow-md">
      <h1 className="text-lg font-bold tracking-wide">
        KRI Dashboard
      </h1>

      <div className="flex gap-6 items-center">
        <Link
          to="/dashboard"
          className="hover:text-blue-200 transition"
        >
          Dashboard
        </Link>

        <Link
          to="/risks"
          className="hover:text-blue-200 transition"
        >
          Risks
        </Link>

        <button
          onClick={handleLogout}
          className="bg-blue-400 px-3 py-1 rounded hover:bg-blue-300 text-white"
        >
          Logout
        </button>
      </div>
    </div>
  );
}