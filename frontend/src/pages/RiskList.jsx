import { useEffect, useState } from "react";
import API from "../services/api";

export default function RiskList() {
  const [risks, setRisks] = useState([]);
  const [loading, setLoading] = useState(true);

  // Fetch data when page loads
  useEffect(() => {
    fetchRisks();
  }, []);

  const fetchRisks = async () => {
    try {
      const res = await API.get("/risks"); // backend API
      setRisks(res.data);
    } catch (err) {
      console.error("Error fetching data", err);
    } finally {
      setLoading(false);
    }
  };

  // 🔄 LOADING STATE
  if (loading) {
    return (
      <div className="p-4">
        <p className="text-gray-500">Loading data...</p>
      </div>
    );
  }

  // 📭 EMPTY STATE
  if (risks.length === 0) {
    return (
      <div className="p-4">
        <p className="text-gray-500">No data available</p>
      </div>
    );
  }

  // 📊 TABLE VIEW
  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">Risk List</h2>

      <table className="w-full border border-gray-300">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2 border">ID</th>
            <th className="p-2 border">Name</th>
            <th className="p-2 border">Status</th>
            <th className="p-2 border">Score</th>
            <th className="p-2 border">Date</th>
          </tr>
        </thead>

        <tbody>
          {risks.map((risk) => (
            <tr key={risk.id}>
              <td className="p-2 border">{risk.id}</td>
              <td className="p-2 border">{risk.name}</td>
              <td className="p-2 border">{risk.status}</td>
              <td className="p-2 border">{risk.score}</td>
              <td className="p-2 border">{risk.date}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}