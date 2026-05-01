import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import { analyticsData } from "../data/mockData";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";

import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer
} from "recharts";

export default function Analytics() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [period, setPeriod] = useState("6M");

  useEffect(() => {
    fetchData();
  }, [period]);

  const fetchData = () => {
    setLoading(true);

    setTimeout(() => {
      setData(analyticsData);
      setLoading(false);
    }, 500);
  };

  const COLORS = ["#ef4444", "#facc15", "#22c55e"];

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6">

        {/* HEADER */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-[#1B4F8A]">
            Analytics Dashboard
          </h2>

          {/* PERIOD SELECTOR */}
          <select
            value={period}
            onChange={(e) => setPeriod(e.target.value)}
            className="p-2 border rounded"
          >
            <option value="3M">Last 3 Months</option>
            <option value="6M">Last 6 Months</option>
            <option value="12M">Last 12 Months</option>
          </select>
        </div>

        {/* 🔄 LOADING */}
        {loading && <Skeleton rows={4} />}

        {/* 📭 EMPTY */}
        {!loading && !data && (
          <EmptyState message="No analytics data available" />
        )}

        {/* ✅ MAIN CONTENT */}
        {!loading && data && (
          <div className="grid grid-cols-2 gap-6">

            {/* 📊 BAR CHART */}
            <div className="bg-white p-4 rounded-xl shadow">
              <h3 className="font-bold mb-2 text-[#1B4F8A]">
                Risks by Category
              </h3>

              <ResponsiveContainer width="100%" height={250}>
                <BarChart data={data.byCategory}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="value" fill="#1B4F8A" />
                </BarChart>
              </ResponsiveContainer>
            </div>

            {/* 🥧 PIE CHART */}
            <div className="bg-white p-4 rounded-xl shadow">
              <h3 className="font-bold mb-2 text-[#1B4F8A]">
                Risks by Status
              </h3>

              <ResponsiveContainer width="100%" height={250}>
                <PieChart>
                  <Pie data={data.byStatus} dataKey="value" outerRadius={80}>
                    {data.byStatus.map((_, i) => (
                      <Cell key={i} fill={COLORS[i]} />
                    ))}
                  </Pie>
                </PieChart>
              </ResponsiveContainer>
            </div>

            {/* 📈 LINE CHART */}
            <div className="bg-white p-4 rounded-xl shadow col-span-2">
              <h3 className="font-bold mb-2 text-[#1B4F8A]">
                Risks Over Time ({period})
              </h3>

              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={data.overTime}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <Tooltip />
                  <Line
                    type="monotone"
                    dataKey="value"
                    stroke="#1B4F8A"
                  />
                </LineChart>
              </ResponsiveContainer>
            </div>

          </div>
        )}
      </div>
    </div>
  );
}