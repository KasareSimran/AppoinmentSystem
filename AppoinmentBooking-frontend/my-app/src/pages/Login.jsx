import { useState } from "react";
import { loginUser } from "../api/authAPI";

const Login = () => {

  const [form, setForm] = useState({
    phone: "",
    password: ""
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const token = await loginUser(form);

      localStorage.setItem("token", token);

      alert("Login Successful ✅");

      window.location.href = "/dashboard";

    } catch (err) {
      setError(err.response?.data?.message || "Login Failed");
    }
  };

  return (
    <div className="h-screen flex items-center justify-center bg-[#e7a9a1]">

      <div className="flex w-[800px] bg-white rounded-xl shadow-lg overflow-hidden">

        {/* LEFT */}
        <div className="w-1/2 flex items-center justify-center bg-[#f5f5f5]">
          <img
            src="https://cdn.dribbble.com/users/14268/screenshots/3132047/media/fc9c68f17d27c9c41f7c0c6a6b7cda3c.png"
            alt="illustration"
            className="w-72"
          />
        </div>

        {/* RIGHT */}
        <div className="w-1/2 p-10 flex flex-col justify-center">

          <h2 className="text-3xl font-semibold mb-6 text-gray-700">
            Login
          </h2>

          {error && (
            <p className="text-red-500 mb-4">{error}</p>
          )}

          <form onSubmit={handleLogin} className="space-y-5">

            <input
              type="text"
              name="phone"
              placeholder="Phone Number"
              value={form.phone}
              onChange={handleChange}
              className="w-full border-b-2 outline-none py-2"
              required
            />

            <input
              type="password"
              name="password"
              placeholder="Password"
              value={form.password}
              onChange={handleChange}
              className="w-full border-b-2 outline-none py-2"
              required
            />

            <button
              type="submit"
              className="bg-orange-400 hover:bg-orange-500 text-white px-6 py-2 rounded-full w-full"
            >
              Sign in
            </button>

          </form>

          <p className="text-sm mt-4 text-gray-500 cursor-pointer">
            Forgot password?
          </p>

        </div>
      </div>
    </div>
  );
};

export default Login;
