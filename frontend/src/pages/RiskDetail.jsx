import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { risks, aiResponse } from "../data/mockData";


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

  // ✅ MOCK DATA FETCH
  const fetchRisk = () => {
    const found = risks.find((r) => r.id === Number(id));
    setRisk(found);
    setLoading(false);
  };

  // 🤖 AI (DUMMY)
  const handleAskAI = () => {
  setAiLoading(true);
  setAiError(false);

  setTimeout(() => {
    setAiData(aiResponse); // dummy response
    setAiLoading(false);
  }, 1000);
  };

  // ❌ DELETE (FRONTEND ONLY)
  const handleDelete = () => {
    if (!confirm("Are you sure you want to delete?")) return;

    alert("Deleted (dummy mode)");
    navigate("/risks");
  };

  if (loading) return <p className="p-6">Loading...</p>;
  if (!risk) return <p className="p-6">Risk not found</p>;

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6 max-w-3xl mx-auto">

        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-4">
          Risk Details
        </h2>

        {/* MAIN CARD */}
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

          <div className="flex gap-4 mt-4">
            <button
              onClick={() => navigate(`/edit-risk/${id}`)}
              className="bg-blue-500 text-white px-4 py-2 rounded-lg"
            >
              Edit
            </button>

            <button
              onClick={handleDelete}
              className="bg-red-500 text-white px-4 py-2 rounded-lg"
            >
              Delete
            </button>
          </div>
        </div>

        {/* 🤖 AI PANEL */}
        <div className="bg-white p-6 rounded-2xl shadow-lg mt-6 border border-blue-100">

          <h3 className="text-xl font-bold text-[#1B4F8A] mb-4">
            🤖 AI Analysis
          </h3>

          {!aiData && !aiLoading && (
            <button
              onClick={handleAskAI}
              className="bg-[#1B4F8A] text-white px-5 py-2 rounded-lg"
            >
              Ask AI
            </button>
          )}

          {aiLoading && <p className="text-blue-600">Analyzing...</p>}

          {aiError && <p className="text-red-500">Error loading AI</p>}

          {aiData && (
            <div className="bg-blue-50 p-5 rounded-xl mt-4">
              <p>{aiData.description}</p>
            </div>
          )}
        </div>

      </div>
    </div>
  );
}