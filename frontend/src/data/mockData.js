// 🔐 LOGIN USERS
export const users = [
  {
    email: "test@gmail.com",
    password: "1234",
    token: "demo-token"
  }
];

// 📊 RISKS LIST
export const risks = [
  { id: 1, name: "Data Breach", status: "High", score: 90, date: "2026-04-01" },
  { id: 2, name: "API Failure", status: "Medium", score: 60, date: "2026-04-02" },
  { id: 3, name: "Unauthorized Access", status: "High", score: 85, date: "2026-04-03" },
  { id: 4, name: "Server Crash", status: "Medium", score: 70, date: "2026-04-04" },
  { id: 5, name: "Backup Failure", status: "Low", score: 40, date: "2026-04-05" },
  { id: 6, name: "Payment Failure", status: "High", score: 88, date: "2026-04-06" },
  { id: 7, name: "Latency Issue", status: "Low", score: 30, date: "2026-04-07" },
  { id: 8, name: "Database Lock", status: "Medium", score: 65, date: "2026-04-08" },
];

// 📊 DASHBOARD STATS
export const stats = {
  total: 8,
  high: 3,
  medium: 3,
  low: 2,
  byStatus: [
    { name: "High", value: 3 },
    { name: "Medium", value: 3 },
    { name: "Low", value: 2 }
  ]
};

// 🤖 AI RESPONSE
export const aiResponse = {
  description:
    "This risk indicates potential system vulnerability. Immediate action is recommended to reduce impact and improve security posture."
};
// 📈 ANALYTICS DATA
export const analyticsData = {
  byCategory: [
    { name: "Security", value: 5 },
    { name: "Performance", value: 3 },
    { name: "Compliance", value: 2 },
  ],

  byStatus: [
    { name: "High", value: 3 },
    { name: "Medium", value: 3 },
    { name: "Low", value: 2 },
  ],

  overTime: [
    { month: "Jan", value: 2 },
    { month: "Feb", value: 3 },
    { month: "Mar", value: 4 },
    { month: "Apr", value: 5 },
    { month: "May", value: 3 },
    { month: "Jun", value: 6 },
  ]
};