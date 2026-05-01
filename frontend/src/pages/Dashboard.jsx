import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import { stats as mockStats } from "../data/mockData";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";

import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer
} from "recharts";

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = () => {
    setLoading(true);

    setTimeout(() => {
      setStats(mockStats);
      setLoading(false);
    }, 800);
  };

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6">
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-6">
          Dashboard
        </h2>

        {/* 🔄 LOADING STATE */}
        {loading && <Skeleton rows={4} />}

        {/* 📭 EMPTY STATE */}
        {!loading && !stats && (
          <EmptyState message="No dashboard data available" />
        )}

        {/* ✅ MAIN CONTENT */}
        {!loading && stats && (
          <>
            {/* KPI CARDS */}
            <div className="grid grid-cols-4 gap-4 mb-8">
              <div className="bg-white p-4 rounded-xl shadow">
                <p className="text-gray-500">Total Risks</p>
                <h3 className="text-xl font-bold text-blue-600">
                  {stats.total}
                </h3>
              </div>

              <div className="bg-white p-4 rounded-xl shadow">
                <p className="text-gray-500">High Risk</p>
                <h3 className="text-xl font-bold text-red-500">
                  {stats.high}
                </h3>
              </div>

              <div className="bg-white p-4 rounded-xl shadow">
                <p className="text-gray-500">Medium Risk</p>
                <h3 className="text-xl font-bold text-yellow-500">
                  {stats.medium}
                </h3>
              </div>

              <div className="bg-white p-4 rounded-xl shadow">
                <p className="text-gray-500">Low Risk</p>
                <h3 className="text-xl font-bold text-green-500">
                  {stats.low}
                </h3>
              </div>
            </div>

            {/* BAR CHART */}
            <div className="bg-white p-6 rounded-xl shadow">
              <h3 className="text-lg font-bold text-[#1B4F8A] mb-4">
                Risks by Status
              </h3>

              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={stats.byStatus}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="value" fill="#1B4F8A" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </>
        )}
      </div>
    </div>
  );
}