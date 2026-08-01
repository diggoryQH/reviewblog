import axios from "axios";
import Cookies from "js-cookie";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api";
const api = axios.create({
  baseURL: API_URL,
  withCredentials: true,
});

// Tự động gắn JWT token vào header Authorization của MỌI request nếu đã đăng nhập
api.interceptors.request.use((config) => {
  const token = Cookies.get("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// ===== Types (khớp với PostResponse, CommentResponse... bên backend) =====
export interface Post {
  id: number;
  title: string;
  slug: string;
  content: string;
  summary: string;
  coverImageUrl: string;
  type: "BOOK" | "MOVIE";
  averageRating: number;
  viewCount: number;
  categoryName: string;
  authorName: string;
  tags: string[];
  createdAt: string;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  type: "BOOK" | "MOVIE";
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
}

export interface Comment {
  id: number;
  content: string;
  username: string;
  userAvatar: string;
  createdAt: string;
}
export default api;
