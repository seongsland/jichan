import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import api from '../utils/api';
import Message from '../components/Message';
import {useAuth} from '../context/AuthContext';
import {useLoading} from '../context/LoadingContext';
import './User.css';

const User = () => {
  const { isAuthenticated, loading: authLoading } = useAuth();
  const navigate = useNavigate();
  const { showLoading, hideLoading } = useLoading();
  const [formData, setFormData] = useState({
    name: '',
    gender: '',
    region: '',
    introduction: '',
    isVisible: false,
    phone: '',
    phoneMessage: '',
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    // 인증 상태 확인 후 로그인되지 않았으면 로그인 페이지로 이동
    if (!authLoading) {
      if (!isAuthenticated) {
        navigate('/login');
        return;
      }
      fetchProfile();
    }
  }, [authLoading, isAuthenticated, navigate]);

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
      });
    } catch (error) {
      setMessage({
        type: 'error',
        text: '프로필을 불러오는데 실패했습니다.',
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

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage({ type: '', text: '' });
    showLoading();

    try {
      await api.put('/user/profile', formData);
      setMessage({
        type: 'success',
        text: '프로필이 업데이트되었습니다.',
      });

      window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || '프로필 업데이트에 실패했습니다.',
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
        onClose={() => setMessage({ type: '', text: '' })}
      />
      <form onSubmit={handleSubmit} className="profile-form">
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
        <button type="submit" className="btn btn-primary btn-block">
          저장
        </button>
      </form>
    </div>
  );
};

export default User;
