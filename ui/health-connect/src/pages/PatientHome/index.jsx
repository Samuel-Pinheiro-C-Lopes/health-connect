import { useState, useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { STORAGE_KEYS, PATIENT_HOME_LABELS } from "../../config/constants";
import { getSpecialtyLabel } from "../../utils/specialtyLabels";
import { SPECIALTY_LABELS } from "../../utils/specialtyLabels";
import { listDoctorsActiveFull } from "../../query/doctorQuery";
import { listAppointmentsByPatientCurrentUser } from "../../query/appointmentQuery";
import { getCurrentUser } from "../../query/userQuery";
import { createAppointment, cancelAppointment } from "../../command/appointmentCommand";
import DoctorCard from "../../components/DoctorCard";
import PatientHeader from "../../components/PatientHeader";
import ErrorModal from "../../components/ErrorModal";
import SuccessModal from "../../components/SuccessModal";
import "./styles.css";

function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return { date: "—", time: "—", full: "" };
    const d = new Date(dateTimeStr);
    const date = d.toLocaleDateString("pt-BR", { day: "2-digit", month: "2-digit", year: "2-digit" });
    const time = d.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const isTomorrow =
        d.getDate() === tomorrow.getDate() &&
        d.getMonth() === tomorrow.getMonth() &&
        d.getFullYear() === tomorrow.getFullYear();
    const dayLabel = isTomorrow ? PATIENT_HOME_LABELS.AMANHA : date;
    return { date, time, full: `${dayLabel}, ${time}`, dayLabel };
}

function PatientHome() {
    const navigate = useNavigate();
    const [token, setToken] = useState(null);
    const [doctors, setDoctors] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const [search, setSearch] = useState("");
    const [selectedSpecialties, setSelectedSpecialties] = useState({});
    const [loading, setLoading] = useState(true);
    const [errors, setErrors] = useState([]);
    const [showErrors, setShowErrors] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");
    const [scheduleModal, setScheduleModal] = useState({ open: false, doctor: null });
    const [scheduleDate, setScheduleDate] = useState("");
    const [scheduleTime, setScheduleTime] = useState("14:00");
    const [scheduleLoading, setScheduleLoading] = useState(false);
    const [cancelLoading, setCancelLoading] = useState(false);

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
            const [docsRes, appsRes, userRes] = await Promise.all([
                listDoctorsActiveFull(token),
                listAppointmentsByPatientCurrentUser(token),
                getCurrentUser(token),
            ]);

            if (docsRes.success && docsRes.data) setDoctors(docsRes.data);
            else if (!docsRes.success) setErrors((e) => [...e, docsRes.message ?? "Erro ao carregar médicos"]);

            if (appsRes.success && appsRes.data) setAppointments(appsRes.data);
            else if (!appsRes.success) setErrors((e) => [...e, appsRes.message ?? "Erro ao carregar consultas"]);

            if (userRes.success && userRes.data) setCurrentUser(userRes.data);
            else if (!userRes.success) setErrors((e) => [...e, userRes.message ?? "Erro ao carregar usuário"]);
        } catch (err) {
            setErrors((e) => [...e, err.message ?? "Erro ao carregar dados"]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (token) loadData();
    }, [token]);

    const upcomingAppointments = useMemo(() => {
        if (!Array.isArray(appointments)) return [];
        const now = new Date();
        return appointments
            .filter((a) => a.status !== "CANCELED" && new Date(a.dateTime) > now)
            .sort((a, b) => new Date(a.dateTime) - new Date(b.dateTime));
    }, [appointments]);

    const nextAppointment = upcomingAppointments[0] ?? null;

    const filteredDoctors = useMemo(() => {
        let list = doctors;
        const hasSpecialty = Object.values(selectedSpecialties).some(Boolean);
        if (hasSpecialty) {
            list = list.filter((d) => selectedSpecialties[d.specialty]);
        }
        if (search.trim()) {
            const q = search.trim().toLowerCase();
            list = list.filter(
                (d) =>
                    (d.name && d.name.toLowerCase().includes(q)) ||
                    (d.specialty && getSpecialtyLabel(d.specialty).toLowerCase().includes(q))
            );
        }
        return list;
    }, [doctors, search, selectedSpecialties]);

    const toggleSpecialty = (key) => {
        setSelectedSpecialties((prev) => ({ ...prev, [key]: !prev[key] }));
    };

    const openScheduleModal = (doctor) => {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        setScheduleDate(tomorrow.toISOString().slice(0, 10));
        setScheduleTime("14:00");
        setScheduleModal({ open: true, doctor });
    };

    const closeScheduleModal = () => {
        setScheduleModal({ open: false, doctor: null });
    };

    const handleScheduleSubmit = async (e) => {
        e.preventDefault();
        if (!scheduleModal.doctor || !currentUser?.patientId || !token) return;
        const dateTime = `${scheduleDate}T${scheduleTime}:00`;
        setScheduleLoading(true);
        setErrors([]);
        try {
            const res = await createAppointment(token, {
                dateTime,
                patientId: currentUser.patientId,
                doctorId: scheduleModal.doctor.id,
            });
            if (res.success) {
                setSuccessMessage("Consulta agendada com sucesso.");
                setShowSuccess(true);
                closeScheduleModal();
                loadData();
            } else {
                setErrors([res.message ?? "Erro ao agendar"]);
                setShowErrors(true);
            }
        } catch (err) {
            setErrors([err.message ?? "Erro ao agendar"]);
            setShowErrors(true);
        } finally {
            setScheduleLoading(false);
        }
    };

    const handleCancelAppointment = async () => {
        if (!nextAppointment || !token) return;
        setCancelLoading(true);
        setErrors([]);
        try {
            const res = await cancelAppointment(token, nextAppointment.id, "PATIENT_GAVE_UP");
            if (res.success) {
                setSuccessMessage("Agendamento cancelado.");
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
            setCancelLoading(false);
        }
    };

    if (!token) return null;

    const specialtyKeys = Object.keys(SPECIALTY_LABELS);

    return (
        <div className="patient-home-layout">
            <PatientHeader activePage="inicio" />

            <div className="patient-home-banner">
                <div className="patient-home-search-wrap">
                    <span className="patient-home-search-icon">🔍</span>
                    <input
                        type="text"
                        className="patient-home-search"
                        placeholder={PATIENT_HOME_LABELS.SEARCH_PLACEHOLDER}
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </div>
            </div>

            <div className="patient-home-main">
                <aside className="patient-home-sidebar patient-home-sidebar-left">
                    <div className="patient-home-filter-block">
                        <h3 className="patient-home-filter-title">{PATIENT_HOME_LABELS.ESPECIALIDADE}</h3>
                        <div className="patient-home-check-list">
                            {specialtyKeys.map((key) => (
                                <label key={key} className="patient-home-check-item">
                                    <input
                                        type="checkbox"
                                        checked={!!selectedSpecialties[key]}
                                        onChange={() => toggleSpecialty(key)}
                                    />
                                    <span>{SPECIALTY_LABELS[key]}</span>
                                </label>
                            ))}
                        </div>
                    </div>
                    <div className="patient-home-filter-block">
                        <h3 className="patient-home-filter-title">{PATIENT_HOME_LABELS.DATA}</h3>
                        <div className="patient-home-check-list">
                            <label className="patient-home-check-item">
                                <input type="checkbox" readOnly />
                                <span>Hoje</span>
                            </label>
                            <label className="patient-home-check-item">
                                <input type="checkbox" readOnly />
                                <span>{PATIENT_HOME_LABELS.AMANHA}</span>
                            </label>
                        </div>
                    </div>
                </aside>

                <main className="patient-home-content">
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
                        <div className="patient-home-loading">{PATIENT_HOME_LABELS.SEARCH_PLACEHOLDER}</div>
                    ) : (
                        <div className="patient-home-cards">
                            {filteredDoctors.map((d) => (
                                <DoctorCard
                                    key={d.id}
                                    doctor={d}
                                    nextSlotLabel={`${PATIENT_HOME_LABELS.AMANHA}, 14h`}
                                    onSchedule={openScheduleModal}
                                />
                            ))}
                        </div>
                    )}
                    {!loading && filteredDoctors.length === 0 && (
                        <div className="patient-home-empty">Nenhum médico encontrado.</div>
                    )}
                </main>

                <aside className="patient-home-sidebar patient-home-sidebar-right">
                    <div className="patient-home-next-card">
                        <h3 className="patient-home-next-title">{PATIENT_HOME_LABELS.SUA_PROXIMA_CONSULTA}</h3>
                        {nextAppointment ? (
                            <>
                                <p className="patient-home-next-doctor">{nextAppointment.doctorName}</p>
                                <p className="patient-home-next-specialty">
                                    - {getSpecialtyLabel(nextAppointment.doctorSpecialty)}
                                </p>
                                <p className="patient-home-next-detail">
                                    {PATIENT_HOME_LABELS.HORARIO}: {formatDateTime(nextAppointment.dateTime).dayLabel}
                                </p>
                                <p className="patient-home-next-detail">
                                    {PATIENT_HOME_LABELS.CONSULTA}: {formatDateTime(nextAppointment.dateTime).time}
                                </p>
                                <p className="patient-home-next-detail">
                                    {PATIENT_HOME_LABELS.DATA_MARCADA}: {formatDateTime(nextAppointment.dateTime).date}
                                </p>
                                <button
                                    type="button"
                                    className="patient-home-cancel-btn"
                                    onClick={handleCancelAppointment}
                                    disabled={cancelLoading}
                                >
                                    {PATIENT_HOME_LABELS.CANCELAR_AGENDAMENTO}
                                </button>
                            </>
                        ) : (
                            <p className="patient-home-next-empty">{PATIENT_HOME_LABELS.NENHUMA_CONSULTA}</p>
                        )}
                    </div>
                </aside>
            </div>

            {scheduleModal.open && scheduleModal.doctor && (
                <div className="patient-home-modal-overlay" onClick={closeScheduleModal}>
                    <div className="patient-home-modal" onClick={(e) => e.stopPropagation()}>
                        <h3 className="patient-home-modal-title">
                            Agendar com {scheduleModal.doctor.name} -{" "}
                            {getSpecialtyLabel(scheduleModal.doctor.specialty)}
                        </h3>
                        <form onSubmit={handleScheduleSubmit}>
                            <div className="patient-home-modal-field">
                                <label htmlFor="schedule-date">Data</label>
                                <input
                                    id="schedule-date"
                                    type="date"
                                    value={scheduleDate}
                                    onChange={(e) => setScheduleDate(e.target.value)}
                                    required
                                    min={new Date().toISOString().slice(0, 10)}
                                />
                            </div>
                            <div className="patient-home-modal-field">
                                <label htmlFor="schedule-time">Horário</label>
                                <input
                                    id="schedule-time"
                                    type="time"
                                    value={scheduleTime}
                                    onChange={(e) => setScheduleTime(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="patient-home-modal-actions">
                                <button type="button" className="btn-primary patient-home-modal-cancel" onClick={closeScheduleModal}>
                                    Cancelar
                                </button>
                                <button type="submit" className="btn-primary" disabled={scheduleLoading}>
                                    {scheduleLoading ? "Agendando..." : "Confirmar"}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default PatientHome;
