import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import API from "../services/api";

export default function RiskDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [risk, setRisk] = useState(null);
  const [loading, setLoading] = useState(true);

  // 🤖 AI states
  const [aiLoading, setAiLoading] = useState(false);
  const [aiData, setAiData] = useState(null);
  const [aiError, setAiError] = useState(false);

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

  // 🤖 AI CALL
  const handleAskAI = async () => {
    setAiLoading(true);
    setAiError(false);

    try {
      const res = await API.post("/ai/describe", {
        text: risk.name,
      });

      setAiData(res.data);
    } catch (err) {
      console.error(err);
      setAiError(true);
    } finally {
      setAiLoading(false);
    }
  };

  // ❌ DELETE
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

        {/* 🔹 MAIN CARD */}
        <div className="bg-white p-6 rounded-2xl shadow-lg border border-blue-100">

          <p className="mb-3">
            <strong>Name:</strong> {risk.name}
          </p>

          <p className="mb-3">
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

          <p className="mb-3">
            <strong>Score:</strong> {risk.score}
          </p>

          <p className="mb-3">
            <strong>Date:</strong> {risk.date}
          </p>

          {/* 🔘 ACTION BUTTONS */}
          <div className="flex gap-4 mt-4">
            <button
              onClick={() => navigate(`/edit-risk/${id}`)}
              className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
            >
              Edit
            </button>

            <button
              onClick={handleDelete}
              className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
            >
              Delete
            </button>
          </div>
        </div>

        {/* 🤖 AI PANEL */}
        <div className="bg-white p-6 rounded-2xl shadow-lg mt-6 border border-blue-100">

          <h3 className="text-xl font-bold text-[#1B4F8A] mb-4 flex items-center gap-2">
            🤖 AI Analysis
          </h3>

          {/* Ask AI */}
          {!aiData && !aiLoading && (
            <button
              onClick={handleAskAI}
              className="bg-[#1B4F8A] text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition"
            >
              Ask AI
            </button>
          )}

          {/* Loading */}
          {aiLoading && (
            <div className="flex items-center gap-3 text-blue-600">
              <div className="w-6 h-6 border-4 border-blue-400 border-t-transparent rounded-full animate-spin"></div>
              <span className="font-medium">Analyzing risk...</span>
            </div>
          )}

          {/* Error */}
          {aiError && (
            <div className="text-center">
              <p className="text-red-500 mb-3">
                ⚠️ Failed to get AI response
              </p>

              <button
                onClick={handleAskAI}
                className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
              >
                Retry
              </button>
            </div>
          )}

          {/* AI RESULT */}
          {aiData && (
            <div className="bg-blue-50 p-5 rounded-xl border border-blue-200 mt-4">

              <h4 className="font-semibold text-[#1B4F8A] mb-2">
                AI Insights
              </h4>

              <p className="text-gray-700 leading-relaxed">
                {aiData.description || "No response available"}
              </p>

            </div>
          )}
        </div>

      </div>
    </div>
  );
}