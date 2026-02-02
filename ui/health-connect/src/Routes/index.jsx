import  { Routes,BrowserRouter,Route } from 'react-router-dom'
import Login from '../pages/Login';
import RegisterPatient from '../pages/RegisterPatient';
import CheckEmail from '../pages/CheckEmail';

function AppRoutes () {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="cadastrar-paciente" element={<RegisterPatient/>}/>
            <Route path="verificar-email" element={<CheckEmail/>}/>
        </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;