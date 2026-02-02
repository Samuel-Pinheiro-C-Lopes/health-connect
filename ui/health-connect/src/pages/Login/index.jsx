import loginImage from '../../assets/images/login_image.png';
import "./styles.css";
import { useState } from 'react';
import { Link } from 'react-router-dom';


function Login() { 
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");

    const handleSubmit = (event) =>{
        event.preventDefault();
        console.log("Try login");
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
                        <a href="">Esqueci minha senha</a>
                    <button
                        type="submit"
                        className="btn-primary"
                    >
                        Entrar
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