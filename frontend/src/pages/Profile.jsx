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

  const [categories, setCategories] = useState([]);
  const [details, setDetails] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');

  useEffect(() => {
    void fetchSpecialties();
  }, []);

  useEffect(() => {
    void fetchProfiles(0, true);
  }, [specialty, sortBy]);

  const fetchSpecialties = async () => {
    try {
      const [categoriesResponse, detailsResponse] = await Promise.all([
        api.get('/specialty/categories'),
        api.get('/specialty/details')
      ]);
      setCategories(categoriesResponse.data);
      setDetails(detailsResponse.data);
    } catch (error) {
      console.error('특기 정보를 불러오는데 실패했습니다.', error);
    }
  };

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
      console.log(data);

      if (reset) {
        setProfiles(data.content);
      } else {
        setProfiles(prevProfiles=> [...prevProfiles, ...data.content]);
      }
      setHasNext(data.hasNext);
      setPage(pageNum);

      // 이미 본 연락처 정보가 있다면 상태 업데이트
      const newContactViews = {};
      data.content.forEach(profile => {
        if (profile.isEmailViewed) {
          newContactViews[`${profile.id}-EMAIL`] = { contact: profile.email };
        }
        if (profile.isPhoneViewed) {
          newContactViews[`${profile.id}-PHONE`] = { 
            contact: profile.phone,
            phoneMessage: profile.phoneMessage
          };
        }
      });
      
      setContactViews(prev => ({
        ...prev,
        ...newContactViews
      }));

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

      setContactViews(prev => ({
        ...prev,
        [`${expertId}-${contactType}`]: {
          contact,
          phoneMessage,
        },
      }));
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || '연락처를 불러오는데 실패했습니다.',
        timestamp: Date.now(),
      });
    }
  };

  const filteredDetails = details.filter(detail => !selectedCategory || detail.categoryId === parseInt(selectedCategory));

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
          <label>특기</label>
          <div style={{ display: 'flex', gap: '10px' }}>
            <select
              value={selectedCategory}
              onChange={(e) => {
                setSelectedCategory(e.target.value);
                setSpecialty('');
              }}
            >
              <option value="">카테고리 선택</option>
              {categories.map(category => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
            <select
              value={specialty}
              onChange={(e) => setSpecialty(e.target.value)}
              disabled={!selectedCategory}
            >
              <option value="">세부항목 선택</option>
              {filteredDetails.map(detail => (
                <option key={detail.id} value={detail.id}>
                  {detail.name}
                </option>
              ))}
            </select>
          </div>
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
            <div className="profile-header">
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <h3>{profile.name}</h3>
                <div className="profile-stats">
                  {profile.averageRating !== null && (
                    <span className="rating">★ {profile.averageRating.toFixed(1)}</span>
                  )}
                  <span className="view-count">👁 {profile.viewCount || 0}</span>
                </div>
              </div>
            </div>
            <div className="profile-info">
              {profile.gender && <span>성별: {profile.gender}</span>}
              {profile.region && <span>지역: {profile.region}</span>}
            </div>
            {profile.specialties && profile.specialties.length > 0 && (
              <div className="specialties">
                {profile.specialties.map((spec, idx) => {
                  let name = spec.name;
                  if (!name && spec.specialtyDetailId) {
                    const detail = details.find(d => d.id === spec.specialtyDetailId);
                    if (detail) {
                      const category = categories.find(c => c.id === detail.categoryId);
                      name = category ? `${category.name} - ${detail.name}` : detail.name;
                    }
                  }
                  return (
                    <div key={idx} className="specialty">
                      {name} - {spec.hourlyRate?.toLocaleString()}원/시간
                    </div>
                  );
                })}
              </div>
            )}
            {profile.introduction && (
              <p className="introduction">{profile.introduction}</p>
            )}
            <div className="profile-actions">
              {!contactViews[`${profile.id}-EMAIL`] && (
                <button
                  onClick={() => handleContactView(profile.id, 'EMAIL')}
                  className="btn btn-outline"
                >
                  이메일 보기
                </button>
              )}
              {!contactViews[`${profile.id}-PHONE`] && (
                <button
                  onClick={() => handleContactView(profile.id, 'PHONE')}
                  className="btn btn-outline"
                >
                  핸드폰 보기
                </button>
              )}
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
