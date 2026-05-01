import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import { risks as mockRisks } from "../data/mockData";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";

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

  const [debouncedSearch, setDebouncedSearch] = useState(search);

  // Debounce search
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

  // Fetch risks (dummy)
  useEffect(() => {
    fetchRisks();
  }, [page, sortBy, sortDir, debouncedSearch, status, fromDate, toDate]);

  const fetchRisks = () => {
    setLoading(true);

    let filtered = [...mockRisks];

    // Search
    if (debouncedSearch) {
      filtered = filtered.filter((r) =>
        r.name.toLowerCase().includes(debouncedSearch.toLowerCase())
      );
    }

    // Status filter
    if (status) {
      filtered = filtered.filter((r) => r.status === status);
    }

    // Date filter
    if (fromDate) {
      filtered = filtered.filter((r) => r.date >= fromDate);
    }
    if (toDate) {
      filtered = filtered.filter((r) => r.date <= toDate);
    }

    // Sorting
    filtered.sort((a, b) => {
      if (sortDir === "asc") return a[sortBy] > b[sortBy] ? 1 : -1;
      return a[sortBy] < b[sortBy] ? 1 : -1;
    });

    // Pagination
    const pageSize = 5;
    const start = page * pageSize;
    const paginated = filtered.slice(start, start + pageSize);

    setRisks(paginated);
    setTotalPages(Math.ceil(filtered.length / pageSize));
    setLoading(false);
  };

  const handleSort = (field) => {
    if (sortBy === field) {
      setSortDir(sortDir === "asc" ? "desc" : "asc");
    } else {
      setSortBy(field);
      setSortDir("asc");
    }
  };

  // CSV Export
  const handleExport = () => {
    if (risks.length === 0) {
      alert("No data to export");
      return;
    }

    const csv = [
      ["ID", "Name", "Status", "Score", "Date"],
      ...risks.map((r) => [
        r.id,
        r.name,
        r.status,
        r.score,
        r.date,
      ]),
    ];

    const blob = new Blob(
      [csv.map((e) => e.join(",")).join("\n")],
      { type: "text/csv;charset=utf-8;" }
    );

    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "filtered-risks.csv";
    a.click();
  };

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6 pb-12">

        {/* HEADER */}
        <div className="flex justify-between mb-4 flex-wrap gap-4">
          <h2 className="text-2xl font-bold text-[#1B4F8A]">
            Risk List
          </h2>

          <div className="flex gap-3">
            <button
              onClick={handleExport}
              className="bg-green-500 text-white px-4 py-2 rounded-lg shadow hover:bg-green-600"
            >
              Download CSV
            </button>

            <button
              onClick={() => navigate("/create-risk")}
              className="bg-[#1B4F8A] text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700"
            >
              + Create Risk
            </button>
          </div>
        </div>

        {/* FILTERS */}
        <div className="bg-white p-4 rounded-xl shadow mb-6 flex gap-4 flex-wrap">
          <input
            placeholder="Search..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="p-2 border rounded-lg shadow-sm"
          />

          <select
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            className="p-2 border rounded-lg shadow-sm"
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
            className="p-2 border rounded-lg shadow-sm"
          />

          <input
            type="date"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
            className="p-2 border rounded-lg shadow-sm"
          />
        </div>

        {/* LOADING */}
        {loading && <Skeleton rows={5} />}

        {/* EMPTY */}
        {!loading && risks.length === 0 && (
          <EmptyState message="No risks available" />
        )}

        {/* TABLE */}
        {!loading && risks.length > 0 && (
          <div className="bg-white rounded-xl shadow-lg overflow-hidden">
            <table className="w-full table-fixed">
              <thead className="bg-blue-100 text-[#1B4F8A]">
                <tr>
                  <th className="w-16 p-4 text-center cursor-pointer" onClick={() => handleSort("id")}>ID</th>
                  <th className="w-1/3 p-4 text-left cursor-pointer" onClick={() => handleSort("name")}>Name</th>
                  <th className="w-32 p-4 text-center">Status</th>
                  <th className="w-24 p-4 text-center cursor-pointer" onClick={() => handleSort("score")}>Score</th>
                  <th className="w-40 p-4 text-center">Date</th>
                </tr>
              </thead>

              <tbody>
                {risks.map((r) => (
                  <tr
                    key={r.id}
                    className="border-t hover:bg-blue-50 transition"
                  >
                    <td className="p-4 text-center">{r.id}</td>

                    <td className="p-4 text-left font-medium">
                      {r.name}
                    </td>

                    <td className="p-4 text-center">
                      <span
                        className={`px-3 py-1 rounded-full text-white text-sm ${
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

                    <td className="p-4 text-center">{r.score}</td>

                    <td className="p-4 text-center">{r.date}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* PAGINATION */}
        <div className="flex justify-center items-center gap-4 mt-6">
          <button
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
            className="px-4 py-2 bg-white border rounded-lg shadow hover:bg-blue-50 disabled:opacity-50"
          >
            Prev
          </button>

          <span className="font-semibold text-[#1B4F8A]">
            Page {page + 1} of {totalPages}
          </span>

          <button
            disabled={page === totalPages - 1}
            onClick={() => setPage(page + 1)}
            className="px-4 py-2 bg-white border rounded-lg shadow hover:bg-blue-50 disabled:opacity-50"
          >
            Next
          </button>
        </div>

      </div>
    </div>
  );
}