import { Navigate } from 'react-router-dom';
import { STORAGE_KEYS } from '../../config/constants';

function TokenNonAdminRoute({ children, redirectTo = '/' }) {
  const token = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
  const role = sessionStorage.getItem(STORAGE_KEYS.ROLE);

  if (!token) {
    return <Navigate to="/" replace />;
  }

  if (role === 'ADMIN') {
    return <Navigate to={redirectTo} replace />;
  }

  return children;
}

export default TokenNonAdminRoute;
