import {Link, useLocation, useNavigate} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';
import './Layout.css';
import logo_mark from '../assets/logo_mark.gif';
import {useMessage} from "../context/MessageContext";

const Layout = ({children}) => {
    const {isAuthenticated, logout} = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const {showMessage} = useMessage();

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

    const handleEmailClick = (email) => {
        navigator.clipboard.writeText(email)
            .then(() => {
                showMessage('success', '이메일 주소가 클립보드에 복사되었습니다.');
            })
            .catch(err => {
                console.error('이메일 복사 실패:', err);
                showMessage('error', '이메일 복사에 실패했습니다.');
            });
    };

    return (
        <div className="layout">
            <header className="header">
                <Link to="/" className="logo">
                    <img src={logo_mark} alt="지찬 로고" className="logo-mark" width="35"/>
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
                        <h3><img src={logo_mark} alt="지찬 로고" width="23px" className="logo-mark"/>지찬</h3>
                        <p>자유롭게 직접 소통하는<br/>전문가 매칭 플랫폼</p>
                    </div>
                    <div className="footer-section">
                        <h4>서비스 문의</h4>
                        <p>
                            이메일: <span
                            onClick={() => handleEmailClick('help@jichan.site')}
                            style={{cursor: 'pointer', textDecoration: 'underline'}}
                            title="클릭하여 이메일 주소 복사"
                        >
                            help@jichan.site
                        </span>
                        </p>
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
