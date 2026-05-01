import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { users } from "../data/mockData";

export default function Register() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    phone: "",
    email: "",
    gender: "",
    dob: "",
    password: "",
    confirmPassword: ""
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // ✅ Validation
  const validate = () => {
    let newErrors = {};

    if (!form.name.trim()) newErrors.name = "Name is required";
    if (!form.phone) newErrors.phone = "Phone is required";
    if (!form.email) newErrors.email = "Email is required";

    if (!form.gender) newErrors.gender = "Select gender";

    if (!form.dob) newErrors.dob = "Date of birth required";

    if (!form.password) newErrors.password = "Password is required";

    if (!form.confirmPassword)
      newErrors.confirmPassword = "Confirm your password";
    else if (form.password !== form.confirmPassword)
      newErrors.confirmPassword = "Passwords do not match";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // 🚀 Submit
  const handleSubmit = (e) => {
    e.preventDefault();

    if (!validate()) return;

    const newUser = {
      email: form.email,
      password: form.password,
      token: "demo-token-" + Date.now()
    };

    users.push(newUser);

    alert("Registered successfully 💙");
    navigate("/");
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-[#E3F2FD]">
      <form className="bg-white p-8 rounded-2xl shadow-lg w-96" onSubmit={handleSubmit}>

        <h2 className="text-2xl font-bold text-[#1B4F8A] mb-6 text-center">
          REGISTER
        </h2>

        {/* Name */}
        <input
          name="name"
          placeholder="Full Name"
          value={form.name}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300"
        />
        {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}

        {/* Phone */}
        <input
          name="phone"
          placeholder="Phone Number"
          value={form.phone}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300"
        />
        {errors.phone && <p className="text-red-500 text-sm">{errors.phone}</p>}

        {/* Email */}
        <input
          name="email"
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300"
        />
        {errors.email && <p className="text-red-500 text-sm">{errors.email}</p>}

        {/* ✅ Gender (MOVED HERE) */}
        <select
          name="gender"
          value={form.gender}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300"
        >
          <option value="">Select Gender</option>
          <option>Male</option>
          <option>Female</option>
        </select>
        {errors.gender && <p className="text-red-500 text-sm">{errors.gender}</p>}

        {/* DOB */}
        <input
          type="date"
          name="dob"
          value={form.dob}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300"
        />
        {errors.dob && <p className="text-red-500 text-sm">{errors.dob}</p>}

        {/* Password */}
        <input
          type="password"
          name="password"
          placeholder="New Password"
          value={form.password}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300"
        />
        {errors.password && <p className="text-red-500 text-sm">{errors.password}</p>}

        {/* Confirm Password */}
        <input
          type="password"
          name="confirmPassword"
          placeholder="Confirm Password"
          value={form.confirmPassword}
          onChange={handleChange}
          className="w-full p-2 mb-2 border rounded focus:ring-2 focus:ring-blue-300"
        />
        {errors.confirmPassword && (
          <p className="text-red-500 text-sm">{errors.confirmPassword}</p>
        )}

        {/* Submit */}
        <button className="w-full bg-[#1B4F8A] text-white p-2 rounded-lg hover:bg-blue-700">
          Register
        </button>

        {/* Login link */}
        <p
          onClick={() => navigate("/")}
          className="text-sm text-blue-600 mt-4 cursor-pointer text-center hover:underline"
        >
          Already have an account? Login
        </p>
      </form>
    </div>
  );
}