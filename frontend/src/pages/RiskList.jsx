import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import API from "../services/api";

export default function RiskList() {
  const navigate = useNavigate();

  const [risks, setRisks] = useState([]);
  const [loading, setLoading] = useState(true);

  // Pagination
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  // Sorting
  const [sortBy, setSortBy] = useState("id");
  const [sortDir, setSortDir] = useState("asc");

  useEffect(() => {
    fetchRisks();
  }, [page, sortBy, sortDir]);

  const fetchRisks = async () => {
    setLoading(true);
    try {
      const res = await API.get("/all", {
        params: {
          page,
          size: 5,
          sortBy,
          sortDir,
        },
      });

      setRisks(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Sorting handler
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
        {/* Title + Create */}
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

        {/* Empty */}
        {!loading && risks.length === 0 && (
          <div className="bg-white p-4 rounded shadow text-center">
            <p>No risks available</p>
          </div>
        )}

        {/* Table */}
        {!loading && risks.length > 0 && (
          <table className="w-full bg-white rounded-xl shadow">
            <thead className="bg-blue-200">
              <tr>
                <th
                  className="p-2 cursor-pointer"
                  onClick={() => handleSort("id")}
                >
                  ID 🔽
                </th>
                <th
                  className="cursor-pointer"
                  onClick={() => handleSort("name")}
                >
                  Name 🔽
                </th>
                <th
                  className="cursor-pointer"
                  onClick={() => handleSort("status")}
                >
                  Status 🔽
                </th>
                <th
                  className="cursor-pointer"
                  onClick={() => handleSort("score")}
                >
                  Score 🔽
                </th>
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

                  {/* Status badge */}
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

                  {/* Edit button */}
                  <td>
                    <button
                      onClick={(e) => {
                        e.stopPropagation(); // prevent row click
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

        {/* Pagination */}
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