import { Link } from "react-router-dom";
import imageRegisterPatient from "../../assets/images/image_register_patient.png";
import { useState } from "react";
import { validateCPF } from "../../utils/validations";
import ErrorModal from "../../components/ErrorModal";

function RegisterPatient(){
    const [cpf, setCpf] = useState("");

    const [cpfError, setCpfError] = useState(false);
    const [showErrors, setShowErrors] = useState(false);

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
        if (!validateCPF(cpf)) {
            setCpfError(true);
            setShowErrors(true);
            return false;
        }
        
        setShowErrors(false);
        return true;
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
        <section className="page-split-container">
            <img src={imageRegisterPatient} alt="logo" className="page-split-image"/>
            <div className="page-split-content scrollable custom-scrollbar">
                <img src="health_connect_logo.png" alt="logo"/> 
                <div className="page-split-content-text">
                    <h1 className="page-title">Crie sua conta de Paciente</h1>
                </div>
                <form className="form-container" >
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
                        isOpen={showErrors}
                        errors={["CPF inválido"]}
                        onClose={() => setShowErrors(false)}
                    />
                    <button
                        type="submit"
                        onClick={handleSubmit}
                        className="btn-primary"
                    >
                        Criar Conta
                    </button>
                    <div className="redirect-container">
                        <p className="page-text">Já tem uma conta? <Link to='/'>Faça login</Link></p>
                    </div>
                </form>   
            </div>
        </section>
    );
}
export default RegisterPatient;