import { useState, useEffect, useMemo } from "react";
import { Link, useNavigate } from "react-router-dom";
import { CommonNavbar } from "@/components/Layout/Navbar";
import { CommonSidebar } from "@/components/Layout/Slidebar";
import { Button } from "@/components/UI/Button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/UI/Card";
import { Badge } from "@/components/UI/Badge";
import { Input } from "@/components/UI/Input";
import { Label } from "@/components/UI/Label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/UI/Select";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/UI/Dialog";
import { useAuth } from "@/contexts/authContext/authContext";
import {
  getAppointmentsByCustomerId,
  type AppointmentResponse,
} from "@/services/appointmentService";
import {
  getVehiclesByUserId,
  type VehicleResponse,
  createVehicle,
} from "@/services/vehicleService";
import { updateUser } from "@/services/userService";
import { passwordReset } from "@/firebase/auth";
import {
  Car,
  Calendar,
  Clock,
  Wrench,
  Plus,
  CheckCircle,
  LayoutDashboard,
  History,
  User,
  Phone,
  Mail,
  Lock,
} from "lucide-react";

export default function CustomerDashboard() {
  const { userData, logout, accessToken, setUserData } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("overview");
  const [isAddVehicleOpen, setIsAddVehicleOpen] = useState(false);
  const [allAppointments, setAllAppointments] = useState<AppointmentResponse[]>(
    []
  );
  const [vehicles, setVehicles] = useState<VehicleResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [newVehicle, setNewVehicle] = useState({
    make: "",
    model: "",
    year: "",
    plate: "",
    color: "",
    mileage: "",
    type: "",
  });
  const [isEditingProfile, setIsEditingProfile] = useState(false);
  const [profileData, setProfileData] = useState({
    name: userData?.name || "",
    email: userData?.email || "",
    phone: userData?.phone || "",
  });
  const [modificationRequest, setModificationRequest] = useState("");
  const [isSubmittingRequest] = useState(false);

  // Update profile data when userData changes
  useEffect(() => {
    if (userData) {
      setProfileData({
        name: userData.name || "",
        email: userData.email || "",
        phone: userData.phone || "",
      });
    }
  }, [userData]);

  // Fetch data on mount
  useEffect(() => {
    const fetchData = async () => {
      if (!userData?.userId || !accessToken) return;

      try {
        setLoading(true);

        // Fetch appointments and vehicles in parallel
        const [appointmentsData, vehiclesData] = await Promise.all([
          getAppointmentsByCustomerId(userData.userId, accessToken),
          getVehiclesByUserId(userData.userId, accessToken),
        ]);

        setAllAppointments(appointmentsData);
        setVehicles(vehiclesData);
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [userData, accessToken]);

  // Filter appointments by status using useMemo for performance
  const ongoingAppointments = useMemo(
    () => allAppointments.filter((apt) => apt.status === "ONGOING"),
    [allAppointments]
  );

  const scheduledAppointments = useMemo(
    () => allAppointments.filter((apt) => apt.status === "SCHEDULED"),
    [allAppointments]
  );

  const completedAppointments = useMemo(
    () => allAppointments.filter((apt) => apt.status === "COMPLETED"),
    [allAppointments]
  );

  // Get next scheduled appointment
  const nextScheduledAppointment = useMemo(() => {
    if (scheduledAppointments.length === 0) return null;
    return scheduledAppointments.sort(
      (a, b) =>
        new Date(a.scheduledStart).getTime() -
        new Date(b.scheduledStart).getTime()
    )[0];
  }, [scheduledAppointments]);

  const handleAddVehicle = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!userData?.userId || !accessToken) {
      alert("User not authenticated");
      return;
    }

    try {
      const vehicleData = {
        model: newVehicle.model,
        registrationNo: newVehicle.plate,
        year: parseInt(newVehicle.year),
        color: newVehicle.color,
        vehicleType: newVehicle.type,
        userId: userData.userId,
      };

      await createVehicle(vehicleData, accessToken);

      // Refresh vehicles list
      const updatedVehicles = await getVehiclesByUserId(
        userData.userId,
        accessToken
      );
      setVehicles(updatedVehicles);

      setIsAddVehicleOpen(false);

      // Reset form
      setNewVehicle({
        make: "",
        model: "",
        year: "",
        plate: "",
        color: "",
        mileage: "",
        type: "",
      });

      alert("Vehicle added successfully!");
    } catch (error) {
      console.error("Error adding vehicle:", error);
      alert("Failed to add vehicle. Please try again.");
    }
  };

  const handleLogout = async () => {
    try {
      await logout();
      navigate("/login");
    } catch (error) {
      console.error("Logout error:", error);
    }
  };

  const handleSaveProfile = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!userData?.userId || !accessToken) {
      alert("User not authenticated");
      return;
    }

    // Validate phone number
    if (
      !profileData.phone ||
      profileData.phone.length !== 10 ||
      !/^\d+$/.test(profileData.phone)
    ) {
      alert("Phone number must be exactly 10 digits");
      return;
    }

    try {
      const updateData = {
        name: profileData.name,
        phone: profileData.phone,
      };

      const updatedUser = await updateUser(
        userData.userId,
        updateData,
        accessToken
      );

      // Update the context with the new user data
      setUserData(updatedUser);

      setIsEditingProfile(false);
      alert("Profile updated successfully!");
    } catch (error) {
      console.error("Error updating profile:", error);
      alert("Failed to update profile. Please try again.");
    }
  };

  const handleForgotPassword = async () => {
    if (!userData?.email) {
      alert("No email found");
      return;
    }

    try {
      await passwordReset(userData.email);
      alert("Password reset email sent! Please check your inbox.");
    } catch (error) {
      console.error("Error sending password reset email:", error);
      alert("Failed to send password reset email. Please try again.");
    }
  };

  const handleCancelEdit = () => {
    setProfileData({
      name: userData?.name || "",
      email: userData?.email || "",
      phone: userData?.phone || "",
    });
    setIsEditingProfile(false);
  };

  // Navigation items for Customer Dashboard
  const navItems = [
    { id: "overview", label: "Overview", icon: LayoutDashboard },
    { id: "vehicles", label: "My Vehicles", icon: Car },
    { id: "appointments", label: "Appointments", icon: Calendar },
    { id: "history", label: "Service History", icon: History },
    { id: "modification", label: "Modification Request", icon: Wrench },
    { id: "profile", label: "My Profile", icon: User },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-cyan-50 to-blue-100">
      {/* IMPORTED COMMON NAVBAR */}
      <CommonNavbar
        userName={userData?.name || "User"}
        onNotificationClick={() => console.log("Notifications")}
        onProfileClick={() => setActiveTab("profile")}
        onSettingsClick={() => console.log("Settings")}
        onLogoutClick={handleLogout}
      />

      <div className="flex">
        {/* IMPORTED COMMON SIDEBAR WITH CUSTOM NAV ITEMS */}
        <CommonSidebar
          navItems={navItems}
          activeTab={activeTab}
          onTabChange={setActiveTab}
        />

        {/* Main Content */}
        <main className="flex-1 min-h-[calc(100vh-64px)]">
          <div className="h-full max-w-[1600px] mx-auto px-6 sm:px-8 lg:px-12 py-8">
            {loading ? (
              <div className="flex items-center justify-center h-96">
                <div className="text-center">
                  <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-cyan-600 mx-auto mb-4"></div>
                  <p className="text-gray-600">Loading your data...</p>
                </div>
              </div>
            ) : (
            <>
              {/* Header */}
              <div className="mb-8">
                <h1 className="text-4xl font-bold text-gray-900">
                  Welcome back, {userData?.name || "User"}
                </h1>
                <p className="text-lg text-gray-600 mt-2">
                  Manage your vehicles and track service appointments
                </p>
              </div>

              {/* Active Service Alert */}
              {ongoingAppointments.length > 0 && (
                <Card className="mb-6 border-cyan-200 bg-white shadow-md">
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-3">
                        <div className="bg-cyan-600 rounded-lg p-2.5">
                          <Wrench className="h-5 w-5 text-white" />
                        </div>
                        <div>
                          <CardTitle className="text-gray-900">
                            Service in Progress
                          </CardTitle>
                          <CardDescription className="text-cyan-700">
                            {vehicles.find(
                              (v) =>
                                v.vehicleId ===
                                ongoingAppointments[0]?.vehicleId
                            )?.model || "Your vehicle"}{" "}
                            is currently being serviced
                          </CardDescription>
                        </div>
                      </div>
                      <Badge className="bg-cyan-100 text-cyan-700 border-cyan-200 hover:bg-cyan-200">
                        Live
                      </Badge>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                        <div className="flex items-center space-x-2">
                          <Calendar className="h-4 w-4 text-slate-500" />
                          <span className="text-slate-700">
                            Start:{" "}
                            <span className="font-medium">
                              {new Date(
                                ongoingAppointments[0].scheduledStart
                              ).toLocaleString()}
                            </span>
                          </span>
                        </div>
                        <div className="flex items-center space-x-2">
                          <Clock className="h-4 w-4 text-slate-500" />
                          <span className="text-slate-700">
                            Est. end:{" "}
                            <span className="font-medium">
                              {new Date(
                                ongoingAppointments[0].scheduledEnd
                              ).toLocaleString()}
                            </span>
                          </span>
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              )}

              {/* Overview Tab */}
              {activeTab === "overview" && (
                <div className="space-y-6">
                  {/* Quick Stats */}
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <Card className="hover:shadow-lg transition-shadow border-slate-200">
                      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium text-slate-700">
                          Registered Vehicles
                        </CardTitle>
                        <div className="bg-blue-100 rounded-lg p-2">
                          <Car className="h-4 w-4 text-blue-600" />
                        </div>
                      </CardHeader>
                      <CardContent>
                        <div className="text-3xl font-bold text-slate-900">
                          {vehicles.length}
                        </div>
                        <p className="text-xs text-slate-500 mt-1">
                          Active vehicles
                        </p>
                      </CardContent>
                    </Card>

                    <Card className="hover:shadow-lg transition-shadow border-slate-200">
                      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium text-slate-700">
                          Scheduled Services
                        </CardTitle>
                        <div className="bg-green-100 rounded-lg p-2">
                          <Wrench className="h-4 w-4 text-green-600" />
                        </div>
                      </CardHeader>
                      <CardContent>
                        <div className="text-3xl font-bold text-slate-900">
                          {scheduledAppointments.length}
                        </div>
                        <p className="text-xs text-slate-500 mt-1">
                          Upcoming appointments
                        </p>
                      </CardContent>
                    </Card>

                    <Card className="hover:shadow-lg transition-shadow border-slate-200">
                      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium text-slate-700">
                          Next Service
                        </CardTitle>
                        <div className="bg-purple-100 rounded-lg p-2">
                          <Calendar className="h-4 w-4 text-purple-600" />
                        </div>
                      </CardHeader>
                      <CardContent>
                        {nextScheduledAppointment ? (
                          <>
                            <div className="text-2xl font-bold text-slate-900">
                              {new Date(
                                nextScheduledAppointment.scheduledStart
                              ).toLocaleDateString("en-US", {
                                month: "short",
                                day: "numeric",
                                year: "numeric",
                              })}
                            </div>
                            <p className="text-xs text-slate-500 mt-1">
                              {new Date(
                                nextScheduledAppointment.scheduledStart
                              ).toLocaleTimeString("en-US", {
                                hour: "2-digit",
                                minute: "2-digit",
                              })}
                            </p>
                            <p className="text-xs text-slate-600 mt-1 font-medium">
                              {vehicles.find(
                                (v) =>
                                  v.vehicleId ===
                                  nextScheduledAppointment.vehicleId
                              )?.model || "Vehicle"}
                            </p>
                          </>
                        ) : (
                          <>
                            <div className="text-xl font-bold text-slate-900">
                              Not Scheduled
                            </div>
                            <p className="text-xs text-slate-500 mt-1">
                              No upcoming services
                            </p>
                          </>
                        )}
                      </CardContent>
                    </Card>
                  </div>

                  {/* Recent Activity */}
                  <Card className="border-gray-200 shadow-md bg-white">
                    <CardHeader>
                      <CardTitle className="text-gray-900">
                        Recent Activity
                      </CardTitle>
                      <CardDescription className="text-cyan-700">
                        Your latest service updates and notifications
                      </CardDescription>
                    </CardHeader>
                    <CardContent>
                      <div className="space-y-4">
                        <div className="flex items-center space-x-4 p-3 rounded-lg hover:bg-cyan-50 transition-colors">
                          <div className="bg-green-100 rounded-full p-2.5">
                            <CheckCircle className="h-5 w-5 text-green-600" />
                          </div>
                          <div className="flex-1">
                            <p className="text-sm font-medium text-slate-900">
                              Oil change completed
                            </p>
                            <p className="text-xs text-slate-500">
                              Toyota Corolla • 2 hours ago
                            </p>
                          </div>
                        </div>
                        <div className="flex items-center space-x-4 p-3 rounded-lg hover:bg-cyan-50 transition-colors">
                          <div className="bg-blue-100 rounded-full p-2.5">
                            <Wrench className="h-5 w-5 text-blue-600" />
                          </div>
                          <div className="flex-1">
                            <p className="text-sm font-medium text-slate-900">
                              Service started
                            </p>
                            <p className="text-xs text-slate-500">
                              Toyota Corolla • 3 hours ago
                            </p>
                          </div>
                        </div>
                        <div className="flex items-center space-x-4 p-3 rounded-lg hover:bg-cyan-50 transition-colors">
                          <div className="bg-indigo-100 rounded-full p-2.5">
                            <Calendar className="h-5 w-5 text-indigo-600" />
                          </div>
                          <div className="flex-1">
                            <p className="text-sm font-medium text-slate-900">
                              Appointment confirmed
                            </p>
                            <p className="text-xs text-slate-500">
                              Toyota Corolla • Yesterday
                            </p>
                          </div>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </div>
              )}

              {/* My Vehicles Tab */}
              {activeTab === "vehicles" && (
                <div className="space-y-6">
                  <div className="flex justify-between items-center">
                    <h2 className="text-2xl font-bold text-gray-900">
                      My Vehicles
                    </h2>
                    <Dialog
                      open={isAddVehicleOpen}
                      onOpenChange={setIsAddVehicleOpen}
                    >
                      <DialogTrigger asChild>
                        <Button className="bg-cyan-600 hover:bg-cyan-700 shadow-md">
                          <Plus className="h-4 w-4 mr-2" />
                          Add Vehicle
                        </Button>
                      </DialogTrigger>
                      <DialogContent className="w-full max-w-[90vw] sm:max-w-[900px] bg-white">
                        <DialogHeader>
                          <DialogTitle className="text-xl font-semibold text-gray-900">
                            Add New Vehicle
                          </DialogTitle>
                          <DialogDescription className="text-gray-600">
                            Enter your vehicle details below
                          </DialogDescription>
                        </DialogHeader>
                        <form
                          onSubmit={handleAddVehicle}
                          className="space-y-4 py-4"
                        >
                          <div className="grid grid-cols-2 gap-4">
                            <div className="space-y-2">
                              <Label htmlFor="model" className="text-gray-700">
                                Model
                              </Label>
                              <Input
                                id="model"
                                value={newVehicle.model}
                                onChange={(e) =>
                                  setNewVehicle({
                                    ...newVehicle,
                                    model: e.target.value,
                                  })
                                }
                                placeholder="e.g., Toyota Corolla"
                                className="border-gray-200 focus:border-cyan-500 focus:ring-cyan-500"
                                required
                              />
                            </div>
                            <div className="space-y-2">
                              <Label htmlFor="plate" className="text-gray-700">
                                Registration Number
                              </Label>
                              <Input
                                id="plate"
                                value={newVehicle.plate}
                                onChange={(e) =>
                                  setNewVehicle({
                                    ...newVehicle,
                                    plate: e.target.value,
                                  })
                                }
                                placeholder="e.g., ABC-123"
                                className="border-gray-200 focus:border-cyan-500 focus:ring-cyan-500"
                                required
                              />
                            </div>
                          </div>

                          <div className="grid grid-cols-2 gap-4">
                            <div className="space-y-2">
                              <Label htmlFor="year" className="text-gray-700">
                                Year
                              </Label>
                              <Input
                                id="year"
                                type="number"
                                value={newVehicle.year}
                                onChange={(e) =>
                                  setNewVehicle({
                                    ...newVehicle,
                                    year: e.target.value,
                                  })
                                }
                                placeholder="e.g., 2020"
                                min="1900"
                                max="2026"
                                className="border-gray-200 focus:border-cyan-500 focus:ring-cyan-500"
                                required
                              />
                            </div>
                            <div className="space-y-2">
                              <Label htmlFor="color" className="text-gray-700">
                                Color
                              </Label>
                              <Input
                                id="color"
                                value={newVehicle.color}
                                onChange={(e) =>
                                  setNewVehicle({
                                    ...newVehicle,
                                    color: e.target.value,
                                  })
                                }
                                placeholder="e.g., White"
                                className="border-gray-200 focus:border-cyan-500 focus:ring-cyan-500"
                                required
                              />
                            </div>
                          </div>

                          <div className="grid grid-cols-1 gap-4">
                            <div className="space-y-2">
                              <Label htmlFor="type" className="text-gray-700">
                                Vehicle Type
                              </Label>
                              <Select
                                value={newVehicle.type}
                                onValueChange={(value) =>
                                  setNewVehicle({ ...newVehicle, type: value })
                                }
                              >
                                <SelectTrigger
                                  id="type"
                                  className="border-gray-200 focus:border-cyan-500 focus:ring-cyan-500"
                                >
                                  <SelectValue placeholder="Select type" />
                                </SelectTrigger>
                                <SelectContent>
                                  <SelectItem value="sedan">Sedan</SelectItem>
                                  <SelectItem value="suv">SUV</SelectItem>
                                  <SelectItem value="hatchback">
                                    Hatchback
                                  </SelectItem>
                                  <SelectItem value="pickup">
                                    Pickup Truck
                                  </SelectItem>
                                  <SelectItem value="van">Van</SelectItem>
                                </SelectContent>
                              </Select>
                            </div>
                          </div>

                          <div className="flex justify-end space-x-3 pt-4">
                            <Button
                              type="button"
                              variant="outline"
                              onClick={() => setIsAddVehicleOpen(false)}
                              className="border-gray-200"
                            >
                              Cancel
                            </Button>
                            <Button
                              type="submit"
                              className="bg-cyan-600 hover:bg-cyan-700 text-white"
                            >
                              Add Vehicle
                            </Button>
                          </div>
                        </form>
                      </DialogContent>
                    </Dialog>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {vehicles.length > 0 ? (
                      vehicles.map((vehicle) => {
                        const vehicleAppointments = allAppointments.filter(
                          (apt) => apt.vehicleId === vehicle.vehicleId
                        );
                        return (
                          <Card
                            key={vehicle.vehicleId}
                            className="hover:shadow-md transition-all border-gray-200 bg-white"
                          >
                            <CardHeader>
                              <div className="flex items-center justify-between">
                                <div>
                                  <CardTitle className="text-slate-900">
                                    {vehicle.year} {vehicle.model}
                                  </CardTitle>
                                  <CardDescription>
                                    License: {vehicle.registrationNo}
                                  </CardDescription>
                                </div>
                                <div className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-xl p-3">
                                  <Car className="h-6 w-6 text-blue-600" />
                                </div>
                              </div>
                            </CardHeader>
                            <CardContent>
                              <div className="space-y-3">
                                <div className="flex justify-between text-sm p-2 bg-slate-50 rounded-lg">
                                  <span className="text-slate-600">
                                    Total Appointments
                                  </span>
                                  <span className="font-semibold text-slate-900">
                                    {vehicleAppointments.length}
                                  </span>
                                </div>
                                <div className="flex justify-between text-sm p-2 bg-slate-50 rounded-lg">
                                  <span className="text-slate-600">Color</span>
                                  <span className="font-semibold text-slate-900">
                                    {vehicle.color}
                                  </span>
                                </div>
                                <div className="flex justify-between text-sm p-2 bg-slate-50 rounded-lg">
                                  <span className="text-slate-600">
                                    Vehicle Type
                                  </span>
                                  <span className="font-semibold text-slate-900">
                                    {vehicle.vehicleType}
                                  </span>
                                </div>
                                <div className="flex space-x-2 pt-2">
                                  <Link to="/book" className="flex-1">
                                    <Button
                                      variant="outline"
                                      size="sm"
                                      className="w-full border-cyan-200 text-cyan-700 hover:bg-cyan-50 hover:text-cyan-800"
                                    >
                                      Book Service
                                    </Button>
                                  </Link>
                                </div>
                              </div>
                            </CardContent>
                          </Card>
                        );
                      })
                    ) : (
                      <div className="col-span-full">
                        <Card className="border-gray-200 bg-white">
                          <CardContent className="text-center py-12">
                            <div className="bg-blue-50 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                              <Car className="h-8 w-8 text-blue-600" />
                            </div>
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">
                              No vehicles registered
                            </h3>
                            <p className="text-gray-600 mb-6">
                              Add your first vehicle to start booking services
                            </p>
                            <Button
                              onClick={() => setIsAddVehicleOpen(true)}
                              className="bg-cyan-600 hover:bg-cyan-700 shadow-md"
                            >
                              <Plus className="h-4 w-4 mr-2" />
                              Add Your First Vehicle
                            </Button>
                          </CardContent>
                        </Card>
                      </div>
                    )}
                  </div>
                </div>
              )}

              {/* Appointments Tab */}
              {activeTab === "appointments" && (
                <div className="space-y-6">
                  <div className="flex justify-between items-center">
                    <h2 className="text-2xl font-bold text-gray-900">
                      Appointments
                    </h2>
                    <Link to="/book">
                      <Button className="bg-cyan-600 hover:bg-cyan-700 shadow-md">
                        <Plus className="h-4 w-4 mr-2" />
                        Book Service
                      </Button>
                    </Link>
                  </div>

                  {allAppointments.length > 0 ? (
                    <div className="space-y-4">
                      {allAppointments.map((appointment) => {
                        const vehicle = vehicles.find(
                          (v) => v.vehicleId === appointment.vehicleId
                        );
                        return (
                          <Card
                            key={appointment.appointmentId}
                            className="border-gray-200 shadow-md bg-white"
                          >
                            <CardHeader>
                              <div className="flex items-center justify-between">
                                <div>
                                  <CardTitle className="flex items-center space-x-2 text-slate-900">
                                    <span>Service Appointment</span>
                                    <Badge
                                      className={
                                        appointment.status === "ONGOING"
                                          ? "bg-blue-600 text-white shadow-lg shadow-blue-500/30"
                                          : appointment.status === "COMPLETED"
                                          ? "bg-green-600 text-white"
                                          : appointment.status === "CANCELLED"
                                          ? "bg-red-600 text-white"
                                          : "bg-purple-600 text-white"
                                      }
                                    >
                                      {appointment.status}
                                    </Badge>
                                  </CardTitle>
                                  <CardDescription>
                                    {new Date(
                                      appointment.scheduledStart
                                    ).toLocaleString()}
                                  </CardDescription>
                                </div>
                              </div>
                            </CardHeader>
                            <CardContent>
                              <div className="space-y-4">
                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                                  <div>
                                    <span className="text-slate-600 font-medium">
                                      Vehicle:
                                    </span>
                                    <p className="font-semibold text-slate-900 mt-1">
                                      {vehicle
                                        ? `${vehicle.year} ${vehicle.model} : ${vehicle.registrationNo}`
                                        : "Unknown"}
                                    </p>
                                  </div>
                                  <div>
                                    <span className="text-slate-600 font-medium">
                                      Estimate Ending Time:
                                    </span>
                                    <p className="font-semibold text-slate-900 mt-1">
                                      {new Date(
                                        appointment.scheduledEnd
                                      ).toLocaleString()}
                                    </p>
                                  </div>
                                </div>
                              </div>
                            </CardContent>
                          </Card>
                        );
                      })}
                    </div>
                  ) : (
                    <Card className="border-gray-200 bg-white">
                      <CardContent className="text-center py-12">
                        <div className="bg-cyan-50 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                          <Calendar className="h-8 w-8 text-cyan-600" />
                        </div>
                        <h3 className="text-lg font-semibold text-gray-900 mb-2">
                          No upcoming appointments
                        </h3>
                        <p className="text-gray-600 mb-6">
                          Schedule your next service appointment
                        </p>
                        <Link to="/book">
                          <Button className="bg-cyan-600 hover:bg-cyan-700 shadow-md">
                            Book Service
                          </Button>
                        </Link>
                      </CardContent>
                    </Card>
                  )}
                </div>
              )}

              {/* Service History Tab */}
              {activeTab === "history" && (
                <div className="space-y-6">
                  <h2 className="text-2xl font-bold text-slate-900">
                    Service History
                  </h2>

                  {completedAppointments.length > 0 ? (
                    <div className="space-y-4">
                      {completedAppointments.map((appointment) => {
                        const vehicle = vehicles.find(
                          (v) => v.vehicleId === appointment.vehicleId
                        );
                        return (
                          <Card
                            key={appointment.appointmentId}
                            className="border-slate-200 hover:shadow-lg transition-shadow"
                          >
                            <CardHeader>
                              <div className="flex items-center justify-between">
                                <div>
                                  <CardTitle className="flex items-center space-x-2 text-slate-900">
                                    <span>
                                      {vehicle
                                        ? `${vehicle.year} ${vehicle.model}`
                                        : "Unknown Vehicle"}
                                    </span>
                                    <Badge
                                      variant="outline"
                                      className="text-green-700 border-green-300 bg-green-50"
                                    >
                                      <CheckCircle className="h-3 w-3 mr-1" />
                                      Completed
                                    </Badge>
                                  </CardTitle>
                                  <CardDescription>
                                    {new Date(
                                      appointment.scheduledStart
                                    ).toLocaleDateString()}
                                  </CardDescription>
                                </div>
                                <div className="text-right">
                                  <p className="text-sm font-medium text-slate-600">
                                    Appointment #{appointment.appointmentId}
                                  </p>
                                </div>
                              </div>
                            </CardHeader>
                            <CardContent>
                              <div className="space-y-2">
                                <div className="flex justify-between text-sm">
                                  <span className="text-slate-600">
                                    Started:
                                  </span>
                                  <span className="font-medium text-slate-900">
                                    {new Date(
                                      appointment.scheduledStart
                                    ).toLocaleString()}
                                  </span>
                                </div>
                                <div className="flex justify-between text-sm">
                                  <span className="text-slate-600">
                                    Completed:
                                  </span>
                                  <span className="font-medium text-slate-900">
                                    {new Date(
                                      appointment.scheduledEnd
                                    ).toLocaleString()}
                                  </span>
                                </div>
                              </div>
                            </CardContent>
                          </Card>
                        );
                      })}
                    </div>
                  ) : (
                    <Card className="border-slate-200">
                      <CardContent className="text-center py-12">
                        <div className="bg-slate-50 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                          <History className="h-8 w-8 text-slate-600" />
                        </div>
                        <h3 className="text-lg font-semibold text-slate-900 mb-2">
                          No service history
                        </h3>
                        <p className="text-slate-600">
                          Your completed appointments will appear here
                        </p>
                      </CardContent>
                    </Card>
                  )}
                </div>
              )}

              {/* Modification Request Tab */}
              {activeTab === "modification" && (
                <div className="space-y-6">
                  <div className="flex justify-between items-center">
                    <div>
                      <h2 className="text-2xl font-bold text-gray-900">
                        Modification Request
                      </h2>
                      <p className="text-gray-600 mt-1"></p>
                    </div>
                  </div>

                  <Card className="border-gray-200 bg-white shadow-md">
                    <CardHeader>
                      <CardTitle className="text-slate-900">
                         Modification Details *
                      </CardTitle>
                      <CardDescription className="text-cyan-700">
                        Describe the modifications you'd like to make to your
                        vehicle
                      </CardDescription>
                    </CardHeader>
                    <CardContent>
                      <form className="space-y-6">
                        <div className="space-y-3">
                          
                          <textarea
                            id="modification-details"
                            value={modificationRequest}
                            onChange={(e) =>
                              setModificationRequest(e.target.value)
                            }
                            placeholder="Please provide a detailed description of the modifications you would like:

- Type of modification (e.g., custom paint job, performance upgrades, interior modifications, audio system, body kit, etc.)
- Specific requirements or preferences."
                            className="w-full min-h-[400px] px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-cyan-500 focus:border-transparent resize-y text-base"
                            required
                          />
                          <p className="text-sm text-slate-600">
                            Be as detailed as possible to help us understand
                            your requirements
                          </p>
                        </div>

                        <div className="flex justify-end space-x-3 pt-4">
                          <Button
                            type="button"
                            variant="outline"
                            onClick={() => setModificationRequest("")}
                            className="border-gray-300 text-gray-700 hover:bg-gray-50"
                          >
                            Clear
                          </Button>
                          <Button
                            type="submit"
                            disabled={isSubmittingRequest}
                            className="bg-cyan-600 hover:bg-cyan-700 text-white shadow-md px-8"
                          >
                            {isSubmittingRequest ? (
                              <>
                                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                                Submitting Request...
                              </>
                            ) : (
                              "Submit Request"
                            )}
                          </Button>
                        </div>
                      </form>
                    </CardContent>
                  </Card>

                  {/* Information Card */}
                  <Card className="border-2 border-cyan-200 bg-transparent shadow-none">
                    <CardContent className="pt-6">
                      <div className="flex items-start space-x-3">
                        <div className="bg-cyan-100 rounded-full p-2 mt-0.5">
                          <Wrench className="h-5 w-5 text-cyan-600" />
                        </div>
                        <div>
                          <h3 className="font-semibold text-gray-900 mb-2">
                            What happens next?
                          </h3>
                          <ul className="font-semibold space-y-2 text-sm text-gray-700">
                            <li>
                              • Our team will review your modification request
                              within 24-48 hours
                            </li>
                            <li>
                              • We'll contact you to discuss the feasibility and
                              provide a detailed quote
                            </li>
                            <li>
                              • Once approved, we'll schedule an appointment for
                              the modification work
                            </li>
                            <li>
                              • You'll receive updates throughout the
                              modification process
                            </li>
                          </ul>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </div>
              )}

              {/* {My Profile tab} */}
              {activeTab === "profile" && (
                <div className="max-w-3xl mx-auto">
                  <Card className="border-gray-200 bg-white">
                    <CardHeader>
                      <div className="flex items-center justify-between">
                        <CardTitle className="text-slate-900">
                          My Profile
                        </CardTitle>
                        {!isEditingProfile && (
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={() => setIsEditingProfile(true)}
                            className="border-cyan-200 text-cyan-700 hover:bg-cyan-50 hover:text-cyan-800"
                          >
                            Edit Profile
                          </Button>
                        )}
                      </div>
                    </CardHeader>
                    <CardContent>
                      {isEditingProfile ? (
                        <div className="space-y-6">
                          <div className="flex items-center space-x-4">
                            <div className="w-24 h-24 rounded-full bg-gradient-to-br from-cyan-100 to-blue-100 flex items-center justify-center">
                              <User className="h-12 w-12 text-cyan-600" />
                            </div>
                            <div>
                              <h3 className="text-lg font-semibold text-slate-900">
                                {profileData.name}
                              </h3>
                              <p className="text-sm text-slate-600">
                                Update your profile information
                              </p>
                            </div>
                          </div>

                          <div className="grid grid-cols-1 gap-4">
                            <div className="space-y-2">
                              <Label
                                htmlFor="edit-name"
                                className="text-gray-700 flex items-center space-x-2"
                              >
                                <User className="h-4 w-4" />
                                <span>Full Name</span>
                              </Label>
                              <Input
                                id="edit-name"
                                value={profileData.name}
                                onChange={(e) =>
                                  setProfileData({
                                    ...profileData,
                                    name: e.target.value,
                                  })
                                }
                                className="border-gray-200 focus:border-cyan-500 focus:ring-cyan-500"
                                required
                              />
                            </div>

                            <div className="space-y-2">
                              <Label
                                htmlFor="edit-email"
                                className="text-gray-700 flex items-center space-x-2"
                              >
                                <Mail className="h-4 w-4" />
                                <span>Email</span>
                              </Label>
                              <Input
                                id="edit-email"
                                type="email"
                                value={profileData.email}
                                className="border-gray-200 bg-gray-50 cursor-not-allowed"
                                disabled
                                readOnly
                              />
                              <p className="text-xs text-slate-500">
                                Email cannot be changed
                              </p>
                            </div>

                            <div className="space-y-2">
                              <Label
                                htmlFor="edit-phone"
                                className="text-gray-700 flex items-center space-x-2"
                              >
                                <Phone className="h-4 w-4" />
                                <span>Phone Number</span>
                              </Label>
                              <Input
                                id="edit-phone"
                                value={profileData.phone}
                                onChange={(e) => {
                                  const value = e.target.value
                                    .replace(/\D/g, "")
                                    .slice(0, 10);
                                  setProfileData({
                                    ...profileData,
                                    phone: value,
                                  });
                                }}
                                placeholder="10 digit phone number"
                                maxLength={10}
                                className="border-gray-200 focus:border-cyan-500 focus:ring-cyan-500"
                                required
                              />
                              <p className="text-xs text-slate-500">
                                Must be exactly 10 digits
                              </p>
                            </div>
                          </div>

                          <div className="flex flex-col space-y-3 pt-4">
                            <div className="flex space-x-3">
                              <Button
                                onClick={handleSaveProfile}
                                className="flex-1 bg-gradient-to-r from-cyan-600 to-blue-600 hover:from-cyan-700 hover:to-blue-700 text-white"
                              >
                                Save Changes
                              </Button>
                              <Button
                                variant="outline"
                                onClick={handleCancelEdit}
                                className="flex-1 border-gray-300 text-gray-700 hover:bg-gray-50"
                              >
                                Cancel
                              </Button>
                            </div>
                            <Button
                              type="button"
                              variant="ghost"
                              onClick={handleForgotPassword}
                              className="w-full text-cyan-600 hover:text-cyan-700 hover:bg-cyan-50"
                            >
                              <Lock className="h-4 w-4 mr-2" />
                              Forgot Password?
                            </Button>
                          </div>
                        </div>
                      ) : (
                        <div className="space-y-6">
                          <div className="flex items-center space-x-4">
                            <div className="w-24 h-24 rounded-full bg-gradient-to-br from-cyan-100 to-blue-100 flex items-center justify-center">
                              <User className="h-12 w-12 text-cyan-600" />
                            </div>
                            <div>
                              <h3 className="text-lg font-semibold text-slate-900">
                                {profileData.name}
                              </h3>
                              <p className="text-sm text-slate-600">Customer</p>
                            </div>
                          </div>

                          <div className="space-y-4">
                            <div className="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg">
                              <Mail className="h-5 w-5 text-slate-600" />
                              <div>
                                <p className="text-xs text-slate-500">Email</p>
                                <p className="text-sm font-medium text-slate-900">
                                  {profileData.email}
                                </p>
                              </div>
                            </div>

                            <div className="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg">
                              <Phone className="h-5 w-5 text-slate-600" />
                              <div>
                                <p className="text-xs text-slate-500">Phone</p>
                                <p className="text-sm font-medium text-slate-900">
                                  {profileData.phone}
                                </p>
                              </div>
                            </div>
                          </div>
                        </div>
                      )}
                    </CardContent>
                  </Card>
                </div>
              )}
            </>
           )} 
          </div>
        </main>
      </div>
    </div>
  );
}
