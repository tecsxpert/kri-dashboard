import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";
import Dashboard from "./pages/Dashboard";
import RiskList from "./pages/RiskList";
import RiskForm from "./pages/RiskForm";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />

        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/risks" element={<RiskList />} />
        <Route path="/create-risk" element={<RiskForm />} />
        <Route path="/edit-risk/:id" element={<RiskForm />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
