import { Routes, BrowserRouter, Route } from 'react-router-dom';
import Login from '../pages/Login';
import RegisterPatient from '../pages/RegisterPatient';
import RegisterPerson from '../pages/RegisterPerson';
import RegisterDoctor from '../pages/RegisterDoctor';
import ForgotPassword from '../pages/Forgotpassword';
import AdminManagement from '../pages/AdminManagement';
import OptionsLogin from '../pages/OptionsLogin';
import ProtectedRoute from '../components/ProtectedRoute';
import TokenNonAdminRoute from '../components/TokenNonAdminRoute';
import AdminManagement from '../pages/AdminManagement';

function AppRoutes() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="esqueci-senha" element={<ForgotPassword />} />
            <Route path="admin" element={<AdminManagement />} />
            <Route path="cadastrar" element={<RegisterPerson/>}/>
            <Route path="opcoes-login" element={<OptionsLogin/>} />
            <Route path="admin" element={<AdminManagement />} />
            <Route
              path="cadastrar-paciente"
              element={
                <TokenNonAdminRoute>
                  <RegisterPatient />
                </TokenNonAdminRoute>
              }
            />
            <Route
              path="cadastrar-medico"
              element={
                <TokenNonAdminRoute>
                  <RegisterDoctor />
                </TokenNonAdminRoute>
              }
            />
        </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;