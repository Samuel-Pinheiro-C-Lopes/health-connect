import "./success-styles.css";

function SuccessModal({ isOpen, message, onClose, title }) {
    if (!isOpen || !message) {
        return null;
    }

    const modalTitle = title || `✅ Sucesso`;

    return (
        <>
            <div className="success-modal-backdrop" onClick={onClose}></div>
            <div className="success-modal">
                <div className="success-modal-header">
                    <span>{modalTitle}</span>
                </div>
                
                <div className="success-modal-content">
                    <p>{message}</p>
                </div>

                <div className="success-modal-footer">
                    <button 
                        className="success-modal-ok-button"
                        onClick={onClose}
                    >
                        Continuar
                    </button>
                </div>
            </div>
        </>
    );
}

export default SuccessModal;