import { Link } from "react-router-dom";
import imageRegisterPatient from "../../assets/images/image_register_patient.png";
import "./styles.css";
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
        <section className="register-patient">
            <img src={imageRegisterPatient} alt="logo" className="register-patient-image"/>
            <div className="register-patient-content">
                <img src="health_connect_logo.png" alt="logo"/> 
                <div className="register-patient-content-text">
                    <h1 className="title">Crie sua conta de Paciente</h1>
                </div>
                <form className="form-register-patient" >
                    <div>
                        <label htmlFor="cpf">CPF</label>
                        <input type="text" 
                                name="cpf"
                                required
                                value={cpf}
                                onChange={handleCpfChange}
                                className={cpfError || showErrors ? 'error' : ''}
                                placeholder="Somente números"/>
                        {cpfError && <span className="error-message">CPF inválido</span>}
                    </div>
                    <ErrorModal 
                        isOpen={showErrors}
                        errors={["CPF inválido"]}
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