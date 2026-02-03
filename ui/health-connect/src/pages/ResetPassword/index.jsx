import { useState } from "react";
import { useHistory } from "react-router-dom";
import ErrorModal from "../../components/ErrorModal";
import { resetPassword } from "../../command/resetPasswordCommand";
import { getAuthHeader } from "../../utils/httpUtils";
function ResetPassword() {
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordError, setPasswordError] = useState(false);
    const [confirmPasswordError, setConfirmPasswordError] = useState(false);
    const [showErrors, setShowErrors] = useState(false);
    const history = useHistory();

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
    };

    const validateForm = () => {
        let isValid = true;
        if (password.length < 6) {
            setPasswordError(true);
            isValid = false;
        } else {
            setPasswordError(false);
        }
        if (confirmPassword !== password) {
            setConfirmPasswordError(true);
            isValid = false;
        } else {
            setConfirmPasswordError(false);
        }
        setShowErrors(!isValid);
        return isValid;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (validateForm()) {
            try {
                const token = localStorage.getItem('auth_token'); 
                const response = await resetPassword(token, password); 
                if (response.success) {
                    console.log("Senha redefinida com sucesso.");
                    history.push("/login");  
                } else {
                    console.log("Erro ao redefinir a senha.");
                }
            } catch (error) {
                console.error("Erro ao enviar solicitação:", error);
            }
        }
    };
    return (
        <div className="reset-password">
            <h2>Redefinir Senha</h2>
            {showErrors && <ErrorModal />}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Nova Senha</label>
                    <input
                        type="password"
                        value={password}
                        onChange={handlePasswordChange}
                        placeholder="Digite a nova senha"
                    />
                    {passwordError && <span>A senha deve ter pelo menos 6 caracteres.</span>}
                </div>
                <div>
                    <label>Confirmar Senha</label>
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={handleConfirmPasswordChange}
                        placeholder="Confirme a nova senha"
                    />
                    {confirmPasswordError && <span>As senhas não coincidem.</span>}
                </div>
                <button type="submit">Redefinir Senha</button>
            </form>
        </div>
    );
}
export default ResetPassword;
