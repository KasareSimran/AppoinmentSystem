import API from "./axios";

// ✅ LOGIN API
export const loginUser = async (data) => {
  const response = await API.post("/auth/login", data);
  return response.data;
};

// ✅ REGISTER (future use)
export const registerUser = async (data) => {
  const response = await API.post("/auth/register", data);
  return response.data;
};
