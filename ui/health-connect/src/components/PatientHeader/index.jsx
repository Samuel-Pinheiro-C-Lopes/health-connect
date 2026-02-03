import { Link } from "react-router-dom";
import { PATIENT_HOME_LABELS } from "../../config/constants";
import "./styles.css";

export default function PatientHeader({ activePage = "inicio" }) {
    return (
        <header className="patient-header">
            <div className="patient-header-left">
                <Link to="/inicio" className="patient-header-logo-link">
                    <img src="/health_connect_logo.png" alt="" className="patient-header-logo" />
                    <span className="patient-header-app-name">{PATIENT_HOME_LABELS.APP_NAME}</span>
                </Link>
            </div>
            <nav className="patient-header-nav">
                <Link
                    to="/inicio"
                    className={`patient-header-nav-link ${activePage === "inicio" ? "active" : ""}`}
                >
                    {PATIENT_HOME_LABELS.INICIO}
                </Link>
                <Link
                    to="/minhas-consultas"
                    className={`patient-header-nav-link ${activePage === "minhas-consultas" ? "active" : ""}`}
                >
                    {PATIENT_HOME_LABELS.MINHAS_CONSULTAS}
                </Link>
                <Link
                    to="/perfil"
                    className={`patient-header-nav-link ${activePage === "perfil" ? "active" : ""}`}
                >
                    {PATIENT_HOME_LABELS.PERFIL}
                </Link>
            </nav>
            <div className="patient-header-right">
                <Link to="/" className="patient-header-avatar">↩️</Link>
            </div>
        </header>
    );
}
