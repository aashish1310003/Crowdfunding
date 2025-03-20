import axios from "axios";
import { toast, Bounce } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { BASE_URL } from "./api";

// Create Axios instance
const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
});

// Global error handling interceptor
axiosInstance.interceptors.response.use(
  (response) => response, // ✅ Return response as-is if successful
  (error) => {
    const status = error.response?.status;
    const message =
      error.response?.data?.message ||
      `Error ${status || ""}: ${error.message}`;

    // 🚀 Show toast notification for any API error
    toast.error(`❌ ${message}`, {
      position: "top-right",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: false,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "light",
      transition: Bounce,
    });

    return Promise.reject(error);
  }
);

export default axiosInstance;
