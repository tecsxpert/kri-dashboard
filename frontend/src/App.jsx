import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";

import Dashboard from "./pages/Dashboard";
import RiskList from "./pages/RiskList";
import RiskForm from "./pages/RiskForm";
import RiskDetail from "./pages/RiskDetail";
import Report from "./pages/Report";
import Analytics from "./pages/Analytics";

import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Public */}
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/report" element={<Report />} />
        <Route path="/analytics" element={<Analytics />} />

        {/* Protected */}
        <Route path="/dashboard" element={
          <ProtectedRoute><Dashboard /></ProtectedRoute>
        } />

        <Route path="/risks" element={
          <ProtectedRoute><RiskList /></ProtectedRoute>
        } />

        <Route path="/create-risk" element={
          <ProtectedRoute><RiskForm /></ProtectedRoute>
        } />

        <Route path="/edit-risk/:id" element={
          <ProtectedRoute><RiskForm /></ProtectedRoute>
        } />

        <Route path="/risks/:id" element={
          <ProtectedRoute><RiskDetail /></ProtectedRoute>
        } />

      </Routes>
    </BrowserRouter>
  );
}

export default App;