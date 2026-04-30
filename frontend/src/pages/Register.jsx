import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function Register() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    phone: "",
    email: "",
    dob: "",
    password: "",
    gender: ""
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(form);
    alert("Registered successfully (demo)");
    navigate("/");
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-[#E3F2FD]">
      <form onSubmit={handleSubmit} className="bg-white p-8 rounded shadow w-96">
        <h2 className="text-xl font-bold text-[#1B4F8A] mb-4 text-center">Register</h2>

        <input name="name" placeholder="Name" onChange={handleChange} className="w-full p-2 mb-2 border rounded" />
        <input name="phone" placeholder="Phone Number" onChange={handleChange} className="w-full p-2 mb-2 border rounded" />
        <input name="email" placeholder="Email" onChange={handleChange} className="w-full p-2 mb-2 border rounded" />
        
        <input type="date" name="dob" onChange={handleChange} className="w-full p-2 mb-2 border rounded" />

        <input type="password" name="password" placeholder="Password" onChange={handleChange} className="w-full p-2 mb-2 border rounded" />

        <select name="gender" onChange={handleChange} className="w-full p-2 mb-3 border rounded">
          <option value="">Select Gender</option>
          <option>Male</option>
          <option>Female</option>
        </select>

        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded">Register</button>

        <p onClick={() => navigate("/")} className="text-sm text-blue-600 mt-3 cursor-pointer text-center">
          Already have account? Login
        </p>
      </form>
    </div>
  );
}