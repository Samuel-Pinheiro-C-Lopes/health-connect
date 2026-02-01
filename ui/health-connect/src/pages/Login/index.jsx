import loginImage from '../../assets/images/login_image.png';
import healthConnectLogo from '../../assets/images/health_connect_logo.png';
import "./styles.css";
import { useState } from 'react';


function Login() { 
    const [login, setLogin] = useState("");
    const [senha, setSenha] = useState("");

    const handleSubmit = (event) =>{
        event.preventDefault();
        console.log("Try login");
    }

    return(
        <section className="login">
            <img src={loginImage} alt="logo" className="login-image"/>
            <div className="login-content">
                <img src={healthConnectLogo} alt="logo"/>
                <div className="login-content-text">
                    <h1 className="title">Bem vindo de volta!</h1>
                    <p className="text">Acesse sua conta para gerenciar suas consultas</p>
                </div>
                <form className="form-login" onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="login">Login</label>
                        <input type="text" 
                                name="login"
                                value={login}
                                onChange={(e) => setLogin(e.target.value)}/>
                    </div>
                    <div>
                        <label htmlFor="senha">Senha</label>
                        <input type="password"
                                name="senha"
                                value={senha}
                                onChange={(e) => setSenha(e.target.value)}/>
                    </div>
                        <a href="">Esqueci minha senha</a>
                    <button
                        type="submit"
                    >
                        Entrar
                    </button>
                    <div className="redirect-container">
                        <p className="text">Não tem uma conta? <a href="">Cadastre-se como paciente.</a></p>
                        <p className="text">É um médico? <a href="">Solicite seu cadastro.</a></p>
                    </div>
                </form>
            </div>
        </section>
        );
}

export default Login;