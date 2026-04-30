import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import API from "../services/api";

export default function RiskDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [risk, setRisk] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRisk();
  }, []);

  const fetchRisk = async () => {
    try {
      const res = await API.get(`/risks/${id}`);
      setRisk(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!confirm("Are you sure you want to delete?")) return;

    try {
      await API.delete(`/risks/${id}`);
      alert("Deleted successfully");
      navigate("/risks");
    } catch (err) {
      console.error(err);
      alert("Delete failed");
    }
  };

  if (loading) return <p className="p-6">Loading...</p>;
  if (!risk) return <p className="p-6">Risk not found</p>;

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6 max-w-3xl mx-auto">
        {/* 🔹 Title */}
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-4">
          Risk Details
        </h2>

        {/* 🔹 Main Card */}
        <div className="bg-white p-6 rounded-xl shadow mb-6">

          <p className="mb-2">
            <strong>Name:</strong> {risk.name}
          </p>

          <p className="mb-2">
            <strong>Status:</strong>{" "}
            <span
              className={`px-3 py-1 rounded text-white text-sm ${
                risk.status === "High"
                  ? "bg-red-500"
                  : risk.status === "Medium"
                  ? "bg-yellow-500"
                  : "bg-green-500"
              }`}
            >
              {risk.status}
            </span>
          </p>

          <p className="mb-2">
            <strong>Score:</strong> {risk.score}
          </p>

          <p className="mb-2">
            <strong>Date:</strong> {risk.date}
          </p>

          {/* 🔘 Buttons */}
          <div className="flex gap-4 mt-4">
            <button
              onClick={() => navigate(`/edit-risk/${id}`)}
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
              Edit
            </button>

            <button
              onClick={handleDelete}
              className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
            >
              Delete
            </button>
          </div>
        </div>

        {/* 🤖 AI Analysis Section */}
        <div className="bg-white p-6 rounded-xl shadow">
          <h3 className="text-lg font-bold text-[#1B4F8A] mb-3">
            AI Analysis
          </h3>

          <p className="text-gray-700">
            {risk.aiAnalysis || "No AI analysis available yet."}
          </p>
        </div>
      </div>
    </div>
  );
}