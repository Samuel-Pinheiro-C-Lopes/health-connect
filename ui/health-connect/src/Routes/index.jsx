import  { Routes,BrowserRouter,Route } from 'react-router-dom'
import Login from '../pages/Login';
import RegisterPatient from '../pages/RegisterPatient';

function AppRoutes () {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="cadastrar-paciente" element={<RegisterPatient/>}/>
        </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;