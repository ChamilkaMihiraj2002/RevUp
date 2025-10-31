
import Layout from "../../components/Layout/layout";

const CustomerDashboard = () => {
  return (
    <Layout
      role="customer"
      username="John Doe"
      subtitle="Customer Dashboard"
    >
      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Service Dashboard</h1>
        <p className="text-gray-600 mb-6">Track your vehicle services and appointments</p>

        <div className="grid gap-6">
          {/* Your customer dashboard content will go here */}
          <div className="bg-white rounded-lg shadow p-4">
            <h2 className="text-lg font-semibold mb-2">Current Appointments</h2>
            <p>2020 Toyota Corolla - Scheduled Maintenance</p>
            {/* Add more appointment details */}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default CustomerDashboard;
