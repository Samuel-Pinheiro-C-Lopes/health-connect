import { useState, useEffect, useMemo } from "react";
import { useNavigate, Link } from "react-router-dom";
import { STORAGE_KEYS, DOCTOR_HOME_LABELS } from "../../config/constants";
import { listAppointmentsByDoctorCurrentUser } from "../../query/appointmentQuery";
import "./styles.css";

function formatDate(d) {
    return d.toLocaleDateString("pt-BR", { weekday: "short", day: "2-digit", month: "2-digit" });
}

function formatTime(dateTimeStr) {
    if (!dateTimeStr) return "—";
    return new Date(dateTimeStr).toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
}

function getStatusLabel(status) {
    if (status === "SCHEDULED") return "Agendada";
    if (status === "ATTENTED") return "Concluída";
    if (status === "CANCELED") return "Cancelada";
    return status ?? "—";
}
function DoctorHome() {
    const navigate = useNavigate();
    const [token, setToken] = useState(null);
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errors, setErrors] = useState([]);
    const [weekOffset, setWeekOffset] = useState(0);

    useEffect(() => {
        const t = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
        if (!t) {
            navigate("/");
            return;
        }
        const role = (sessionStorage.getItem(STORAGE_KEYS.ROLE) || "").toUpperCase();
        if (role.includes("PATIENT") || role.includes("PACIENTE")) {
            navigate("/", { replace: true });
            return;
        }
        setToken(t);
    }, [navigate]);

    const loadData = async () => {
        if (!token) return;
        setLoading(true);
        setErrors([]);
        try {
            const res = await listAppointmentsByDoctorCurrentUser(token);
            if (res.success && res.data) setAppointments(res.data);
            else if (!res.success) setErrors([res.message ?? "Erro ao carregar consultas"]);
        } catch (err) {
            setErrors([err.message ?? "Erro ao carregar dados"]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (token) loadData();
    }, [token]);

    const today = useMemo(() => {
        const d = new Date();
        d.setHours(0, 0, 0, 0);
        return d;
    }, []);

    const todayCount = useMemo(() => {
        if (!Array.isArray(appointments)) return 0;
        return appointments.filter((a) => {
            const dt = new Date(a.dateTime);
            return (
                dt.getDate() === today.getDate() &&
                dt.getMonth() === today.getMonth() &&
                dt.getFullYear() === today.getFullYear() &&
                a.status !== "CANCELED"
            );
        }).length;
    }, [appointments, today]);

    const sortedAppointments = useMemo(() => {
        if (!Array.isArray(appointments)) return [];
        return [...appointments].sort((a, b) => new Date(a.dateTime) - new Date(b.dateTime));
    }, [appointments]);

    if (!token) return null;

    return (
        <div className="doctor-home-layout">
            <aside className="doctor-home-sidebar">
                <Link to="/agenda" className="doctor-home-sidebar-logo">
                    <img src="/health_connect_logo.png" alt="" className="doctor-home-logo-img" />
                    <span className="doctor-home-logo-text">{DOCTOR_HOME_LABELS.APP_NAME}</span>
                </Link>
                <nav className="doctor-home-nav">
                    <Link to="/agenda" className="doctor-home-nav-link active">
                        <span className="doctor-home-nav-icon">📅</span>
                        {DOCTOR_HOME_LABELS.MINHA_AGENDA}
                    </Link>
                    <Link to="/agenda" className="doctor-home-nav-link">
                        <span className="doctor-home-nav-icon">👥</span>
                        {DOCTOR_HOME_LABELS.PACIENTES}
                    </Link>
                    <Link to="/perfil" className="doctor-home-nav-link">
                        <span className="doctor-home-nav-icon">👤</span>
                        {DOCTOR_HOME_LABELS.PERFIL}
                    </Link>
                </nav>
            </aside>

            <div className="doctor-home-main">
                <header className="doctor-home-header">
                    <div className="doctor-home-header-left">
                        <button
                            type="button"
                            className="doctor-home-header-arrow"
                            onClick={() => setWeekOffset((o) => o - 1)}
                            aria-label="Semana anterior"
                        >
                            ←
                        </button>
                        <h1 className="doctor-home-title">{DOCTOR_HOME_LABELS.HORARIO_AGENDA}</h1>
                        <button
                            type="button"
                            className="doctor-home-header-arrow"
                            onClick={() => setWeekOffset((o) => o + 1)}
                            aria-label="Próxima semana"
                        >
                            →
                        </button>
                    </div>
                    <div className="doctor-home-header-right">
                        <span className="doctor-home-header-bell">🔔</span>
                        <Link to="/" className="doctor-home-header-avatar">↩️</Link>
                        <span className="doctor-home-today-count">
                            {DOCTOR_HOME_LABELS.HOJE_CONSULTAS.replace("{count}", String(todayCount))}
                        </span>
                    </div>
                </header>

                <div className="doctor-home-content">
                    {errors.length > 0 && (
                        <div className="doctor-home-errors">
                            {errors.map((e, i) => (
                                <p key={i}>{e}</p>
                            ))}
                        </div>
                    )}
                    {loading ? (
                        <div className="doctor-home-loading">Carregando...</div>
                    ) : (
                        <div className="doctor-home-table-wrap">
                            <table className="doctor-home-table">
                                <thead>
                                    <tr>
                                        <th>{DOCTOR_HOME_LABELS.DATA}</th>
                                        <th>{DOCTOR_HOME_LABELS.HORA}</th>
                                        <th>{DOCTOR_HOME_LABELS.PACIENTE}</th>
                                        <th>{DOCTOR_HOME_LABELS.STATUS}</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {sortedAppointments.length === 0 ? (
                                        <tr>
                                            <td colSpan="4" className="doctor-home-empty">
                                                {DOCTOR_HOME_LABELS.NENHUMA_CONSULTA}
                                            </td>
                                        </tr>
                                    ) : (
                                        sortedAppointments.map((a) => (
                                            <tr key={a.id}>
                                                <td>{formatDate(new Date(a.dateTime))}</td>
                                                <td>{formatTime(a.dateTime)}</td>
                                                <td>{a.patientName ?? "—"}</td>
                                                <td>{getStatusLabel(a.status)}</td>
                                            </tr>
                                        ))
                                    )}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default DoctorHome;
