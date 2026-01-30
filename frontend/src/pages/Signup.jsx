import {useState} from 'react';
import {Link} from 'react-router-dom';
import api from '../utils/api';
import {validateName, validatePassword} from '../utils/validation';
import {useMessage} from '../context/MessageContext';
import {useLoading} from '../context/LoadingContext';
import './Auth.css';

const Signup = () => {
    const {showLoading, hideLoading} = useLoading();
    const {showMessage} = useMessage();
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        passwordConfirm: '',
    });
    const [agreed, setAgreed] = useState(false);

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

        const nameError = validateName(formData.name);
        if (nameError) {
            showMessage('error', nameError);
            return;
        }

        const passwordError = validatePassword(formData.password);
        if (passwordError) {
            showMessage('error', passwordError);
            return;
        }

        if (formData.password !== formData.passwordConfirm) {
            showMessage('error', '비밀번호가 일치하지 않습니다.');
            return;
        }

        if (!agreed) {
            showMessage(
                'error',
                '이용약관 및 개인정보 처리방침에 동의해야 합니다.',
            );
            return;
        }

        showLoading();

        try {
            const {passwordConfirm, ...signupData} = formData;
            await api.post('/auth/signup', signupData);
            showMessage(
                'success',
                '회원가입이 완료되었습니다.\n이메일을 확인해주세요.',
                '/login',
            );
        } catch (error) {
            showMessage(
                'error',
                error.response?.data?.message || '회원가입에 실패했습니다.',
            );
        } finally {
            hideLoading();
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>회원가입</h2>
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
                            <p>영문 대/소문자, 숫자, 특수문자 중 2종류 이상을 혼합하여 설정해 주세요.</p>
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
