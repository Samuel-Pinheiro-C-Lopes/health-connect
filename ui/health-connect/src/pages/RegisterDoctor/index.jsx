import { useState } from "react";
import { validateCPF } from "../../utils/validations";
import ErrorModal from "../../components/ErrorModal";
function RegisterDoctor() {
    const [cpf, setCpf] = useState("");
    const [crm, setCrm] = useState("");
    const [specialty, setSpecialty] = useState("");
    const [cpfError, setCpfError] = useState(false);
    const [crmError, setCrmError] = useState(false);
    const [specialtyError, setSpecialtyError] = useState(false);
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
    const handleCrmChange = (e) => {
        const newCrm = e.target.value;
        setCrm(newCrm);

        if (newCrm.length > 0) {
            setCrmError(false);
        }
    };
    const handleSpecialtyChange = (e) => {
        const newSpecialty = e.target.value;
        setSpecialty(newSpecialty);

        if (newSpecialty.length > 0) {
            setSpecialtyError(false);
        }
    };
    const validateForm = () => {
        let isValid = true;
        if (!validateCPF(cpf)) {
            setCpfError(true);
            isValid = false;
        }
        if (crm.length === 0) {
            setCrmError(true);
            isValid = false;
        }
        if (specialty.length === 0) {
            setSpecialtyError(true);
            isValid = false;
        }
        setShowErrors(!isValid);
        return isValid;
    };
    const handleSubmit = (e) => {
        e.preventDefault();
        if (validateForm()) {
            
            console.log("Cadastro do médico realizado com sucesso!");
        }
    };
    return (
        <div className="register-doctor">
            <h2>Cadastro de Médico</h2>
            {showErrors && <ErrorModal />}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>CPF</label>
                    <input
                        type="text"
                        value={cpf}
                        onChange={handleCpfChange}
                        placeholder="Digite o CPF"
                    />
                    {cpfError && <span>CPF inválido</span>}
                </div>
                <div>
                    <label>CRM</label>
                    <input
                        type="text"
                        value={crm}
                        onChange={handleCrmChange}
                        placeholder="Digite o CRM"
                    />
                    {crmError && <span>CRM é obrigatório</span>}
                </div>
                <div>
                    <label>Especialidade</label>
                    <input
                        type="text"
                        value={specialty}
                        onChange={handleSpecialtyChange}
                        placeholder="Digite a especialidade"
                    />
                    {specialtyError && <span>Especialidade é obrigatória</span>}
                </div>
                <button type="submit">Cadastrar</button>
            </form>
        </div>
    );
}
export default RegisterDoctor;
