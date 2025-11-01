"use client"

import { useState } from "react"
import { Button } from "../../components/UI/Button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../../components/UI/Card"
import { Badge } from "../../components/UI/Badge"
import { Progress } from "../../components/UI/Progress"
import {
  Car,
  Calendar,
  Clock,
  Wrench,
  Bell,
  Plus,
  User,
  Settings,
  CheckCircle,
  AlertCircle,
  LayoutDashboard,
  History,
} from "lucide-react"
import { Link } from "react-router-dom"

export default function CustomerDashboard() {
  const [activeTab, setActiveTab] = useState("overview")

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

  const navItems = [
    { id: "overview", label: "Overview", icon: LayoutDashboard },
    { id: "vehicles", label: "My Vehicles", icon: Car },
    { id: "appointments", label: "Appointments", icon: Calendar },
    { id: "history", label: "Service History", icon: History },
  ]

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
            </div>
            <div className="flex items-center space-x-4">
              <Button variant="ghost" size="sm">
                <Bell className="h-4 w-4" />
              </Button>
              <Button variant="ghost" size="sm">
                <User className="h-4 w-4" />
                Ravi
              </Button>
              <Button variant="ghost" size="sm">
                <Settings className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </div>
      </nav>

      <div className="flex">
        <aside className="w-64 border-r border-border/50 bg-secondary/30 min-h-[calc(100vh-64px)]">
          <div className="p-6 space-y-2">
            <h3 className="text-sm font-semibold text-muted-foreground px-2 mb-4">MENU</h3>
            {navItems.map((item) => {
              const Icon = item.icon
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveTab(item.id)}
                  className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                    activeTab === item.id ? "bg-primary text-primary-foreground" : "text-foreground hover:bg-secondary"
                  }`}
                >
                  <Icon className="h-5 w-5" />
                  <span className="font-medium">{item.label}</span>
                </button>
              )
            })}
          </div>
        </aside>

        {/* Main Content */}
        <main className="flex-1">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            {/* Header */}
            <div className="mb-8">
              <h1 className="text-3xl font-bold text-balance">Welcome back, Ravi</h1>
              <p className="text-muted-foreground mt-2">Manage your vehicles and track service appointments</p>
            </div>

            {/* Active Service Alert */}
            {currentAppointments.length > 0 && (
              <Card className="mb-8 border-primary/20 bg-primary/5">
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-3">
                      <div className="bg-primary rounded-lg p-2">
                        <Wrench className="h-5 w-5 text-primary-foreground" />
                      </div>
                      <div>
                        <CardTitle className="text-primary">Service in Progress</CardTitle>
                        <CardDescription>Your Toyota Corolla is currently being serviced</CardDescription>
                      </div>
                    </div>
                    <Badge variant="secondary" className="bg-primary/10 text-primary">
                      Live
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    <div className="flex items-center justify-between text-sm">
                      <span>Progress</span>
                      <span className="text-primary font-medium">65% Complete</span>
                    </div>
                    <Progress value={65} className="h-2" />
                    <div className="grid grid-cols-2 gap-4 text-sm">
                      <div className="flex items-center space-x-2">
                        <User className="h-4 w-4 text-muted-foreground" />
                        <span>Technician: Nimal</span>
                      </div>
                      <div className="flex items-center space-x-2">
                        <Clock className="h-4 w-4 text-muted-foreground" />
                        <span>Est. completion: 11:30 AM</span>
                      </div>
                    </div>
                    <div className="flex items-center space-x-2">
                      <CheckCircle className="h-4 w-4 text-green-500" />
                      <span className="text-sm">Oil Change Completed</span>
                    </div>
                    <div className="flex items-center space-x-2">
                      <AlertCircle className="h-4 w-4 text-primary" />
                      <span className="text-sm">Tire Rotation in Progress</span>
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
                  <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                      <CardTitle className="text-sm font-medium">Registered Vehicles</CardTitle>
                      <Car className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                      <div className="text-2xl font-bold">{vehicles.length}</div>
                      <p className="text-xs text-muted-foreground">Active vehicles</p>
                    </CardContent>
                  </Card>

                  <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                      <CardTitle className="text-sm font-medium">Active Services</CardTitle>
                      <Wrench className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                      <div className="text-2xl font-bold">{currentAppointments.length}</div>
                      <p className="text-xs text-muted-foreground">In progress</p>
                    </CardContent>
                  </Card>

                  <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                      <CardTitle className="text-sm font-medium">Next Service</CardTitle>
                      <Calendar className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                      <div className="text-2xl font-bold">Feb 15</div>
                      <p className="text-xs text-muted-foreground">Toyota Corolla</p>
                    </CardContent>
                  </Card>
                </div>

                {/* Recent Activity */}
                <Card>
                  <CardHeader>
                    <CardTitle>Recent Activity</CardTitle>
                    <CardDescription>Your latest service updates and notifications</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <div className="flex items-center space-x-4">
                        <div className="bg-green-500/10 rounded-full p-2">
                          <CheckCircle className="h-4 w-4 text-green-500" />
                        </div>
                        <div className="flex-1">
                          <p className="text-sm font-medium">Oil change completed</p>
                          <p className="text-xs text-muted-foreground">Toyota Corolla • 2 hours ago</p>
                        </div>
                      </div>
                      <div className="flex items-center space-x-4">
                        <div className="bg-primary/10 rounded-full p-2">
                          <Wrench className="h-4 w-4 text-primary" />
                        </div>
                        <div className="flex-1">
                          <p className="text-sm font-medium">Service started</p>
                          <p className="text-xs text-muted-foreground">Toyota Corolla • 3 hours ago</p>
                        </div>
                      </div>
                      <div className="flex items-center space-x-4">
                        <div className="bg-blue-500/10 rounded-full p-2">
                          <Calendar className="h-4 w-4 text-blue-500" />
                        </div>
                        <div className="flex-1">
                          <p className="text-sm font-medium">Appointment confirmed</p>
                          <p className="text-xs text-muted-foreground">Toyota Corolla • Yesterday</p>
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
                  <h2 className="text-2xl font-bold">My Vehicles</h2>
                  <Link to="/dashboard/add-vehicle">
                    <Button>
                      <Plus className="h-4 w-4 mr-2" />
                      Add Vehicle
                    </Button>
                  </Link>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  {vehicles.map((vehicle) => (
                    <Card key={vehicle.id}>
                      <CardHeader>
                        <div className="flex items-center justify-between">
                          <div>
                            <CardTitle>
                              {vehicle.year} {vehicle.make} {vehicle.model}
                            </CardTitle>
                            <CardDescription>License: {vehicle.plate}</CardDescription>
                          </div>
                          <div className="bg-secondary rounded-lg p-3">
                            <Car className="h-6 w-6 text-primary" />
                          </div>
                        </div>
                      </CardHeader>
                      <CardContent>
                        <div className="space-y-3">
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Mileage</span>
                            <span className="font-medium">{vehicle.mileage.toLocaleString()} miles</span>
                          </div>
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Next Service</span>
                            <span className="font-medium">{vehicle.nextService}</span>
                          </div>
                          <div className="flex space-x-2 pt-2">
                            <Link to="/book" className="flex-1">
                              <Button variant="outline" size="sm" className="w-full bg-transparent">
                                Book Service
                              </Button>
                            </Link>
                            <Button variant="ghost" size="sm">
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
                  <h2 className="text-2xl font-bold">Appointments</h2>
                  <Link to="/book">
                    <Button>
                      <Plus className="h-4 w-4 mr-2" />
                      Book Service
                    </Button>
                  </Link>
                </div>

                {currentAppointments.length > 0 ? (
                  <div className="space-y-4">
                    {currentAppointments.map((appointment) => (
                      <Card key={appointment.id}>
                        <CardHeader>
                          <div className="flex items-center justify-between">
                            <div>
                              <CardTitle className="flex items-center space-x-2">
                                <span>Service Appointment</span>
                                <Badge
                                  variant={appointment.status === "in-progress" ? "default" : "secondary"}
                                  className={
                                    appointment.status === "in-progress" ? "bg-primary text-primary-foreground" : ""
                                  }
                                >
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
                            <div className="grid grid-cols-2 gap-4 text-sm">
                              <div>
                                <span className="text-muted-foreground">Services:</span>
                                <div className="mt-1">
                                  {appointment.services.map((service, index) => (
                                    <Badge key={index} variant="outline" className="mr-1 mb-1">
                                      {service}
                                    </Badge>
                                  ))}
                                </div>
                              </div>
                              <div>
                                <span className="text-muted-foreground">Technician:</span>
                                <p className="font-medium">{appointment.technician}</p>
                              </div>
                            </div>
                            {appointment.status === "in-progress" && (
                              <div className="space-y-2">
                                <div className="flex justify-between text-sm">
                                  <span>Progress</span>
                                  <span className="text-primary font-medium">{appointment.progress}%</span>
                                </div>
                                <Progress value={appointment.progress} className="h-2" />
                                <p className="text-sm text-muted-foreground">
                                  Estimated completion: {appointment.estimatedCompletion}
                                </p>
                              </div>
                            )}
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                ) : (
                  <Card>
                    <CardContent className="text-center py-12">
                      <Calendar className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                      <h3 className="text-lg font-medium mb-2">No upcoming appointments</h3>
                      <p className="text-muted-foreground mb-4">Schedule your next service appointment</p>
                      <Link to="/dashboard/book-service">
                        <Button>Book Service</Button>
                      </Link>
                    </CardContent>
                  </Card>
                )}
              </div>
            )}

            {/* Service History Tab */}
            {activeTab === "history" && (
              <div className="space-y-6">
                <h2 className="text-2xl font-bold">Service History</h2>

                <div className="space-y-4">
                  {serviceHistory.map((service) => (
                    <Card key={service.id}>
                      <CardHeader>
                        <div className="flex items-center justify-between">
                          <div>
                            <CardTitle className="flex items-center space-x-2">
                              <span>{service.vehicle}</span>
                              <Badge variant="outline" className="text-green-600 border-green-600">
                                Completed
                              </Badge>
                            </CardTitle>
                            <CardDescription>{service.date}</CardDescription>
                          </div>
                          <div className="text-right">
                            <p className="text-lg font-bold">{service.cost}</p>
                          </div>
                        </div>
                      </CardHeader>
                      <CardContent>
                        <div className="flex flex-wrap gap-2">
                          {service.services.map((serviceType, index) => (
                            <Badge key={index} variant="secondary">
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
