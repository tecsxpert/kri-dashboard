import { useEffect, useState } from "react";
import API from "../services/api";

function RiskList() {
  const [risks, setRisks] = useState([]);

  useEffect(() => {
    API.get("/api/risks")
      .then(res => setRisks(res.data))
      .catch(err => console.log(err));
  }, []);

  return (
    <div className="p-5">
      <h1 className="text-xl mb-3">Risks</h1>

      {risks.map(r => (
        <div key={r.id} className="border p-2 mb-2">
          {r.title}
        </div>
      ))}
    </div>
  );
}

export default RiskList;