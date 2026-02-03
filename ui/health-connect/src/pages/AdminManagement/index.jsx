import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { STORAGE_KEYS, ADMIN_LABELS } from "../../config/constants";
import DashboardCard from "../../components/DashboardCard";
import ErrorModal from "../../components/ErrorModal";
import { getSpecialtyLabel } from "../../utils/specialtyLabels";
import {
    listDoctorsActiveFull,
    listDoctorsActive,
    listDoctorsPending,
    approveDoctor,
    rejectDoctor,
    deleteDoctor,
} from "../../command/doctorCommand";
import { listPatients, deletePatient } from "../../command/patientCommand";
import { listAppointments } from "../../query/appointmentQuery";
import "./styles.css";

function formatDateISO(date) {
    return date.toISOString().split("T")[0];
}

function AdminManagement() {
    const navigate = useNavigate();
    const [token, setToken] = useState(null);
    const [doctors, setDoctors] = useState([]);
    const [activeDoctorsCount, setActiveDoctorsCount] = useState(0);
    const [patients, setPatients] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [pendingDoctors, setPendingDoctors] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errors, setErrors] = useState([]);
    const [showErrors, setShowErrors] = useState(false);
    const [actionLoading, setActionLoading] = useState({});

    useEffect(() => {
        const t = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
        if (!t) {
            navigate("/");
            return;
        }
        setToken(t);
    }, [navigate]);

    const loadData = async () => {
        if (!token) return;
        setLoading(true);
        setErrors([]);
        try {
            const [docsRes, activeRes, patsRes, appsRes, pendingRes] = await Promise.all([
                listDoctorsActiveFull(token),
                listDoctorsActive(token),
                listPatients(token),
                listAppointments(token),
                listDoctorsPending(token),
            ]);

            if (docsRes.success && docsRes.data) setDoctors(docsRes.data);
            if (activeRes.success && Array.isArray(activeRes.data))
                setActiveDoctorsCount(activeRes.data.length);
            else if (!docsRes.success) setErrors((e) => [...e, docsRes.message]);

            if (patsRes.success && patsRes.data) setPatients(patsRes.data);
            else if (!patsRes.success) setErrors((e) => [...e, patsRes.message]);

            if (appsRes.success && appsRes.data) setAppointments(appsRes.data);
            else if (!appsRes.success) setErrors((e) => [...e, appsRes.message]);

            if (pendingRes.success && pendingRes.data) setPendingDoctors(pendingRes.data);
            else if (!pendingRes.success) setErrors((e) => [...e, pendingRes.message]);
        } catch (err) {
            setErrors((e) => [...e, err.message || "Erro ao carregar dados"]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (token) loadData();
    }, [token]);

    const todayStr = formatDateISO(new Date());
    const appointmentsToday = Array.isArray(appointments)
        ? appointments.filter(
              (a) =>
                  a.dateTime &&
                  a.dateTime.startsWith &&
                  a.dateTime.startsWith(todayStr) &&
                  a.status !== "CANCELED"
          ).length
        : 0;

    const handleDeactivateDoctor = async (doctorId) => {
        if (!token) return;
        setActionLoading((prev) => ({ ...prev, [`doctor-${doctorId}`]: true }));
        try {
            const res = await deleteDoctor(token, doctorId, false);
            if (res.success) await loadData();
            else setErrors([res.message]);
            setShowErrors(!res.success);
        } finally {
            setActionLoading((prev) => ({ ...prev, [`doctor-${doctorId}`]: false }));
        }
    };

    const handleDeactivatePatient = async (patientId) => {
        if (!token) return;
        setActionLoading((prev) => ({ ...prev, [`patient-${patientId}`]: true }));
        try {
            const res = await deletePatient(token, patientId, false);
            if (res.success) await loadData();
            else setErrors([res.message]);
            setShowErrors(!res.success);
        } finally {
            setActionLoading((prev) => ({ ...prev, [`patient-${patientId}`]: false }));
        }
    };

    const handleApproveDoctor = async (doctorId) => {
        if (!token) return;
        setActionLoading((prev) => ({ ...prev, [`approve-${doctorId}`]: true }));
        try {
            const res = await approveDoctor(token, doctorId);
            if (res.success) await loadData();
            else setErrors([res.message]);
            setShowErrors(!res.success);
        } finally {
            setActionLoading((prev) => ({ ...prev, [`approve-${doctorId}`]: false }));
        }
    };

    const handleRejectDoctor = async (doctorId) => {
        if (!token) return;
        setActionLoading((prev) => ({ ...prev, [`reject-${doctorId}`]: true }));
        try {
            const res = await rejectDoctor(token, doctorId);
            if (res.success) await loadData();
            else setErrors([res.message]);
            setShowErrors(!res.success);
        } finally {
            setActionLoading((prev) => ({ ...prev, [`reject-${doctorId}`]: false }));
        }
    };

    const fmt = (v) => (v ?? "—");

    if (!token) return null;

    return (
        <div className="admin-layout">
            <header className="admin-header">{ADMIN_LABELS.HEADER_TITLE}</header>
            <main className="admin-content">
                <ErrorModal
                    isOpen={showErrors && errors.length > 0}
                    errors={errors}
                    onClose={() => setShowErrors(false)}
                />
                {loading ? (
                    <div className="admin-loading">Carregando...</div>
                ) : (
                    <>
                        <div className="admin-dashboard-cards">
                            <DashboardCard
                                title={ADMIN_LABELS.TOTAL_PATIENTS}
                                value={patients.length.toLocaleString("pt-BR")}
                            />
                            <DashboardCard
                                title={ADMIN_LABELS.ACTIVE_DOCTORS}
                                value={activeDoctorsCount.toLocaleString("pt-BR")}
                            />
                            <DashboardCard
                                title={ADMIN_LABELS.APPOINTMENTS_TODAY}
                                value={appointmentsToday.toLocaleString("pt-BR")}
                            />
                            <DashboardCard
                                title={ADMIN_LABELS.PENDING_APPROVALS}
                                value={pendingDoctors.length.toLocaleString("pt-BR")}
                            />
                        </div>
                        <div className="admin-main-grid">
                            <div className="admin-tables-section">
                                <div className="admin-table-container">
                                    <div className="admin-table-title">{ADMIN_LABELS.DOCTORS}</div>
                                    <div className="admin-table-wrapper">
                                        <table className="admin-table">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>User ID</th>
                                                    <th>CRM</th>
                                                    <th>Especialidade</th>
                                                    <th>Nome</th>
                                                    <th>Telefone</th>
                                                    <th>CEP</th>
                                                    <th>Av.</th>
                                                    <th>Comp.</th>
                                                    <th>Cidade</th>
                                                    <th>UF</th>
                                                    <th>Ações</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {doctors.length === 0 ? (
                                                    <tr>
                                                        <td colSpan="12" className="admin-empty-state">
                                                            Nenhum médico cadastrado
                                                        </td>
                                                    </tr>
                                                ) : (
                                                    doctors.map((d) => (
                                                        <tr key={d.id}>
                                                            <td>{fmt(d.id)}</td>
                                                            <td>{fmt(d.userId)}</td>
                                                            <td>{fmt(d.crm)}</td>
                                                            <td>{getSpecialtyLabel(d.specialty)}</td>
                                                            <td>{fmt(d.name)}</td>
                                                            <td>{fmt(d.phone)}</td>
                                                            <td>{fmt(d.postalCode)}</td>
                                                            <td>{fmt(d.avenue)}</td>
                                                            <td>{fmt(d.complement)}</td>
                                                            <td>{fmt(d.city)}</td>
                                                            <td>{fmt(d.state)}</td>
                                                            <td>
                                                                <button
                                                                    className="admin-btn-danger"
                                                                    onClick={() =>
                                                                        handleDeactivateDoctor(d.id)
                                                                    }
                                                                    disabled={
                                                                        actionLoading[
                                                                            `doctor-${d.id}`
                                                                        ]
                                                                    }
                                                                >
                                                                    {ADMIN_LABELS.DEACTIVATE}
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    ))
                                                )}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div className="admin-table-container">
                                    <div className="admin-table-title">
                                        {ADMIN_LABELS.PATIENTS}
                                    </div>
                                    <div className="admin-table-wrapper">
                                        <table className="admin-table">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>User ID</th>
                                                    <th>Nome</th>
                                                    <th>Email</th>
                                                    <th>Telefone</th>
                                                    <th>CEP</th>
                                                    <th>Av.</th>
                                                    <th>Comp.</th>
                                                    <th>No.</th>
                                                    <th>Cidade</th>
                                                    <th>UF</th>
                                                    <th>Ações</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {patients.length === 0 ? (
                                                    <tr>
                                                        <td colSpan="13" className="admin-empty-state">
                                                            Nenhum paciente cadastrado
                                                        </td>
                                                    </tr>
                                                ) : (
                                                    patients.map((p) => (
                                                        <tr key={p.id}>
                                                            <td>{fmt(p.id)}</td>
                                                            <td>{fmt(p.userId)}</td>
                                                            <td>{fmt(p.name)}</td>
                                                            <td>{fmt(p.email)}</td>
                                                            <td>{fmt(p.phone)}</td>
                                                            <td>{fmt(p.postalCode)}</td>
                                                            <td>{fmt(p.avenue)}</td>
                                                            <td>{fmt(p.complement)}</td>
                                                            <td>{fmt(p.number)}</td>
                                                            <td>{fmt(p.city)}</td>
                                                            <td>{fmt(p.state)}</td>
                                                            <td>
                                                                <button
                                                                    className="admin-btn-danger"
                                                                    onClick={() =>
                                                                        handleDeactivatePatient(p.id)
                                                                    }
                                                                    disabled={
                                                                        actionLoading[
                                                                            `patient-${p.id}`
                                                                        ]
                                                                    }
                                                                >
                                                                    {ADMIN_LABELS.DEACTIVATE}
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    ))
                                                )}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div className="admin-pending-section">
                                <div className="admin-pending-title">
                                    {ADMIN_LABELS.RECENT_DOCTOR_REQUESTS}
                                </div>
                                <div className="admin-pending-list">
                                    {pendingDoctors.length === 0 ? (
                                        <div className="admin-empty-state">
                                            Nenhuma solicitação pendente
                                        </div>
                                    ) : (
                                        pendingDoctors.map((d) => (
                                            <div key={d.id} className="admin-pending-item">
                                                <span className="admin-pending-name">
                                                    {fmt(d.name)}
                                                </span>
                                                <div className="admin-pending-actions">
                                                    <button
                                                        className="admin-btn-approve"
                                                        onClick={() => handleApproveDoctor(d.id)}
                                                        disabled={actionLoading[`approve-${d.id}`]}
                                                    >
                                                        {ADMIN_LABELS.APPROVE}
                                                    </button>
                                                    <button
                                                        className="admin-btn-danger"
                                                        onClick={() => handleRejectDoctor(d.id)}
                                                        disabled={actionLoading[`reject-${d.id}`]}
                                                    >
                                                        {ADMIN_LABELS.REJECT}
                                                    </button>
                                                </div>
                                            </div>
                                        ))
                                    )}
                                </div>
                            </div>
                        </div>
                    </>
                )}
            </main>
        </div>
    );
}

export default AdminManagement;
