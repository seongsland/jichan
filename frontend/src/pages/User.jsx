import {useEffect, useState} from 'react';
import api from '../utils/api';
import Message from '../components/Message';
import {useLoading} from '../context/LoadingContext';
import './User.css';

const User = () => {
  const { showLoading, hideLoading } = useLoading();
  const [formData, setFormData] = useState({
    name: '',
    gender: '',
    region: '',
    introduction: '',
    isVisible: false,
    phone: '',
    phoneMessage: '',
    specialties: [],
  });
  const [categories, setCategories] = useState([]);
  const [details, setDetails] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [selectedDetail, setSelectedDetail] = useState('');
  const [hourlyRate, setHourlyRate] = useState('');
  const [message, setMessage] = useState({ type: '', text: '', timestamp: null });

  useEffect(() => {
    fetchProfile();
    fetchSpecialties();
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

  const fetchProfile = async () => {
    showLoading();
    try {
      const response = await api.get('/user/profile');
      const data = response.data;
      setFormData({
        name: data.name || '',
        gender: data.gender || '',
        region: data.region || '',
        introduction: data.introduction || '',
        isVisible: data.isVisible || false,
        phone: data.phone || '',
        phoneMessage: data.phoneMessage || '',
        specialties: data.specialties ? data.specialties.map(s => ({
          specialtyDetailId: s.specialtyDetailId,
          hourlyRate: s.hourlyRate
        })) : [],
      });
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

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  // Format number with commas for Korean currency
  const formatCurrency = (value) => {
    if (!value) return '';
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  };

  // Remove commas and parse to number
  const parseCurrency = (value) => {
    if (!value) return 0;
    return parseInt(value.toString().replace(/,/g, ''), 10);
  };

  const handleHourlyRateChange = (e) => {
    const value = e.target.value.replace(/,/g, ''); // Remove existing commas
    if (/^\d*$/.test(value)) { // Only allow digits
      setHourlyRate(formatCurrency(value));
    }
  };

  const handleAddSpecialty = () => {
    if (!selectedDetail || !hourlyRate) {
      setMessage({
        type: 'error',
        text: '카테고리, 세부항목, 시간당 금액을 모두 입력해주세요.',
        timestamp: Date.now(),
      });
      return;
    }

    const detailId = parseInt(selectedDetail);
    const rate = parseCurrency(hourlyRate);

    if (isNaN(rate) || rate <= 0) {
      setMessage({
        type: 'error',
        text: '유효한 시간당 금액을 입력해주세요.',
        timestamp: Date.now(),
      });
      return;
    }

    // Check if already exists
    if (formData.specialties.some(s => s.specialtyDetailId === detailId)) {
      setMessage({
        type: 'error',
        text: '이미 추가된 전문 분야입니다.',
        timestamp: Date.now(),
      });
      return;
    }

    setFormData({
      ...formData,
      specialties: [...formData.specialties, { specialtyDetailId: detailId, hourlyRate: rate }],
    });

    setSelectedCategory('');
    setSelectedDetail('');
    setHourlyRate('');
    setMessage({ type: '', text: '', timestamp: Date.now() });
  };

  const handleRemoveSpecialty = (detailId) => {
    setFormData({
      ...formData,
      specialties: formData.specialties.filter(s => s.specialtyDetailId !== detailId),
    });
  };

  const filteredDetails = details.filter(detail => !selectedCategory || detail.categoryId === parseInt(selectedCategory));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage({ type: '', text: '', timestamp: Date.now() });

    if (formData.isVisible) {
      if (!formData.name || !formData.gender || !formData.region || !formData.introduction) {
        setMessage({
          type: 'error',
          text: '프로필을 공개하려면 이름, 성별, 지역, 소개글을 모두 입력해야 합니다.',
          timestamp: Date.now(),
        });
        return;
      }

      if (formData.phone && !formData.phoneMessage) {
        setMessage({
          type: 'error',
          text: '핸드폰 번호를 입력한 경우 연락 가능 시간 메모를 입력해야 합니다.',
          timestamp: Date.now(),
        });
        return;
      }

      if (!formData.specialties || formData.specialties.length === 0) {
        setMessage({
          type: 'error',
          text: '프로필을 공개하려면 최소 1개 이상의 특기를 등록해야 합니다.',
          timestamp: Date.now(),
        });
        return;
      }
    }

    showLoading();

    try {
      await api.put('/user/profile', formData);
      setMessage({
        type: 'success',
        text: '프로필이 업데이트되었습니다.',
        timestamp: Date.now(),
      });
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || '프로필 업데이트에 실패했습니다.',
        timestamp: Date.now(),
      });
    } finally {
      hideLoading();
    }
  };

  return (
    <div className="user">
      <h2>프로필 관리</h2>
      <Message
        type={message.type}
        message={message.text}
        timestamp={message.timestamp}
        onClose={() => setMessage({ type: '', text: '', timestamp: Date.now() })}
      />
      <form onSubmit={handleSubmit} className="profile-form">
        <div className="form-group checkbox-group">
          <label>
            <input
                type="checkbox"
                name="isVisible"
                checked={formData.isVisible}
                onChange={handleChange}
            />
            프로필 노출하기
          </label>
        </div>
        <div className="form-group">
          <label htmlFor="name">이름</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="gender">성별</label>
          <select
            id="gender"
            name="gender"
            value={formData.gender}
            onChange={handleChange}
          >
            <option value="">선택하세요</option>
            <option value="남성">남성</option>
            <option value="여성">여성</option>
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="region">지역</label>
          <input
            type="text"
            id="region"
            name="region"
            value={formData.region}
            onChange={handleChange}
            placeholder="예: 서울"
          />
        </div>
        <div className="form-group">
          <label htmlFor="introduction">소개글</label>
          <textarea
            id="introduction"
            name="introduction"
            value={formData.introduction}
            onChange={handleChange}
            rows="5"
            placeholder="자신을 소개해주세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="phone">핸드폰 번호</label>
          <input
            type="tel"
            id="phone"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            placeholder="예: 010-1234-5678"
          />
        </div>
        <div className="form-group">
          <label htmlFor="phoneMessage">연락 가능 시간 메모</label>
          <textarea
            id="phoneMessage"
            name="phoneMessage"
            value={formData.phoneMessage}
            onChange={handleChange}
            rows="3"
            placeholder="예: 평일 7시 이후, 주말 항상 통화 가능"
          />
        </div>

        <div className="form-group">
          <label>특기</label>
          <div className="specialty-input-group">
            <select
              value={selectedCategory}
              onChange={(e) => {
                setSelectedCategory(e.target.value);
                setSelectedDetail('');
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
              value={selectedDetail}
              onChange={(e) => setSelectedDetail(e.target.value)}
              disabled={!selectedCategory}
            >
              <option value="">세부항목 선택</option>
              {filteredDetails.map(detail => (
                <option key={detail.id} value={detail.id}>
                  {detail.name}
                </option>
              ))}
            </select>
            <input
              type="text"
              placeholder="시간당 금액"
              value={hourlyRate}
              onChange={handleHourlyRateChange}
            />
            <button type="button" onClick={handleAddSpecialty} className="btn btn-secondary">
              추가
            </button>
          </div>
          <div className="specialty-list">
            {formData.specialties.map(specialty => {
              const detail = details.find(d => d.id === specialty.specialtyDetailId);
              const category = categories.find(c => c.id === detail?.categoryId);
              return (
                <div key={specialty.specialtyDetailId} className="specialty-item">
                  <span>{category?.name} - {detail?.name}: {formatCurrency(specialty.hourlyRate)}원/시간</span>
                  <button type="button" onClick={() => handleRemoveSpecialty(specialty.specialtyDetailId)} className="btn btn-danger btn-sm">
                    제거
                  </button>
                </div>
              );
            })}
          </div>
        </div>

        <button type="submit" className="btn btn-primary btn-block">
          저장
        </button>
      </form>
    </div>
  );
};

export default User;
