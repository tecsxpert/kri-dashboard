import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import API from "../services/api";

export default function RiskList() {
  const [risks, setRisks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRisks();
  }, []);

  const fetchRisks = async () => {
    try {
      const res = await API.get("/risks");
      setRisks(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6">
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-4">Risk List</h2>

        {loading && <p>Loading...</p>}

        {!loading && risks.length === 0 && <p>No data</p>}

        {!loading && risks.length > 0 && (
          <table className="w-full bg-white rounded shadow">
            <thead className="bg-blue-200">
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Status</th>
                <th>Score</th>
              </tr>
            </thead>
            <tbody>
              {risks.map((r) => (
                <tr key={r.id} className="text-center border-t">
                  <td>{r.id}</td>
                  <td>{r.name}</td>
                  <td>{r.status}</td>
                  <td>{r.score}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}