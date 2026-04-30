import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";

export default function Report() {
  const [data, setData] = useState("");

  useEffect(() => {
    const eventSource = new EventSource(
      "http://localhost:5000/generate-report"
    );

    eventSource.onmessage = (event) => {
      setData((prev) => prev + event.data);
    };

    eventSource.onerror = () => {
      eventSource.close();
    };

    return () => eventSource.close();
  }, []);

  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6">
        <h2 className="text-xl font-bold text-[#1B4F8A] mb-4">
          AI Report (Live)
        </h2>

        <div className="bg-white p-4 rounded shadow whitespace-pre-wrap">
          {data || "Generating report..."}
        </div>
      </div>
    </div>
  );
}