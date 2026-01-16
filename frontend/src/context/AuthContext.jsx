import {createContext, useContext, useEffect, useState} from 'react';
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
    // accessToken은 메모리에만 저장되므로 페이지 새로고침 시 초기 상태는 false
    // refreshToken은 httpOnly 쿠키에 저장되어 JavaScript에서 접근 불가
    // 초기 인증 상태는 서버에 요청을 보내서 확인하거나, 로그인 시에만 true로 설정
    setIsAuthenticated(false);
    setLoading(false);
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


