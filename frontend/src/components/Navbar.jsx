import { Link, useNavigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

export default function Navbar() {
  const navigate = useNavigate();
  const { logout } = useContext(AuthContext);

  const handleLogout = () => {
    logout(); // remove token + update state
    navigate("/");
  };

  return (
    <div className="bg-[#1B4F8A] text-white px-6 py-4 flex justify-between items-center shadow-md">
      {/* Logo */}
      <h1 className="text-lg font-bold tracking-wide">
        KRI DASHBOARD
      </h1>

      {/* Navigation Links */}
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

        {/* ✅ NEW ANALYTICS LINK */}
        <Link to="/analytics" className="hover:text-blue-200 transition">
          Analytics
        </Link>


        {/* 📊 REPORT PAGE */}
        <Link to="/report" className="hover:text-blue-200">
          AI Report
        </Link>

        {/* 📄 SAMPLE RISK DETAIL (for demo only) */}
        <Link to="/risks/1" className="hover:text-blue-200">
          Risk Detail
        </Link>

        {/* Logout */}
        <button
          onClick={handleLogout}
          className="bg-blue-400 px-3 py-1 rounded hover:bg-blue-300 transition"
        >
          Logout
        </button>
      </div>
    </div>
  );
}