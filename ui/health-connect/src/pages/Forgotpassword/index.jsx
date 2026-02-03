import { useState } from "react";
import ErrorModal from "../../components/ErrorModal";
function ForgotPassword() {
    const [email, setEmail] = useState("");
    const [emailError, setEmailError] = useState(false);
    const [showErrors, setShowErrors] = useState(false);
    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    };
    const validateForm = () => {
        if (email.length === 0 || !email.includes("@")) {
            setEmailError(true);
            setShowErrors(true);
            return false;
        }
        setShowErrors(false);
        return true;
    };
    const handleSubmit = (e) => {
        e.preventDefault();
        if (validateForm()) {
            console.log("E-mail enviado para redefinir a senha.");
        }
    };
    return (
        <div className="forgot-password">
            <h2>Esqueceu a Senha?</h2>
            {showErrors && <ErrorModal />}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Digite seu e-mail</label>
                    <input
                        type="email"
                        value={email}
                        onChange={handleEmailChange}
                        placeholder="Digite seu e-mail"
                    />
                    {emailError && <span>Por favor, insira um e-mail válido.</span>}
                </div>
                <button type="submit">Enviar e-mail de redefinição</button>
            </form>
        </div>
    );
}
export default ForgotPassword;
