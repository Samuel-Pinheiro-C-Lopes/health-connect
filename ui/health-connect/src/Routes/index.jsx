import  { Routes,BrowserRouter,Route } from 'react-router-dom'
import Login from '../components/Login';

function AppRoutes () {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} />
        </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;