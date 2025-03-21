import axios from "axios";
import { toast } from "react-toastify";
import ExpandableToast from "./ExpandableToast"; // Import component
import { BASE_URL } from "../api/api";

let token = localStorage.getItem("token");

const headers = {
  "Content-Type": "application/json",
};

if (token) {
  headers["Authorization"] = `Bearer ${token}`;
}
const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: headers,
});

// Global Error Handling Middleware
axiosInstance.interceptors.response.use(
  (response) => response, // Pass through successful responses
  (error) => {
    const status = error.response?.status;
    const responseData = error.response?.data || {};

    const message = responseData.message || `Error ${status || ""}`;
    const details = responseData.data || ""; // Extract the "data" field

    toast.error(<ExpandableToast message={message} details={details} />);

    return Promise.reject(error);
  }
);

export default axiosInstance;
