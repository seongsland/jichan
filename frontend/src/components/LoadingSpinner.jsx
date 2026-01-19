import React from 'react';
import './LoadingSpinner.css'; // Optional: for styling the spinner

const LoadingSpinner = ({ message = '로딩 중...' }) => {
  return (
    <div className="loading-spinner-container">
      <div className="loading-spinner-modal">
        <div className="loading-spinner"></div>
        <p>{message}</p>
      </div>
    </div>
  );
};

export default LoadingSpinner;
