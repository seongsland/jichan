import React, {useState} from 'react';
import api from '../utils/api';
import {useLoading} from '../context/LoadingContext';
import './ApiTest.css';

const API_GROUPS = [
  {
    name: 'Auth',
    endpoints: [
      { name: 'Signup', method: 'POST', path: '/auth/signup', body: { name: '', email: '', password: '' } },
      { name: 'Verify Email', method: 'GET', path: '/auth/verify_email', params: { token: '' } },
      { name: 'Login', method: 'POST', path: '/auth/login', body: { email: '', password: '' } },
      { name: 'Token Refresh', method: 'POST', path: '/auth/token_refresh', body: {} },
      { name: 'Logout', method: 'POST', path: '/auth/logout', body: {} },
    ]
  },
  {
    name: 'User/Profile',
    endpoints: [
      { name: 'Update Profile', method: 'PUT', path: '/user/profile', body: { name: '', isVisible: true } },
      { name: 'Get Profile', method: 'GET', path: '/profile', params: { specialty: '', sortBy: 'rating', page: 0 } },
      { name: 'View Contact', method: 'POST', path: '/profile/contact_view', body: { expertId: 1, contactType: 'EMAIL' } },
    ]
  },
  {
    name: 'Contacts/Ratings',
    endpoints: [
      { name: 'My Contacts', method: 'GET', path: '/users/contacts' },
      { name: 'Add Rating', method: 'POST', path: '/users/contacts/rating', body: { expertId: 1, score: 5, comment: '' } },
    ]
  }
];

const ApiTest = () => {
  const [selectedEndpoint, setSelectedEndpoint] = useState(null);
  const [requestBody, setRequestBody] = useState('');
  const [queryParams, setQueryParams] = useState('');
  const [response, setResponse] = useState(null);
  const { loading, showLoading, hideLoading } = useLoading();

  const selectEndpoint = (endpoint) => {
    setSelectedEndpoint(endpoint);
    setRequestBody(endpoint.body ? JSON.stringify(endpoint.body, null, 2) : '');
    setQueryParams(endpoint.params ? Object.entries(endpoint.params).map(([k, v]) => `${k}=${v}`).join('&') : '');
    setResponse(null);
  };

  const handleExecute = async () => {
    showLoading();
    setResponse(null);
    try {
      let url = selectedEndpoint.path;
      if (queryParams) {
        url += `?${queryParams}`;
      }

      let res;
      if (selectedEndpoint.method === 'GET') {
        res = await api.get(url);
      } else if (selectedEndpoint.method === 'POST') {
        res = await api.post(url, requestBody ? JSON.parse(requestBody) : {});
      } else if (selectedEndpoint.method === 'PUT') {
        res = await api.put(url, requestBody ? JSON.parse(requestBody) : {});
      }

      setResponse({
        status: res.status,
        data: res.data
      });
    } catch (err) {
      setResponse({
        status: err.response?.status || 'Error',
        data: err.response?.data || err.message
      });
    } finally {
      hideLoading();
    }
  };

  return (
    <div className="api-test-container">
      <div className="api-test-sidebar">
        <h2>API Endpoints</h2>
        {API_GROUPS.map(group => (
          <div key={group.name} className="api-group">
            <h3>{group.name}</h3>
            <ul>
              {group.endpoints.map(ep => (
                <li 
                  key={ep.name} 
                  className={selectedEndpoint?.name === ep.name ? 'active' : ''}
                  onClick={() => selectEndpoint(ep)}
                >
                  <span className={`method ${ep.method}`}>{ep.method}</span> {ep.name}
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>

      <div className="api-test-main">
        {selectedEndpoint ? (
          <>
            <div className="endpoint-header">
              <h2>{selectedEndpoint.name}</h2>
              <code>{selectedEndpoint.method} {selectedEndpoint.path}</code>
            </div>

            <div className="request-section">
              {selectedEndpoint.params && (
                <div className="input-group">
                  <label>Query Parameters (key=value&...)</label>
                  <input 
                    type="text" 
                    value={queryParams} 
                    onChange={(e) => setQueryParams(e.target.value)} 
                  />
                </div>
              )}

              {selectedEndpoint.body && (
                <div className="input-group">
                  <label>Request Body (JSON)</label>
                  <textarea 
                    value={requestBody} 
                    onChange={(e) => setRequestBody(e.target.value)}
                    rows={8}
                  />
                </div>
              )}

              <button
                className="execute-btn"
                onClick={handleExecute}
                disabled={loading}
              >
                {loading ? 'Sending...' : 'Send Request'}
              </button>
            </div>

            {response && (
              <div className="response-section">
                <h3>Response</h3>
                <div className={`status-badge ${response.status >= 200 && response.status < 300 ? 'success' : 'error'}`}>
                  Status: {response.status}
                </div>
                <pre>{JSON.stringify(response.data, null, 2)}</pre>
              </div>
            )}
          </>
        ) : (
          <div className="no-selection">
            Select an endpoint from the sidebar to start testing.
          </div>
        )}
      </div>
    </div>
  );
};

export default ApiTest;
