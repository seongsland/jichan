import {Link} from 'react-router-dom';
import './Home.css';

const Home = () => {
  return (
    <div className="home">
      <section className="home-hero">
        <div className="hero-content">
          <h1>아는 사람 없을 때, <br />당황하지 말고 <span className="highlight">지인찬스</span></h1>
          <p className="hero-subtitle">
            누구에게 물어봐야 할지 막막한 순간이 있나요?<br />
            내 곁의 실용적인 도움부터 전문적인 조언까지,<br />
            직접 연락하고 자유롭게 조율하는 나만의 든든한 지인을 만나보세요.
          </p>
          <div className="hero-actions">
            <>
              <Link to="/profile" className="btn btn-primary btn-lg">
                내게 필요한 지인 찾기
              </Link>
              <Link to="/user" className="btn btn-secondary btn-lg">
                나도 지인으로 등록하기
              </Link>
            </>
          </div>
        </div>
      </section>

      <section className="home-features">
        <div className="section-header">
          <h2>지찬은 이렇게 다릅니다</h2>
        </div>
        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">📞</div>
            <h3>직접 소통의 자유</h3>
            <p>복잡한 단계 없이 바로 연락하세요.<br/>지인과 직접 소통하고<br/>비용과 일정을 자유롭게 협의할 수 있습니다.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🙋</div>
            <h3>누구나 지인이 되는 곳</h3>
            <p>도움을 줄 분야와 비용을 등록하고<br/>연락처를 남겨보세요.<br/>나의 작은 노하우가 가치 있게 쓰입니다.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">⭐</div>
            <h3>별점으로 전하는 감사</h3>
            <p>도움이 되셨나요?<br/>정성스러운 별점으로<br/>지인에게 고마운 마음을 남겨보세요.</p>
          </div>
        </div>
      </section>

      <section className="home-philosophy">
        <div className="philosophy-content">
          <h2>지찬은 연결의 가치에만 집중합니다.</h2>
          <p>우리는 두 사람의 대화나 거래에 개입하지 않습니다.</p>
          <p>중개 수수료 없이 서로가 정한 합리적인 비용으로 가장 자유롭고 편안하게 도움을 주고받으세요.</p>
        </div>
      </section>
    </div>
  );
};

export default Home;
