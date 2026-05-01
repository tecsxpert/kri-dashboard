export default function Skeleton({ rows = 5 }) {
  return (
    <div className="space-y-3">
      {[...Array(rows)].map((_, i) => (
        <div
          key={i}
          className="h-6 bg-gray-300 rounded animate-pulse"
        ></div>
      ))}
    </div>
  );
}