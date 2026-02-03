import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import imageRegisterDoctor from "../../assets/images/image_register_doctor.png";
import ErrorModal from "../../components/ErrorModal";
import SuccessModal from "../../components/SuccessModal";

function RegisterDoctor() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        crm: '',
        specialty: ''
    });

    const [formDataValid, setFormDataValid] = useState({
        crm: true,
        specialty: true
    });

    const [errors, setErrors] = useState([]);
    const [showError, setShowError] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    
    const specialties = ["Ortopedia", "Cardiologia", "Ginecologia", "Dermatologia"];

    const handleChange = (e) => {
        const { name, value } = e.target;
        
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        if (formDataValid[name] === false) {
            setFormDataValid(prev => ({ ...prev, [name]: true }));
        }
    };

    const validateForm = () => {
        const newErrors = [];
        const newValidState = { crm: true, specialty: true };

        if (!formData.crm || !formData.crm.trim()) {
            newErrors.push("O campo CRM é obrigatório.");
            newValidState.crm = false;
        }

        if (!formData.specialty || formData.specialty === "") {
            newErrors.push("Por favor, selecione uma especialidade.");
            newValidState.specialty = false;
        }

        setFormDataValid(newValidState);
        setErrors(newErrors);

        return newErrors.length === 0;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        
        const isValid = validateForm();

        if (isValid) {
            setShowSuccess(true);
            setErrors([]);
            setShowError(false);
        } else {
            setShowError(true);
        }
    };

    const handleSuccessClose = () => {
        setShowSuccess(false);
        setFormData({ crm: '', specialty: '' });
        navigate('/agenda');
    };

    return (
        <section className="page-split-container">
            <img 
                src={imageRegisterDoctor} 
                alt="Cadastro Médico" 
                className="page-split-image"
            />
            <div className="page-split-content scrollable custom-scrollbar">
                <div className="page-split-content-text">
                    <h1 className="page-title">Credenciais Médicas</h1>
                    <p className="page-subtitle" style={{ marginTop: '8px', color: 'var(--color-text-muted)' }}>
                        Preencha as informações profissionais para concluir.
                    </p>
                </div>

                <form className="form-container" onSubmit={handleSubmit}>
                    <div className="form-field">
                        <label htmlFor="crm" className="form-label">CRM</label>
                        <input
                            type="text"
                            id="crm"
                            name="crm"
                            value={formData.crm}
                            onChange={handleChange}
                            placeholder="000000/UF"
                            maxLength={10}
                            className={`form-input ${showError && !formDataValid.crm ? 'error' : ''}`}
                        />
                    </div>
                    <div className="form-field">
                        <label htmlFor="specialty" className="form-label">Especialidade</label>
                        <select
                            id="specialty"
                            name="specialty"
                            value={formData.specialty}
                            onChange={handleChange}
                            className={`form-input ${showError && !formDataValid.specialty ? 'error' : ''}`}
                            style={{ backgroundColor: '#fff' }}
                        >
                            <option value="" disabled>Selecione a especialidade</option>
                            {specialties.map((spec, index) => (
                                <option key={index} value={spec}>{spec}</option>
                            ))}
                        </select>
                    </div>
                    <button
                        type="submit"
                        className="btn-primary"
                        style={{ marginTop: '20px' }}
                    >
                        Finalizar Cadastro
                    </button>
                    <div className="redirect-container">
                        <p className="page-text">
                            <Link to='/opcoes-login'>Voltar</Link>
                        </p>
                    </div>
                </form>
            </div>
            <ErrorModal 
                isOpen={showError && errors.length > 0} 
                errors={errors} 
                onClose={() => setShowError(false)} 
            />           
            <SuccessModal 
                isOpen={showSuccess} 
                message="Cadastro médico realizado com sucesso!" 
                onClose={handleSuccessClose} 
            />
        </section>
    );
}

export default RegisterDoctor;