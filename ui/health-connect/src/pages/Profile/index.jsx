import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { STORAGE_KEYS, PROFILE_LABELS, PATIENT_HOME_LABELS, DOCTOR_HOME_LABELS } from "../../config/constants";
import { getCurrentUser } from "../../query/userQuery";
import { updateUserAsync } from "../../command/userCommand";
import PatientHeader from "../../components/PatientHeader";
import ErrorModal from "../../components/ErrorModal";
import SuccessModal from "../../components/SuccessModal";
import "./styles.css";

function Profile() {
    const navigate = useNavigate();
    const [token, setToken] = useState(null);
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [errors, setErrors] = useState([]);
    const [showErrors, setShowErrors] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const [activeTab, setActiveTab] = useState("dados");
    const [form, setForm] = useState({
        name: "",
        email: "",
        password: "",
        phone: "",
        postalCode: "",
        avenue: "",
        complement: "",
        number: "",
        city: "",
        district: "",
        state: "",
    });

    const role = typeof window !== "undefined" ? sessionStorage.getItem(STORAGE_KEYS.ROLE) || "" : "";
    const isDoctor = role.toUpperCase().includes("DOCTOR") || role.toUpperCase().includes("MEDICO");

    useEffect(() => {
        const t = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
        if (!t) {
            navigate("/");
            return;
        }
        setToken(t);
    }, [navigate]);

    const loadUser = async () => {
        if (!token) return;
        setLoading(true);
        setErrors([]);
        try {
            const res = await getCurrentUser(token);
            if (res.success && res.data) {
                const u = res.data;
                setUser(u);
                setForm({
                    name: u.name ?? "",
                    email: u.email ?? "",
                    password: "",
                    phone: u.phone ?? "",
                    postalCode: u.postalCode ?? "",
                    avenue: u.avenue ?? "",
                    complement: u.complement ?? "",
                    number: u.number ?? "",
                    city: u.city ?? "",
                    district: u.district ?? "",
                    state: u.state ?? "",
                });
            } else if (!res.success) setErrors([res.message ?? "Erro ao carregar perfil"]);
        } catch (err) {
            setErrors([err.message ?? "Erro ao carregar dados"]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (token) loadUser();
    }, [token]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!token || !user) return;
        if (!form.password.trim()) {
            setErrors(["Senha é obrigatória para salvar. Use sua senha atual se não quiser alterá-la."]);
            setShowErrors(true);
            return;
        }
        setSaving(true);
        setErrors([]);
        try {
            const payload = {
                email: form.email,
                password: form.password,
                name: form.name,
                phone: form.phone,
                postalCode: form.postalCode,
                avenue: form.avenue,
                complement: form.complement,
                number: form.number,
                city: form.city,
                district: form.district,
                state: form.state,
                doctorId: user.doctorId ?? null,
                patientId: user.patientId ?? null,
            };
            const res = await updateUserAsync(token, user.id, payload);
            if (res.success) {
                setShowSuccess(true);
                setForm((prev) => ({ ...prev, password: "" }));
            } else {
                setErrors([res.message ?? "Erro ao salvar"]);
                setShowErrors(true);
            }
        } catch (err) {
            setErrors([err.message ?? "Erro ao salvar"]);
            setShowErrors(true);
        } finally {
            setSaving(false);
        }
    };

    if (!token) return null;

    const roleLabel = isDoctor
        ? PROFILE_LABELS.MEDICO_DESDE.replace("{date}", "Jan 2023")
        : PROFILE_LABELS.PACIENTE_DESDE.replace("{date}", "Jan 2023");

    return (
        <div className={`profile-layout ${isDoctor ? "profile-layout-doctor" : ""}`}>
            {!isDoctor && (
                <PatientHeader activePage="perfil" />
            )}
            {isDoctor && (
                <aside className="profile-doctor-sidebar">
                    <Link to="/agenda" className="profile-doctor-sidebar-logo">
                        <img src="/health_connect_logo.png" alt="" className="profile-doctor-logo-img" />
                        <span className="profile-doctor-logo-text">{DOCTOR_HOME_LABELS.APP_NAME}</span>
                    </Link>
                    <nav className="profile-doctor-nav">
                        <Link to="/agenda" className="profile-doctor-nav-link">
                            <span className="profile-doctor-nav-icon">📅</span>
                            {DOCTOR_HOME_LABELS.MINHA_AGENDA}
                        </Link>
                        <Link to="/agenda" className="profile-doctor-nav-link">
                            <span className="profile-doctor-nav-icon">👥</span>
                            {DOCTOR_HOME_LABELS.PACIENTES}
                        </Link>
                        <Link to="/perfil" className="profile-doctor-nav-link active">
                            <span className="profile-doctor-nav-icon">👤</span>
                            {DOCTOR_HOME_LABELS.PERFIL}
                        </Link>
                    </nav>
                </aside>
            )}

            <main className={`profile-main ${isDoctor ? "profile-main-with-sidebar" : ""}`}>
                <h1 className="profile-title">{PROFILE_LABELS.MEU_PERFIL}</h1>

                <div className="profile-content">
                    <aside className="profile-sidebar-card">
                        <div className="profile-sidebar-name">{user?.name ?? "—"}</div>
                        <div className="profile-sidebar-role">{roleLabel}</div>
                        <nav className="profile-sidebar-tabs">
                            <button
                                type="button"
                                className={`profile-sidebar-tab ${activeTab === "dados" ? "active" : ""}`}
                                onClick={() => setActiveTab("dados")}
                            >
                                {PROFILE_LABELS.DADOS_PESSOAIS}
                            </button>
                            <button
                                type="button"
                                className={`profile-sidebar-tab ${activeTab === "seguranca" ? "active" : ""}`}
                                onClick={() => setActiveTab("seguranca")}
                            >
                                {PROFILE_LABELS.SEGURANCA}
                            </button>
                        </nav>
                    </aside>

                    <div className="profile-form-card">
                        <ErrorModal
                            isOpen={showErrors && errors.length > 0}
                            errors={errors}
                            onClose={() => setShowErrors(false)}
                        />
                        <SuccessModal
                            isOpen={showSuccess}
                            message="Alterações salvas com sucesso."
                            onClose={() => setShowSuccess(false)}
                        />

                        {loading ? (
                            <div className="profile-loading">Carregando...</div>
                        ) : activeTab === "dados" ? (
                            <form onSubmit={handleSubmit} className="profile-form">
                                <div className="profile-field">
                                    <label htmlFor="profile-name">{PROFILE_LABELS.NOME_COMPLETO}</label>
                                    <input
                                        id="profile-name"
                                        name="name"
                                        type="text"
                                        value={form.name}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-email">{PROFILE_LABELS.EMAIL}</label>
                                    <input
                                        id="profile-email"
                                        name="email"
                                        type="email"
                                        value={form.email}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-phone">{PROFILE_LABELS.TELEFONE}</label>
                                    <input
                                        id="profile-phone"
                                        name="phone"
                                        type="text"
                                        value={form.phone}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-password">
                                        Senha (obrigatória para salvar; use a atual se não quiser alterar)
                                    </label>
                                    <input
                                        id="profile-password"
                                        name="password"
                                        type="password"
                                        value={form.password}
                                        onChange={handleChange}
                                        className="profile-input"
                                        placeholder="••••••••"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-postalCode">{PROFILE_LABELS.CEP}</label>
                                    <input
                                        id="profile-postalCode"
                                        name="postalCode"
                                        type="text"
                                        value={form.postalCode}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-avenue">{PROFILE_LABELS.LOGRADOURO}</label>
                                    <input
                                        id="profile-avenue"
                                        name="avenue"
                                        type="text"
                                        value={form.avenue}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-complement">{PROFILE_LABELS.COMPLEMENTO}</label>
                                    <input
                                        id="profile-complement"
                                        name="complement"
                                        type="text"
                                        value={form.complement}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-number">{PROFILE_LABELS.NUMERO}</label>
                                    <input
                                        id="profile-number"
                                        name="number"
                                        type="text"
                                        value={form.number}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-city">{PROFILE_LABELS.CIDADE}</label>
                                    <input
                                        id="profile-city"
                                        name="city"
                                        type="text"
                                        value={form.city}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-district">{PROFILE_LABELS.BAIRRO}</label>
                                    <input
                                        id="profile-district"
                                        name="district"
                                        type="text"
                                        value={form.district}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <div className="profile-field">
                                    <label htmlFor="profile-state">{PROFILE_LABELS.ESTADO}</label>
                                    <input
                                        id="profile-state"
                                        name="state"
                                        type="text"
                                        value={form.state}
                                        onChange={handleChange}
                                        className="profile-input"
                                    />
                                </div>
                                <button type="submit" className="btn-primary profile-submit" disabled={saving}>
                                    {saving ? "Salvando..." : PROFILE_LABELS.SALVAR_ALTERACOES}
                                </button>
                            </form>
                        ) : (
                            <div className="profile-seguranca">
                                <p className="profile-muted">Alteração de senha e opções de segurança em breve.</p>
                            </div>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
}

export default Profile;
