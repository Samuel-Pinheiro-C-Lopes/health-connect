export const SPECIALTY_LABELS = {
    Orthopedics: "Ortopedia",
    Cardiology: "Cardiologia",
    Gynecology: "Ginecologia",
    Dermatology: "Dermatologia",
};

export function getSpecialtyLabel(specialty) {
    return SPECIALTY_LABELS[specialty] ?? specialty ?? "—";
}
