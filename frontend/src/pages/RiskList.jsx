import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import API from "../services/api";

export default function RiskList() {
  const navigate = useNavigate();
  const [params, setParams] = useSearchParams();

  const [risks, setRisks] = useState([]);
  const [loading, setLoading] = useState(true);

  // Pagination
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  // Sorting
  const [sortBy, setSortBy] = useState("id");
  const [sortDir, setSortDir] = useState("asc");

  // Filters
  const [search, setSearch] = useState("");
  const [status, setStatus] = useState("");
  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");

  // Debounce
  const [debouncedSearch, setDebouncedSearch] = useState(search);

  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(search);
    }, 300);
    return () => clearTimeout(timer);
  }, [search]);

  // Load from URL
  useEffect(() => {
    setSearch(params.get("q") || "");
    setStatus(params.get("status") || "");
    setFromDate(params.get("from") || "");
    setToDate(params.get("to") || "");
  }, []);

  // Update URL
  useEffect(() => {
    setParams({
      q: search,
      status,
      from: fromDate,
      to: toDate,
    });
  }, [search, status, fromDate, toDate]);

  // Fetch data
  useEffect(() => {
    fetchRisks();
  }, [page, sortBy, sortDir, debouncedSearch, status, fromDate, toDate]);

  const fetchRisks = async () => {
    setLoading(true);
    try {
      const res = await API.get("/all", {
        params: {
          page,
          size: 5,
          sortBy,
          sortDir,
          q: debouncedSearch,
          status,
          from: fromDate,
          to: toDate,
        },
      });

      setRisks(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      console.error("Error fetching risks:", err);
    } finally {
      setLoading(false);
    }
  };

  // Sorting
  const handleSort = (field) => {
    if (sortBy === field) {
      setSortDir(sortDir === "asc" ? "desc" : "asc");
    } else {
      setSortBy(field);
      setSortDir("asc");
    }
  };

  // CSV Export
  const handleExport = async () => {
    try {
      const res = await API.get("/export", {
        responseType: "blob",
      });

      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "risks.csv");
      document.body.appendChild(link);
      link.click();
    } catch (err) {
      console.error("Export failed", err);
    }
  };

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6">

        {/* 🔹 TITLE + ACTIONS */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold text-[#1B4F8A]">
            Risk List
          </h2>

          <div className="flex gap-3">
            <button
              onClick={handleExport}
              className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
            >
              Export CSV
            </button>

            <button
              onClick={() => navigate("/create-risk")}
              className="bg-[#1B4F8A] text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              + Create Risk
            </button>
          </div>
        </div>

        {/* 🔍 FILTER BAR */}
        <div className="bg-white p-4 rounded-xl shadow mb-4 flex flex-wrap gap-4">

          <input
            type="text"
            placeholder="Search..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="p-2 border rounded w-48 focus:ring-2 focus:ring-blue-300"
          />

          <select
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            className="p-2 border rounded"
          >
            <option value="">All Status</option>
            <option>High</option>
            <option>Medium</option>
            <option>Low</option>
          </select>

          <input
            type="date"
            value={fromDate}
            onChange={(e) => setFromDate(e.target.value)}
            className="p-2 border rounded"
          />

          <input
            type="date"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
            className="p-2 border rounded"
          />
        </div>

        {/* 🔄 LOADING */}
        {loading && (
          <div className="space-y-2">
            {[...Array(5)].map((_, i) => (
              <div
                key={i}
                className="h-6 bg-gray-300 animate-pulse rounded"
              ></div>
            ))}
          </div>
        )}

        {/* 📭 EMPTY */}
        {!loading && risks.length === 0 && (
          <div className="bg-white p-4 rounded shadow text-center">
            <p>No risks available</p>
          </div>
        )}

        {/* 📊 TABLE */}
        {!loading && risks.length > 0 && (
          <table className="w-full bg-white rounded-xl shadow">
            <thead className="bg-blue-200">
              <tr>
                <th onClick={() => handleSort("id")} className="cursor-pointer">ID 🔽</th>
                <th onClick={() => handleSort("name")} className="cursor-pointer">Name 🔽</th>
                <th>Status</th>
                <th onClick={() => handleSort("score")} className="cursor-pointer">Score 🔽</th>
                <th>Date</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {risks.map((r) => (
                <tr
                  key={r.id}
                  className="text-center border-t hover:bg-blue-50 cursor-pointer"
                  onClick={() => navigate(`/risks/${r.id}`)}
                >
                  <td className="p-2">{r.id}</td>
                  <td>{r.name}</td>

                  <td>
                    <span
                      className={`px-2 py-1 rounded text-white text-sm ${
                        r.status === "High"
                          ? "bg-red-500"
                          : r.status === "Medium"
                          ? "bg-yellow-500"
                          : "bg-green-500"
                      }`}
                    >
                      {r.status}
                    </span>
                  </td>

                  <td>{r.score}</td>
                  <td>{r.date}</td>

                  <td>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        navigate(`/edit-risk/${r.id}`);
                      }}
                      className="text-blue-600 hover:underline"
                    >
                      Edit
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {/* 🔢 PAGINATION */}
        <div className="flex justify-center items-center mt-4 gap-4">
          <button
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
            className="bg-blue-300 px-3 py-1 rounded disabled:opacity-50"
          >
            Prev
          </button>

          <span className="font-bold text-[#1B4F8A]">
            Page {page + 1} of {totalPages}
          </span>

          <button
            disabled={page === totalPages - 1}
            onClick={() => setPage(page + 1)}
            className="bg-blue-300 px-3 py-1 rounded disabled:opacity-50"
          >
            Next
          </button>
        </div>

      </div>
    </div>
  );
}