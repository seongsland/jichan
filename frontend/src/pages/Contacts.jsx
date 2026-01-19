import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import api from '../utils/api';
import Message from '../components/Message';
import {useAuth} from '../context/AuthContext';
import './Contacts.css';

const Contacts = () => {
  const { isAuthenticated, loading: authLoading } = useAuth();
  const navigate = useNavigate();
  const [contacts, setContacts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ type: '', text: '' });
  const [ratingForms, setRatingForms] = useState({});

  useEffect(() => {
    // 인증 상태 확인 후 로그인되지 않았으면 로그인 페이지로 이동
    if (!authLoading) {
      if (!isAuthenticated) {
        navigate('/login');
        return;
      }
      fetchContacts();
    }
  }, [authLoading, isAuthenticated, navigate]);

  const fetchContacts = async () => {
    setLoading(true);
    try {
      const response = await api.get('/contact');
      setContacts(response.data);
    } catch (error) {
      setMessage({
        type: 'error',
        text: '전문가 목록을 불러오는데 실패했습니다.',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleRatingSubmit = async (expertId, e) => {
    e.preventDefault();
    const formData = ratingForms[expertId] || { score: '', comment: '' };

    try {
      await api.post('/contact/rating', {
        expertId,
        score: parseInt(formData.score),
        comment: formData.comment || null,
      });
      setMessage({
        type: 'success',
        text: '평가가 등록되었습니다.',
      });
      setRatingForms({
        ...ratingForms,
        [expertId]: { score: '', comment: '' },
      });
      fetchContacts();
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || '평가 등록에 실패했습니다.',
      });
    }
  };

  const handleRatingChange = (expertId, field, value) => {
    setRatingForms({
      ...ratingForms,
      [expertId]: {
        ...(ratingForms[expertId] || {}),
        [field]: value,
      },
    });
  };

  if (loading) {
    return <div className="loading">로딩 중...</div>;
  }

  return (
    <div className="contacts">
      <h2>내가 본 전문가 목록</h2>
      <Message
        type={message.type}
        message={message.text}
        onClose={() => setMessage({ type: '', text: '' })}
      />

      {contacts.length === 0 ? (
        <div className="empty-state">
          <p>아직 본 전문가가 없습니다.</p>
          <button
            onClick={() => navigate('/profile')}
            className="btn btn-primary"
          >
            전문가 검색하기
          </button>
        </div>
      ) : (
        <div className="contacts-list">
          {contacts.map((contact) => (
            <div key={contact.expertId} className="contact-card">
              <div className="contact-header">
                <h3>{contact.expertName}</h3>
                {contact.rating && (
                  <div className="rating-badge">
                    별점: {contact.rating}점
                  </div>
                )}
              </div>
              <div className="contact-info">
                {contact.gender && <span>성별: {contact.gender}</span>}
                {contact.region && <span>지역: {contact.region}</span>}
                <div className="contact-views">
                  {contact.hasEmailView && (
                    <span className="view-badge">이메일 확인함</span>
                  )}
                  {contact.hasPhoneView && (
                    <span className="view-badge">핸드폰 확인함</span>
                  )}
                </div>
              </div>
              {contact.introduction && (
                <p className="introduction">{contact.introduction}</p>
              )}

              {!contact.rating && (
                <div className="rating-form">
                  <h4>평가하기</h4>
                  <form
                    onSubmit={(e) => handleRatingSubmit(contact.expertId, e)}
                  >
                    <div className="form-group">
                      <label htmlFor={`score-${contact.expertId}`}>
                        별점 (1-5)
                      </label>
                      <select
                        id={`score-${contact.expertId}`}
                        value={ratingForms[contact.expertId]?.score || ''}
                        onChange={(e) =>
                          handleRatingChange(
                            contact.expertId,
                            'score',
                            e.target.value
                          )
                        }
                        required
                      >
                        <option value="">선택하세요</option>
                        <option value="1">1점</option>
                        <option value="2">2점</option>
                        <option value="3">3점</option>
                        <option value="4">4점</option>
                        <option value="5">5점</option>
                      </select>
                    </div>
                    <div className="form-group">
                      <label htmlFor={`comment-${contact.expertId}`}>
                        코멘트 (선택사항)
                      </label>
                      <textarea
                        id={`comment-${contact.expertId}`}
                        value={ratingForms[contact.expertId]?.comment || ''}
                        onChange={(e) =>
                          handleRatingChange(
                            contact.expertId,
                            'comment',
                            e.target.value
                          )
                        }
                        rows="3"
                        placeholder="평가 코멘트를 입력하세요"
                      />
                    </div>
                    <button type="submit" className="btn btn-primary">
                      평가 등록
                    </button>
                  </form>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Contacts;
