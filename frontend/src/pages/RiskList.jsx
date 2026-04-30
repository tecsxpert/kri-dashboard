import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import API from "../services/api";

export default function RiskList() {
  const navigate = useNavigate();

  const [risks, setRisks] = useState([]);
  const [loading, setLoading] = useState(true);

  // 🆕 Pagination states
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  // 🆕 Sorting states
  const [sortBy, setSortBy] = useState("id");
  const [sortDir, setSortDir] = useState("asc");

  // 🔄 Fetch whenever page/sort changes
  useEffect(() => {
    fetchRisks();
  }, [page, sortBy, sortDir]);

  const fetchRisks = async () => {
    setLoading(true);
    try {
      const res = await API.get("/all", {
        params: {
          page: page,
          size: 5,
          sortBy: sortBy,
          sortDir: sortDir
        }
      });

      setRisks(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      console.error("Error:", err);
    } finally {
      setLoading(false);
    }
  };

  // 🔽 Handle sorting
  const handleSort = (field) => {
    if (sortBy === field) {
      setSortDir(sortDir === "asc" ? "desc" : "asc");
    } else {
      setSortBy(field);
      setSortDir("asc");
    }
  };

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6">
        {/* Title + Button */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold text-[#1B4F8A]">
            Risk List
          </h2>

          <button
            onClick={() => navigate("/create-risk")}
            className="bg-[#1B4F8A] text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            + Create Risk
          </button>
        </div>

        {/* Loading */}
        {loading && <p>Loading...</p>}

        {/* Empty */}
        {!loading && risks.length === 0 && (
          <p>No data available</p>
        )}

        {/* Table */}
        {!loading && risks.length > 0 && (
          <table className="w-full bg-white rounded shadow">
            <thead className="bg-blue-200">
              <tr>
                <th className="p-2 cursor-pointer" onClick={() => handleSort("id")}>
                  ID 🔽
                </th>
                <th onClick={() => handleSort("name")} className="cursor-pointer">
                  Name 🔽
                </th>
                <th onClick={() => handleSort("status")} className="cursor-pointer">
                  Status 🔽
                </th>
                <th onClick={() => handleSort("score")} className="cursor-pointer">
                  Score 🔽
                </th>
                <th>Date</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {risks.map((r) => (
                <tr key={r.id} className="text-center border-t">
                  <td className="p-2">{r.id}</td>
                  <td>{r.name}</td>
                  <td>{r.status}</td>
                  <td>{r.score}</td>
                  <td>{r.date}</td>

                  <td>
                    <button
                      onClick={() => navigate(`/edit-risk/${r.id}`)}
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

        {/* 🔢 Pagination Controls */}
        <div className="flex justify-center items-center mt-4 gap-4">
          <button
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
            className="bg-blue-300 px-3 py-1 rounded disabled:opacity-50"
          >
            Prev
          </button>

          <span className="text-[#1B4F8A] font-bold">
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