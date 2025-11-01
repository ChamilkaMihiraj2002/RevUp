import { useState } from "react"
import { CommonNavbar } from "@/components/Layout/Navbar"
import { CommonSidebar } from "@/components/Layout/Slidebar"
import { Button } from "@/components/UI/Button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/UI/Card"
import { Badge } from "@/components/UI/Badge"
import { Progress } from "@/components/UI/Progress"
import {
  Car,
  Calendar,
  Clock,
  Wrench,
  Plus,
  User,
  Settings,
  CheckCircle,
  AlertCircle,
  LayoutDashboard,
  History,
} from "lucide-react"

export default function CustomerDashboard() {
  const [activeTab, setActiveTab] = useState("overview")

  // Navigation items for Customer Dashboard
  const navItems = [
    { id: "overview", label: "Overview", icon: LayoutDashboard },
    { id: "vehicles", label: "My Vehicles", icon: Car },
    { id: "appointments", label: "Appointments", icon: Calendar },
    { id: "history", label: "Service History", icon: History },
  ]

  // Mock data
  const vehicles = [
    {
      id: 1,
      make: "Toyota",
      model: "Corolla",
      year: 2020,
      plate: "ABC-123",
      nextService: "2025-02-15",
      mileage: 45000,
    },
    {
      id: 2,
      make: "Honda",
      model: "Civic",
      year: 2019,
      plate: "XYZ-789",
      nextService: "2025-03-20",
      mileage: 52000,
    },
  ]

  const currentAppointments = [
    {
      id: 1,
      vehicleId: 1,
      date: "2025-01-15",
      time: "09:00 AM",
      services: ["Oil Change", "Tire Rotation"],
      status: "in-progress",
      technician: "Nimal",
      progress: 65,
      estimatedCompletion: "11:30 AM",
    },
  ]

  const serviceHistory = [
    {
      id: 1,
      date: "2024-12-10",
      vehicle: "Toyota Corolla",
      services: ["Oil Change", "Brake Inspection"],
      cost: "$120",
      status: "completed",
    },
    {
      id: 2,
      date: "2024-11-05",
      vehicle: "Honda Civic",
      services: ["Tire Replacement"],
      cost: "$280",
      status: "completed",
    },
  ]

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-cyan-50 to-blue-100">
      {/* IMPORTED COMMON NAVBAR */}
      <CommonNavbar 
        userName="Ravi"
        onNotificationClick={() => console.log("Notifications")}
        onProfileClick={() => console.log("Profile")}
        onSettingsClick={() => console.log("Settings")}
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
          <div className="h-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
            {/* Header */}
            <div className="mb-6">
              <h1 className="text-3xl font-bold text-gray-900">Welcome back, Ravi</h1>
              <p className="text-gray-600 mt-2">Manage your vehicles and track service appointments</p>
            </div>

            {/* Active Service Alert */}
            {currentAppointments.length > 0 && (
              <Card className="mb-6 border-cyan-200 bg-white shadow-md">
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-3">
                      <div className="bg-cyan-600 rounded-lg p-2.5">
                        <Wrench className="h-5 w-5 text-white" />
                      </div>
                      <div>
                        <CardTitle className="text-gray-900">Service in Progress</CardTitle>
                        <CardDescription className="text-cyan-700">Your Toyota Corolla is currently being serviced</CardDescription>
                      </div>
                    </div>
                    <Badge className="bg-cyan-100 text-cyan-700 border-cyan-200 hover:bg-cyan-200">
                      Live
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    <div className="flex items-center justify-between text-sm">
                      <span className="text-gray-600">Progress</span>
                      <span className="text-cyan-700 font-semibold">65% Complete</span>
                    </div>
                    <Progress value={65} className="h-2 bg-cyan-100" />
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                      <div className="flex items-center space-x-2">
                        <User className="h-4 w-4 text-slate-500" />
                        <span className="text-slate-700">Technician: <span className="font-medium">Nimal</span></span>
                      </div>
                      <div className="flex items-center space-x-2">
                        <Clock className="h-4 w-4 text-slate-500" />
                        <span className="text-slate-700">Est. completion: <span className="font-medium">11:30 AM</span></span>
                      </div>
                    </div>
                    <div className="space-y-2 pt-2 border-t border-blue-100">
                      <div className="flex items-center space-x-2">
                        <CheckCircle className="h-4 w-4 text-green-600" />
                        <span className="text-sm text-slate-700">Oil Change Completed</span>
                      </div>
                      <div className="flex items-center space-x-2">
                        <AlertCircle className="h-4 w-4 text-blue-600" />
                        <span className="text-sm text-slate-700">Tire Rotation in Progress</span>
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
                      <CardTitle className="text-sm font-medium text-slate-700">Registered Vehicles</CardTitle>
                      <div className="bg-blue-100 rounded-lg p-2">
                        <Car className="h-4 w-4 text-blue-600" />
                      </div>
                    </CardHeader>
                    <CardContent>
                      <div className="text-3xl font-bold text-slate-900">{vehicles.length}</div>
                      <p className="text-xs text-slate-500 mt-1">Active vehicles</p>
                    </CardContent>
                  </Card>

                  <Card className="hover:shadow-lg transition-shadow border-slate-200">
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                      <CardTitle className="text-sm font-medium text-slate-700">Active Services</CardTitle>
                      <div className="bg-green-100 rounded-lg p-2">
                        <Wrench className="h-4 w-4 text-green-600" />
                      </div>
                    </CardHeader>
                    <CardContent>
                      <div className="text-3xl font-bold text-slate-900">{currentAppointments.length}</div>
                      <p className="text-xs text-slate-500 mt-1">In progress</p>
                    </CardContent>
                  </Card>

                  <Card className="hover:shadow-lg transition-shadow border-slate-200">
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                      <CardTitle className="text-sm font-medium text-slate-700">Next Service</CardTitle>
                      <div className="bg-purple-100 rounded-lg p-2">
                        <Calendar className="h-4 w-4 text-purple-600" />
                      </div>
                    </CardHeader>
                    <CardContent>
                      <div className="text-3xl font-bold text-slate-900">Feb 15</div>
                      <p className="text-xs text-slate-500 mt-1">Toyota Corolla</p>
                    </CardContent>
                  </Card>
                </div>

                {/* Recent Activity */}
                <Card className="border-gray-200 shadow-md bg-white">
                  <CardHeader>
                    <CardTitle className="text-gray-900">Recent Activity</CardTitle>
                    <CardDescription className="text-cyan-700">Your latest service updates and notifications</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <div className="flex items-center space-x-4 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                        <div className="bg-green-100 rounded-full p-2.5">
                          <CheckCircle className="h-5 w-5 text-green-600" />
                        </div>
                        <div className="flex-1">
                          <p className="text-sm font-medium text-slate-900">Oil change completed</p>
                          <p className="text-xs text-slate-500">Toyota Corolla • 2 hours ago</p>
                        </div>
                      </div>
                      <div className="flex items-center space-x-4 p-3 rounded-lg hover:bg-slate-50 transition-colors">
                        <div className="bg-blue-100 rounded-full p-2.5">
                          <Wrench className="h-5 w-5 text-blue-600" />
                        </div>
                        <div className="flex-1">
                          <p className="text-sm font-medium text-slate-900">Service started</p>
                          <p className="text-xs text-slate-500">Toyota Corolla • 3 hours ago</p>
                        </div>
                      </div>
                      <div className="flex items-center space-x-4 p-3 rounded-lg hover:bg-slate-50 transition-colors">
                        <div className="bg-indigo-100 rounded-full p-2.5">
                          <Calendar className="h-5 w-5 text-indigo-600" />
                        </div>
                        <div className="flex-1">
                          <p className="text-sm font-medium text-slate-900">Appointment confirmed</p>
                          <p className="text-xs text-slate-500">Toyota Corolla • Yesterday</p>
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
                  <h2 className="text-2xl font-bold text-gray-900">My Vehicles</h2>
                  <Button className="bg-cyan-600 hover:bg-cyan-700 shadow-md">
                    <Plus className="h-4 w-4 mr-2" />
                    Add Vehicle
                  </Button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  {vehicles.map((vehicle) => (
                    <Card key={vehicle.id} className="hover:shadow-md transition-all border-gray-200 bg-white">
                      <CardHeader>
                        <div className="flex items-center justify-between">
                          <div>
                            <CardTitle className="text-slate-900">
                              {vehicle.year} {vehicle.make} {vehicle.model}
                            </CardTitle>
                            <CardDescription>License: {vehicle.plate}</CardDescription>
                          </div>
                          <div className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-xl p-3">
                            <Car className="h-6 w-6 text-blue-600" />
                          </div>
                        </div>
                      </CardHeader>
                      <CardContent>
                        <div className="space-y-3">
                          <div className="flex justify-between text-sm p-2 bg-slate-50 rounded-lg">
                            <span className="text-slate-600">Mileage</span>
                            <span className="font-semibold text-slate-900">{vehicle.mileage.toLocaleString()} miles</span>
                          </div>
                          <div className="flex justify-between text-sm p-2 bg-slate-50 rounded-lg">
                            <span className="text-slate-600">Next Service</span>
                            <span className="font-semibold text-slate-900">{vehicle.nextService}</span>
                          </div>
                          <div className="flex space-x-2 pt-2">
                            <Button variant="outline" size="sm" className="flex-1 border-blue-200 text-blue-700 hover:bg-blue-50">
                              Book Service
                            </Button>
                            <Button variant="ghost" size="sm" className="hover:bg-slate-100">
                              <Settings className="h-4 w-4" />
                            </Button>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </div>
            )}

            {/* Appointments Tab */}
            {activeTab === "appointments" && (
              <div className="space-y-6">
                <div className="flex justify-between items-center">
                  <h2 className="text-2xl font-bold text-gray-900">Appointments</h2>
                  <Button className="bg-cyan-600 hover:bg-cyan-700 shadow-md">
                    <Plus className="h-4 w-4 mr-2" />
                    Book Service
                  </Button>
                </div>

                {currentAppointments.length > 0 ? (
                  <div className="space-y-4">
                    {currentAppointments.map((appointment) => (
                      <Card key={appointment.id} className="border-gray-200 shadow-md bg-white">
                        <CardHeader>
                          <div className="flex items-center justify-between">
                            <div>
                              <CardTitle className="flex items-center space-x-2 text-slate-900">
                                <span>Service Appointment</span>
                                <Badge className={
                                  appointment.status === "in-progress" 
                                    ? "bg-blue-600 text-white shadow-lg shadow-blue-500/30" 
                                    : "bg-slate-200 text-slate-700"
                                }>
                                  {appointment.status === "in-progress" ? "In Progress" : appointment.status}
                                </Badge>
                              </CardTitle>
                              <CardDescription>
                                {appointment.date} at {appointment.time}
                              </CardDescription>
                            </div>
                          </div>
                        </CardHeader>
                        <CardContent>
                          <div className="space-y-4">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                              <div>
                                <span className="text-slate-600 font-medium">Services:</span>
                                <div className="mt-2 flex flex-wrap gap-2">
                                  {appointment.services.map((service, index) => (
                                    <Badge key={index} variant="outline" className="border-blue-200 text-blue-700">
                                      {service}
                                    </Badge>
                                  ))}
                                </div>
                              </div>
                              <div>
                                <span className="text-slate-600 font-medium">Technician:</span>
                                <p className="font-semibold text-slate-900 mt-1">{appointment.technician}</p>
                              </div>
                            </div>
                            {appointment.status === "in-progress" && (
                              <div className="space-y-2 pt-3 border-t border-slate-200">
                                <div className="flex justify-between text-sm">
                                  <span className="text-slate-600">Progress</span>
                                  <span className="text-blue-700 font-semibold">{appointment.progress}%</span>
                                </div>
                                <Progress value={appointment.progress} className="h-2 bg-blue-100" />
                                <p className="text-sm text-slate-600">
                                  Estimated completion: <span className="font-medium text-slate-900">{appointment.estimatedCompletion}</span>
                                </p>
                              </div>
                            )}
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                ) : (
                  <Card className="border-gray-200 bg-white">
                    <CardContent className="text-center py-12">
                      <div className="bg-cyan-50 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                        <Calendar className="h-8 w-8 text-cyan-600" />
                      </div>
                      <h3 className="text-lg font-semibold text-gray-900 mb-2">No upcoming appointments</h3>
                      <p className="text-gray-600 mb-6">Schedule your next service appointment</p>
                      <Button className="bg-cyan-600 hover:bg-cyan-700 shadow-md">
                        Book Service
                      </Button>
                    </CardContent>
                  </Card>
                )}
              </div>
            )}

            {/* Service History Tab */}
            {activeTab === "history" && (
              <div className="space-y-6">
                <h2 className="text-2xl font-bold text-slate-900">Service History</h2>

                <div className="space-y-4">
                  {serviceHistory.map((service) => (
                    <Card key={service.id} className="border-slate-200 hover:shadow-lg transition-shadow">
                      <CardHeader>
                        <div className="flex items-center justify-between">
                          <div>
                            <CardTitle className="flex items-center space-x-2 text-slate-900">
                              <span>{service.vehicle}</span>
                              <Badge variant="outline" className="text-green-700 border-green-300 bg-green-50">
                                <CheckCircle className="h-3 w-3 mr-1" />
                                Completed
                              </Badge>
                            </CardTitle>
                            <CardDescription>{service.date}</CardDescription>
                          </div>
                          <div className="text-right">
                            <p className="text-2xl font-bold text-slate-900">{service.cost}</p>
                          </div>
                        </div>
                      </CardHeader>
                      <CardContent>
                        <div className="flex flex-wrap gap-2">
                          {service.services.map((serviceType, index) => (
                            <Badge key={index} className="bg-slate-100 text-slate-700 hover:bg-slate-200">
                              {serviceType}
                            </Badge>
                          ))}
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </div>
            )}
          </div>
        </main>
      </div>
    </div>
  )
}