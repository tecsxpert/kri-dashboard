import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import API from "../services/api";

export default function RiskForm() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [form, setForm] = useState({
    name: "",
    status: "",
    score: "",
    date: ""
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  // 🔄 Load data if editing
  useEffect(() => {
    if (id) fetchRisk();
  }, [id]);

  const fetchRisk = async () => {
    try {
      const res = await API.get(`/risks/${id}`);
      setForm(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  // 🔁 Controlled input handler
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // ✅ Validation
  const validate = () => {
    let newErrors = {};

    if (!form.name.trim()) newErrors.name = "Name is required";

    if (!form.status) newErrors.status = "Select a status";

    if (!form.score) newErrors.score = "Score is required";
    else if (form.score < 0 || form.score > 100)
      newErrors.score = "Score must be between 0–100";

    if (!form.date) newErrors.date = "Date is required";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // 🚀 Submit
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) return;

    setLoading(true);

    try {
      if (id) {
        await API.put(`/risks/${id}`, form);
      } else {
        await API.post("/risks", form);
      }

      alert("Saved successfully 💙");
      navigate("/risks");
    } catch (err) {
      console.error(err);
      alert("Error saving data");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="flex justify-center items-center p-6">
        <form
          onSubmit={handleSubmit}
          className="bg-white p-8 rounded-xl shadow-lg w-96"
        >
          <h2 className="text-2xl font-bold text-[#1B4F8A] mb-6 text-center">
            {id ? "Edit Risk" : "Create Risk"}
          </h2>

          {/* Name */}
          <div className="mb-3">
            <input
              name="name"
              placeholder="Risk Name"
              value={form.name}
              onChange={handleChange}
              className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-300"
            />
            {errors.name && (
              <p className="text-red-500 text-sm">{errors.name}</p>
            )}
          </div>

          {/* Status */}
          <div className="mb-3">
            <select
              name="status"
              value={form.status}
              onChange={handleChange}
              className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-300"
            >
              <option value="">Select Status</option>
              <option>Low</option>
              <option>Medium</option>
              <option>High</option>
            </select>
            {errors.status && (
              <p className="text-red-500 text-sm">{errors.status}</p>
            )}
          </div>

          {/* Score */}
          <div className="mb-3">
            <input
              type="number"
              name="score"
              placeholder="Score (0-100)"
              value={form.score}
              onChange={handleChange}
              className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-300"
            />
            {errors.score && (
              <p className="text-red-500 text-sm">{errors.score}</p>
            )}
          </div>

          {/* Date */}
          <div className="mb-3">
            <input
              type="date"
              name="date"
              value={form.date}
              onChange={handleChange}
              className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-300"
            />
            {errors.date && (
              <p className="text-red-500 text-sm">{errors.date}</p>
            )}
          </div>

          {/* Submit */}
          <button
            disabled={loading}
            className="w-full bg-[#1B4F8A] text-white p-2 mt-4 rounded hover:bg-blue-700 transition"
          >
            {loading ? "Saving..." : "Submit"}
          </button>

          {/* Back */}
          <p
            onClick={() => navigate("/risks")}
            className="text-sm text-blue-600 mt-4 cursor-pointer text-center hover:underline"
          >
            Back to Risk List
          </p>
        </form>
      </div>
    </div>
  );
}