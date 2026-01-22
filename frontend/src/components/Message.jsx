import './Message.css';

const Message = ({ type, message, onClose }) => {
  if (!message) return null;

  return (
    <div className="message-overlay" onClick={onClose}>
      <div className={`message-box message-${type}`}>
        <span>{message}</span>
      </div>
    </div>
  );
};

export default Message;
