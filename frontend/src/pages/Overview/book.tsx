"use client"

import { useState } from "react"
import { Button } from "@/components/UI/Button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/UI/Card"
import { Badge } from "@/components/UI/Badge"
import { Checkbox } from "@/components/UI/Checkbox"
import { Textarea } from "@/components/UI/Textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/UI/Select"
import { Calendar } from "@/components/UI/Calendar"
import { Car, Clock, ArrowLeft, CheckCircle, Wrench, Shield, Zap } from "lucide-react"
import { Link } from "react-router-dom"

export default function BookServicePage() {
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date())
  const [selectedTime, setSelectedTime] = useState("")
  const [selectedVehicle, setSelectedVehicle] = useState("")
  const [selectedServices, setSelectedServices] = useState<string[]>([])
  const [notes, setNotes] = useState("")
  const [step, setStep] = useState(1)

  // Mock data
  const vehicles = [
    { id: "1", name: "2020 Toyota Corolla", plate: "ABC-123" },
    { id: "2", name: "2019 Honda Civic", plate: "XYZ-789" },
  ]

  const services = [
    {
      id: "oil-change",
      name: "Oil Change",
      duration: "45 min",
  price: "RS.45",
      description: "Full synthetic oil change with filter replacement",
      category: "maintenance",
    },
    {
      id: "tire-rotation",
      name: "Tire Rotation",
      duration: "30 min",
  price: "RS.25",
      description: "Rotate tires for even wear and extended life",
      category: "maintenance",
    },
    {
      id: "brake-inspection",
      name: "Brake Inspection",
      duration: "60 min",
  price: "RS.75",
      description: "Complete brake system inspection and testing",
      category: "safety",
    },
    {
      id: "ac-service",
      name: "AC Service",
      duration: "90 min",
  price: "RS.120",
      description: "Air conditioning system check and refrigerant top-up",
      category: "comfort",
    },
    {
      id: "battery-test",
      name: "Battery Test",
      duration: "20 min",
  price: "RS.15",
      description: "Battery health check and terminal cleaning",
      category: "electrical",
    },
    {
      id: "diagnostic",
      name: "Diagnostic Scan",
      duration: "45 min",
  price: "RS.85",
      description: "Computer diagnostic scan for error codes",
      category: "electrical",
    },
  ]

  const timeSlots = [
    "8:00 AM",
    "8:30 AM",
    "9:00 AM",
    "9:30 AM",
    "10:00 AM",
    "10:30 AM",
    "11:00 AM",
    "11:30 AM",
    "1:00 PM",
    "1:30 PM",
    "2:00 PM",
    "2:30 PM",
    "3:00 PM",
    "3:30 PM",
    "4:00 PM",
    "4:30 PM",
  ]

  const handleServiceToggle = (serviceId: string) => {
    setSelectedServices((prev) =>
      prev.includes(serviceId) ? prev.filter((id) => id !== serviceId) : [...prev, serviceId],
    )
  }

  const getSelectedServicesDetails = () => {
    return services.filter((service) => selectedServices.includes(service.id))
  }

  const getTotalDuration = () => {
    const selectedServicesDetails = getSelectedServicesDetails()
    return selectedServicesDetails.reduce((total, service) => {
      const duration = Number.parseInt(service.duration)
      return total + duration
    }, 0)
  }

  const getTotalPrice = () => {
    const selectedServicesDetails = getSelectedServicesDetails()
    return selectedServicesDetails.reduce((total, service) => {
      // Strip non-numeric characters (handles formats like "RS.120" or "$120")
      const numeric = Number.parseFloat(service.price.replace(/[^0-9.]/g, "")) || 0
      return total + numeric
    }, 0)
  }

  const getCategoryIcon = (category: string) => {
    switch (category) {
      case "maintenance":
        return <Wrench className="h-4 w-4" />
      case "safety":
        return <Shield className="h-4 w-4" />
      case "electrical":
        return <Zap className="h-4 w-4" />
      default:
        return <Car className="h-4 w-4" />
    }
  }

  const getCategoryColor = (category: string) => {
    switch (category) {
      case "maintenance":
        return "bg-blue-500/10 text-blue-600 border-blue-200"
      case "safety":
        return "bg-red-500/10 text-red-600 border-red-200"
      case "electrical":
        return "bg-yellow-500/10 text-yellow-600 border-yellow-200"
      default:
        return "bg-gray-500/10 text-gray-600 border-gray-200"
    }
  }

  const handleBooking = () => {
    // Handle booking logic here
    console.log({
      vehicle: selectedVehicle,
      date: selectedDate,
      time: selectedTime,
      services: selectedServices,
      notes,
    })
    setStep(4) // Show confirmation
  }

  if (step === 4) {
    return (
    <div className="min-h-screen bg-background">
      {/* Navigation (sticky so only content scrolls) */}
      <nav className="sticky top-0 z-50 border-b border-border/50 backdrop-blur-sm bg-white/95">
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
            </div>
          </div>
        </nav>

        <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="text-center">
            <div className="bg-green-500/10 rounded-full p-4 w-16 h-16 mx-auto mb-6">
              <CheckCircle className="h-8 w-8 text-green-500" />
            </div>
            <h1 className="text-3xl font-bold text-balance mb-4">Booking Confirmed!</h1>
            <p className="text-muted-foreground text-pretty mb-8">
              Your service appointment has been successfully scheduled. 
            </p>

            <Card className="text-left mb-8">
              <CardHeader>
                <CardTitle>Appointment Details</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <span className="text-muted-foreground">Vehicle:</span>
                    <p className="font-medium">
                      {vehicles.find((v) => v.id === selectedVehicle)?.name || "Selected Vehicle"}
                    </p>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Date & Time:</span>
                    <p className="font-medium">
                      {selectedDate?.toLocaleDateString()} at {selectedTime}
                    </p>
                  </div>
                </div>
                <div>
                  <span className="text-muted-foreground">Services:</span>
                  <div className="flex flex-wrap gap-2 mt-1">
                    {getSelectedServicesDetails().map((service) => (
                      <Badge key={service.id} variant="secondary">
                        {service.name}
                      </Badge>
                    ))}
                  </div>
                </div>
                <div className="grid grid-cols-2 gap-4 text-sm pt-4 border-t">
                <div>
                  <span className="text-muted-foreground">Estimated Duration:</span>
                  <p className="font-medium">{getTotalDuration()} minutes</p>
                </div>
                <div>
                  <span className="text-muted-foreground">Total Cost:</span>
                  <p className="font-medium">RS.{getTotalPrice()}</p>
                </div>
                </div>
              </CardContent>
            </Card>

            <div className="flex space-x-4 justify-center">
              <Link to="/dashboard">
                <Button>Go to Dashboard</Button>
              </Link>
              <Link to="/overview">
                <Button variant="outline">Book Another Service</Button>
              </Link>
            </div>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-cyan-50 to-blue-100">
      {/* Navigation */}
  <nav className="sticky top-0 z-50 border-b border-cyan-200 bg-gradient-to-r from-blue-50 via-cyan-50 to-blue-100 backdrop-blur-sm">
        <div className="max-w-8xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-3">
              <Link to="/" className="flex items-center space-x-3">
                <div className="bg-cyan-600 rounded-lg p-2">
                  <Car className="h-6 w-6 text-white" />
                </div>
                <span className="text-xl font-bold text-gray-900">RevUp</span>
              </Link>
            </div>
            <Link to="/dashboard">
              <Button variant="ghost">
                <ArrowLeft className="h-4 w-4 mr-2" />
                Back to Dashboard
              </Button>
            </Link>
          </div>
        </div>
      </nav>

      <div style={{ maxWidth: "1440px" }} className="mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900">Book a Service</h1>
          <p className="text-lg text-gray-600 mt-2">Schedule your vehicle service appointment</p>
        </div>

        {/* Progress Steps */}
        <div className="flex items-center justify-center mb-8">
          <div className="flex items-center space-x-4">
            <div className={`flex items-center space-x-2 ${step >= 1 ? "text-cyan-600" : "text-gray-500"}`}>
              <div
                className={`w-8 h-8 rounded-full flex items-center justify-center ${
                  step >= 1 ? "bg-cyan-600 text-white" : "bg-gray-100"
                }`}
              >
                1
              </div>
              <span className="text-base font-medium">Vehicle & Services</span>
            </div>
            <div className="w-8 h-px bg-gray-200" />
            <div className={`flex items-center space-x-2 ${step >= 2 ? "text-cyan-600" : "text-gray-500"}`}>
              <div
                className={`w-8 h-8 rounded-full flex items-center justify-center ${
                  step >= 2 ? "bg-primary text-primary-foreground" : "bg-muted"
                }`}
              >
                2
              </div>
              <span className="text-sm font-medium">Date & Time</span>
            </div>
            <div className="w-8 h-px bg-border" />
            <div className={`flex items-center space-x-2 ${step >= 3 ? "text-primary" : "text-muted-foreground"}`}>
              <div
                className={`w-8 h-8 rounded-full flex items-center justify-center ${
                  step >= 3 ? "bg-primary text-primary-foreground" : "bg-muted"
                }`}
              >
                3
              </div>
              <span className="text-sm font-medium">Review & Confirm</span>
            </div>
          </div>
        </div>

        <div className="grid lg:grid-cols-3 gap-8">
          {/* Main Content */}
          <div className="lg:col-span-2">
            {step === 1 && (
              <div className="space-y-6">
                {/* Vehicle Selection */}
                <Card className="border-gray-200 bg-white shadow-md">
                  <CardHeader>
                    <CardTitle className="text-xl text-gray-900">Select Vehicle</CardTitle>
                    <CardDescription className="text-gray-600">Choose which vehicle needs service</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <Select value={selectedVehicle} onValueChange={setSelectedVehicle}>
                      <SelectTrigger className="w-full border-2 border-gray-200 text-gray-700 h-12 text-base hover:border-cyan-300 focus:border-cyan-500 focus:ring-1 focus:ring-cyan-500">
                        <SelectValue placeholder="Select your vehicle" className="text-base" />
                      </SelectTrigger>
                      <SelectContent 
                        className="bg-white border-2 border-gray-200 w-[100%] shadow-lg" 
                        position="popper"
                      >
                        {vehicles.map((vehicle) => (
                          <SelectItem 
                            key={vehicle.id} 
                            value={vehicle.id} 
                            className="text-gray-700 text-base py-3 px-4 data-[highlighted]:bg-cyan-50 data-[highlighted]:text-gray-700 cursor-pointer border-b border-gray-100 last:border-0"
                          >
                            {vehicle.name} ({vehicle.plate})
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    {selectedVehicle && (
                      <div className="mt-2 px-1">
                        <p className="text-base text-cyan-600">
                          Selected: {vehicles.find(v => v.id === selectedVehicle)?.name} ({vehicles.find(v => v.id === selectedVehicle)?.plate})
                        </p>
                      </div>
                    )}
                  </CardContent>
                </Card>

                {/* Service Selection */}
                <Card className="border-gray-200 bg-white shadow-md">
                  <CardHeader>
                    <CardTitle className="text-xl text-gray-900">Select Services</CardTitle>
                    <CardDescription className="text-gray-600">Choose the services you need</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="grid gap-4">
                      {services.map((service) => (
                        <div
                          key={service.id}
                          className={`border rounded-lg p-4 cursor-pointer transition-colors ${
                            selectedServices.includes(service.id)
                              ? "border-cyan-600 bg-cyan-50"
                              : "border-gray-200 hover:border-cyan-300"
                          }`}
                          onClick={() => handleServiceToggle(service.id)}
                        >
                          <div className="flex items-start space-x-3">
                            <Checkbox
                              checked={selectedServices.includes(service.id)}
                              onChange={() => handleServiceToggle(service.id)}
                              className="text-cyan-600"
                            />
                            <div className="flex-1">
                              <div className="flex items-center justify-between mb-2">
                                <div className="flex items-center space-x-2">
                                  <h3 className="text-lg font-medium text-gray-900">{service.name}</h3>
                                  <Badge variant="outline" className={`text-base px-3 py-1 ${getCategoryColor(service.category)}`}>
                                    {getCategoryIcon(service.category)}
                                    <span className="ml-2 capitalize">{service.category}</span>
                                  </Badge>
                                </div>
                                <div className="text-right">
                                  <p className="text-lg font-bold text-cyan-600">{service.price}</p>
                                  <p className="text-base text-gray-600">{service.duration}</p>
                                </div>
                              </div>
                              <p className="text-base text-gray-600 mt-2">{service.description}</p>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>

                <div className="flex justify-end">
                  <Button 
                    onClick={() => setStep(2)} 
                    disabled={!selectedVehicle || selectedServices.length === 0}
                    className="bg-cyan-600 hover:bg-cyan-700"
                  >
                    Continue to Date & Time
                  </Button>
                </div>
              </div>
            )}

            {step === 2 && (
              <div className="space-y-6">
                {/* Date Selection */}
                <Card className="border-gray-200 bg-white shadow-md">
                  <CardHeader>
                    <CardTitle className="text-xl text-gray-900">Select Date</CardTitle>
                    <CardDescription className="text-base text-gray-600">Choose your preferred service date</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <Calendar
                      mode="single"
                      selected={selectedDate}
                      onSelect={setSelectedDate}
                      disabled={(date) => date < new Date() || date.getDay() === 0}
                      className="rounded-lg border-gray-200 p-4 bg-white [&_.rdp-day]:text-base [&_.rdp-day_button]:h-10 [&_.rdp-day_button]:w-10 [&_.rdp-caption]:text-lg [&_.rdp-head_cell]:text-base [&_.rdp-head_cell]:font-semibold [&_.rdp-nav_button]:h-10 [&_.rdp-nav_button]:w-10 [&_.rdp-nav_button]:bg-gray-50 [&_.rdp-day_selected]:bg-cyan-600 [&_.rdp-day_selected]:text-white [&_.rdp-day_today]:bg-cyan-50 [&_.rdp-day_today]:text-cyan-600"
                    />
                  </CardContent>
                </Card>

                {/* Time Selection */}
                <Card className="border-gray-200 bg-white shadow-md">
                  <CardHeader>
                    <CardTitle className="text-xl text-gray-900">Select Time</CardTitle>
                    <CardDescription className="text-base text-gray-600">Choose your preferred time slot</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="grid grid-cols-4 gap-3">
                      {timeSlots.map((time) => (
                        <Button
                          key={time}
                          variant={selectedTime === time ? "default" : "outline"}
                          onClick={() => setSelectedTime(time)}
                          className={`h-12 text-base font-medium ${
                            selectedTime === time 
                              ? "bg-cyan-600 hover:bg-cyan-700 text-white"
                              : "bg-white border-gray-200 text-gray-700 hover:bg-cyan-50 hover:text-cyan-700"
                          }`}
                        >
                          {time}
                        </Button>
                      ))}
                    </div>
                  </CardContent>
                </Card>

                <div className="flex justify-between">
                  <Button variant="outline" onClick={() => setStep(1)}>
                    Back
                  </Button>
                  <Button onClick={() => setStep(3)} disabled={!selectedDate || !selectedTime}>
                    Continue to Review
                  </Button>
                </div>
              </div>
            )}

            {step === 3 && (
              <div className="space-y-6">
                {/* Review */}
                <Card className="border-gray-200 bg-white shadow-md">
                  <CardHeader>
                    <CardTitle className="text-xl text-gray-900">Review Your Booking</CardTitle>
                    <CardDescription className="text-base text-gray-600">Please review your appointment details</CardDescription>
                  </CardHeader>
                  <CardContent className="space-y-6">
                    <div className="grid grid-cols-2 gap-6">
                      <div>
                        <h4 className="text-lg font-medium text-gray-900 mb-2">Vehicle</h4>
                        <p className="text-base text-gray-600">{vehicles.find((v) => v.id === selectedVehicle)?.name}</p>
                      </div>
                      <div>
                        <h4 className="text-lg font-medium text-gray-900 mb-2">Date & Time</h4>
                        <p className="text-base text-gray-600">
                          {selectedDate?.toLocaleDateString()} at {selectedTime}
                        </p>
                      </div>
                    </div>

                    <div>
                      <h4 className="font-medium mb-2">Selected Services</h4>
                      <div className="space-y-2">
                        {getSelectedServicesDetails().map((service) => (
                          <div key={service.id} className="flex justify-between items-center">
                            <span>{service.name}</span>
                            <div className="text-right">
                              <span className="font-medium">{service.price}</span>
                              <span className="text-sm text-muted-foreground ml-2">({service.duration})</span>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>

                    <div>
                      <h4 className="font-medium mb-2">Additional Notes</h4>
                      <Textarea
                        placeholder="Any special instructions or concerns..."
                        value={notes}
                        onChange={(e) => setNotes(e.target.value)}
                      />
                    </div>
                  </CardContent>
                </Card>

                <div className="flex justify-between">
                  <Button variant="outline" onClick={() => setStep(2)}>
                    Back
                  </Button>
                  <Button onClick={handleBooking}>Confirm Booking</Button>
                </div>
              </div>
            )}
          </div>

          {/* Sidebar */}
          <div className="lg:col-span-1">
            <Card className="sticky top-8 border-gray-200 bg-white shadow-md">
              <CardHeader>
                <CardTitle className="flex items-center space-x-2 text-xl text-gray-900">
                  <Clock className="h-6 w-6 text-cyan-600" />
                  <span>Booking Summary</span>
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-5">
                {selectedVehicle && (
                  <div>
                    <h4 className="font-medium text-base text-gray-600 mb-1">Vehicle</h4>
                    <p className="text-lg font-medium text-gray-900">{vehicles.find((v) => v.id === selectedVehicle)?.name}</p>
                  </div>
                )}

                {selectedServices.length > 0 && (
                  <div>
                    <h4 className="font-medium text-sm text-muted-foreground mb-2">Services</h4>
                    <div className="space-y-1">
                      {getSelectedServicesDetails().map((service) => (
                        <div key={service.id} className="flex justify-between text-sm">
                          <span>{service.name}</span>
                          <span>{service.price}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                {selectedDate && selectedTime && (
                  <div>
                    <h4 className="font-medium text-sm text-muted-foreground">Date & Time</h4>
                    <p className="font-medium">
                      {selectedDate.toLocaleDateString()} at {selectedTime}
                    </p>
                  </div>
                )}

                {selectedServices.length > 0 && (
                  <div className="border-t pt-4">
                    <div className="flex justify-between items-center mb-2">
                      <span className="font-medium">Total Duration</span>
                      <span className="font-medium">{getTotalDuration()} min</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="font-medium">Total Cost</span>
                      <span className="font-bold text-primary text-lg">RS.{getTotalPrice()}</span>
                    </div>
                  </div>
                )}
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  )
}
