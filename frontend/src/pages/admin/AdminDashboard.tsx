"use client"

import { useState } from "react"
import { Badge } from "@/components/UI/Badge"
import { Button } from "@/components/UI/Button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/UI/Card"
import { Input } from "@/components/UI/Input"
import { Progress } from "@/components/UI/Progress"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/UI/Select"
import {
    Car,
    Calendar,
    BarChart3,
    Clock,
    CheckCircle,
    Search,
    Filter,
    Settings,
    Bell,
    User,
    Wrench,
    DollarSign,
    TrendingUp,
    Activity,
    Users,
    LineChart,
    Sliders,
} from "lucide-react"
import Link from "next/link"

export default function AdminDashboard() {
    const [activeTab, setActiveTab] = useState("appointments")
    const [searchQuery, setSearchQuery] = useState("")

    // Mock data
    const admin = {
        name: "Shanika",
        role: "Service Advisor",
    }

    const stats = {
        totalAppointments: 24,
        activeServices: 8,
        completedToday: 12,
        revenue: 2850,
        avgServiceTime: 85,
        customerSatisfaction: 4.8,
    }

    const technicians = [
        {
            id: "tech-001",
            name: "Nimal",
            status: "active",
            currentJob: "Oil Change - Toyota Corolla",
            completedToday: 3,
            efficiency: 95,
            shift: "8:00 AM - 5:00 PM",
        },
        {
            id: "tech-002",
            name: "Kasun",
            status: "active",
            currentJob: "Brake Inspection - Honda Civic",
            completedToday: 2,
            efficiency: 88,
            shift: "9:00 AM - 6:00 PM",
        },
        {
            id: "tech-003",
            name: "Priya",
            status: "break",
            currentJob: null,
            completedToday: 4,
            efficiency: 92,
            shift: "7:00 AM - 4:00 PM",
        },
    ]

    const appointments = [
        {
            id: "appt-001",
            customer: "Ravi",
            vehicle: "2020 Toyota Corolla",
            services: ["Oil Change", "Tire Rotation"],
            technician: "Nimal",
            status: "in-progress",
            startTime: "9:00 AM",
            progress: 75,
            priority: "normal",
            estimatedCompletion: "10:15 AM",
        },
        {
            id: "appt-002",
            customer: "Sarah",
            vehicle: "2019 Honda Civic",
            services: ["Brake Inspection", "AC Service"],
            technician: "Kasun",
            status: "in-progress",
            startTime: "10:30 AM",
            progress: 30,
            priority: "high",
            estimatedCompletion: "1:00 PM",
        },
        {
            id: "appt-003",
            customer: "Mike",
            vehicle: "2021 Ford Focus",
            services: ["Battery Test"],
            technician: "unassigned",
            status: "pending",
            startTime: "11:00 AM",
            progress: 0,
            priority: "low",
            estimatedCompletion: "11:30 AM",
        },
        {
            id: "appt-004",
            customer: "Lisa",
            vehicle: "2018 BMW X3",
            services: ["Diagnostic Scan", "Oil Change"],
            technician: "Priya",
            status: "completed",
            startTime: "8:00 AM",
            progress: 100,
            priority: "normal",
            estimatedCompletion: "9:30 AM",
        },
    ]

    const getStatusColor = (status: string) => {
        switch (status) {
            case "completed":
                return "bg-green-500/10 text-green-600 border-green-200"
            case "in-progress":
                return "bg-primary/10 text-primary border-primary/20"
            case "pending":
                return "bg-yellow-500/10 text-yellow-600 border-yellow-200"
            default:
                return "bg-gray-500/10 text-gray-600 border-gray-200"
        }
    }

    const getPriorityColor = (priority: string) => {
        switch (priority) {
            case "high":
                return "bg-red-500/10 text-red-600 border-red-200"
            case "normal":
                return "bg-blue-500/10 text-blue-600 border-blue-200"
            case "low":
                return "bg-gray-500/10 text-gray-600 border-gray-200"
            default:
                return "bg-gray-500/10 text-gray-600 border-gray-200"
        }
    }

    const getTechnicianStatusColor = (status: string) => {
        switch (status) {
            case "active":
                return "bg-green-500/10 text-green-600 border-green-200"
            case "break":
                return "bg-yellow-500/10 text-yellow-600 border-yellow-200"
            case "offline":
                return "bg-gray-500/10 text-gray-600 border-gray-200"
            default:
                return "bg-gray-500/10 text-gray-600 border-gray-200"
        }
    }

    const assignTechnician = (appointmentId: string, technicianId: string) => {
        // Handle technician assignment logic
        console.log(`Assigning technician ${technicianId} to appointment ${appointmentId}`)
    }

    return (
        <div className="min-h-screen bg-background">
            {/* Navigation */}
            <nav className="border-b border-border/50 backdrop-blur-sm">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center h-16">
                        <div className="flex items-center space-x-3">
                            <Link href="/" className="flex items-center space-x-3">
                                <div className="bg-primary rounded-lg p-2">
                                    <Car className="h-6 w-6 text-primary-foreground" />
                                </div>
                                <span className="text-xl font-bold">RevUp</span>
                            </Link>
                            <Badge variant="secondary" className="ml-4">
                                Admin Portal
                            </Badge>
                        </div>
                        <div className="flex items-center space-x-4">
                            <Button variant="ghost" size="sm">
                                <Bell className="h-4 w-4" />
                            </Button>
                            <div className="text-right">
                                <p className="text-sm font-medium">{admin.name}</p>
                                <p className="text-xs text-muted-foreground">{admin.role}</p>
                            </div>
                            <Button variant="ghost" size="sm">
                                <User className="h-4 w-4" />
                            </Button>
                            <Button variant="ghost" size="sm">
                                <Settings className="h-4 w-4" />
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
                            onClick={() => setActiveTab("appointments")}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                                activeTab === "appointments" ? "bg-primary text-primary-foreground" : "text-foreground hover:bg-muted"
                            }`}
                        >
                            <Calendar className="h-5 w-5" />
                            <span>Appointments</span>
                        </button>
                        <button
                            onClick={() => setActiveTab("technicians")}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                                activeTab === "technicians" ? "bg-primary text-primary-foreground" : "text-foreground hover:bg-muted"
                            }`}
                        >
                            <Users className="h-5 w-5" />
                            <span>Technicians</span>
                        </button>
                        <button
                            onClick={() => setActiveTab("analytics")}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                                activeTab === "analytics" ? "bg-primary text-primary-foreground" : "text-foreground hover:bg-muted"
                            }`}
                        >
                            <LineChart className="h-5 w-5" />
                            <span>Analytics</span>
                        </button>
                        <button
                            onClick={() => setActiveTab("settings")}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                                activeTab === "settings" ? "bg-primary text-primary-foreground" : "text-foreground hover:bg-muted"
                            }`}
                        >
                            <Sliders className="h-5 w-5" />
                            <span>Settings</span>
                        </button>
                    </div>
                </aside>

                {/* Main Content */}
                <main className="flex-1">
                    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                        {/* Header */}
                        <div className="mb-8">
                            <h1 className="text-3xl font-bold text-balance">Service Center Dashboard</h1>
                            <p className="text-muted-foreground mt-2">
                                Monitor operations, manage appointments, and oversee technicians
                            </p>
                        </div>

                        {/* Key Metrics */}
                        <div className="grid grid-cols-2 lg:grid-cols-6 gap-4 mb-8">
                            <Card>
                                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                    <CardTitle className="text-sm font-medium">Total Appointments</CardTitle>
                                    <Calendar className="h-4 w-4 text-muted-foreground" />
                                </CardHeader>
                                <CardContent>
                                    <div className="text-2xl font-bold">{stats.totalAppointments}</div>
                                    <p className="text-xs text-muted-foreground">Today</p>
                                </CardContent>
                            </Card>

                            <Card>
                                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                    <CardTitle className="text-sm font-medium">Active Services</CardTitle>
                                    <Activity className="h-4 w-4 text-muted-foreground" />
                                </CardHeader>
                                <CardContent>
                                    <div className="text-2xl font-bold text-primary">{stats.activeServices}</div>
                                    <p className="text-xs text-muted-foreground">In progress</p>
                                </CardContent>
                            </Card>

                            <Card>
                                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                    <CardTitle className="text-sm font-medium">Completed</CardTitle>
                                    <CheckCircle className="h-4 w-4 text-muted-foreground" />
                                </CardHeader>
                                <CardContent>
                                    <div className="text-2xl font-bold text-green-600">{stats.completedToday}</div>
                                    <p className="text-xs text-muted-foreground">Today</p>
                                </CardContent>
                            </Card>

                            <Card>
                                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                    <CardTitle className="text-sm font-medium">Revenue</CardTitle>
                                    <DollarSign className="h-4 w-4 text-muted-foreground" />
                                </CardHeader>
                                <CardContent>
                                    <div className="text-2xl font-bold">${stats.revenue}</div>
                                    <p className="text-xs text-muted-foreground">Today</p>
                                </CardContent>
                            </Card>

                            <Card>
                                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                    <CardTitle className="text-sm font-medium">Avg Service Time</CardTitle>
                                    <Clock className="h-4 w-4 text-muted-foreground" />
                                </CardHeader>
                                <CardContent>
                                    <div className="text-2xl font-bold">{stats.avgServiceTime}m</div>
                                    <p className="text-xs text-green-600 flex items-center">
                                        <TrendingUp className="h-3 w-3 mr-1" />
                                        5% faster
                                    </p>
                                </CardContent>
                            </Card>

                            <Card>
                                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                    <CardTitle className="text-sm font-medium">Satisfaction</CardTitle>
                                    <BarChart3 className="h-4 w-4 text-muted-foreground" />
                                </CardHeader>
                                <CardContent>
                                    <div className="text-2xl font-bold">{stats.customerSatisfaction}</div>
                                    <p className="text-xs text-muted-foreground">out of 5.0</p>
                                </CardContent>
                            </Card>
                        </div>

                        {/* Content based on active tab */}
                        {activeTab === "appointments" && (
                            <div className="space-y-6">
                                {/* Filters */}
                                <Card>
                                    <CardContent className="py-4">
                                        <div className="flex items-center space-x-4">
                                            <div className="flex-1">
                                                <div className="relative">
                                                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                                                    <Input
                                                        placeholder="Search appointments..."
                                                        value={searchQuery}
                                                        onChange={(e) => setSearchQuery(e.target.value)}
                                                        className="pl-10"
                                                    />
                                                </div>
                                            </div>
                                            <Select>
                                                <SelectTrigger className="w-40">
                                                    <SelectValue placeholder="Status" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem value="all">All Status</SelectItem>
                                                    <SelectItem value="pending">Pending</SelectItem>
                                                    <SelectItem value="in-progress">In Progress</SelectItem>
                                                    <SelectItem value="completed">Completed</SelectItem>
                                                </SelectContent>
                                            </Select>
                                            <Select>
                                                <SelectTrigger className="w-40">
                                                    <SelectValue placeholder="Priority" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem value="all">All Priority</SelectItem>
                                                    <SelectItem value="high">High</SelectItem>
                                                    <SelectItem value="normal">Normal</SelectItem>
                                                    <SelectItem value="low">Low</SelectItem>
                                                </SelectContent>
                                            </Select>
                                            <Button variant="outline" size="sm">
                                                <Filter className="h-4 w-4" />
                                            </Button>
                                        </div>
                                    </CardContent>
                                </Card>

                                {/* Appointments List */}
                                <div className="space-y-4">
                                    {appointments.map((appointment) => (
                                        <Card key={appointment.id}>
                                            <CardHeader>
                                                <div className="flex items-center justify-between">
                                                    <div>
                                                        <CardTitle className="flex items-center space-x-3">
                                                            <span>{appointment.customer}</span>
                                                            <Badge variant="outline" className={getStatusColor(appointment.status)}>
                                                                {appointment.status}
                                                            </Badge>
                                                            <Badge variant="outline" className={getPriorityColor(appointment.priority)}>
                                                                {appointment.priority}
                                                            </Badge>
                                                        </CardTitle>
                                                        <CardDescription>
                                                            {appointment.vehicle} • Start: {appointment.startTime} • Est. completion:{" "}
                                                            {appointment.estimatedCompletion}
                                                        </CardDescription>
                                                    </div>
                                                    <div className="text-right">
                                                        <p className="text-sm text-muted-foreground">Progress</p>
                                                        <p className="text-lg font-bold text-primary">{appointment.progress}%</p>
                                                    </div>
                                                </div>
                                                {appointment.status === "in-progress" && (
                                                    <Progress value={appointment.progress} className="h-2" />
                                                )}
                                            </CardHeader>

                                            <CardContent>
                                                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                                    <div>
                                                        <h4 className="font-medium text-sm text-muted-foreground mb-2">Services</h4>
                                                        <div className="flex flex-wrap gap-1">
                                                            {appointment.services.map((service, index) => (
                                                                <Badge key={index} variant="secondary" className="text-xs">
                                                                    {service}
                                                                </Badge>
                                                            ))}
                                                        </div>
                                                    </div>

                                                    <div>
                                                        <h4 className="font-medium text-sm text-muted-foreground mb-2">Technician</h4>
                                                        {appointment.technician === "unassigned" ? (
                                                            <Select onValueChange={(value) => assignTechnician(appointment.id, value)}>
                                                                <SelectTrigger className="w-full">
                                                                    <SelectValue placeholder="Assign technician" />
                                                                </SelectTrigger>
                                                                <SelectContent>
                                                                    {technicians
                                                                        .filter((tech) => tech.status === "active")
                                                                        .map((tech) => (
                                                                            <SelectItem key={tech.id} value={tech.id}>
                                                                                {tech.name}
                                                                            </SelectItem>
                                                                        ))}
                                                                </SelectContent>
                                                            </Select>
                                                        ) : (
                                                            <div className="flex items-center space-x-2">
                                                                <User className="h-4 w-4 text-muted-foreground" />
                                                                <span className="font-medium">{appointment.technician}</span>
                                                            </div>
                                                        )}
                                                    </div>

                                                    <div className="flex justify-end items-center space-x-2">
                                                        {appointment.status === "pending" && (
                                                            <Button size="sm" variant="outline">
                                                                Start Service
                                                            </Button>
                                                        )}
                                                        {appointment.status === "completed" && (
                                                            <Button size="sm" variant="outline">
                                                                View Details
                                                            </Button>
                                                        )}
                                                        <Button size="sm" variant="ghost">
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

                        {activeTab === "technicians" && (
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                {technicians.map((technician) => (
                                    <Card key={technician.id}>
                                        <CardHeader>
                                            <div className="flex items-center justify-between">
                                                <div>
                                                    <CardTitle className="flex items-center space-x-2">
                                                        <span>{technician.name}</span>
                                                        <Badge variant="outline" className={getTechnicianStatusColor(technician.status)}>
                                                            {technician.status}
                                                        </Badge>
                                                    </CardTitle>
                                                    <CardDescription>{technician.shift}</CardDescription>
                                                </div>
                                                <div className="text-right">
                                                    <p className="text-sm text-muted-foreground">Efficiency</p>
                                                    <p className="text-lg font-bold text-primary">{technician.efficiency}%</p>
                                                </div>
                                            </div>
                                        </CardHeader>

                                        <CardContent className="space-y-4">
                                            <div>
                                                <h4 className="font-medium text-sm text-muted-foreground mb-1">Current Job</h4>
                                                {technician.currentJob ? (
                                                    <p className="text-sm">{technician.currentJob}</p>
                                                ) : (
                                                    <p className="text-sm text-muted-foreground">No active job</p>
                                                )}
                                            </div>

                                            <div className="grid grid-cols-2 gap-4 text-sm">
                                                <div>
                                                    <span className="text-muted-foreground">Completed Today</span>
                                                    <p className="font-medium">{technician.completedToday}</p>
                                                </div>
                                                <div>
                                                    <span className="text-muted-foreground">Status</span>
                                                    <p className="font-medium capitalize">{technician.status}</p>
                                                </div>
                                            </div>

                                            <div className="flex space-x-2">
                                                <Button size="sm" variant="outline" className="flex-1 bg-transparent">
                                                    <Wrench className="h-4 w-4 mr-2" />
                                                    Assign Job
                                                </Button>
                                                <Button size="sm" variant="ghost">
                                                    <Settings className="h-4 w-4" />
                                                </Button>
                                            </div>
                                        </CardContent>
                                    </Card>
                                ))}
                            </div>
                        )}

                        {activeTab === "analytics" && (
                            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                                <Card>
                                    <CardHeader>
                                        <CardTitle>Service Performance</CardTitle>
                                        <CardDescription>Daily service completion trends</CardDescription>
                                    </CardHeader>
                                    <CardContent>
                                        <div className="h-64 flex items-center justify-center text-muted-foreground">
                                            <BarChart3 className="h-12 w-12 mb-4" />
                                            <p>Analytics chart would be displayed here</p>
                                        </div>
                                    </CardContent>
                                </Card>

                                <Card>
                                    <CardHeader>
                                        <CardTitle>Revenue Trends</CardTitle>
                                        <CardDescription>Monthly revenue and growth</CardDescription>
                                    </CardHeader>
                                    <CardContent>
                                        <div className="h-64 flex items-center justify-center text-muted-foreground">
                                            <TrendingUp className="h-12 w-12 mb-4" />
                                            <p>Revenue chart would be displayed here</p>
                                        </div>
                                    </CardContent>
                                </Card>

                                <Card>
                                    <CardHeader>
                                        <CardTitle>Customer Satisfaction</CardTitle>
                                        <CardDescription>Feedback and ratings over time</CardDescription>
                                    </CardHeader>
                                    <CardContent>
                                        <div className="space-y-4">
                                            <div className="flex justify-between items-center">
                                                <span>Overall Rating</span>
                                                <span className="text-2xl font-bold text-primary">{stats.customerSatisfaction}/5.0</span>
                                            </div>
                                            <div className="space-y-2">
                                                <div className="flex justify-between text-sm">
                                                    <span>5 stars</span>
                                                    <span>68%</span>
                                                </div>
                                                <Progress value={68} className="h-2" />
                                                <div className="flex justify-between text-sm">
                                                    <span>4 stars</span>
                                                    <span>22%</span>
                                                </div>
                                                <Progress value={22} className="h-2" />
                                                <div className="flex justify-between text-sm">
                                                    <span>3 stars</span>
                                                    <span>8%</span>
                                                </div>
                                                <Progress value={8} className="h-2" />
                                            </div>
                                        </div>
                                    </CardContent>
                                </Card>

                                <Card>
                                    <CardHeader>
                                        <CardTitle>Service Types</CardTitle>
                                        <CardDescription>Most popular services this month</CardDescription>
                                    </CardHeader>
                                    <CardContent>
                                        <div className="space-y-4">
                                            <div className="flex justify-between items-center">
                                                <span>Oil Change</span>
                                                <span className="font-medium">45 services</span>
                                            </div>
                                            <div className="flex justify-between items-center">
                                                <span>Brake Inspection</span>
                                                <span className="font-medium">32 services</span>
                                            </div>
                                            <div className="flex justify-between items-center">
                                                <span>Tire Rotation</span>
                                                <span className="font-medium">28 services</span>
                                            </div>
                                            <div className="flex justify-between items-center">
                                                <span>AC Service</span>
                                                <span className="font-medium">19 services</span>
                                            </div>
                                        </div>
                                    </CardContent>
                                </Card>
                            </div>
                        )}

                        {activeTab === "settings" && (
                            <Card>
                                <CardHeader>
                                    <CardTitle>Service Center Settings</CardTitle>
                                    <CardDescription>Configure your service center operations</CardDescription>
                                </CardHeader>
                                <CardContent>
                                    <div className="space-y-6">
                                        <div>
                                            <h4 className="font-medium mb-4">Operating Hours</h4>
                                            <div className="grid grid-cols-2 gap-4">
                                                <div>
                                                    <label className="text-sm font-medium">Opening Time</label>
                                                    <Input type="time" defaultValue="08:00" />
                                                </div>
                                                <div>
                                                    <label className="text-sm font-medium">Closing Time</label>
                                                    <Input type="time" defaultValue="17:00" />
                                                </div>
                                            </div>
                                        </div>

                                        <div>
                                            <h4 className="font-medium mb-4">Service Pricing</h4>
                                            <div className="space-y-3">
                                                <div className="flex justify-between items-center">
                                                    <span>Oil Change</span>
                                                    <Input type="number" defaultValue="45" className="w-20" />
                                                </div>
                                                <div className="flex justify-between items-center">
                                                    <span>Brake Inspection</span>
                                                    <Input type="number" defaultValue="75" className="w-20" />
                                                </div>
                                                <div className="flex justify-between items-center">
                                                    <span>Tire Rotation</span>
                                                    <Input type="number" defaultValue="25" className="w-20" />
                                                </div>
                                            </div>
                                        </div>

                                        <div className="flex justify-end">
                                            <Button>Save Settings</Button>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        )}
                    </div>
                </main>
            </div>
        </div>
    )
}

