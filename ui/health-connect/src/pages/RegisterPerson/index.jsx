import { Link, useNavigate } from "react-router-dom";
import imageRegisterPerson from "../../assets/images/image_register_person.png";
import { useState } from "react";
import { validateEmail } from "../../utils/validations";
import ErrorModal from "../../components/ErrorModal";
import SuccessModal from "../../components/SuccessModal";
import { registerUserAsync, loginAsync } from "../../command/userCommand";
import { STORAGE_KEYS } from "../../config/constants";

function RegisterPerson(){
    const [formData, setFormData] = useState({
        nome: "",
        email: "",
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
        nome: true, email: true, telefone: true, logradouro: true,
        bairro: true, cidade: true, uf: true, cep: true, senha: true, confirmarSenha: true,
    };

    const [formDataValid, setFormDataValid] = useState(initialValidState);
    const [emailError, setEmailError] = useState(false);
    const [showErrors, setShowErrors] = useState(false);
    const [errors, setErrors] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        if (name === 'email') {
            const isValid = value === '' || validateEmail(value);
            setEmailError(!isValid && value.length > 0);
        }
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

    const handleSubmit = async (e) => {
        e.preventDefault();
        const isValid = validateForm();
        if (!isValid) {
            return;
        }

        setLoading(true);
        setShowErrors(false);

        try {
            const userData = {
                email: formData.email,
                password: formData.senha,
                doctorId: null,
                patientId: null,
                name: formData.nome,
                phone: formData.telefone ? formData.telefone.replace(/\D/g, '') : '',
                postalCode: formData.cep.replace(/\D/g, ''),
                avenue: formData.logradouro,
                complement: formData.complemento?.trim() || '-',
                number: formData.numeroEndereco,
                city: formData.cidade,
                district: formData.bairro,
                state: formData.uf ? formData.uf.toUpperCase().trim() : ''
            };

            const registerResult = await registerUserAsync(userData);
            if (!registerResult.success) {
                setErrors([registerResult.message || 'Erro ao criar conta']);
                setShowErrors(true);
                setLoading(false);
                return;
            }

            const loginResult = await loginAsync(formData.email, formData.senha);
            if (!loginResult.success) {
                setErrors([loginResult.message || 'Erro ao fazer login após cadastro']);
                setShowErrors(true);
                setLoading(false);
                return;
            }

            const token = loginResult.data.token;
            sessionStorage.setItem(STORAGE_KEYS.TOKEN, token);
            const userId = registerResult.data?.id;
            if (userId) {
                sessionStorage.setItem(STORAGE_KEYS.PERSON_ID, String(userId));
            }

            setSuccessMessage('Cadastro realizado com sucesso!');
            setShowSuccess(true);
        } catch (err) {
            setErrors([err.message || 'Erro desconhecido']);
            setShowErrors(true);
        } finally {
            setLoading(false);
        }
    }

    return(
        <section className="page-split-container">
            <img src={imageRegisterPerson} alt="logo" className="page-split-image"/>
            <div className="page-split-content scrollable custom-scrollbar">
                <img src="health_connect_logo.png" alt="logo"/> 
                <div className="page-split-content-text">
                    <h1 className="page-title">Crie sua conta</h1>
                </div>
                <form className="form-container" >
                    <div className="form-field">
                        <label htmlFor="nome" className="form-label">Nome Completo</label>
                        <input type="text" 
                                name="nome"
                                required
                                value={formData.nome}
                                onChange={handleChange}
                                className={`form-input ${showErrors && !formDataValid.nome ? 'error' : ''}`}/>
                    </div>
                    <div className="form-field">
                        <label htmlFor="email" className="form-label">Email</label>
                        <input type="email" 
                                name="email"
                                required
                                value={formData.email}
                                onChange={handleChange}
                                className={`form-input ${emailError || (showErrors && !formDataValid.email) ? 'error' : ''}`}/>
                        {emailError && <span className="form-error-message">Email inválido</span>}
                    </div>
                    <div className="form-field">
                        <label htmlFor="telefone" className="form-label">Telefone</label>
                        <input type="tel" 
                                name="telefone"
                                required
                                value={formData.telefone}
                                onChange={handleChange}
                                placeholder="(00) 00000-0000"
                                className={`form-input ${showErrors && !formDataValid.telefone ? 'error' : ''}`}/>
                    </div>
                    <div className="form-field">
                        <label htmlFor="cep" className="form-label">CEP</label>
                        <input type="text" 
                                name="cep"
                                required
                                value={formData.cep}
                                onChange={handleChange}
                                placeholder="00000-000"
                                className={`form-input ${showErrors && !formDataValid.cep ? 'error' : ''}`}/>
                    </div>
                    <div className="form-field">
                        <label htmlFor="logradouro" className="form-label">Logradouro</label>
                        <input type="text" 
                                name="logradouro"
                                required
                                value={formData.logradouro}
                                onChange={handleChange}
                                placeholder="Rua, Avenida, etc."
                                className={`form-input ${showErrors && !formDataValid.logradouro ? 'error' : ''}`}/>
                    </div>
                    <div className="row-fields">
                        <div className="form-field">
                            <label htmlFor="numeroEndereco" className="form-label">Número</label>
                            <input type="text" 
                                    name="numeroEndereco"
                                    required
                                    value={formData.numeroEndereco}
                                    onChange={handleChange}
                                    className={`form-input ${showErrors && !formDataValid.numeroEndereco ? 'error' : ''}`}/>
                        </div>
                        <div className="form-field">
                            <label htmlFor="complemento" className="form-label">Complemento</label>
                            <input type="text" 
                                    name="complemento"
                                    value={formData.complemento}
                                    onChange={handleChange}
                                    placeholder="Apto, Bloco, etc. (opcional)"
                                    className="form-input"/>
                        </div>
                    </div>
                    <div className="row-fields-three">
                        <div className="form-field">
                            <label htmlFor="bairro" className="form-label">Bairro</label>
                            <input type="text" 
                                    name="bairro"
                                    required
                                    value={formData.bairro}
                                    onChange={handleChange}
                                    className={`form-input ${showErrors && !formDataValid.bairro ? 'error' : ''}`}/>
                        </div>
                        <div className="form-field">
                            <label htmlFor="cidade" className="form-label">Cidade</label>
                            <input type="text" 
                                    name="cidade"
                                    required
                                    value={formData.cidade}
                                    onChange={handleChange}
                                    className={`form-input ${showErrors && !formDataValid.cidade ? 'error' : ''}`}/>
                        </div>
                        <div className="form-field">
                            <label htmlFor="uf" className="form-label">UF</label>
                            <input type="text" 
                                    name="uf"
                                    required
                                    maxLength={2}
                                    value={formData.uf}
                                    onChange={handleChange}
                                    placeholder="SP"
                                    style={{textTransform: 'uppercase'}}
                                    className={`form-input ${showErrors && !formDataValid.uf ? 'error' : ''}`}/>
                        </div>
                    </div>
                    <div className="form-field">
                        <label htmlFor="senha" className="form-label">Senha</label>
                        <input type="password"
                                name="senha"
                                required
                                value={formData.senha}
                                onChange={handleChange}
                                className={`form-input ${showErrors && !formDataValid.senha ? 'error' : ''}`}/>
                    </div>
                    <div className="form-field">
                        <label htmlFor="confirmarSenha" className="form-label">Confirmar Senha</label>
                        <input type="password"
                                name="confirmarSenha"
                                required
                                value={formData.confirmarSenha}
                                onChange={handleChange}
                                className={`form-input ${showErrors && !formDataValid.confirmarSenha ? 'error' : ''}`}/>
                        {showErrors && !formDataValid.confirmarSenha && formData.senha !== formData.confirmarSenha && (
                            <span className="form-error-message">As senhas não coincidem</span>
                        )}
                    </div>
                    <ErrorModal 
                        isOpen={showErrors && errors.length > 0}
                        errors={errors}
                        onClose={() => setShowErrors(false)}
                    />
                    {loading && <p className="loading-text">Enviando requisição... ⏳</p>}
                    <SuccessModal
                        isOpen={showSuccess}
                        message={successMessage}
                        onClose={() => { setShowSuccess(false); navigate('/'); }}
                    />
                    <button
                        type="submit"
                        onClick={handleSubmit}
                        className="btn-primary"
                        disabled={loading}
                    >
                        {loading ? 'Carregando...' : 'Criar Conta'}
                    </button>
                    <div className="redirect-container">
                        <p className="page-text">Já tem uma conta? <Link to='/'>Faça login</Link></p>
                    </div>
                </form>   
            </div>
        </section>
    );
}

export default RegisterPerson;