import { Link } from "react-router-dom";
import imageRegisterPatient from "../../assets/images/image_register_patient.png";
import "./styles.css";
import { useState } from "react";
import { validateEmail, validateCPF } from "../../utils/validations";
import ErrorModal from "../../components/ErrorModal";

function RegisterPatient(){
    const [formData, setFormData] = useState({
        nome: "",
        email: "",
        cpf: "",
        telefone: "",
        logradouro: "",
        numeroEndereco: "",
        complemento: "",
        bairro: "",
        cidade: "",
        uf: "",
        cep: "",
        senha: "",
        confirmarSenha: "",
    });

    const initialValidState = {
        nome: true, email: true, cpf: true, telefone: true, logradouro: true,
        bairro: true, cidade: true, uf: true, cep: true, senha: true, confirmarSenha: true,
      };

    const [formDataValid, setFormDataValid] = useState(initialValidState);
    const [cpfError, setCpfError] = useState(false);
    const [emailError, setEmailError] = useState(false);
    const [showErrors, setShowErrors] = useState(false);
    const [errors, setErrors] = useState([]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        if (name === 'cpf') {
            const isValid = value === '' || validateCPF(value);
            setCpfError(!isValid && value.length > 0);
        }

        if (name === 'email') {
            const isValid = value === '' || validateEmail(value);
            setEmailError(!isValid && value.length > 0);
        }
    };

    const getFieldLabel = (fieldName) => {
        const labels = {
            nome: "Nome Completo",
            email: "Email",
            cpf: "CPF",
            telefone: "Telefone",
            cep: "CEP",
            logradouro: "Logradouro",
            numeroEndereco: "Número",
            bairro: "Bairro",
            cidade: "Cidade",
            uf: "UF",
            senha: "Senha",
            confirmarSenha: "Confirmar Senha"
        };
        return labels[fieldName] || fieldName;
    };

    const validateForm = () => {
        const newErrors = [];
        const newValidState = { ...initialValidState };

        if (!formData.nome || formData.nome.trim() === "") {
            newErrors.push("Nome Completo é obrigatório");
            newValidState.nome = false;
        }

        if (!formData.email || formData.email.trim() === "") {
            newErrors.push("Email é obrigatório");
            newValidState.email = false;
        } else if (!validateEmail(formData.email)) {
            newErrors.push("Email inválido");
            newValidState.email = false;
        }

        if (!formData.cpf || formData.cpf.trim() === "") {
            newErrors.push("CPF é obrigatório");
            newValidState.cpf = false;
        } else if (!validateCPF(formData.cpf)) {
            newErrors.push("CPF inválido");
            newValidState.cpf = false;
        }

        if (!formData.telefone || formData.telefone.trim() === "") {
            newErrors.push("Telefone é obrigatório");
            newValidState.telefone = false;
        }

        if (!formData.cep || formData.cep.trim() === "") {
            newErrors.push("CEP é obrigatório");
            newValidState.cep = false;
        }

        if (!formData.logradouro || formData.logradouro.trim() === "") {
            newErrors.push("Logradouro é obrigatório");
            newValidState.logradouro = false;
        }

        if (!formData.numeroEndereco || formData.numeroEndereco.trim() === "") {
            newErrors.push("Número é obrigatório");
            newValidState.numeroEndereco = false;
        }

        if (!formData.bairro || formData.bairro.trim() === "") {
            newErrors.push("Bairro é obrigatório");
            newValidState.bairro = false;
        }

        if (!formData.cidade || formData.cidade.trim() === "") {
            newErrors.push("Cidade é obrigatória");
            newValidState.cidade = false;
        }

        if (!formData.uf || formData.uf.trim() === "") {
            newErrors.push("UF é obrigatória");
            newValidState.uf = false;
        } else if (formData.uf.length !== 2) {
            newErrors.push("UF deve ter 2 caracteres");
            newValidState.uf = false;
        }

        if (!formData.senha || formData.senha.trim() === "") {
            newErrors.push("Senha é obrigatória");
            newValidState.senha = false;
        } else if (formData.senha.length < 6) {
            newErrors.push("Senha deve ter no mínimo 6 caracteres");
            newValidState.senha = false;
        }

        if (!formData.confirmarSenha || formData.confirmarSenha.trim() === "") {
            newErrors.push("Confirmar Senha é obrigatório");
            newValidState.confirmarSenha = false;
        } else if (formData.senha !== formData.confirmarSenha) {
            newErrors.push("As senhas não coincidem");
            newValidState.confirmarSenha = false;
        }

        setFormDataValid(newValidState);
        setErrors(newErrors);
        setShowErrors(newErrors.length > 0);

        return newErrors.length === 0;
    };

    const handleSubmit = (e) =>{
        e.preventDefault();
        const isValid = validateForm();
        if(!isValid){
            return;
        }
        //Chama a API
    }

    return(
        <section className="register-patient">
            <img src={imageRegisterPatient} alt="logo" className="register-patient-image"/>
            <div className="register-patient-content">
                <img src="health_connect_logo.png" alt="logo"/> 
                <div className="register-patient-content-text">
                    <h1 className="title">Crie sua conta de Paciente</h1>
                </div>
                <form className="form-register-patient" >
                    <div>
                        <label htmlFor="nome">Nome Completo</label>
                        <input type="text" 
                                name="nome"
                                required
                                value={formData.nome}
                                onChange={handleChange}
                                className={showErrors && !formDataValid.nome ? 'error' : ''}/>
                    </div>
                    <div>
                        <label htmlFor="email">Email</label>
                        <input type="email" 
                                name="email"
                                required
                                value={formData.email}
                                onChange={handleChange}
                                className={emailError || (showErrors && !formDataValid.email) ? 'error' : ''}/>
                        {emailError && <span className="error-message">Email inválido</span>}
                    </div>
                    <div className="row-fields">
                        <div>
                            <label htmlFor="cpf">CPF</label>
                            <input type="text" 
                                    name="cpf"
                                    required
                                    value={formData.cpf}
                                    onChange={handleChange}
                                    className={cpfError || (showErrors && !formDataValid.cpf) ? 'error' : ''}
                                    placeholder="000.000.000-00"/>
                            {cpfError && <span className="error-message">CPF inválido</span>}
                        </div>
                        <div>
                            <label htmlFor="telefone">Telefone</label>
                            <input type="tel" 
                                    name="telefone"
                                    required
                                    value={formData.telefone}
                                    onChange={handleChange}
                                    placeholder="(00) 00000-0000"
                                    className={showErrors && !formDataValid.telefone ? 'error' : ''}/>
                        </div>
                    </div>
                    <div>
                        <label htmlFor="cep">CEP</label>
                        <input type="text" 
                                name="cep"
                                required
                                value={formData.cep}
                                onChange={handleChange}
                                placeholder="00000-000"
                                className={showErrors && !formDataValid.cep ? 'error' : ''}/>
                    </div>
                    <div>
                        <label htmlFor="logradouro">Logradouro</label>
                        <input type="text" 
                                name="logradouro"
                                required
                                value={formData.logradouro}
                                onChange={handleChange}
                                placeholder="Rua, Avenida, etc."
                                className={showErrors && !formDataValid.logradouro ? 'error' : ''}/>
                    </div>
                    <div className="row-fields">
                        <div>
                            <label htmlFor="numeroEndereco">Número</label>
                            <input type="text" 
                                    name="numeroEndereco"
                                    required
                                    value={formData.numeroEndereco}
                                    onChange={handleChange}
                                    className={showErrors && !formDataValid.numeroEndereco ? 'error' : ''}/>
                        </div>
                        <div>
                            <label htmlFor="complemento">Complemento</label>
                            <input type="text" 
                                    name="complemento"
                                    value={formData.complemento}
                                    onChange={handleChange}
                                    placeholder="Apto, Bloco, etc. (opcional)"/>
                        </div>
                    </div>
                    <div className="row-fields-three">
                        <div>
                            <label htmlFor="bairro">Bairro</label>
                            <input type="text" 
                                    name="bairro"
                                    required
                                    value={formData.bairro}
                                    onChange={handleChange}
                                    className={showErrors && !formDataValid.bairro ? 'error' : ''}/>
                        </div>
                        <div>
                            <label htmlFor="cidade">Cidade</label>
                            <input type="text" 
                                    name="cidade"
                                    required
                                    value={formData.cidade}
                                    onChange={handleChange}
                                    className={showErrors && !formDataValid.cidade ? 'error' : ''}/>
                        </div>
                        <div>
                            <label htmlFor="uf">UF</label>
                            <input type="text" 
                                    name="uf"
                                    required
                                    maxLength={2}
                                    value={formData.uf}
                                    onChange={handleChange}
                                    placeholder="SP"
                                    style={{textTransform: 'uppercase'}}
                                    className={showErrors && !formDataValid.uf ? 'error' : ''}/>
                        </div>
                    </div>
                    <div>
                        <label htmlFor="senha">Senha</label>
                        <input type="password"
                                name="senha"
                                required
                                value={formData.senha}
                                onChange={handleChange}
                                className={showErrors && !formDataValid.senha ? 'error' : ''}/>
                    </div>
                    <div>
                        <label htmlFor="confirmarSenha">Confirmar Senha</label>
                        <input type="password"
                                name="confirmarSenha"
                                required
                                value={formData.confirmarSenha}
                                onChange={handleChange}
                                className={showErrors && !formDataValid.confirmarSenha ? 'error' : ''}/>
                        {showErrors && !formDataValid.confirmarSenha && formData.senha !== formData.confirmarSenha && (
                            <span className="error-message">As senhas não coincidem</span>
                        )}
                    </div>
                    <ErrorModal 
                        isOpen={showErrors && errors.length > 0}
                        errors={errors}
                        onClose={() => setShowErrors(false)}
                    />
                    <button
                        type="submit"
                        onClick={handleSubmit}
                    >
                        Criar Conta
                    </button>
                    <div className="redirect-container">
                        <p className="text">Já tem uma conta? <Link to='/'>Faça login</Link></p>
                    </div>
                </form>   
            </div>
        </section>
    );
}
export default RegisterPatient;