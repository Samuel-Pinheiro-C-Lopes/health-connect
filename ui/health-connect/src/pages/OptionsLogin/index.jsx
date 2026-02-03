
import { useNavigate } from 'react-router-dom';
import { STORAGE_KEYS } from '../../config/constants';

function OptionsLogin() {
    const navigate = useNavigate();

    const getRoles = () => {
        const raw = sessionStorage.getItem(STORAGE_KEYS.ROLE);
        if (!raw) return [];
        try {
            const parsed = JSON.parse(raw);
            if (Array.isArray(parsed)) return parsed.map(String);
        } catch (e) {
            // not JSON
        }
        return [String(raw)];
    };

    const hasRole = (check) => {
        const roles = getRoles().map(r => r.toUpperCase());
        const candidates = [check.toUpperCase()];
        if (check.toUpperCase() === 'PATIENT') candidates.push('PACIENTE');
        if (check.toUpperCase() === 'DOCTOR') candidates.push('MEDICO');
        return roles.some(r => candidates.some(c => r.includes(c)));
    };

    const handlePatient = (e) => {
        e.preventDefault();
        const token = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
        if (!token) return navigate('/');
        if (hasRole('PATIENT')) {
            return navigate('/inicio');
        }
        return navigate('/cadastrar-paciente');
    };

    const handleDoctor = (e) => {
        e.preventDefault();
        const token = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
        if (!token) return navigate('/');
        if (hasRole('DOCTOR')) {
            return navigate('/agenda');
        }
        return navigate('/cadastrar-medico');
    };

    return (
        <section className="page-split-container">
            <div className="page-split-content">
                <img src="health_connect_logo.png" alt="logo" />
                <div className="page-split-content-text">
                    <h1 className="page-title">Como deseja entrar?</h1>
                    <p className="page-text">Escolha entrar como paciente ou como médico</p>
                </div>

                <div className="form-container" style={{display: 'flex', gap: '12px', flexDirection: 'column'}}>
                    <button className="btn-primary" onClick={handlePatient}>Entrar como Paciente</button>
                    <button className="btn-primary" onClick={handleDoctor}>Entrar como Médico</button>
                </div>
            </div>
        </section>
    );
}

export default OptionsLogin;