import {Link} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';
import './Home.css';

const Home = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div className="home">
      <div className="home-hero">
        <h1>지찬 - 지인찬스</h1>
        <p>전문가 프로필을 기반으로 사용자와 전문가를 연결하는 플랫폼</p>
      </div>

      <div className="home-features">
        <div className="feature-card">
          <h3>신뢰할 수 있는 전문가 검색</h3>
          <p>투명한 연락처 공개 및 로그 관리</p>
        </div>
        <div className="feature-card">
          <h3>사용자 평가 시스템</h3>
          <p>별점을 통한 품질 관리</p>
        </div>
        <div className="feature-card">
          <h3>쉬운 전문가 찾기</h3>
          <p>특기별 검색 및 정렬 기능</p>
        </div>
      </div>

      <div className="home-actions">
        {isAuthenticated ? (
          <>
            <Link to="/profile" className="btn btn-primary">
              전문가 검색하기
            </Link>
            <Link to="/contacts" className="btn btn-secondary">
              나의 지인
            </Link>
          </>
        ) : (
          <>
            <Link to="/signup" className="btn btn-primary">
              회원가입
            </Link>
            <Link to="/login" className="btn btn-secondary">
              로그인
            </Link>
            <Link to="/profile" className="btn btn-outline">
              전문가 검색
            </Link>
          </>
        )}
      </div>
    </div>
  );
};

export default Home;
