function DashboardCard({ title, value }) {
    return (
        <div className="admin-card">
            <div className="admin-card-title">{title}</div>
            <div className="admin-card-value">{value}</div>
        </div>
    );
}

export default DashboardCard;
