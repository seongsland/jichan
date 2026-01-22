import {useEffect, useState} from 'react';
import api from '../utils/api';
import Message from '../components/Message';
import ConfirmDialog from '../components/ConfirmDialog';
import {useLoading} from '../context/LoadingContext';
import './Profile.css';

const Profile = () => {
  const { loading, showLoading, hideLoading } = useLoading();
  
  const [profileData, setProfileData] = useState({
    content: [],
    hasNext: false,
    page: 0
  });

  const [filters, setFilters] = useState({
    category: '',
    specialty: '',
    sortBy: ''
  });

  const [message, setMessage] = useState({ type: '', text: '' });
  const [contactViews, setContactViews] = useState({});
  const [visibleCategories, setVisibleCategories] = useState({});
  const [confirmDialog, setConfirmDialog] = useState({ 
    isOpen: false, 
    expertId: null, 
    contactType: null,
    message: ''
  });

  const [categories, setCategories] = useState([]);
  const [details, setDetails] = useState([]);

  useEffect(() => {
    void fetchSpecialties();
    void fetchProfiles(0, true);
  }, []);


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

  const fetchProfiles = async (pageNum, reset = false, currentFilters = null) => {
    showLoading();
    try {
      const activeFilters = currentFilters || filters;
      const params = {
        page: pageNum,
        ...(activeFilters.category && { category: activeFilters.category }),
        ...(activeFilters.specialty && { specialty: activeFilters.specialty }),
        ...(activeFilters.sortBy && { sortBy: activeFilters.sortBy }),
      };
      const response = await api.get('/profile', { params });
      const data = response.data;
      console.log(data);

      setProfileData(prev => ({
        content: reset ? data.content : [...prev.content, ...data.content],
        hasNext: data.hasNext,
        page: pageNum
      }));

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
      });
    } finally {
      hideLoading();
    }
  };

  const handleLoadMore = () => {
    void fetchProfiles(profileData.page + 1, false);
  };

  const handleContactViewClick = (expertId, contactType) => {
    const message = contactType === 'EMAIL' 
      ? '이메일을 확인 하시겠습니까?' 
      : '핸드폰 번호를 확인 하시겠습니까?';
    
    setConfirmDialog({
      isOpen: true,
      expertId,
      contactType,
      message
    });
  };

  const executeContactView = async () => {
    const { expertId, contactType } = confirmDialog;
    if (!expertId || !contactType) return;

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

      // 조회수 증가 반영
      setProfileData(prev => ({
        ...prev,
        content: prev.content.map(profile => 
          profile.id === expertId 
            ? { ...profile, reviewCount: (profile.reviewCount || 0) + 1 }
            : profile
        )
      }));

    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || '연락처를 불러오는데 실패했습니다.',
      });
    } finally {
      setConfirmDialog({ isOpen: false, expertId: null, contactType: null, message: '' });
    }
  };

  const handleSearch = () => {
    void fetchProfiles(0, true);
  };

  const handleReset = () => {
    const resetFilters = { category: '', specialty: '', sortBy: '' };
    setFilters(resetFilters);
    void fetchProfiles(0, true, resetFilters);
  };

  const filteredDetails = details.filter(detail => !filters.category || detail.categoryId === parseInt(filters.category));

  return (
    <div className="profile">
      <h2>전문가 검색</h2>
      <Message
        type={message.type}
        message={message.text}
        onClose={() => setMessage({ type: '', text: '' })}
      />

      <ConfirmDialog
        isOpen={confirmDialog.isOpen}
        message={confirmDialog.message}
        onConfirm={executeContactView}
        onCancel={() => setConfirmDialog({ isOpen: false, expertId: null, contactType: null, message: '' })}
        confirmText="확인"
        cancelText="취소"
      />

      <div className="profile-filters">
        <div className="filter-group">
          <label>특기</label>
          <div style={{ display: 'flex', gap: '10px' }}>
            <select
              value={filters.category}
              onChange={(e) => {
                setFilters(prev => ({ ...prev, category: e.target.value, specialty: '' }));
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
              value={filters.specialty}
              onChange={(e) => setFilters(prev => ({ ...prev, specialty: e.target.value }))}
              disabled={!filters.category}
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
            value={filters.sortBy}
            onChange={(e) => setFilters(prev => ({ ...prev, sortBy: e.target.value }))}
          >
            <option value="">기본</option>
            <option value="rating">평점순</option>
            <option value="price">금액순</option>
          </select>
        </div>
        <div className="filter-actions" style={{ alignSelf: 'flex-end' }}>
          <button
            onClick={handleSearch}
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? '검색 중...' : '조회'}
          </button>
          <button
            onClick={handleReset}
            className="btn btn-outline"
            disabled={loading}
            title="초기화"
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8"/>
              <path d="M3 3v5h5"/>
            </svg>
          </button>
        </div>
      </div>

      <div className="profile-grid">
        {profileData.content.map((profile) => (
          <div key={profile.id} className="profile-card">
            <div className="profile-header">
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <h3>{profile.name}</h3>
                <div className="profile-stats">
                  {profile.averageRating !== null && (
                    <span className="rating">★ {profile.averageRating.toFixed(1)}</span>
                  )}
                  <span className="review-count"> ({profile.reviewCount || 0})</span>
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
                  let categoryName = "";
                  const detail = details.find(d => d.id === spec.specialtyDetailId);
                  if (detail) {
                    const category = categories.find(c => c.id === detail.categoryId);
                    categoryName = category.name;
                  }

                  const specialtyKey = `${profile.id}-${idx}`;

                  return (
                    <div 
                      key={idx} 
                      className="specialty" 
                      title={categoryName}
                      onClick={() => setVisibleCategories(prev => ({ ...prev, [specialtyKey]: !prev[specialtyKey] }))}
                      style={{ cursor: 'pointer', position: 'relative' }}
                    >
                      {name} - {spec.hourlyRate?.toLocaleString()}원/시간
                      {visibleCategories[specialtyKey] && categoryName && (
                        <div className="specialty-overlay">
                          {categoryName}
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
            )}
            {profile.introduction && (
              <p className="introduction">{profile.introduction}</p>
            )}
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

            <div className="profile-actions">
              {!contactViews[`${profile.id}-EMAIL`] && (
                  <button
                      onClick={() => handleContactViewClick(profile.id, 'EMAIL')}
                      className="btn btn-outline"
                  >
                    이메일 보기
                  </button>
              )}
              {!contactViews[`${profile.id}-PHONE`] && (
                  <button
                      onClick={() => handleContactViewClick(profile.id, 'PHONE')}
                      className="btn btn-outline"
                  >
                    핸드폰 보기
                  </button>
              )}
            </div>
          </div>
        ))}
      </div>
      {profileData.hasNext && (
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
