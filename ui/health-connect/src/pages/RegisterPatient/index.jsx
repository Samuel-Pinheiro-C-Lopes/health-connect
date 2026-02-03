import { Link, useNavigate } from "react-router-dom";
import imageRegisterPatient from "../../assets/images/image_register_patient.png";
import { useState } from "react";
import { validateCPF } from "../../utils/validations";
import ErrorModal from "../../components/ErrorModal";
import SuccessModal from "../../components/SuccessModal";
import { createPatient } from "../../command/patientCommand";
import { updateUserAsync, assignRolesAsync } from "../../command/userCommand";
import { STORAGE_KEYS } from "../../config/constants";

function RegisterPatient(){
    const navigate = useNavigate();
    const [cpf, setCpf] = useState("");

    const [cpfError, setCpfError] = useState(false);
    const [showErrors, setShowErrors] = useState(false);
    const [errors, setErrors] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");

    const handleCpfChange = (e) => {
        const newValue = e.target.value; 
        setCpf(newValue);

        if (newValue.length > 0) {
            setCpfError(!validateCPF(newValue));
        } else {
            setCpfError(false);
        }
    };

    const validateForm = () => {    
        const newErrors = [];
        if (!validateCPF(cpf)) {
            newErrors.push("CPF inválido");
        }

        setErrors(newErrors);
        setShowErrors(newErrors.length > 0);
        setCpfError(newErrors.length > 0);

        return newErrors.length === 0;
    };

    const handleSubmit = async (e) =>{
        e.preventDefault();
        const isValid = validateForm();
        if(!isValid){
            return;
        }

        setLoading(true);
        setShowErrors(false);
        setErrors([]);

        const token = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
        const personId = sessionStorage.getItem(STORAGE_KEYS.PERSON_ID);

        if (!token) {
            setErrors(["Usuário não autenticado. Faça login e tente novamente."]);
            setShowErrors(true);
            setLoading(false);
            return;
        }

        try {
            const patientData = {
                cpf: cpf.replace(/\D/g, '')
            };

            if (personId) {
                patientData.personId = Number(personId);
            }

            const result = await createPatient(token, patientData);
            if (!result.success) {
                setErrors([result.message || 'Erro ao criar paciente.']);
                setShowErrors(true);
                setLoading(false);
                return;
            }

            const createdPatientId = result.data?.id;
            if (personId) {
                await updateUserAsync(token, Number(personId), { patientId: createdPatientId });
                try {
                    await assignRolesAsync(token, Number(personId), ['PATIENT']);
                } catch (e) {
                    console.warn('Não foi possível atribuir papel:', e.message || e);
                }
            }

            setSuccessMessage('Cadastro de paciente realizado com sucesso!');
            setShowSuccess(true);
        } catch (err) {
            setErrors([err.message || 'Erro desconhecido']);
            setShowErrors(true);
        } finally {
            setLoading(false);
        }
    }

    const handleSuccessClose = () => {
        setShowSuccess(false);
        navigate('/home-paciente');
    }

    return(
        <section className="page-split-container">
            <img src={imageRegisterPatient} alt="logo" className="page-split-image"/>
            <div className="page-split-content scrollable custom-scrollbar">
                <img src="health_connect_logo.png" alt="logo"/> 
                <div className="page-split-content-text">
                    <h1 className="page-title">Crie sua conta de Paciente</h1>
                </div>
                <form className="form-container" onSubmit={handleSubmit}>
                    <div className="form-field">
                        <label htmlFor="cpf" className="form-label">CPF</label>
                        <input type="text" 
                                name="cpf"
                                required
                                value={cpf}
                                onChange={handleCpfChange}
                                className={`form-input ${cpfError || showErrors ? 'error' : ''}`}
                                placeholder="Somente números"/>
                        {cpfError && <span className="form-error-message">CPF inválido</span>}
                    </div>
                    <ErrorModal 
                        isOpen={showErrors && errors.length > 0}
                        errors={errors}
                        onClose={() => setShowErrors(false)}
                    />
                    <SuccessModal
                        isOpen={showSuccess}
                        message={successMessage}
                        onClose={handleSuccessClose}
                    />
                    {loading && <p className="loading-text">Enviando requisição... ⏳</p>}
                    <button
                        type="submit"
                        className="btn-primary"
                        disabled={loading}
                    >
                        {loading ? 'Carregando...' : 'Criar Conta'}
                    </button>
                    <div className="redirect-container">
                        <p className="page-text"><Link to='/cadastrar'>Voltar</Link></p>
                    </div>
                </form>   
            </div>
        </section>
    );
}
export default RegisterPatient;