export const refreshAccessToken = async () => {
  const refreshToken = localStorage.getItem('refreshToken');
  if (!refreshToken) throw new Error('No refresh token');

  const res = await axios.post('http://localhost:8080/api/users/refresh-token', {
    refreshToken,
  });

  const newToken = res.data.token;
  localStorage.setItem('token', newToken);
  return newToken;
};
