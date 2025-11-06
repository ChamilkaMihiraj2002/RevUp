"use client"

import { useState, useEffect } from "react"
import { Badge } from "@/components/UI/Badge"
import { Button } from "@/components/UI/Button"
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/UI/Card"
import { Progress } from "@/components/UI/Progress"
import { Textarea } from "@/components/UI/Textarea"
import { useAuth } from "@/contexts/authContext/authContext"
import { useNavigate } from "react-router-dom"
import {
    Car,
    Clock,
    Play,
    Pause,
    CheckCircle,
    User,
    Wrench,
    Timer,
    MessageSquare,
    Settings,
    LayoutList,
    CheckSquare,
    Calendar,
    LogOut,
} from "lucide-react"
import Link from "next/link"

export default function TechnicianDashboard() {
    const { userData, logout } = useAuth()
    const navigate = useNavigate()
    const [activeTab, setActiveTab] = useState("assignments")
    const [activeTimer, setActiveTimer] = useState<string | null>(null)
    const [timers, setTimers] = useState<Record<string, number>>({})
    const [notes, setNotes] = useState<Record<string, string>>({})

    const handleLogout = async () => {
        try {
            await logout()
            navigate("/login")
        } catch (error) {
            console.error("Logout error:", error)
        }
    }

    interface Service {
        id: string
        name: string
        duration: number
        status: "completed" | "in-progress" | "pending" | string
        timeSpent: number
    }

    interface Appointment {
        id: string
        customer: string
        vehicle: string
        plate: string
        services: Service[]
        startTime: string
        estimatedCompletion: string
        priority: "high" | "normal" | "low" | string
        customerNotes?: string
    }

    const assignments = [
        {
            id: "appt-001",
            customer: "Ravi",
            vehicle: "2020 Toyota Corolla",
            plate: "ABC-123",
            services: [
                { id: "oil-change", name: "Oil Change", duration: 45, status: "completed", timeSpent: 45 },
                { id: "tire-rotation", name: "Tire Rotation", duration: 30, status: "in-progress", timeSpent: 15 },
            ],
            startTime: "9:00 AM",
            estimatedCompletion: "10:15 AM",
            priority: "normal",
            customerNotes: "Customer mentioned unusual noise from front wheels",
        },
        {
            id: "appt-002",
            customer: "Sarah",
            vehicle: "2019 Honda Civic",
            plate: "XYZ-789",
            services: [
                { id: "brake-inspection", name: "Brake Inspection", duration: 60, status: "pending", timeSpent: 0 },
                { id: "ac-service", name: "AC Service", duration: 90, status: "pending", timeSpent: 0 },
            ],
            startTime: "11:00 AM",
            estimatedCompletion: "1:30 PM",
            priority: "high",
            customerNotes: "AC not cooling properly, brake pedal feels soft",
        },
    ]

    const [appointmentData, setAppointmentData] = useState<Appointment[]>(assignments)

    // Timer logic
    useEffect(() => {
        let interval: ReturnType<typeof setInterval> | undefined
        if (activeTimer) {
            interval = setInterval(() => {
                setTimers((prev) => ({
                    ...prev,
                    [activeTimer]: (prev[activeTimer] || 0) + 1,
                }))
            }, 1000)
        }
        return () => {
            if (interval !== undefined) clearInterval(interval)
        }
    }, [activeTimer])

    const startTimer = (serviceId: string) => {
        setActiveTimer(serviceId)
    }

    const pauseTimer = () => {
        setActiveTimer(null)
    }

    const completeService = (appointmentId: string, serviceId: string) => {
        setAppointmentData((prev) =>
            prev.map((appointment) =>
                appointment.id === appointmentId
                    ? {
                        ...appointment,
                        services: appointment.services.map((service) =>
                            service.id === serviceId
                                ? {
                                    ...service,
                                    status: "completed",
                                    timeSpent: timers[serviceId] || service.timeSpent,
                                }
                                : service,
                        ),
                    }
                    : appointment,
            ),
        )
        setActiveTimer(null)
    }

    const formatTime = (seconds: number) => {
        const mins = Math.floor(seconds / 60)
        const secs = seconds % 60
        return `${mins}:${secs.toString().padStart(2, "0")}`
    }

    const getAppointmentProgress = (appointment: Appointment) => {
        const totalServices = appointment.services.length
        const completedServices = appointment.services.filter((s: Service) => s.status === "completed").length
        return totalServices === 0 ? 0 : (completedServices / totalServices) * 100
    }

    const getStatusColor = (status: string) => {
        switch (status) {
            case "completed":
                return "bg-green-500/10 text-green-600 border-green-200"
            case "in-progress":
                return "bg-primary/10 text-primary border-primary/20"
            case "pending":
                return "bg-gray-500/10 text-gray-600 border-gray-200"
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
                                Technician Portal
                            </Badge>
                        </div>
                        <div className="flex items-center space-x-4">
                            <div className="text-right">
                                <p className="text-sm font-medium">{userData?.name || "Technician"}</p>
                                <p className="text-xs text-muted-foreground">Technician Portal</p>
                            </div>
                            <Button variant="ghost" size="sm">
                                <User className="h-4 w-4" />
                            </Button>
                            <Button variant="ghost" size="sm">
                                <Settings className="h-4 w-4" />
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
                                activeTab === "assignments" ? "bg-primary text-primary-foreground" : "text-foreground hover:bg-muted"
                            }`}
                        >
                            <LayoutList className="h-5 w-5" />
                            <span>My Assignments</span>
                        </button>
                        <button
                            onClick={() => setActiveTab("completed")}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                                activeTab === "completed" ? "bg-primary text-primary-foreground" : "text-foreground hover:bg-muted"
                            }`}
                        >
                            <CheckSquare className="h-5 w-5" />
                            <span>Completed Today</span>
                        </button>
                        <button
                            onClick={() => setActiveTab("schedule")}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                                activeTab === "schedule" ? "bg-primary text-primary-foreground" : "text-foreground hover:bg-muted"
                            }`}
                        >
                            <Calendar className="h-5 w-5" />
                            <span>Schedule</span>
                        </button>
                    </div>
                </aside>

                {/* Main Content */}
                <main className="flex-1">
                    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                        {/* Header */}
                        <div className="mb-8">
                            <h1 className="text-3xl font-bold text-balance">Service Dashboard</h1>
                            <p className="text-muted-foreground mt-2">Track your assigned appointments and log service time</p>
                        </div>

                        {/* Active Timer Alert */}
                        {activeTimer && (
                            <Card className="mb-8 border-primary/20 bg-primary/5">
                                <CardContent className="py-4">
                                    <div className="flex items-center justify-between">
                                        <div className="flex items-center space-x-3">
                                            <div className="bg-primary rounded-lg p-2">
                                                <Timer className="h-5 w-5 text-primary-foreground animate-pulse" />
                                            </div>
                                            <div>
                                                <p className="font-medium text-primary">Timer Active</p>
                                                <p className="text-sm text-muted-foreground">
                                                    {appointmentData.flatMap((a) => a.services).find((s) => s.id === activeTimer)?.name ||
                                                        "Service"}{" "}
                                                    - {formatTime(timers[activeTimer] || 0)}
                                                </p>
                                            </div>
                                        </div>
                                        <Button variant="outline" size="sm" onClick={pauseTimer}>
                                            <Pause className="h-4 w-4 mr-2" />
                                            Pause
                                        </Button>
                                    </div>
                                </CardContent>
                            </Card>
                        )}

                        {/* Content based on active tab */}
                        {activeTab === "assignments" && (
                            <div className="space-y-6">
                                {appointmentData.map((appointment) => (
                                    <Card key={appointment.id} className="overflow-hidden">
                                        <CardHeader>
                                            <div className="flex items-center justify-between">
                                                <div>
                                                    <CardTitle className="flex items-center space-x-3">
                                                        <span>{appointment.vehicle}</span>
                                                        <Badge variant="outline" className={getPriorityColor(appointment.priority)}>
                                                            {appointment.priority} priority
                                                        </Badge>
                                                    </CardTitle>
                                                    <CardDescription>
                                                        Customer: {appointment.customer} • Plate: {appointment.plate} • Start:{" "}
                                                        {appointment.startTime}
                                                    </CardDescription>
                                                </div>
                                                <div className="text-right">
                                                    <p className="text-sm text-muted-foreground">Progress</p>
                                                    <p className="text-lg font-bold text-primary">
                                                        {Math.round(getAppointmentProgress(appointment))}%
                                                    </p>
                                                </div>
                                            </div>
                                            <Progress value={getAppointmentProgress(appointment)} className="h-2" />
                                        </CardHeader>

                                        <CardContent className="space-y-6">
                                            {/* Customer Notes */}
                                            {appointment.customerNotes && (
                                                <div className="bg-muted/50 rounded-lg p-4">
                                                    <div className="flex items-start space-x-2">
                                                        <MessageSquare className="h-4 w-4 text-muted-foreground mt-0.5" />
                                                        <div>
                                                            <p className="text-sm font-medium">Customer Notes</p>
                                                            <p className="text-sm text-muted-foreground">{appointment.customerNotes}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            )}

                                            {/* Services */}
                                            <div className="space-y-4">
                                                <h4 className="font-medium">Services</h4>
                                                {appointment.services.map((service) => (
                                                    <div key={service.id} className="border rounded-lg p-4">
                                                        <div className="flex items-center justify-between mb-4">
                                                            <div className="flex items-center space-x-3">
                                                                <Wrench className="h-5 w-5 text-muted-foreground" />
                                                                <div>
                                                                    <h5 className="font-medium">{service.name}</h5>
                                                                    <p className="text-sm text-muted-foreground">
                                                                        Estimated: {service.duration} min • Spent: {service.timeSpent} min
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <Badge variant="outline" className={getStatusColor(service.status)}>
                                                                {service.status === "in-progress" && activeTimer === service.id && (
                                                                    <Timer className="h-3 w-3 mr-1 animate-pulse" />
                                                                )}
                                                                {service.status}
                                                            </Badge>
                                                        </div>

                                                        {/* Timer Display */}
                                                        {(service.status === "in-progress" || activeTimer === service.id) && (
                                                            <div className="bg-secondary/50 rounded-lg p-3 mb-4">
                                                                <div className="flex items-center justify-between">
                                                                    <div className="flex items-center space-x-2">
                                                                        <Clock className="h-4 w-4 text-primary" />
                                                                        <span className="font-mono text-lg font-bold">
                                      {formatTime(timers[service.id] || service.timeSpent * 60)}
                                    </span>
                                                                    </div>
                                                                    <div className="flex space-x-2">
                                                                        {activeTimer === service.id ? (
                                                                            <>
                                                                                <Button variant="outline" size="sm" onClick={pauseTimer}>
                                                                                    <Pause className="h-4 w-4" />
                                                                                </Button>
                                                                                <Button
                                                                                    size="sm"
                                                                                    onClick={() => completeService(appointment.id, service.id)}
                                                                                    className="bg-green-600 hover:bg-green-700"
                                                                                >
                                                                                    <CheckCircle className="h-4 w-4 mr-2" />
                                                                                    Complete
                                                                                </Button>
                                                                            </>
                                                                        ) : service.status !== "completed" ? (
                                                                            <Button size="sm" onClick={() => startTimer(service.id)}>
                                                                                <Play className="h-4 w-4 mr-2" />
                                                                                Start
                                                                            </Button>
                                                                        ) : null}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        )}

                                                        {/* Service Notes */}
                                                        <div className="space-y-2">
                                                            <label className="text-sm font-medium">Service Notes</label>
                                                            <Textarea
                                                                placeholder="Add notes about this service..."
                                                                value={notes[service.id] || ""}
                                                                onChange={(e) =>
                                                                    setNotes((prev) => ({
                                                                        ...prev,
                                                                        [service.id]: e.target.value,
                                                                    }))
                                                                }
                                                                rows={2}
                                                            />
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>

                                            {/* Appointment Actions */}
                                            <div className="flex justify-between items-center pt-4 border-t">
                                                <div className="text-sm text-muted-foreground">
                                                    Est. completion: {appointment.estimatedCompletion}
                                                </div>
                                                <div className="flex space-x-2">
                                                    {getAppointmentProgress(appointment) === 100 ? (
                                                        <Button className="bg-green-600 hover:bg-green-700">
                                                            <CheckCircle className="h-4 w-4 mr-2" />
                                                            Mark Complete & Notify Customer
                                                        </Button>
                                                    ) : (
                                                        <Button variant="outline">
                                                            <MessageSquare className="h-4 w-4 mr-2" />
                                                            Update Customer
                                                        </Button>
                                                    )}
                                                </div>
                                            </div>
                                        </CardContent>
                                    </Card>
                                ))}
                            </div>
                        )}

                        {activeTab === "completed" && (
                            <Card>
                                <CardHeader>
                                    <CardTitle>Completed Services Today</CardTitle>
                                    <CardDescription>Services you've completed today</CardDescription>
                                </CardHeader>
                                <CardContent>
                                    <div className="text-center py-12">
                                        <CheckCircle className="h-12 w-12 text-green-500 mx-auto mb-4" />
                                        <h3 className="text-lg font-medium mb-2">Great work!</h3>
                                        <p className="text-muted-foreground">You've completed 3 services today</p>
                                    </div>
                                </CardContent>
                            </Card>
                        )}

                        {activeTab === "schedule" && (
                            <Card>
                                <CardHeader>
                                    <CardTitle>Today's Schedule</CardTitle>
                                    <CardDescription>Your assigned appointments for today</CardDescription>
                                </CardHeader>
                                <CardContent>
                                    <div className="space-y-4">
                                        {appointmentData.map((appointment) => (
                                            <div key={appointment.id} className="flex items-center justify-between p-4 border rounded-lg">
                                                <div>
                                                    <p className="font-medium">{appointment.startTime}</p>
                                                    <p className="text-sm text-muted-foreground">
                                                        {appointment.customer} - {appointment.vehicle}
                                                    </p>
                                                </div>
                                                <Badge variant="outline" className={getPriorityColor(appointment.priority)}>
                                                    {appointment.priority}
                                                </Badge>
                                            </div>
                                        ))}
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
