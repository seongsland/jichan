import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {AuthProvider} from './context/AuthContext';
import {LoadingProvider} from './context/LoadingContext';
import Layout from './components/Layout';
import Home from './pages/Home';
import Signup from './pages/Signup';
import Login from './pages/Login';
import VerifyEmail from './pages/VerifyEmail';
import User from './pages/User';
import Profile from './pages/Profile';
import Contacts from './pages/Contacts';
import ApiTest from './pages/ApiTest';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <AuthProvider>
      <LoadingProvider>
        <Router>
          <Layout>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/login" element={<Login />} />
              <Route path="/verify_email" element={<VerifyEmail />} />
              <Route
                path="/user"
                element={
                  <ProtectedRoute>
                    <User />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/profile"
                element={
                  <ProtectedRoute>
                    <Profile />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/contacts"
                element={
                  <ProtectedRoute>
                    <Contacts />
                  </ProtectedRoute>
                }
              />
              <Route path="/test/api" element={<ApiTest />} />
            </Routes>
          </Layout>
        </Router>
      </LoadingProvider>
    </AuthProvider>
  );
}

export default App;
