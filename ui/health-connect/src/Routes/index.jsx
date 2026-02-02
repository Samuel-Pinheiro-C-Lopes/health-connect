import  { Routes,BrowserRouter,Route } from 'react-router-dom'
import Login from '../pages/Login';
import RegisterPatient from '../pages/RegisterPatient';
import RegisterPerson from '../pages/RegisterPerson';

function AppRoutes () {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="cadastrar" element={<RegisterPerson/>}/>
            <Route path="cadastrar-paciente" element={<RegisterPatient/>}/>
        </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;