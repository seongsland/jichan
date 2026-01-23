import {Link, useNavigate, useLocation} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';
import './Layout.css';

const Layout = ({ children }) => {
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  const handleNavClick = (e, path) => {
    // 현재 페이지와 같은 경로를 클릭했을 때
    if (location.pathname === path) {
      e.preventDefault();
      // 새로고침 대신 스크롤만 최상단으로 이동
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    }
  };

  return (
    <div className="layout">
      <header className="header">
        <Link to="/" className="logo">
          지찬 - <span className="logo-highlight">지인찬스</span>
        </Link>
        <nav className="nav">
          {isAuthenticated ? (
            <>
              <Link 
                to="/profile" 
                className={`nav-link ${location.pathname === '/profile' ? 'active' : ''}`}
                onClick={(e) => handleNavClick(e, '/profile')}
              >
                전문가 검색
              </Link>
              <Link 
                to="/contacts" 
                className={`nav-link ${location.pathname === '/contacts' ? 'active' : ''}`}
                onClick={(e) => handleNavClick(e, '/contacts')}
              >
                나의 지인
              </Link>
              <Link 
                to="/user" 
                className={`nav-link ${location.pathname === '/user' ? 'active' : ''}`}
                onClick={(e) => handleNavClick(e, '/user')}
              >
                프로필 관리
              </Link>
              <button onClick={handleLogout} className="nav-btn btn-logout">
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link 
                to="/login" 
                className={`nav-link ${location.pathname === '/login' ? 'active' : ''}`}
                onClick={(e) => handleNavClick(e, '/login')}
              >
                로그인
              </Link>
              <Link to="/signup" className="nav-btn btn-signup">회원가입</Link>
            </>
          )}
        </nav>
      </header>
      <main className="main-content">{children}</main>
      <footer className="footer">
        <div className="footer-content">
          <div className="footer-section">
            <h3>지찬</h3>
            <p>신뢰할 수 있는 지인 기반<br/>전문가 매칭 플랫폼</p>
          </div>
          <div className="footer-section">
            <h4>서비스 문의</h4>
            <p>이메일: seongsland@gmail.com</p>
            <p>문의 확인 후 순차적으로 답변드립니다.</p>
          </div>
          <div className="footer-section">
            <h4>바로가기</h4>
            <Link to="/terms">이용약관</Link>
            <Link to="/privacy">개인정보처리방침</Link>
          </div>
        </div>
        <div className="footer-bottom">
          <p>&copy; {new Date().getFullYear()} Jichan. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;
