import "./styles.css";

function ErrorModal({ isOpen, errors, onClose, title }) {
    if (!isOpen || !errors || errors.length === 0) {
        return null;
    }

    const errorCount = errors.length;
    const errorText = errorCount === 1 ? 'erro encontrado' : 'erros encontrados';
    const modalTitle = title || `⚠️ ${errorCount} ${errorText}`;

    return (
        <>
            <div className="error-modal-backdrop" onClick={onClose}></div>
            <div className="error-modal">
                <div className="error-modal-header">
                    <span>{modalTitle}</span>
                </div>
                <ul className="error-modal-list">
                    {errors.map((error, index) => (
                        <li key={index}>{error}</li>
                    ))}
                </ul>
                <div className="error-modal-footer">
                    <button 
                        className="error-modal-ok-button"
                        onClick={onClose}
                    >
                        Ok
                    </button>
                </div>
            </div>
        </>
    );
}

export default ErrorModal;
