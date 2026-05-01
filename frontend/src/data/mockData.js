// 🔐 LOGIN USERS
export const users = [
  {
    email: "test@gmail.com",
    password: "1234",
    token: "demo-token"
  }
];

// 📊 RISKS LIST (Extended)
export const risks = [
  { id: 1, name: "Data Breach", status: "High", score: 90, date: "2026-04-01" },
  { id: 2, name: "API Failure", status: "Medium", score: 60, date: "2026-04-02" },
  { id: 3, name: "Unauthorized Access", status: "High", score: 85, date: "2026-04-03" },
  { id: 4, name: "Server Crash", status: "Medium", score: 70, date: "2026-04-04" },
  { id: 5, name: "Backup Failure", status: "Low", score: 40, date: "2026-04-05" },
  { id: 6, name: "Payment Failure", status: "High", score: 88, date: "2026-04-06" },
  { id: 7, name: "Latency Issue", status: "Low", score: 30, date: "2026-04-07" },
  { id: 8, name: "Database Lock", status: "Medium", score: 65, date: "2026-04-08" },

  { id: 9, name: "Phishing Attack", status: "High", score: 92, date: "2026-04-09" },
  { id: 10, name: "System Downtime", status: "Medium", score: 75, date: "2026-04-10" },
  { id: 11, name: "Credential Leak", status: "High", score: 89, date: "2026-04-11" },
  { id: 12, name: "Network Congestion", status: "Low", score: 35, date: "2026-04-12" },
  { id: 13, name: "Malware Infection", status: "High", score: 91, date: "2026-04-13" },
  { id: 14, name: "Cloud Misconfiguration", status: "Medium", score: 68, date: "2026-04-14" },
  { id: 15, name: "DNS Failure", status: "Low", score: 42, date: "2026-04-15" },

  { id: 16, name: "Insider Threat", status: "High", score: 87, date: "2026-04-16" },
  { id: 17, name: "Service Timeout", status: "Medium", score: 66, date: "2026-04-17" },
  { id: 18, name: "Hardware Failure", status: "Low", score: 38, date: "2026-04-18" },
  { id: 19, name: "Data Corruption", status: "High", score: 93, date: "2026-04-19" },
  { id: 20, name: "Load Balancer Error", status: "Medium", score: 72, date: "2026-04-20" },

  { id: 21, name: "Encryption Failure", status: "High", score: 90, date: "2026-04-21" },
  { id: 22, name: "Session Hijacking", status: "High", score: 88, date: "2026-04-22" },
  { id: 23, name: "Cache Overflow", status: "Low", score: 33, date: "2026-04-23" },
  { id: 24, name: "Firewall Misrule", status: "Medium", score: 64, date: "2026-04-24" },
  { id: 25, name: "Unauthorized API Access", status: "High", score: 91, date: "2026-04-25" },

  { id: 26, name: "Disk Space Exhaustion", status: "Low", score: 28, date: "2026-04-26" },
  { id: 27, name: "Service Misconfiguration", status: "Medium", score: 67, date: "2026-04-27" },
  { id: 28, name: "Token Expiry Issue", status: "Low", score: 36, date: "2026-04-28" },
  { id: 29, name: "Authentication Failure", status: "High", score: 89, date: "2026-04-29" },
  { id: 30, name: "Third-party API Failure", status: "Medium", score: 74, date: "2026-04-30" },
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

// 🤖 AI RESPONSE (Enhanced)
export const aiResponse = {
  description:
    "This risk highlights a potential vulnerability within the system that could lead to data breaches, service disruption, or unauthorized access if not addressed promptly.",

  impact:
    "High impact. If exploited, it may result in financial loss, reputational damage, and compliance violations.",

  likelihood:
    "Medium to High likelihood based on current system exposure and historical incidents.",

  recommendation: [
    "Implement strict access control mechanisms and role-based permissions.",
    "Perform regular vulnerability assessments and penetration testing.",
    "Enable real-time monitoring and alerting for suspicious activities.",
    "Ensure all sensitive data is encrypted both at rest and in transit."
  ],

  mitigationSteps: [
    "Patch identified vulnerabilities immediately.",
    "Update outdated dependencies and libraries.",
    "Conduct employee awareness training on security best practices.",
    "Introduce multi-factor authentication (MFA)."
  ],

  severity: "High",

  scoreExplanation:
    "The risk score is calculated based on impact severity and likelihood probability. Current score reflects elevated exposure.",

  aiConfidence: "92%",

  generatedAt: "2026-05-01"
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