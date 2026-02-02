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
        <section className="login">
            <img src={loginImage} alt="logo" className="login-image"/>
            <div className="login-content">
                <img src="health_connect_logo.png" alt="logo"/> 
                <div className="login-content-text">
                    <h1 className="title">Bem vindo de volta!</h1>
                    <p className="text">Acesse sua conta para gerenciar suas consultas</p>
                </div>
                <form className="form-login" onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="email">Email</label>
                        <input type="text" 
                                name="email"
                                required
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}/>
                    </div>
                    <div>
                        <label htmlFor="senha">Senha</label>
                        <input type="password"
                                name="senha"
                                required
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
                        <p className="text">Não tem uma conta? <Link to='/cadastrar'>Cadastre-se agora.</Link></p>
                    </div>
                </form>
            </div>
        </section>
        );
}

export default Login;