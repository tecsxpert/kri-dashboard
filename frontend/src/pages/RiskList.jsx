import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import { risks as mockRisks } from "../data/mockData";

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

  // Load filters from URL
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

  // Fetch (dummy)
  useEffect(() => {
    fetchRisks();
  }, [page, sortBy, sortDir, debouncedSearch, status, fromDate, toDate]);

  const fetchRisks = () => {
    setLoading(true);

    let filtered = [...mockRisks];

    // 🔍 Search
    if (debouncedSearch) {
      filtered = filtered.filter((r) =>
        r.name.toLowerCase().includes(debouncedSearch.toLowerCase())
      );
    }

    // 🎯 Status
    if (status) {
      filtered = filtered.filter((r) => r.status === status);
    }

    // 📅 Date filter
    if (fromDate) {
      filtered = filtered.filter((r) => r.date >= fromDate);
    }
    if (toDate) {
      filtered = filtered.filter((r) => r.date <= toDate);
    }

    // 🔽 Sorting
    filtered.sort((a, b) => {
      if (sortDir === "asc") return a[sortBy] > b[sortBy] ? 1 : -1;
      return a[sortBy] < b[sortBy] ? 1 : -1;
    });

    // 📄 Pagination
    const pageSize = 5;
    const start = page * pageSize;
    const paginated = filtered.slice(start, start + pageSize);

    setRisks(paginated);
    setTotalPages(Math.ceil(filtered.length / pageSize));
    setLoading(false);
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

  // ✅ UPDATED CSV EXPORT (FILTERED DATA)
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
      ])
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

      <div className="p-6">

        {/* HEADER */}
        <div className="flex justify-between mb-4">
          <h2 className="text-2xl font-bold text-[#1B4F8A]">
            Risk List
          </h2>

          <div className="flex gap-3">
            <button
              onClick={handleExport}
              className="bg-green-500 text-white px-4 py-2 rounded"
            >
              Export CSV
            </button>

            <button
              onClick={() => navigate("/create-risk")}
              className="bg-[#1B4F8A] text-white px-4 py-2 rounded"
            >
              + Create Risk
            </button>
          </div>
        </div>

        {/* FILTERS */}
        <div className="bg-white p-4 rounded-xl shadow mb-4 flex gap-4 flex-wrap">
          <input
            placeholder="Search..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="p-2 border rounded"
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

          <input type="date" value={fromDate} onChange={(e) => setFromDate(e.target.value)} className="p-2 border rounded" />
          <input type="date" value={toDate} onChange={(e) => setToDate(e.target.value)} className="p-2 border rounded" />
        </div>

        {/* LOADING */}
        {loading && <p>Loading...</p>}

        {/* TABLE */}
        {!loading && (
          <table className="w-full bg-white rounded shadow">
            <thead>
              <tr>
                <th onClick={() => handleSort("id")}>ID</th>
                <th onClick={() => handleSort("name")}>Name</th>
                <th>Status</th>
                <th onClick={() => handleSort("score")}>Score</th>
                <th>Date</th>
              </tr>
            </thead>

            <tbody>
              {risks.map((r) => (
                <tr key={r.id} className="text-center border-t">
                  <td>{r.id}</td>
                  <td>{r.name}</td>
                  <td>{r.status}</td>
                  <td>{r.score}</td>
                  <td>{r.date}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {/* PAGINATION */}
        <div className="flex justify-center gap-4 mt-4">
          <button disabled={page === 0} onClick={() => setPage(page - 1)}>
            Prev
          </button>

          <span>Page {page + 1}</span>

          <button
            disabled={page === totalPages - 1}
            onClick={() => setPage(page + 1)}
          >
            Next
          </button>
        </div>

      </div>
    </div>
  );
}