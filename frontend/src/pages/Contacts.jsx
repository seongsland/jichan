import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import api from '../utils/api';
import Message from '../components/Message';
import ConfirmDialog from '../components/ConfirmDialog';
import {useLoading} from '../context/LoadingContext';
import './Contacts.css';

const StarRating = ({ score, onRate }) => {
  return (
    <div className="star-rating-inline">
      {[1, 2, 3, 4, 5].map((star) => (
        <span
          key={star}
          className={`star ${star <= score ? 'filled' : ''}`}
          onClick={() => onRate(star)}
        >
          ★
        </span>
      ))}
    </div>
  );
};

const Contacts = () => {
  const navigate = useNavigate();
  const { loading, showLoading, hideLoading } = useLoading();
  
  const [contactData, setContactData] = useState({
    content: [],
    hasNext: false,
    page: 0
  });

  const [message, setMessage] = useState({ type: '', text: '' });
  const [confirmDialog, setConfirmDialog] = useState({ isOpen: false, expertId: null });
  const [filters, setFilters] = useState({
    category: '',
    specialty: '',
  });
  const [categories, setCategories] = useState([]);
  const [details, setDetails] = useState([]);
  const [visibleCategories, setVisibleCategories] = useState({});

  useEffect(() => {
    void fetchSpecialties();
    void fetchContacts(0, true);
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

  const fetchContacts = async (pageNum, reset = false, currentFilters = null) => {
    showLoading();
    try {
      const activeFilters = currentFilters || filters;
      const params = {
        page: pageNum,
        ...(activeFilters.category && { category: activeFilters.category }),
        ...(activeFilters.specialty && { specialty: activeFilters.specialty }),
      };
      const response = await api.get('/contact', { params });
      const data = response.data;

      setContactData(prev => ({
        content: reset ? data.content : [...prev.content, ...data.content],
        hasNext: data.hasNext,
        page: pageNum
      }));
    } catch (error) {
      setMessage({
        type: 'error',
        text: '전문가 목록을 불러오는데 실패했습니다.',
      });
    } finally {
      hideLoading();
    }
  };

  const handleLoadMore = () => {
    void fetchContacts(contactData.page + 1, false);
  };

  const handleRatingSubmit = async (expertId, score) => {
    try {
      await api.post('/contact/rating', {
        expertId: expertId,
        score: score,
      });
      setMessage({
        type: 'success',
        text: '평가가 등록되었습니다.',
      });
      
      // 로컬 상태 업데이트
      setContactData(prev => ({
        ...prev,
        content: prev.content.map(c => 
          c.expertId === expertId ? { ...c, rating: score } : c
        )
      }));
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || '평가 등록에 실패했습니다.',
      });
    }
  };

  const handleDeleteClick = (expertId) => {
    setConfirmDialog({ isOpen: true, expertId });
  };

  const executeDelete = async () => {
    const expertId = confirmDialog.expertId;
    if (!expertId) return;

    try {
      await api.delete(`/contact/${expertId}`);
      setMessage({
        type: 'success',
        text: '전문가가 목록에서 삭제되었습니다.',
      });
      
      // 로컬 상태에서 제거
      setContactData(prev => ({
        ...prev,
        content: prev.content.filter(c => c.expertId !== expertId)
      }));
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || '삭제에 실패했습니다.',
      });
    } finally {
      setConfirmDialog({ isOpen: false, expertId: null });
    }
  };

  const handleSearch = () => {
    void fetchContacts(0, true, filters);
  };

  const handleReset = () => {
    const resetFilters = { category: '', specialty: '' };
    setFilters(resetFilters);
    void fetchContacts(0, true, resetFilters);
  };

  const filteredDetails = details.filter(detail => !filters.category || detail.categoryId === parseInt(filters.category));

  return (
    <div className="contacts-profile">
      <h2 className="page-title">나의 지인</h2>
      <Message
        type={message.type}
        message={message.text}
        onClose={() => setMessage({ type: '', text: '' })}
      />

      <ConfirmDialog
        isOpen={confirmDialog.isOpen}
        message="정말로 이 전문가를 목록에서 삭제하시겠습니까?"
        onConfirm={executeDelete}
        onCancel={() => setConfirmDialog({ isOpen: false, expertId: null })}
        confirmText="삭제"
        cancelText="취소"
      />

      <div className="profile-filters">
        <div className="filter-group">
          <label className="filter-label">특기</label>
          <div className="filter-row">
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
        <div className="filter-actions">
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
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8"/>
              <path d="M3 3v5h5"/>
            </svg>
          </button>
        </div>
      </div>

      {loading && contactData.content.length === 0 && <div className="loading">로딩 중...</div>}

      {!loading && contactData.content.length === 0 ? (
        <div className="empty-state">
          <p>아직 등록된 지인이 없습니다.</p>
          <button
            onClick={() => navigate('/profile')}
            className="btn btn-primary"
          >
            전문가 검색하기
          </button>
        </div>
      ) : (
        <>
          <div className="profile-grid">
            {contactData.content.map((contact) => (
              <div key={contact.expertId} className="profile-card">
                <button
                  onClick={() => handleDeleteClick(contact.expertId)}
                  className="delete-btn"
                  title="삭제"
                >
                  &times;
                </button>
                <div className="profile-header">
                  <h3>{contact.expertName}</h3>
                </div>
                <div className="profile-info">
                  {contact.gender && <span>성별: {contact.gender}</span>}
                  {contact.region && <span>지역: {contact.region}</span>}
                </div>
                {contact.specialties && contact.specialties.length > 0 && (
                  <div className="specialties">
                    {contact.specialties.map((spec, idx) => {
                      let name = spec.name;
                      let categoryName = "";
                      if (spec.specialtyDetailId) {
                        const detail = details.find(d => d.id === spec.specialtyDetailId);
                        if (detail) {
                          const category = categories.find(c => c.id === detail.categoryId);
                          categoryName = category.name;
                        }
                      }

                      const specialtyKey = `${contact.expertId}-${idx}`;

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
                {contact.introduction && (
                  <p className="introduction">{contact.introduction}</p>
                )}
                {contact.email && (
                  <div className="contact-info-item">
                    <strong>이메일:</strong> {contact.email}
                  </div>
                )}
                {contact.phone && (
                  <div className="contact-info-item">
                    <strong>핸드폰:</strong> {contact.phone}
                    {contact.phoneMessage && (
                      <div className="phone-message">{contact.phoneMessage}</div>
                    )}
                  </div>
                )}
                <div className="contact-actions">
                  <span className="rating-label">평가:</span>
                  <StarRating
                    score={contact.rating}
                    onRate={(score) => handleRatingSubmit(contact.expertId, score)}
                  />
                </div>
              </div>
            ))}
          </div>
          {contactData.hasNext && (
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
        </>
      )}
    </div>
  );
};

export default Contacts;
