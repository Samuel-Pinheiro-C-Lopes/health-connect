import { getSpecialtyLabel } from "../../utils/specialtyLabels";
import { PATIENT_HOME_LABELS } from "../../config/constants";
import "./styles.css";

function DoctorCard({ doctor, nextSlotLabel, onSchedule }) {
    const displayName = doctor.name ? `Dr(a). ${doctor.name}` : "—";
    const specialty = getSpecialtyLabel(doctor.specialty);

    return (
        <div className="doctor-card">
            <div className="doctor-card-body">
                <h3 className="doctor-card-name">{displayName}</h3>
                <p className="doctor-card-specialty">- {specialty}</p>
                <button
                    type="button"
                    className="doctor-card-schedule-btn"
                    onClick={() => onSchedule?.(doctor)}
                >
                    {nextSlotLabel ?? PATIENT_HOME_LABELS.AGENDAR}
                </button>
            </div>
        </div>
    );
}

export default DoctorCard;
