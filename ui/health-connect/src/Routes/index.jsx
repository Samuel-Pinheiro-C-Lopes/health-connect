import { Routes, BrowserRouter, Route } from 'react-router-dom';
import Login from '../pages/Login';
import RegisterPatient from '../pages/RegisterPatient';
import RegisterPerson from '../pages/RegisterPerson';
import RegisterDoctor from '../pages/RegisterDoctor';
import ForgotPassword from '../pages/Forgotpassword';
import AdminManagement from '../pages/AdminManagement';
import OptionsLogin from '../pages/OptionsLogin';
import PatientHome from '../pages/PatientHome';
import PatientAppointments from '../pages/PatientAppointments';
import DoctorHome from '../pages/DoctorHome';
import Profile from '../pages/Profile';
import TokenNonAdminRoute from '../components/TokenNonAdminRoute';
import ProtectedRoute from '../components/ProtectedRoute';

function AppRoutes() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="esqueci-senha" element={<ForgotPassword />} />
            <Route
              path="admin"
              element={
                <ProtectedRoute allowedRoles={["ADMIN"]}>
                  <AdminManagement />
                </ProtectedRoute>
              }
            />
            <Route path="cadastrar" element={<RegisterPerson/>}/>
            <Route path="opcoes-login" element={<OptionsLogin/>} />
            <Route
              path="inicio"
              element={
                <TokenNonAdminRoute>
                  <PatientHome />
                </TokenNonAdminRoute>
              }
            />
            <Route
              path="minhas-consultas"
              element={
                <TokenNonAdminRoute>
                  <PatientAppointments />
                </TokenNonAdminRoute>
              }
            />
            <Route
              path="agenda"
              element={
                <TokenNonAdminRoute>
                  <DoctorHome />
                </TokenNonAdminRoute>
              }
            />
            <Route
              path="perfil"
              element={
                <TokenNonAdminRoute>
                  <Profile />
                </TokenNonAdminRoute>
              }
            />
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