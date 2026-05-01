import { useState } from "react";
import Navbar from "../components/Navbar";

export default function Report() {
  const [data, setData] = useState("");
  const [loading, setLoading] = useState(false);

  // 🧠 Dummy AI report text
  const fullReport = `
📊 AI Risk Analysis Report

- High risk areas detected in system security and payment modules.
- API failures increased by 20% over the last month.
- Performance-related risks are stable but require monitoring.
- Backup failures show improvement compared to previous period.

✅ Recommendations:
- Strengthen authentication mechanisms
- Improve API error handling
- Monitor database performance regularly
- Automate backup verification

🚀 Overall Risk Score: MEDIUM → trending towards HIGH
`;

  // 🔥 Simulate streaming (typing effect)
  const generateReport = () => {
    setData("");
    setLoading(true);

    let index = 0;

    const interval = setInterval(() => {
      setData((prev) => prev + fullReport[index]);
      index++;

      if (index === fullReport.length) {
        clearInterval(interval);
        setLoading(false);
      }
    }, 20); // speed of typing
  };

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6 max-w-3xl mx-auto">

        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-4">
          🤖 AI Report
        </h2>

        {/* 🔘 BUTTON */}
        {!loading && data === "" && (
          <button
            onClick={generateReport}
            className="bg-[#1B4F8A] text-white px-5 py-2 rounded-lg hover:bg-blue-700 mb-4"
          >
            Generate Report
          </button>
        )}

        {/* 🔄 LOADING */}
        {loading && (
          <div className="flex items-center gap-3 text-blue-600 mb-4">
            <div className="w-5 h-5 border-4 border-blue-400 border-t-transparent rounded-full animate-spin"></div>
            <span>Generating report...</span>
          </div>
        )}

        {/* 📄 REPORT OUTPUT */}
        <div className="bg-white p-5 rounded-xl shadow whitespace-pre-wrap border border-blue-100 min-h-[200px]">
          {data || "Click 'Generate Report' to view AI insights..."}
        </div>

        {/* 🔁 RETRY */}
        {data && !loading && (
          <button
            onClick={generateReport}
            className="mt-4 bg-blue-400 text-white px-4 py-2 rounded hover:bg-blue-500"
          >
            Regenerate Report
          </button>
        )}

      </div>
    </div>
  );
}