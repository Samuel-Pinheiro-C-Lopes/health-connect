import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

function CheckEmail() {
    const [email, setEmail] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    const tipoUsuario = location.state?.tipoUsuario || 'paciente';

    const handleCheckEmail = (e) => {
        e.preventDefault();
        
        if (tipoUsuario === 'paciente') {
            navigate('/cadastrar-paciente', { state: { emailValidado: email } });
        } else if (tipoUsuario === 'medico') {
            navigate('/solicitar-medico', { state: { emailValidado: email } });
        }
    };

    return (
        <section className="check-email-container">
            <form onSubmit={handleCheckEmail}>
                <label>Informe seu e-mail para começar:</label>
                <input 
                    type="email" 
                    required
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                    placeholder="exemplo@email.com"
                />
                <button type="submit">Continuar</button>
            </form>
        </section>
    );
}

export default CheckEmail;