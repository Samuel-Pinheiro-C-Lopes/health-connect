import { useState, useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { STORAGE_KEYS, PATIENT_APPOINTMENTS_LABELS, PATIENT_HOME_LABELS } from "../../config/constants";
import { getSpecialtyLabel } from "../../utils/specialtyLabels";
import { listAppointmentsByPatientCurrentUser } from "../../query/appointmentQuery";
import { cancelAppointment } from "../../command/appointmentCommand";
import PatientHeader from "../../components/PatientHeader";
import ErrorModal from "../../components/ErrorModal";
import SuccessModal from "../../components/SuccessModal";
import "./styles.css";

function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return { date: "—", time: "—", dateTime: "—" };
    const d = new Date(dateTimeStr);
    const date = d.toLocaleDateString("pt-BR", { day: "2-digit", month: "2-digit", year: "numeric" });
    const time = d.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const isTomorrow =
        d.getDate() === tomorrow.getDate() &&
        d.getMonth() === tomorrow.getMonth() &&
        d.getFullYear() === tomorrow.getFullYear();
    const dayLabel = isTomorrow ? PATIENT_HOME_LABELS.AMANHA : date;
    return { date, time, dateTime: `${date}, ${time}`, dayLabel };
}

function getStatusLabel(status) {
    if (status === "ATTENTED") return PATIENT_APPOINTMENTS_LABELS.CONCLUIDA;
    if (status === "CANCELED") return PATIENT_APPOINTMENTS_LABELS.CANCELADA;
    return "Agendada";
}

function PatientAppointments() {
    const navigate = useNavigate();
    const [token, setToken] = useState(null);
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errors, setErrors] = useState([]);
    const [showErrors, setShowErrors] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");
    const [cancelLoadingId, setCancelLoadingId] = useState(null);

    useEffect(() => {
        const t = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
        if (!t) {
            navigate("/");
            return;
        }
        const role = (sessionStorage.getItem(STORAGE_KEYS.ROLE) || "").toUpperCase();
        if (role.includes("DOCTOR")) {
            navigate("/agenda", { replace: true });
            return;
        }
        setToken(t);
    }, [navigate]);

    const loadData = async () => {
        if (!token) return;
        setLoading(true);
        setErrors([]);
        try {
            const res = await listAppointmentsByPatientCurrentUser(token);
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

    const scheduledAppointments = useMemo(() => {
        if (!Array.isArray(appointments)) return [];
        const now = new Date();
        return appointments
            .filter((a) => a.status !== "CANCELED" && new Date(a.dateTime) > now)
            .sort((a, b) => new Date(a.dateTime) - new Date(b.dateTime));
    }, [appointments]);

    const historyAppointments = useMemo(() => {
        if (!Array.isArray(appointments)) return [];
        const now = new Date();
        return appointments
            .filter((a) => a.status === "CANCELED" || new Date(a.dateTime) <= now)
            .sort((a, b) => new Date(b.dateTime) - new Date(a.dateTime));
    }, [appointments]);

    const handleCancel = async (appointmentId) => {
        if (!token) return;
        setCancelLoadingId(appointmentId);
        setErrors([]);
        try {
            const res = await cancelAppointment(token, appointmentId, "PATIENT_GAVE_UP");
            if (res.success) {
                setSuccessMessage("Consulta cancelada.");
                setShowSuccess(true);
                loadData();
            } else {
                setErrors([res.message ?? "Erro ao cancelar"]);
                setShowErrors(true);
            }
        } catch (err) {
            setErrors([err.message ?? "Erro ao cancelar"]);
            setShowErrors(true);
        } finally {
            setCancelLoadingId(null);
        }
    };

    if (!token) return null;

    return (
        <div className="patient-appointments-layout">
            <PatientHeader activePage="minhas-consultas" />

            <main className="patient-appointments-main">
                <ErrorModal
                    isOpen={showErrors && errors.length > 0}
                    errors={errors}
                    onClose={() => setShowErrors(false)}
                />
                <SuccessModal
                    isOpen={showSuccess}
                    message={successMessage}
                    onClose={() => setShowSuccess(false)}
                />

                {loading ? (
                    <div className="patient-appointments-loading">Carregando...</div>
                ) : (
                    <>
                        <section className="patient-appointments-section">
                            <h2 className="patient-appointments-section-title">
                                {PATIENT_APPOINTMENTS_LABELS.CONSULTAS_AGENDADAS}
                            </h2>
                            <div className="patient-appointments-cards">
                                {scheduledAppointments.length === 0 ? (
                                    <p className="patient-appointments-empty">
                                        {PATIENT_APPOINTMENTS_LABELS.NENHUMA_AGENDADA}
                                    </p>
                                ) : (
                                    scheduledAppointments.map((a) => {
                                        const fmt = formatDateTime(a.dateTime);
                                        return (
                                            <div key={a.id} className="patient-appointments-card">
                                                <div className="patient-appointments-card-body">
                                                    <h3 className="patient-appointments-card-doctor">
                                                        {a.doctorName ?? "—"}
                                                    </h3>
                                                    <p className="patient-appointments-card-specialty">
                                                        - {getSpecialtyLabel(a.doctorSpecialty)}
                                                    </p>
                                                    <p className="patient-appointments-card-detail">
                                                        {PATIENT_APPOINTMENTS_LABELS.HORARIO}: {fmt.dayLabel}
                                                    </p>
                                                    <p className="patient-appointments-card-detail">
                                                        {fmt.dateTime}
                                                    </p>
                                                    <p className="patient-appointments-card-detail">
                                                        {PATIENT_APPOINTMENTS_LABELS.DATA_MARCADA}: {fmt.date}
                                                    </p>
                                                    <button
                                                        type="button"
                                                        className="patient-appointments-btn-cancel"
                                                        onClick={() => handleCancel(a.id)}
                                                        disabled={cancelLoadingId === a.id}
                                                    >
                                                        {PATIENT_APPOINTMENTS_LABELS.CANCELAR_CONSULTA}
                                                    </button>
                                                </div>
                                            </div>
                                        );
                                    })
                                )}
                            </div>
                        </section>

                        <section className="patient-appointments-section">
                            <h2 className="patient-appointments-section-title">
                                {PATIENT_APPOINTMENTS_LABELS.HISTORICO_CONSULTAS}
                            </h2>
                            <ul className="patient-appointments-history-list">
                                {historyAppointments.length === 0 ? (
                                    <li className="patient-appointments-empty">
                                        {PATIENT_APPOINTMENTS_LABELS.NENHUM_HISTORICO}
                                    </li>
                                ) : (
                                    historyAppointments.map((a) => {
                                        const fmt = formatDateTime(a.dateTime);
                                        const statusLabel = getStatusLabel(a.status);
                                        const isConcluida = a.status === "ATTENTED";
                                        return (
                                            <li key={a.id} className="patient-appointments-history-item">
                                                <div className="patient-appointments-history-info">
                                                    <h3 className="patient-appointments-history-doctor">
                                                        {a.doctorName ?? "—"}
                                                    </h3>
                                                    <p className="patient-appointments-history-specialty">
                                                        - {getSpecialtyLabel(a.doctorSpecialty)}
                                                    </p>
                                                </div>
                                                <div className="patient-appointments-history-meta">
                                                    <span className="patient-appointments-history-datetime">
                                                        {fmt.dateTime}
                                                    </span>
                                                </div>
                                                <div className="patient-appointments-history-status">
                                                    {isConcluida && (
                                                        <span className="patient-appointments-history-check">✓</span>
                                                    )}
                                                    <span
                                                        className={`patient-appointments-history-badge ${
                                                            isConcluida ? "concluida" : "cancelada"
                                                        }`}
                                                    >
                                                        {statusLabel}
                                                    </span>
                                                </div>
                                                <button
                                                    type="button"
                                                    className="patient-appointments-btn-details"
                                                    onClick={() => {}}
                                                >
                                                    {PATIENT_APPOINTMENTS_LABELS.VER_DETALHES}
                                                </button>
                                            </li>
                                        );
                                    })
                                )}
                            </ul>
                        </section>
                    </>
                )}
            </main>
        </div>
    );
}

export default PatientAppointments;
