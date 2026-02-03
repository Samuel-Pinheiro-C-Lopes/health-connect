import { Navigate } from 'react-router-dom';
import { STORAGE_KEYS } from '../../config/constants';

function ProtectedRoute({ children, allowedRoles, denyRoles, redirectTo = '/' }) {
  const token = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
  const role = sessionStorage.getItem(STORAGE_KEYS.ROLE);

  if (!token) {
    return <Navigate to="/" replace />;
  }

  if (denyRoles && role && denyRoles.includes(role)) {
    return <Navigate to={redirectTo} replace />;
  }

  if (allowedRoles && (!role || !allowedRoles.includes(role))) {
    return <Navigate to={redirectTo} replace />;
  }

  return children;
}

export default ProtectedRoute;
