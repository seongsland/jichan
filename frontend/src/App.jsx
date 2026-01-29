import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {AuthProvider} from './context/AuthContext';
import {LoadingProvider} from './context/LoadingContext';
import Layout from './components/Layout';
import Home from './pages/Home';
import Signup from './pages/Signup';
import Login from './pages/Login';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';
import VerifyEmail from './pages/VerifyEmail';
import User from './pages/User';
import Profile from './pages/Profile';
import Contacts from './pages/Contacts';
import Terms from './pages/Terms';
import Privacy from './pages/Privacy';
import ProtectedRoute from './components/ProtectedRoute';
import ScrollToTop from './components/ScrollToTop';

function App() {
    return (<AuthProvider>
        <LoadingProvider>
            <Router>
                <ScrollToTop/>
                <Layout>
                    <Routes>
                        <Route path="/" element={<Home/>}/>
                        <Route path="/signup" element={<Signup/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/forgot_password" element={<ForgotPassword/>}/>
                        <Route path="/reset_password" element={<ResetPassword/>}/>
                        <Route path="/verify_email" element={<VerifyEmail/>}/>
                        <Route path="/terms" element={<Terms/>}/>
                        <Route path="/privacy" element={<Privacy/>}/>
                        <Route
                            path="/user"
                            element={<ProtectedRoute>
                                <User/>
                            </ProtectedRoute>}
                        />
                        <Route path="/profile" element={<Profile/>}/>
                        <Route
                            path="/contacts"
                            element={<ProtectedRoute>
                                <Contacts/>
                            </ProtectedRoute>}
                        />
                    </Routes>
                </Layout>
            </Router>
        </LoadingProvider>
    </AuthProvider>);
}

export default App;
