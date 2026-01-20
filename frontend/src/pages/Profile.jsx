import {useEffect, useState} from 'react';
import api from '../utils/api';
import Message from '../components/Message';
import {useLoading} from '../context/LoadingContext';
import './Profile.css';

const Profile = () => {
  const { loading, showLoading, hideLoading } = useLoading();
  const [profiles, setProfiles] = useState([]);
  const [hasNext, setHasNext] = useState(false);
  const [page, setPage] = useState(0);
  const [specialty, setSpecialty] = useState('');
  const [sortBy, setSortBy] = useState('');
  const [message, setMessage] = useState({ type: '', text: '', timestamp: null });
  const [contactViews, setContactViews] = useState({});

  useEffect(() => {
    fetchProfiles(0, true);
  }, [specialty, sortBy]);

  const fetchProfiles = async (pageNum, reset = false) => {
    showLoading();
    try {
      const params = {
        page: pageNum,
        ...(specialty && { specialty }),
        ...(sortBy && { sortBy }),
      };
      const response = await api.get('/profile', { params });
      const data = response.data;

      if (reset) {
        setProfiles(data.content);
      } else {
        setProfiles([...profiles, ...data.content]);
      }
      setHasNext(data.hasNext);
      setPage(pageNum);
    } catch (error) {
      setMessage({
        type: 'error',
        text: '프로필을 불러오는데 실패했습니다.',
        timestamp: Date.now(),
      });
    } finally {
      hideLoading();
    }
  };

  const handleLoadMore = () => {
    fetchProfiles(page + 1, false);
  };

  const handleContactView = async (expertId, contactType) => {
    try {
      const response = await api.post('/profile/contact_view', {
        expertId,
        contactType,
      });
      const { contact, phoneMessage } = response.data;

      setContactViews({
        ...contactViews,
        [`${expertId}-${contactType}`]: {
          contact,
          phoneMessage,
        },
      });
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || '연락처를 불러오는데 실패했습니다.',
        timestamp: Date.now(),
      });
    }
  };

  return (
    <div className="profile">
      <h2>전문가 검색</h2>
      <Message
        type={message.type}
        message={message.text}
        timestamp={message.timestamp}
        onClose={() => setMessage({ type: '', text: '', timestamp: Date.now() })}
      />

      <div className="profile-filters">
        <div className="filter-group">
          <label htmlFor="specialty">주특기 ID</label>
          <input
            type="number"
            id="specialty"
            value={specialty}
            onChange={(e) => setSpecialty(e.target.value)}
            placeholder="주특기 ID (선택사항)"
          />
        </div>
        <div className="filter-group">
          <label htmlFor="sortBy">정렬</label>
          <select
            id="sortBy"
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
          >
            <option value="">기본</option>
            <option value="rating">평점순</option>
            <option value="price">금액순</option>
          </select>
        </div>
      </div>

      <div className="profile-grid">
        {profiles.map((profile) => (
          <div key={profile.id} className="profile-card">
            <h3>{profile.name}</h3>
            <div className="profile-info">
              {profile.gender && <span>성별: {profile.gender}</span>}
              {profile.region && <span>지역: {profile.region}</span>}
              {profile.averageRating && (
                <span>평점: {profile.averageRating.toFixed(1)}</span>
              )}
            </div>
            {profile.specialties && profile.specialties.length > 0 && (
              <div className="specialties">
                {profile.specialties.map((spec, idx) => (
                  <div key={idx} className="specialty">
                    {spec.name} - {spec.hourlyRate?.toLocaleString()}원/시간
                  </div>
                ))}
              </div>
            )}
            {profile.introduction && (
              <p className="introduction">{profile.introduction}</p>
            )}
            <div className="profile-actions">
              <button
                onClick={() => handleContactView(profile.id, 'EMAIL')}
                className="btn btn-outline"
              >
                이메일 보기
              </button>
              <button
                onClick={() => handleContactView(profile.id, 'PHONE')}
                className="btn btn-outline"
              >
                핸드폰 보기
              </button>
            </div>
            {contactViews[`${profile.id}-EMAIL`] && (
              <div className="contact-info">
                <strong>이메일:</strong>{' '}
                {contactViews[`${profile.id}-EMAIL`].contact}
              </div>
            )}
            {contactViews[`${profile.id}-PHONE`] && (
              <div className="contact-info">
                <strong>핸드폰:</strong>{' '}
                {contactViews[`${profile.id}-PHONE`].contact}
                {contactViews[`${profile.id}-PHONE`].phoneMessage && (
                  <div className="phone-message">
                    {contactViews[`${profile.id}-PHONE`].phoneMessage}
                  </div>
                )}
              </div>
            )}
          </div>
        ))}
      </div>
      {hasNext && (
        <div className="load-more">
          <button
            onClick={handleLoadMore}
            className="btn btn-secondary"
            disabled={loading}
          >
            {loading ? '로딩 중...' : '더보기'}
          </button>
        </div>
      )}
    </div>
  );
};

export default Profile;
