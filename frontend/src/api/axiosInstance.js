import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add the interceptor
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Check if token expired and we haven't retried yet
  if ((error.response.status === 401 || error.response.status === 403) && !originalRequest._retry) {
      originalRequest._retry = true;
    
      try {
        const refreshToken = localStorage.getItem('refreshToken');
                console.log("Trying to refresh token with:", refreshToken);


        const res = await axios.post('http://localhost:8080/api/users/refresh-token', {
        refreshToken:  refreshToken,
        });

        const newAccessToken = res.data.token;
        console.log('New access token:', newAccessToken);
        localStorage.setItem('token', newAccessToken);

        // Update the original request with new token
        originalRequest.headers['Authorization'] = 'Bearer ' + newAccessToken;

        return axiosInstance(originalRequest);
      } catch (err) {
        console.error('Refresh failed. Redirecting to login.');
        window.location.href = '/login'; // or use navigate()
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
