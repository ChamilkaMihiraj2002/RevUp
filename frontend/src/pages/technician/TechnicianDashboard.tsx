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
import { useAuth } from "@/contexts/authContext/authContext";
import { useNavigate } from "react-router-dom";
import {
  getUnassignedAppointments,
  getAppointmentsByTechnicianId,
  updateAppointment,
  type AppointmentResponse,
} from "@/services/appointmentService";
import {
  getVehicleById,
  type VehicleResponse,
} from "@/services/vehicleService";
import {
  getPendingProjects,
  getProjectsByTechnicianId,
  acceptProject,
  type ProjectResponse,
} from "@/services/projectService";
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
  
  // Project requests state
  const [pendingProjects, setPendingProjects] = useState<ProjectResponse[]>([]);
  const [acceptedProjects, setAcceptedProjects] = useState<ProjectResponse[]>([]);
  const [projectEstimateTime, setProjectEstimateTime] = useState<Record<number, string>>({});
  const [projectEstimatedAmount, setProjectEstimatedAmount] = useState<Record<number, string>>({});
  
  // Appointment queue state
  const [queuedAppointments, setQueuedAppointments] = useState<AppointmentResponse[]>([]);
  const [technicianAppointments, setTechnicianAppointments] = useState<AppointmentResponse[]>([]);

  const handleLogout = async () => {
    try {
      await logout();
      navigate("/login");
    } catch (error) {
      console.error("Logout error:", error);
    }
  };

  // Fetch appointments and vehicles
  const fetchData = async () => {
    if (!userData?.userId || !accessToken) return;

    try {
      // Fetch appointments assigned to this technician
      const techAppointments = await getAppointmentsByTechnicianId(userData.userId, accessToken);
      setTechnicianAppointments(techAppointments || []);
      setAppointments(techAppointments || []); // For backward compatibility with existing tabs
      
      // Fetch unassigned appointments for the queue
      const unassigned = await getUnassignedAppointments(accessToken);
      setQueuedAppointments(unassigned || []);

      // Fetch vehicle data for all appointments
      const allAppointments = [...(techAppointments || []), ...(unassigned || [])];
      const vehicleIds = [
        ...new Set(allAppointments.map((apt) => apt.vehicleId)),
      ];
      const vehiclePromises = vehicleIds.map(async (vehicleId) => {
        try {
          const vehicle = await getVehicleById(vehicleId, accessToken);
          return vehicle ? { [vehicleId]: vehicle } : {};
        } catch (err) {
          console.error(`Error fetching vehicle ${vehicleId}:`, err);
          return {};
        }
      });

      const vehicleResults = await Promise.all(vehiclePromises);
      const vehiclesMap = Object.assign({}, ...vehicleResults);
      setVehicles(vehiclesMap);
      
      // Fetch pending projects (unassigned)
      const pending = await getPendingProjects(accessToken);
      setPendingProjects(pending || []);
      
      // Fetch accepted projects for this technician
      if (userData.userId) {
        const accepted = await getProjectsByTechnicianId(userData.userId, accessToken);
        setAcceptedProjects(accepted || []);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
      // Set empty arrays on error to prevent undefined issues
      setTechnicianAppointments([]);
      setAppointments([]);
      setQueuedAppointments([]);
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
        { 
          status: "ONGOING"
        },
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
        { 
          status: "SERVICED"
        },
        accessToken
      );
      // Refresh data to show updated status
      await fetchData();
      alert("Service marked as complete! Waiting for customer confirmation.");
    } catch (error) {
      console.error("Error completing service:", error);
      alert("Failed to complete service");
    }
  };

  const handleAcceptProject = async (projectId: number) => {
    if (!accessToken || !userData?.userId) return;

    const estimateTime = projectEstimateTime[projectId];
    const estimatedAmount = projectEstimatedAmount[projectId];

    if (!estimateTime || !estimatedAmount) {
      alert("Please fill in estimate hours and budget before accepting the project.");
      return;
    }

    try {
      await acceptProject(
        projectId,
        {
          technicianId: userData.userId,
          estimatedAmount: parseFloat(estimatedAmount),
          estimateTime: parseInt(estimateTime),
        },
        accessToken
      );
      await fetchData();
      alert("Project accepted successfully!");
      // Clear the input fields
      setProjectEstimateTime((prev) => {
        const newState = { ...prev };
        delete newState[projectId];
        return newState;
      });
      setProjectEstimatedAmount((prev) => {
        const newState = { ...prev };
        delete newState[projectId];
        return newState;
      });
    } catch (error) {
      console.error("Error accepting project:", error);
      alert("Failed to accept project. Please try again.");
    }
  };

  // Handle accepting an appointment from the queue with timeslot conflict detection
  const handleAcceptAppointment = async (appointmentId: number) => {
    if (!userData?.userId || !accessToken) return;

    try {
      // Find the appointment being accepted
      const appointmentToAccept = queuedAppointments.find(
        (apt) => apt.appointmentId === appointmentId
      );
      
      if (!appointmentToAccept) {
        alert("Appointment not found.");
        return;
      }

      // Check for timeslot conflicts with technician's existing appointments
      const newStart = new Date(appointmentToAccept.scheduledStart);
      const newEnd = new Date(appointmentToAccept.scheduledEnd);

      const hasConflict = technicianAppointments.some((existing) => {
        const existingStart = new Date(existing.scheduledStart);
        const existingEnd = new Date(existing.scheduledEnd);

        // Check for overlap: new appointment overlaps with existing
        return (
          (newStart >= existingStart && newStart < existingEnd) || // New starts during existing
          (newEnd > existingStart && newEnd <= existingEnd) || // New ends during existing
          (newStart <= existingStart && newEnd >= existingEnd) // New encompasses existing
        );
      });

      if (hasConflict) {
        alert(
          "You already have an appointment scheduled during this time slot. Please choose a different appointment."
        );
        return;
      }

      // Accept the appointment by setting the technician ID and changing status to CONFIRMED
      await updateAppointment(
        appointmentId,
        { 
          technicianId: userData.userId,
          status: "CONFIRMED"
        },
        accessToken
      );

      alert("Appointment accepted successfully! It will appear in your schedule.");
      
      // Refresh data to move appointment from queue to schedule
      await fetchData();
    } catch (error) {
      console.error("Error accepting appointment:", error);
      alert("Failed to accept appointment. Please try again.");
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "COMPLETED":
        return "bg-green-500/10 text-green-600 border-green-200";
      case "SERVICED":
        return "bg-emerald-500/10 text-emerald-600 border-emerald-200";
      case "ONGOING":
        return "bg-blue-500/10 text-blue-600 border-blue-200";
      case "CONFIRMED":
        return "bg-purple-500/10 text-purple-600 border-purple-200";
      case "SCHEDULED":
        return "bg-yellow-500/10 text-yellow-600 border-yellow-200";
      case "CANCELLED":
        return "bg-red-500/10 text-red-600 border-red-200";
      default:
        return "bg-gray-500/10 text-gray-600 border-gray-200";
    }
  };

  // My Assignments: CONFIRMED appointments where time has arrived OR ONGOING appointments
  const now = new Date();
  const activeAssignments = appointments.filter((apt) => {
    if (apt.status === "ONGOING") return true;
    if (apt.status === "CONFIRMED") {
      const appointmentTime = new Date(apt.scheduledStart);
      return appointmentTime <= now; // Time has arrived
    }
    return false;
  });
  
  // Schedule: CONFIRMED appointments where time hasn't arrived yet
  const scheduledAppointments = appointments.filter((apt) => {
    if (apt.status === "CONFIRMED") {
      const appointmentTime = new Date(apt.scheduledStart);
      return appointmentTime > now; // Time hasn't arrived yet
    }
    return false;
  });
  
  // Completed Today
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
              onClick={() => setActiveTab("appointmentQueue")}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                activeTab === "appointmentQueue"
                  ? "bg-primary text-primary-foreground"
                  : "text-foreground hover:bg-muted"
              }`}
            >
              <LayoutList className="h-5 w-5" />
              <span>Appointment Queue</span>
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
                {activeAssignments.length === 0 ? (
                  <Card>
                    <CardContent className="py-12">
                      <div className="text-center">
                        <LayoutList className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                        <h3 className="text-lg font-medium mb-2">
                          No Active Assignments
                        </h3>
                        <p className="text-muted-foreground">
                          No appointments are ready to start. Check your Schedule for upcoming appointments.
                        </p>
                      </div>
                    </CardContent>
                  </Card>
                ) : (
                  activeAssignments.map((appointment) => {
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
                          {/* Appointment Actions */}
                          <div className="flex justify-end pt-4 border-t">
                            {appointment.status === "CONFIRMED" && (
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
              <div className="space-y-6">
                <div className="mb-6">
                  <h2 className="text-2xl font-semibold mb-2">
                    My Schedule
                  </h2>
                  <p className="text-muted-foreground">
                    Upcoming confirmed appointments (not yet started)
                  </p>
                </div>
                
                {scheduledAppointments.length === 0 ? (
                  <Card>
                    <CardContent className="py-12 text-center">
                      <Calendar className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                      <h3 className="text-lg font-medium mb-2">
                        No Upcoming Appointments
                      </h3>
                      <p className="text-muted-foreground">
                        You don't have any upcoming appointments. Check the Appointment Queue to accept new ones.
                      </p>
                    </CardContent>
                  </Card>
                ) : (
                  scheduledAppointments.map((appointment) => {
                    const vehicle = vehicles[appointment.vehicleId];
                    
                    return (
                      <Card
                        key={appointment.appointmentId}
                        className="border shadow-sm hover:shadow-md transition-shadow duration-200"
                      >
                        <CardHeader>
                          <CardTitle className="flex items-center justify-between">
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
                            {vehicle && `Registration: ${vehicle.registrationNo} • Color: ${vehicle.color}`}
                          </CardDescription>
                        </CardHeader>

                        <CardContent className="space-y-5">
                          {/* Appointment Time */}
                          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div className="space-y-1">
                              <p className="text-sm font-medium text-gray-700">
                                Start Time
                              </p>
                              <p className="text-sm text-gray-600">
                                {new Date(appointment.scheduledStart).toLocaleString()}
                              </p>
                            </div>
                            <div className="space-y-1">
                              <p className="text-sm font-medium text-gray-700">
                                End Time
                              </p>
                              <p className="text-sm text-gray-600">
                                {new Date(appointment.scheduledEnd).toLocaleString()}
                              </p>
                            </div>
                          </div>

                          {/* Duration */}
                          <div className="bg-gray-50 border border-gray-100 rounded-xl p-4">
                            <p className="text-sm font-medium text-gray-700">
                              Duration
                            </p>
                            <p className="text-sm text-gray-600 mt-1">
                              {Math.round((new Date(appointment.scheduledEnd).getTime() - new Date(appointment.scheduledStart).getTime()) / (1000 * 60))} minutes
                            </p>
                          </div>

                          {/* Vehicle Details */}
                          {vehicle && (
                            <div className="bg-blue-50 border border-blue-100 rounded-xl p-4">
                              <p className="font-semibold text-blue-800 mb-2">
                                Vehicle Details
                              </p>
                              <div className="grid grid-cols-2 gap-2 text-sm">
                                <p className="text-gray-700">
                                  <span className="font-medium">Model:</span> {vehicle.model}
                                </p>
                                <p className="text-gray-700">
                                  <span className="font-medium">Year:</span> {vehicle.year}
                                </p>
                                <p className="text-gray-700">
                                  <span className="font-medium">Type:</span> {vehicle.vehicleType}
                                </p>
                                <p className="text-gray-700">
                                  <span className="font-medium">Registration:</span> {vehicle.registrationNo}
                                </p>
                              </div>
                            </div>
                          )}

                          {/* Info message - service will start at scheduled time */}
                          <div className="bg-blue-50 border border-blue-200 rounded-lg p-3 text-sm text-blue-800">
                            📅 This appointment will move to "My Assignments" at the scheduled time
                          </div>
                        </CardContent>
                      </Card>
                    );
                  })
                )}
              </div>
            )}
            {activeTab === "appointmentQueue" && (
              <div className="space-y-6">
                <div className="mb-6">
                  <h2 className="text-2xl font-semibold mb-2">
                    Appointment Queue
                  </h2>
                  <p className="text-muted-foreground">
                    View unassigned appointments and accept them for your schedule
                  </p>
                </div>
                
                {queuedAppointments.length === 0 ? (
                  <Card>
                    <CardContent className="py-12 text-center">
                      <LayoutList className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                      <h3 className="text-lg font-medium mb-2">
                        No Appointments Available
                      </h3>
                      <p className="text-muted-foreground">
                        There are no unassigned appointments at this time
                      </p>
                    </CardContent>
                  </Card>
                ) : (
                  queuedAppointments.map((appointment) => {
                    const vehicle = vehicles[appointment.vehicleId];
                    
                    return (
                      <Card
                        key={appointment.appointmentId}
                        className="border shadow-sm hover:shadow-md transition-shadow duration-200"
                      >
                        <CardHeader>
                          <CardTitle className="flex items-center justify-between">
                            <span>Appointment #{appointment.appointmentId}</span>
                            <Badge
                              variant="outline"
                              className={getStatusColor(appointment.status)}
                            >
                              {appointment.status}
                            </Badge>
                          </CardTitle>
                          <CardDescription>
                            Scheduled for {new Date(appointment.scheduledStart).toLocaleString()}
                          </CardDescription>
                        </CardHeader>

                        <CardContent className="space-y-5">
                          {/* Appointment Details */}
                          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div className="space-y-1">
                              <p className="text-sm font-medium text-gray-700">
                                Start Time
                              </p>
                              <p className="text-sm text-gray-600">
                                {new Date(appointment.scheduledStart).toLocaleString()}
                              </p>
                            </div>
                            <div className="space-y-1">
                              <p className="text-sm font-medium text-gray-700">
                                End Time
                              </p>
                              <p className="text-sm text-gray-600">
                                {new Date(appointment.scheduledEnd).toLocaleString()}
                              </p>
                            </div>
                          </div>

                          {/* Duration */}
                          <div className="bg-gray-50 border border-gray-100 rounded-xl p-4">
                            <p className="text-sm font-medium text-gray-700">
                              Appointment Duration
                            </p>
                            <p className="text-sm text-gray-600 mt-1">
                              {Math.round((new Date(appointment.scheduledEnd).getTime() - new Date(appointment.scheduledStart).getTime()) / (1000 * 60))} minutes
                            </p>
                          </div>

                          {/* Vehicle Information */}
                          {vehicle && (
                            <div className="bg-blue-50 border border-blue-100 rounded-xl p-4">
                              <p className="font-semibold text-blue-800 mb-2">
                                Vehicle Details
                              </p>
                              <div className="grid grid-cols-2 gap-2 text-sm">
                                <p className="text-gray-700">
                                  <span className="font-medium">Model:</span> {vehicle.model}
                                </p>
                                <p className="text-gray-700">
                                  <span className="font-medium">Year:</span> {vehicle.year}
                                </p>
                                <p className="text-gray-700">
                                  <span className="font-medium">Type:</span> {vehicle.vehicleType}
                                </p>
                                <p className="text-gray-700">
                                  <span className="font-medium">Registration:</span> {vehicle.registrationNo}
                                </p>
                                <p className="text-gray-700 col-span-2">
                                  <span className="font-medium">Color:</span> {vehicle.color}
                                </p>
                              </div>
                            </div>
                          )}

                          {/* Accept Appointment Button */}
                          <div className="flex justify-end pt-4 border-t">
                            <Button
                              className="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded-lg"
                              onClick={() => handleAcceptAppointment(appointment.appointmentId)}
                            >
                              <CheckCircle className="h-4 w-4 mr-2" />
                              Accept Appointment
                            </Button>
                          </div>
                        </CardContent>
                      </Card>
                    );
                  })
                )}
              </div>
            )}
            {activeTab === "projectRequests" && (
              <div className="space-y-6">
                <div className="mb-6">
                  <h2 className="text-2xl font-semibold mb-2">
                    Pending Project Requests
                  </h2>
                  <p className="text-muted-foreground">
                    Review customer requests and provide estimates to accept projects
                  </p>
                </div>
                
                {pendingProjects.length === 0 ? (
                  <Card>
                    <CardContent className="py-12 text-center">
                      <LayoutList className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                      <h3 className="text-lg font-medium mb-2">
                        No Pending Requests
                      </h3>
                      <p className="text-muted-foreground">
                        No new customer project requests available
                      </p>
                    </CardContent>
                  </Card>
                ) : (
                  pendingProjects.map((project) => (
                    <Card
                      key={project.projectId}
                      className="border shadow-sm hover:shadow-md transition-shadow duration-200"
                    >
                      <CardHeader>
                        <CardTitle className="flex items-center justify-between">
                          <span>Project #{project.projectId}</span>
                          <Badge variant="outline" className="bg-yellow-500/10 text-yellow-600 border-yellow-200">
                            PENDING
                          </Badge>
                        </CardTitle>
                        <CardDescription>
                          Requested on {new Date(project.createdAt).toLocaleDateString()}
                        </CardDescription>
                      </CardHeader>

                      <CardContent className="space-y-5">
                        {/* Project Description */}
                        <div className="space-y-2 bg-gray-50 border border-gray-100 rounded-xl p-4">
                          <p>
                            <span className="font-semibold text-gray-800">
                              Project Description:
                            </span>
                            <br />
                            <span className="block mt-2 text-gray-700 bg-white border rounded-lg p-3">
                              {project.description}
                            </span>
                          </p>
                          {project.vehicleId && (
                            <p className="text-sm text-muted-foreground">
                              Vehicle ID: {project.vehicleId}
                            </p>
                          )}
                        </div>

                        {/* Estimate Input Fields */}
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                          <div>
                            <label className="text-sm font-medium text-gray-700">
                              Estimate Hours *
                            </label>
                            <input
                              type="number"
                              placeholder="Enter hours"
                              min="1"
                              value={projectEstimateTime[project.projectId] || ""}
                              onChange={(e) =>
                                setProjectEstimateTime((prev) => ({
                                  ...prev,
                                  [project.projectId]: e.target.value,
                                }))
                              }
                              className="w-full border rounded-lg px-3 py-2 mt-1 focus:outline-none focus:ring-2 focus:ring-primary"
                            />
                          </div>
                          <div>
                            <label className="text-sm font-medium text-gray-700">
                              Estimate Budget (USD) *
                            </label>
                            <input
                              type="number"
                              placeholder="Enter amount"
                              min="0"
                              step="0.01"
                              value={projectEstimatedAmount[project.projectId] || ""}
                              onChange={(e) =>
                                setProjectEstimatedAmount((prev) => ({
                                  ...prev,
                                  [project.projectId]: e.target.value,
                                }))
                              }
                              className="w-full border rounded-lg px-3 py-2 mt-1 focus:outline-none focus:ring-2 focus:ring-primary"
                            />
                          </div>
                        </div>

                        {/* Accept Project Button */}
                        <div className="flex justify-end pt-4 border-t">
                          <Button
                            className="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded-lg"
                            onClick={() => handleAcceptProject(project.projectId)}
                          >
                            <CheckCircle className="h-4 w-4 mr-2" />
                            Accept Project
                          </Button>
                        </div>
                      </CardContent>
                    </Card>
                  ))
                )}

                {/* Accepted/In-Progress Projects Section */}
                <div className="mt-12">
                  <h2 className="text-2xl font-semibold mb-4">
                    My Accepted Projects
                  </h2>
                  <p className="text-muted-foreground mb-6">
                    Projects currently assigned to you
                  </p>

                  {acceptedProjects.length === 0 ? (
                    <Card>
                      <CardContent className="py-12 text-center">
                        <CheckSquare className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                        <h3 className="text-lg font-medium mb-2">
                          No Accepted Projects
                        </h3>
                        <p className="text-muted-foreground">
                          You haven't accepted any projects yet
                        </p>
                      </CardContent>
                    </Card>
                  ) : (
                    <div className="space-y-4">
                      {acceptedProjects.map((project) => (
                        <Card key={project.projectId} className="border">
                          <CardContent className="py-4">
                            <div className="flex items-center justify-between">
                              <div className="flex-1">
                                <div className="flex items-center space-x-3 mb-2">
                                  <h3 className="font-semibold">Project #{project.projectId}</h3>
                                  <Badge
                                    variant="outline"
                                    className={getStatusColor(project.status)}
                                  >
                                    {project.status}
                                  </Badge>
                                </div>
                                <p className="text-sm text-gray-700 mb-2">
                                  {project.description}
                                </p>
                                <div className="flex items-center gap-4 text-sm text-muted-foreground">
                                  <span>⏱️ {project.estimateTime} hours</span>
                                  <span>💰 ${project.estimatedAmount?.toFixed(2)}</span>
                                  <span>📅 Started: {new Date(project.updatedAt).toLocaleDateString()}</span>
                                </div>
                              </div>
                            </div>
                          </CardContent>
                        </Card>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>
        </main>
      </div>
    </div>
  );
}
