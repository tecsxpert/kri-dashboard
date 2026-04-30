import Navbar from "../components/Navbar";

export default function Dashboard() {
  return (
    <div className="bg-[#E3F2FD] min-h-screen">
      <Navbar />

      <div className="p-6">
        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-4">Dashboard</h2>

        <div className="grid grid-cols-3 gap-4">
          <div className="bg-white p-4 rounded shadow">
            <p>Total Risks</p>
            <h3 className="text-xl text-blue-600">24</h3>
          </div>

          <div className="bg-white p-4 rounded shadow">
            <p>High Risks</p>
            <h3 className="text-xl text-red-500">5</h3>
          </div>

          <div className="bg-white p-4 rounded shadow">
            <p>Resolved</p>
            <h3 className="text-xl text-green-500">12</h3>
          </div>
        </div>
      </div>
    </div>
  );
}