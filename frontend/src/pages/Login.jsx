import {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import api from '../utils/api';
import {useAuth} from '../context/AuthContext';
import Message from '../components/Message';
import './Auth.css';

const Login = () => {
    const navigate = useNavigate();
    const {login} = useAuth();
    const [formData, setFormData] = useState({
        email: '', password: '',
    });
    const [message, setMessage] = useState({type: '', text: ''});

    const handleChange = (e) => {
        setFormData({
            ...formData, [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await api.post('/auth/login', formData);
            const {accessToken} = response.data;
            // refreshToken은 서버에서 httpOnly 쿠키로 설정됨
            login(accessToken);
            navigate('/profile');
        } catch (error) {
            setMessage({
                type: 'error', text: error.response?.data?.message || '로그인에 실패했습니다.',
            });
        }
    };

    return (<div className="auth-container">
        <div className="auth-card">
            <h2>로그인</h2>
            <Message
                type={message.type}
                message={message.text}
                onClose={() => setMessage({type: '', text: ''})}
            />
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="email">이메일</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">비밀번호</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary btn-block">
                    로그인
                </button>
            </form>
            <div className="auth-links">
                <p className="auth-link">
                    계정이 없으신가요? <Link to="/signup">회원가입</Link>
                </p>
                <p className="auth-link">
                    <Link to="/forgot_password">비밀번호 찾기</Link>
                </p>
            </div>
        </div>
    </div>);
};

export default Login;
