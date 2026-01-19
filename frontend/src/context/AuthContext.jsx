import {createContext, useContext, useEffect, useState} from 'react';
import axios from 'axios';
import api, {clearAccessToken, setAccessToken} from '../utils/api';

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 앱 초기 로드 시 accessToken이 없으면 refreshToken으로 갱신 시도
    const initializeAuth = async () => {
      try {
        // refreshToken으로 accessToken 갱신 시도 (axios 직접 사용으로 인터셉터 회피)
        const response = await axios.post('/api/auth/token_refresh', {}, {
          withCredentials: true
        });
        const { accessToken } = response.data;
        setAccessToken(accessToken);
        setIsAuthenticated(true);
      } catch (error) {
        // refreshToken이 없거나 유효하지 않으면 인증되지 않은 상태
        setIsAuthenticated(false);
      } finally {
        setLoading(false);
      }
    };

    initializeAuth();
  }, []);

  const login = (accessToken) => {
    // accessToken을 메모리 변수에 저장
    // refreshToken은 서버에서 httpOnly 쿠키로 설정되므로 프론트엔드에서는 처리하지 않음
    setAccessToken(accessToken);
    setIsAuthenticated(true);
  };

  const logout = async () => {
    try {
      await api.post('/auth/logout');
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // accessToken 메모리에서 제거
      // refreshToken 쿠키는 서버에서 삭제됨
      clearAccessToken();
      setIsAuthenticated(false);
    }
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};


