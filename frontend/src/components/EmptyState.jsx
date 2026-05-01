export default function EmptyState({ message = "No data found" }) {
  return (
    <div className="bg-white p-6 rounded-xl shadow text-center">
      <p className="text-gray-500 text-lg">📭 {message}</p>
    </div>
  );
}