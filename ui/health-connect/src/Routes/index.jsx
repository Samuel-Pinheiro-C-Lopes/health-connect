import  { Routes,BrowserRouter,Route } from 'react-router-dom'
import Login from '../pages/Login';
import RegisterPatient from '../pages/RegisterPatient';
import RegisterPerson from '../pages/RegisterPerson';
import RegisterDoctor from '../pages/RegisterDoctor';

function AppRoutes () {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="cadastrar" element={<RegisterPerson/>}/>
            <Route path="cadastrar-paciente" element={<RegisterPatient/>}/>
            <Route path="cadastrar-medico" element={<RegisterDoctor/>}/>
        </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;