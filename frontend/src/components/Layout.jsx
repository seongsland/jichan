import {Link, useNavigate} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';
import './Layout.css';

const Layout = ({ children }) => {
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <div className="layout">
      <header className="header">
        <Link to="/" className="logo">
          지찬 - 지인찬스
        </Link>
        <nav className="nav">
          {isAuthenticated ? (
            <>
              <Link to="/profile">전문가 검색</Link>
              <Link to="/contacts">내가 본 전문가</Link>
              <Link to="/user">프로필 관리</Link>
              <button onClick={handleLogout} className="logout-btn">
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link to="/login">로그인</Link>
              <Link to="/signup">회원가입</Link>
            </>
          )}
        </nav>
      </header>
      <main className="main-content">{children}</main>
    </div>
  );
};

export default Layout;
