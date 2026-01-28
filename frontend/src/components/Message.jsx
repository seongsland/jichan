import {useCallback, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './Message.css';

const Message = ({type, message, onClose, navigateTo}) => {
    const navigate = useNavigate();

    const handleInteraction = useCallback(() => {
        onClose();
        if (navigateTo) {
            navigate(navigateTo);
        }
    }, [onClose, navigateTo, navigate]);

    useEffect(() => {
        document.addEventListener('keydown', handleInteraction);

        return () => {
            document.removeEventListener('keydown', handleInteraction);
        };
    }, [handleInteraction]);

    if (!message) return null;

    return (
        <div className="message-overlay" onClick={handleInteraction}>
            <div className={`message-box message-${type}`}>
                <span>{message}</span>
            </div>
        </div>
    );
};

export default Message;
