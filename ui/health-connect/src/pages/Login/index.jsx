import loginImage from '../../assets/images/login_image.png';
import "./styles.css";
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { loginAsync } from '../../command/userCommand';
import { STORAGE_KEYS } from '../../config/constants';
import ErrorModal from '../../components/ErrorModal';

function Login() { 
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [errors, setErrors] = useState([]);
    const [showErrors, setShowErrors] = useState(false);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);
        setShowErrors(false);

        try {
            const result = await loginAsync(email, senha);
            if (!result.success) {
                setErrors([result.message || 'Erro ao fazer login']);
                setShowErrors(true);
                return;
            }
            const token = result.data.token;
            sessionStorage.setItem(STORAGE_KEYS.TOKEN, token);
            const role = result.data.role || result.data.roles?.[0] || result.data.user?.role;
            if (role) {
                sessionStorage.setItem(STORAGE_KEYS.ROLE, role);
            }
            navigate('/opcoes-login');
        } catch (err) {
            setErrors([err.message || 'Erro desconhecido']);
            setShowErrors(true);
        } finally {
            setLoading(false);
        }
    }

    return(
        <section className="page-split-container">
            <img src={loginImage} alt="logo" className="page-split-image"/>
            <div className="page-split-content">
                <img src="health_connect_logo.png" alt="logo"/> 
                <div className="page-split-content-text">
                    <h1 className="page-title">Bem vindo de volta!</h1>
                    <p className="page-text">Acesse sua conta para gerenciar suas consultas</p>
                </div>
                <form className="form-container" onSubmit={handleSubmit}>
                    <div className="form-field">
                        <label htmlFor="email" className="form-label">Email</label>
                        <input type="text" 
                                name="email"
                                required
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="form-input"/>
                    </div>
                    <div className="form-field">
                        <label htmlFor="senha" className="form-label">Senha</label>
                        <input type="password"
                                name="senha"
                                required
                                value={senha}
                                onChange={(e) => setSenha(e.target.value)}
                                className="form-input"/>
                    </div>
                        <Link to="/esqueci-senha">Esqueci minha senha</Link>
                    <ErrorModal
                        isOpen={showErrors && errors.length > 0}
                        errors={errors}
                        onClose={() => setShowErrors(false)}
                    />
                    <button
                        type="submit"
                        className="btn-primary"
                        disabled={loading}
                    >
                        {loading ? 'Entrando...' : 'Entrar'}
                    </button>
                    <div className="redirect-container">
                        <p className="page-text">Não tem uma conta? <Link to='/cadastrar'>Cadastre-se agora.</Link></p>
                    </div>
                </form>
            </div>
        </section>
        );
}

export default Login;