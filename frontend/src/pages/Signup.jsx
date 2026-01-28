import {useState} from 'react';
import {Link} from 'react-router-dom';
import api from '../utils/api';
import {validateName, validatePassword} from '../utils/validation';
import Message from '../components/Message';
import {useLoading} from '../context/LoadingContext';
import './Auth.css';

const Signup = () => {
    const {showLoading, hideLoading} = useLoading();
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        passwordConfirm: '',
    });
    const [agreed, setAgreed] = useState(false);
    const [message, setMessage] = useState({type: '', text: '', navigateTo: null});

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleAgreementChange = (e) => {
        setAgreed(e.target.checked);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage({type: '', text: '', navigateTo: null});

        const nameError = validateName(formData.name);
        if (nameError) {
            setMessage({type: 'error', text: nameError});
            return;
        }

        const passwordError = validatePassword(formData.password);
        if (passwordError) {
            setMessage({type: 'error', text: passwordError});
            return;
        }

        if (formData.password !== formData.passwordConfirm) {
            setMessage({type: 'error', text: '비밀번호가 일치하지 않습니다.'});
            return;
        }

        if (!agreed) {
            setMessage({
                type: 'error',
                text: '이용약관 및 개인정보 처리방침에 동의해야 합니다.',
            });
            return;
        }

        showLoading();

        try {
            const {passwordConfirm, ...signupData} = formData;
            await api.post('/auth/signup', signupData);
            setMessage({
                type: 'success',
                text: '회원가입이 완료되었습니다. 이메일을 확인해주세요.',
                navigateTo: '/login',
            });
        } catch (error) {
            setMessage({
                type: 'error',
                text: error.response?.data?.message || '회원가입에 실패했습니다.',
            });
        } finally {
            hideLoading();
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>회원가입</h2>
                <Message
                    type={message.type}
                    message={message.text}
                    onClose={() => setMessage({type: '', text: '', navigateTo: null})}
                    navigateTo={message.navigateTo}
                />
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">이름 (닉네임)</label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                            maxLength={13}
                        />
                    </div>
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
                            minLength={8}
                            maxLength={32}
                        />
                        <div className="password-rules">
                            <p>비밀번호는 8~32자로 설정해 주세요.</p>
                            <p>영문 대/소문자, 숫자, 특수문자 중 3가지 이상을 조합해야 합니다.</p>
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="passwordConfirm">비밀번호 확인</label>
                        <input
                            type="password"
                            id="passwordConfirm"
                            name="passwordConfirm"
                            value={formData.passwordConfirm}
                            onChange={handleChange}
                            required
                            minLength={8}
                            maxLength={32}
                        />
                    </div>
                    <div className="form-group checkbox-group">
                        <label className="checkbox-label">
                            <input
                                type="checkbox"
                                checked={agreed}
                                onChange={handleAgreementChange}
                            />
                            <span>
                <Link to="/terms" target="_blank">이용약관</Link> 및 <Link to="/privacy" target="_blank">개인정보 처리방침</Link>에 동의합니다.
              </span>
                        </label>
                    </div>
                    <button type="submit" className="btn btn-primary btn-block">
                        회원가입
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Signup;
