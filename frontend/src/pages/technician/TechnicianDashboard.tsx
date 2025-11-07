"use client";

import { useState, useEffect } from "react";
import { Badge } from "@/components/UI/Badge";
import { Button } from "@/components/UI/Button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/UI/Card";
import { Textarea } from "@/components/UI/Textarea";
import { useAuth } from "@/contexts/authContext/authContext";
import { useNavigate } from "react-router-dom";
import {
  getAppointmentsByCustomerId,
  updateAppointment,
  type AppointmentResponse,
} from "@/services/appointmentService";
import {
  getVehiclesByUserId,
  type VehicleResponse,
} from "@/services/vehicleService";
import {
  Car,
  CheckCircle,
  User,
  LayoutList,
  CheckSquare,
  Calendar,
  LogOut,
  Play,
} from "lucide-react";
import { Link } from "react-router-dom";

export default function TechnicianDashboard() {
  const { userData, logout, accessToken } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("assignments");
  const [appointments, setAppointments] = useState<AppointmentResponse[]>([]);
  const [vehicles, setVehicles] = useState<Record<number, VehicleResponse>>({});
  const [loading, setLoading] = useState(true);
  const [notes, setNotes] = useState<Record<number, string>>({});

  const handleLogout = async () => {
    try {
      await logout();
      navigate("/login");
    } catch (error) {
      console.error("Logout error:", error);
    }
  };

  // Mock project requests (replace with API data later)
  const [projectRequests, setProjectRequests] = useState([
    {
      id: 1,
      name: "Nuwan Perera",
      phone: "0771234567",
      description: "Need a full engine inspection and oil change.",
    },
    {
      id: 2,
      name: "Kavindya Abeykoon",
      phone: "0769876543",
      description: "AC not cooling properly; possible gas leak.",
    },
  ]);
  const [appointmentDate, setAppointmentDate] = useState<
    Record<number, string>
  >({});
  const [appointmentHours, setAppointmentHours] = useState<
    Record<number, string>
  >({});

  // Fetch appointments and vehicles
  const fetchData = async () => {
    if (!userData?.userId || !accessToken) return;

    try {
      // For now, get all appointments (later filter by technician)
      const appointmentsData = await getAppointmentsByCustomerId(
        userData.userId,
        accessToken
      );
      setAppointments(appointmentsData);

      // Fetch vehicle data for each appointment
      const vehicleIds = [
        ...new Set(appointmentsData.map((apt) => apt.vehicleId)),
      ];
      const vehiclePromises = vehicleIds.map(async (vehicleId) => {
        try {
          const vehiclesData = await getVehiclesByUserId(
            userData.userId,
            accessToken
          );
          const vehicle = vehiclesData.find((v) => v.vehicleId === vehicleId);
          return vehicle ? { [vehicleId]: vehicle } : {};
        } catch (err) {
          console.error(`Error fetching vehicle ${vehicleId}:`, err);
          return {};
        }
      });

      const vehicleResults = await Promise.all(vehiclePromises);
      const vehiclesMap = Object.assign({}, ...vehicleResults);
      setVehicles(vehiclesMap);
    } catch (error) {
      console.error("Error fetching data:", error);
    } finally {
      setLoading(false);
    }
  };

  // Initial data fetch
  useEffect(() => {
    fetchData();
  }, [userData, accessToken]);

  // Polling for real-time updates (every 15 seconds)
  useEffect(() => {
    const interval = setInterval(() => {
      fetchData();
    }, 15000); // 15 seconds

    return () => clearInterval(interval);
  }, [userData, accessToken]);

  const handleStartService = async (appointmentId: number) => {
    if (!accessToken) return;

    try {
      await updateAppointment(
        appointmentId,
        { status: "ONGOING" },
        accessToken
      );
      // Refresh data to show updated status
      await fetchData();
      alert("Service started!");
    } catch (error) {
      console.error("Error starting service:", error);
      alert("Failed to start service");
    }
  };

  const handleCompleteService = async (appointmentId: number) => {
    if (!accessToken) return;

    try {
      await updateAppointment(
        appointmentId,
        { status: "COMPLETED" },
        accessToken
      );
      // Refresh data to show updated status
      await fetchData();
      alert("Service completed! Customer has been notified.");
    } catch (error) {
      console.error("Error completing service:", error);
      alert("Failed to complete service");
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "COMPLETED":
        return "bg-green-500/10 text-green-600 border-green-200";
      case "ONGOING":
        return "bg-blue-500/10 text-blue-600 border-blue-200";
      case "SCHEDULED":
        return "bg-yellow-500/10 text-yellow-600 border-yellow-200";
      case "CANCELLED":
        return "bg-red-500/10 text-red-600 border-red-200";
      default:
        return "bg-gray-500/10 text-gray-600 border-gray-200";
    }
  };

  const scheduledAppointments = appointments.filter(
    (apt) => apt.status === "SCHEDULED" || apt.status === "ONGOING"
  );
  const completedAppointments = appointments.filter(
    (apt) => apt.status === "COMPLETED"
  );

  if (loading) {
      return (
          <div className="min-h-screen bg-background flex items-center justify-center">
              <div className="text-center">
                  <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
                  <p className="mt-4 text-muted-foreground">Loading appointments...</p>
              </div>
          </div>
      )
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Navigation */}
      <nav className="border-b border-border/50 backdrop-blur-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-3">
              <Link to="/" className="flex items-center space-x-3">
                <div className="bg-primary rounded-lg p-2">
                  <Car className="h-6 w-6 text-primary-foreground" />
                </div>
                <span className="text-xl font-bold">RevUp</span>
              </Link>
              <Badge variant="secondary" className="ml-4">
                Technician Portal
              </Badge>
            </div>
            <div className="flex items-center space-x-4">
              <div className="text-right">
                <p className="text-sm font-medium">
                  {userData?.name || "Technician"}
                </p>
                <p className="text-xs text-muted-foreground">
                  Updates every 15 seconds
                </p>
              </div>
              <Button variant="ghost" size="sm">
                <User className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={handleLogout}
                className="hover:bg-red-50 hover:text-red-800 text-red-600"
              >
                <LogOut className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </div>
      </nav>

      <div className="flex">
        <aside className="w-64 border-r border-border/50 bg-muted/30 min-h-screen">
          <div className="p-6 space-y-2">
            <h2 className="text-lg font-semibold mb-4">Dashboard</h2>
            <button
              onClick={() => setActiveTab("assignments")}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                activeTab === "assignments"
                  ? "bg-primary text-primary-foreground"
                  : "text-foreground hover:bg-muted"
              }`}
            >
              <LayoutList className="h-5 w-5" />
              <span>My Assignments</span>
            </button>
            <button
              onClick={() => setActiveTab("completed")}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                activeTab === "completed"
                  ? "bg-primary text-primary-foreground"
                  : "text-foreground hover:bg-muted"
              }`}
            >
              <CheckSquare className="h-5 w-5" />
              <span>Completed Today</span>
            </button>
            <button
              onClick={() => setActiveTab("schedule")}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                activeTab === "schedule"
                  ? "bg-primary text-primary-foreground"
                  : "text-foreground hover:bg-muted"
              }`}
            >
              <Calendar className="h-5 w-5" />
              <span>Schedule</span>
            </button>
            <button
              onClick={() => setActiveTab("projectRequests")}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                activeTab === "projectRequests"
                  ? "bg-primary text-primary-foreground"
                  : "text-foreground hover:bg-muted"
              }`}
            >
              <LayoutList className="h-5 w-5" />
              <span>Project Requests</span>
            </button>
          </div>
        </aside>

        {/* Main Content */}
        <main className="flex-1">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            {/* Header */}
            <div className="mb-8">
              <h1 className="text-3xl font-bold text-balance">
                Service Dashboard
              </h1>
              <p className="text-muted-foreground mt-2">
                Manage your assigned appointments
              </p>
            </div>

            {/* Content based on active tab */}
            {activeTab === "assignments" && (
              <div className="space-y-6">
                {scheduledAppointments.length === 0 ? (
                  <Card>
                    <CardContent className="py-12">
                      <div className="text-center">
                        <LayoutList className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                        <h3 className="text-lg font-medium mb-2">
                          No Active Assignments
                        </h3>
                        <p className="text-muted-foreground">
                          You don't have any scheduled or ongoing appointments
                        </p>
                      </div>
                    </CardContent>
                  </Card>
                ) : (
                  scheduledAppointments.map((appointment) => {
                    const vehicle = vehicles[appointment.vehicleId];
                    return (
                      <Card
                        key={appointment.appointmentId}
                        className="overflow-hidden"
                      >
                        <CardHeader>
                          <div className="flex items-center justify-between">
                            <div>
                              <CardTitle className="flex items-center space-x-3">
                                <span>
                                  {vehicle
                                    ? `${vehicle.model} (${vehicle.year})`
                                    : "Loading..."}
                                </span>
                                <Badge
                                  variant="outline"
                                  className={getStatusColor(appointment.status)}
                                >
                                  {appointment.status}
                                </Badge>
                              </CardTitle>
                              <CardDescription>
                                {vehicle &&
                                  `Registration: ${vehicle.registrationNo} • Color: ${vehicle.color}`}
                                <br />
                                Scheduled:{" "}
                                {new Date(
                                  appointment.scheduledStart
                                ).toLocaleString()}
                              </CardDescription>
                            </div>
                          </div>
                        </CardHeader>

                        <CardContent className="space-y-6">
                          {/* Appointment Notes */}
                          <div className="space-y-2">
                            <label className="text-sm font-medium">
                              Appointment Notes
                            </label>
                            <Textarea
                              placeholder="Add notes about this appointment..."
                              value={notes[appointment.appointmentId] || ""}
                              onChange={(e) =>
                                setNotes((prev) => ({
                                  ...prev,
                                  [appointment.appointmentId]: e.target.value,
                                }))
                              }
                              rows={3}
                            />
                          </div>

                          {/* Appointment Actions */}
                          <div className="flex justify-end pt-4 border-t">
                            {appointment.status === "SCHEDULED" && (
                              <Button
                                onClick={() =>
                                  handleStartService(appointment.appointmentId)
                                }
                                className="bg-blue-600 hover:bg-blue-700"
                              >
                                <Play className="h-4 w-4 mr-2" />
                                Start Service
                              </Button>
                            )}
                            {appointment.status === "ONGOING" && (
                              <Button
                                onClick={() =>
                                  handleCompleteService(
                                    appointment.appointmentId
                                  )
                                }
                                className="bg-green-600 hover:bg-green-700"
                              >
                                <CheckCircle className="h-4 w-4 mr-2" />
                                Complete Service
                              </Button>
                            )}
                          </div>
                        </CardContent>
                      </Card>
                    );
                  })
                )}
              </div>
            )}

            {activeTab === "completed" && (
              <Card>
                <CardHeader>
                  <CardTitle>Completed Appointments Today</CardTitle>
                  <CardDescription>
                    Appointments you've completed today
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  {completedAppointments.length === 0 ? (
                    <div className="text-center py-12">
                      <CheckCircle className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                      <h3 className="text-lg font-medium mb-2">
                        No Completed Appointments
                      </h3>
                      <p className="text-muted-foreground">
                        You haven't completed any appointments today
                      </p>
                    </div>
                  ) : (
                    <div className="space-y-4">
                      {completedAppointments.map((appointment) => {
                        const vehicle = vehicles[appointment.vehicleId];
                        return (
                          <div
                            key={appointment.appointmentId}
                            className="border rounded-lg p-4"
                          >
                            <div className="flex items-center justify-between">
                              <div>
                                <p className="font-medium">
                                  {vehicle
                                    ? `${vehicle.model} (${vehicle.year})`
                                    : "Loading..."}
                                </p>
                                <p className="text-sm text-muted-foreground">
                                  {vehicle &&
                                    `Registration: ${vehicle.registrationNo}`}
                                </p>
                                <p className="text-sm text-muted-foreground">
                                  Completed:{" "}
                                  {new Date(
                                    appointment.scheduledEnd
                                  ).toLocaleString()}
                                </p>
                              </div>
                              <Badge
                                variant="outline"
                                className={getStatusColor(appointment.status)}
                              >
                                <CheckCircle className="h-3 w-3 mr-1" />
                                {appointment.status}
                              </Badge>
                            </div>
                          </div>
                        );
                      })}
                    </div>
                  )}
                </CardContent>
              </Card>
            )}

            {activeTab === "schedule" && (
              <Card>
                <CardHeader>
                  <CardTitle>Today's Schedule</CardTitle>
                  <CardDescription>
                    All your appointments for today
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  {appointments.length === 0 ? (
                    <div className="text-center py-12">
                      <Calendar className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                      <h3 className="text-lg font-medium mb-2">
                        No Appointments
                      </h3>
                      <p className="text-muted-foreground">
                        You don't have any appointments scheduled
                      </p>
                    </div>
                  ) : (
                    <div className="space-y-4">
                      {appointments.map((appointment) => {
                        const vehicle = vehicles[appointment.vehicleId];
                        return (
                          <div
                            key={appointment.appointmentId}
                            className="flex items-center justify-between p-4 border rounded-lg"
                          >
                            <div>
                              <p className="font-medium">
                                {new Date(
                                  appointment.scheduledStart
                                ).toLocaleTimeString([], {
                                  hour: "2-digit",
                                  minute: "2-digit",
                                })}
                              </p>
                              <p className="text-sm text-muted-foreground">
                                {vehicle
                                  ? `${vehicle.model} (${vehicle.year}) - ${vehicle.registrationNo}`
                                  : "Loading..."}
                              </p>
                            </div>
                            <Badge
                              variant="outline"
                              className={getStatusColor(appointment.status)}
                            >
                              {appointment.status}
                            </Badge>
                          </div>
                        );
                      })}
                    </div>
                  )}
                </CardContent>
              </Card>
            )}
            {activeTab === "projectRequests" && (
              <div className="space-y-6">
                <h2 className="text-2xl font-semibold mb-4">
                  Project Requests
                </h2>
                <span>Review the request and create an appointment</span>
                {projectRequests.length === 0 ? (
                  <Card>
                    <CardContent className="py-12 text-center">
                      <LayoutList className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                      <h3 className="text-lg font-medium mb-2">
                        No Project Requests
                      </h3>
                      <p className="text-muted-foreground">
                        No new customer project requests available
                      </p>
                    </CardContent>
                  </Card>
                ) : (
                  projectRequests.map((req) => (
                    <Card
                      key={req.id}
                      className="border shadow-sm hover:shadow-md transition-shadow duration-200"
                    >
                      <CardHeader></CardHeader>

                      <CardContent className="space-y-5">
                        {/* Customer Info Section */}
                        <div className="space-y-2 bg-gray-50 border border-gray-100 rounded-xl p-4">
                          <p>
                            <span className="font-semibold text-gray-800">
                              Name:
                            </span>{" "}
                            <span className="text-gray-700">{req.name}</span>
                          </p>
                          <p>
                            <span className="font-semibold text-gray-800">
                              Phone Number:
                            </span>{" "}
                            <span className="text-gray-700">{req.phone}</span>
                          </p>
                          <p>
                            <span className="font-semibold text-gray-800">
                              Project Description:
                            </span>
                            <br />
                            <span className="block mt-1 text-gray-700 bg-white border rounded-lg p-3">
                              {req.description}
                            </span>
                          </p>
                        </div>

                        {/* Appointment Creation Fields */}
                        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                          <div>
                            <label className="text-sm font-medium text-gray-700">
                              Select Date
                            </label>
                            <input
                              type="date"
                              value={appointmentDate[req.id] || ""}
                              onChange={(e) =>
                                setAppointmentDate((prev) => ({
                                  ...prev,
                                  [req.id]: e.target.value,
                                }))
                              }
                              className="w-full border rounded-lg px-3 py-2 mt-1 focus:outline-none focus:ring-2 focus:ring-primary"
                            />
                          </div>
                          <div>
                            <label className="text-sm font-medium text-gray-700">
                              Estimate Hours
                            </label>
                            <input
                              type="number"
                              placeholder="Hours"
                              min="1"
                              value={appointmentHours[req.id] || ""}
                              onChange={(e) =>
                                setAppointmentHours((prev) => ({
                                  ...prev,
                                  [req.id]: e.target.value,
                                }))
                              }
                              className="w-full border rounded-lg px-3 py-2 mt-1 focus:outline-none focus:ring-2 focus:ring-primary"
                            />
                          </div>
                          <div>
                            <label className="text-sm font-medium text-gray-700">
                              Estimate Budget (LKR)
                            </label>
                            <input
                              type="number"
                              placeholder="Amount in LKR"
                              min="0"
                              onChange={(e) => {
                                const value = e.target.value;
                                setProjectRequests((prev) =>
                                  prev.map((r) =>
                                    r.id === req.id
                                      ? { ...r, estimateBudget: value }
                                      : r
                                  )
                                );
                              }}
                              className="w-full border rounded-lg px-3 py-2 mt-1 focus:outline-none focus:ring-2 focus:ring-primary"
                            />
                          </div>
                        </div>

                        {/* Create Appointment Button */}
                        <div className="flex justify-end pt-4 border-t">
                          <Button
                            className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-lg"
                            onClick={() => {
                              const date = appointmentDate[req.id];
                              const hours = appointmentHours[req.id];

                              if (!date || !hours) {
                                alert(
                                  "Please fill date, hours, and estimated budget before creating appointment."
                                );
                                return;
                              }
                              alert(
                                `Appointment created for ${req.name}\n📅 Date: ${date}\n⏱ Hours: ${hours}\n💰 Budget: Rs. 100000`
                              );
                            }}
                          >
                            Create Appointment
                          </Button>
                        </div>
                      </CardContent>
                    </Card>
                  ))
                )}
              </div>
            )}
          </div>
        </main>
      </div>
    </div>
  );
}
