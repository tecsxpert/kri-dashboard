import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import API from "../services/api";

import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer
} from "recharts";

export default function Analytics() {
  const [data, setData] = useState([]);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    const res = await API.get("/stats");
    setData(res.data.byStatus);
  };

  const COLORS = ["#ef4444", "#facc15", "#22c55e"];

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6">
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-6">
          Analytics
        </h2>

        <div className="grid grid-cols-2 gap-6">

          {/* Bar Chart */}
          <div className="bg-white p-4 rounded shadow">
            <h3 className="mb-2 font-bold">Risk by Status</h3>

            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={data}>
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="value" fill="#1B4F8A" />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Pie Chart */}
          <div className="bg-white p-4 rounded shadow">
            <h3 className="mb-2 font-bold">Distribution</h3>

            <ResponsiveContainer width="100%" height={250}>
              <PieChart>
                <Pie data={data} dataKey="value" outerRadius={80}>
                  {data.map((_, index) => (
                    <Cell key={index} fill={COLORS[index]} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
          </div>

        </div>
      </div>
    </div>
  );
}