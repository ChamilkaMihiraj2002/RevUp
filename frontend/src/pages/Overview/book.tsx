"use client"

import { useState, useEffect } from "react"
import axios from "axios"
import { Button } from "@/components/UI/Button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/UI/Card"
import { Badge } from "@/components/UI/Badge"
import { Checkbox } from "@/components/UI/Checkbox"
import { Textarea } from "@/components/UI/Textarea"
import { Label } from "@/components/UI/Label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/UI/Select"
import { Calendar } from "@/components/UI/Calendar"
import { Car, Clock, ArrowLeft, CheckCircle, Wrench, Shield, Zap } from "lucide-react"
import { Link } from "react-router-dom"
import { useAuth } from "@/contexts/authContext/authContext"
import { getVehiclesByUserId, type VehicleResponse } from "@/services/vehicleService"
import { getAllServiceTypes, type ServiceTypeResponse } from "@/services/serviceTypeService"
import { createAppointment, checkSlotAvailability, type SlotAvailabilityResponse } from "@/services/appointmentService"
import { toast } from "sonner"

export default function BookServicePage() {
  const { userData, accessToken } = useAuth()
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date())
  const [selectedTime, setSelectedTime] = useState("")
  const [selectedVehicle, setSelectedVehicle] = useState("")
  const [selectedServices, setSelectedServices] = useState<string[]>([])
  const [customServiceText, setCustomServiceText] = useState("")
  const [notes, setNotes] = useState("")
  const [step, setStep] = useState(1)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  
  // Real data from API
  const [vehicles, setVehicles] = useState<VehicleResponse[]>([])
  const [services, setServices] = useState<ServiceTypeResponse[]>([])
  const [slotAvailability, setSlotAvailability] = useState<Record<string, SlotAvailabilityResponse>>({})

  // Fetch vehicles and services on mount
  useEffect(() => {
    const fetchData = async () => {
      if (!userData?.userId || !accessToken) return
      
      try {
        setLoading(true)
        
        // Fetch vehicles
        console.log('Fetching vehicles for user:', userData.userId)
        const vehiclesData = await getVehiclesByUserId(userData.userId, accessToken)
        console.log('Vehicles fetched:', vehiclesData)
        setVehicles(vehiclesData || [])
        
        // Fetch services
        console.log('Fetching services...')
        const servicesData = await getAllServiceTypes(accessToken)
        console.log('Services fetched:', servicesData)
        setServices(servicesData || [])
        
      } catch (error) {
        console.error('Error fetching data:', error)
        // Only show error if it's a real network/server error, not empty data
        if (axios.isAxiosError(error)) {
          const errorMsg = error.response?.data?.message || error.message
          console.error('API Error:', errorMsg, 'Status:', error.response?.status)
          toast.error(`Failed to load data: ${errorMsg}. Please refresh the page.`)
        } else {
          toast.error('Failed to load data. Please refresh the page.')
        }
      } finally {
        setLoading(false)
      }
    }
    
    fetchData()
  }, [userData, accessToken])

  // Check slot availability when date changes
  useEffect(() => {
    const checkAvailability = async () => {
      if (!selectedDate || !accessToken) return
      
      const dateStr = selectedDate.toISOString().split('T')[0]
      const availability: Record<string, SlotAvailabilityResponse> = {}
      
      for (const timeSlot of timeSlots) {
        try {
          const timeStr = convertTo24Hour(timeSlot)
          const result = await checkSlotAvailability(dateStr, timeStr, accessToken)
          availability[timeSlot] = result
        } catch (error) {
          console.error(`Error checking availability for ${timeSlot}:`, error)
        }
      }
      
      setSlotAvailability(availability)
    }
    
    checkAvailability()
  }, [selectedDate, accessToken])

  // 2-hour time slots
  const timeSlots = [
    "8:00 AM",
    "10:00 AM",
    "12:00 PM",
    "2:00 PM",
    "4:00 PM",
  ]

  // Helper function to convert 12-hour format to 24-hour format
  const convertTo24Hour = (time12h: string): string => {
    const [time, period] = time12h.split(' ')
    let [hours, minutes] = time.split(':')
    
    if (period === 'PM' && hours !== '12') {
      hours = String(Number.parseInt(hours) + 12)
    }
    if (period === 'AM' && hours === '12') {
      hours = '00'
    }
    
    // Ensure hours and minutes have leading zeros
    hours = hours.padStart(2, '0')
    minutes = minutes.padStart(2, '0')
    
    return `${hours}:${minutes}:00`
  }

  const handleServiceToggle = (serviceId: string) => {
    setSelectedServices((prev) =>
      prev.includes(serviceId) ? prev.filter((id) => id !== serviceId) : [...prev, serviceId],
    )
    // Clear custom text if "other" is deselected
    if (serviceId === "other" && selectedServices.includes("other")) {
      setCustomServiceText("")
    }
  }

  const getSelectedServicesDetails = () => {
    return services.filter((service) => selectedServices.includes(String(service.serviceTypeId)))
  }

  const getTotalDuration = () => {
    const selectedServicesDetails = getSelectedServicesDetails()
    const total = selectedServicesDetails.reduce((total, service) => {
      return total + service.baseDurationMinutes
    }, 0)
    // "Other" service assumed to be 60 minutes
    if (selectedServices.includes("other")) {
      return total + 60
    }
    return total
  }

  const getTotalPrice = () => {
    const selectedServicesDetails = getSelectedServicesDetails()
    const total = selectedServicesDetails.reduce((total, service) => {
      return total + service.basePrice
    }, 0)
    // "Other" service has no fixed price
    return total
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

  const handleBooking = async () => {
    if (!userData?.userId || !accessToken || !selectedDate || !selectedTime || !selectedVehicle) {
      toast.error("Missing required information")
      return
    }

    try {
      setSubmitting(true)
      
      // Convert selected date and time to ISO format
      const timeStr = convertTo24Hour(selectedTime)
      const [hours, minutes] = timeStr.split(':')
      
      const startDateTime = new Date(selectedDate)
      startDateTime.setHours(Number.parseInt(hours), Number.parseInt(minutes), 0, 0)
      
      // End time is 2 hours later
      const endDateTime = new Date(startDateTime)
      endDateTime.setHours(startDateTime.getHours() + 2)
      
      const appointmentRequest = {
        customerId: userData.userId,
        vehicleId: Number.parseInt(selectedVehicle),
        scheduledStart: startDateTime.toISOString(),
        scheduledEnd: endDateTime.toISOString()
      }
      
      await createAppointment(appointmentRequest, accessToken)
      setStep(4) // Show confirmation
    } catch (error) {
      console.error("Error creating appointment:", error)
      toast.error("Failed to book appointment. Please try again.")
    } finally {
      setSubmitting(false)
    }
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
                      {vehicles.find((v) => String(v.vehicleId) === selectedVehicle)?.model || "Selected Vehicle"}
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
                      <Badge key={service.serviceTypeId} variant="secondary">
                        {service.name}
                      </Badge>
                    ))}
                    {selectedServices.includes("other") && (
                      <Badge variant="secondary">Custom Service</Badge>
                    )}
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
              <Link to="/overview">
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
            <Link to="/overview">
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

        {loading ? (
          <div className="flex items-center justify-center h-96">
            <div className="text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-cyan-600 mx-auto mb-4"></div>
              <p className="text-gray-600">Loading...</p>
            </div>
          </div>
        ) : (
          <>
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
                            key={vehicle.vehicleId} 
                            value={String(vehicle.vehicleId)} 
                            className="text-gray-700 text-base py-3 px-4 data-[highlighted]:bg-cyan-50 data-[highlighted]:text-gray-700 cursor-pointer border-b border-gray-100 last:border-0"
                          >
                            {vehicle.year} {vehicle.model} ({vehicle.registrationNo})
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    {selectedVehicle && (
                      <div className="mt-2 px-1">
                        <p className="text-base text-cyan-600">
                          Selected: {vehicles.find(v => String(v.vehicleId) === selectedVehicle)?.model} ({vehicles.find(v => String(v.vehicleId) === selectedVehicle)?.registrationNo})
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
                      {services.map((service) => {
                        const serviceId = String(service.serviceTypeId)
                        const category = service.code?.toLowerCase().includes('oil') ? 'maintenance' :
                                        service.code?.toLowerCase().includes('brake') ? 'safety' :
                                        service.code?.toLowerCase().includes('battery') || service.code?.toLowerCase().includes('diagnostic') ? 'electrical' :
                                        'maintenance'
                        
                        return (
                          <div
                            key={service.serviceTypeId}
                            className={`border rounded-lg p-4 cursor-pointer transition-colors ${
                              selectedServices.includes(serviceId)
                                ? "border-cyan-600 bg-cyan-50"
                                : "border-gray-200 hover:border-cyan-300"
                            }`}
                            onClick={() => handleServiceToggle(serviceId)}
                          >
                            <div className="flex items-start space-x-3">
                              <Checkbox
                                checked={selectedServices.includes(serviceId)}
                                onChange={() => handleServiceToggle(serviceId)}
                                className="text-cyan-600"
                              />
                              <div className="flex-1">
                                <div className="flex items-center justify-between mb-2">
                                  <div className="flex items-center space-x-2">
                                    <h3 className="text-lg font-medium text-gray-900">{service.name}</h3>
                                    <Badge variant="outline" className={`text-base px-3 py-1 ${getCategoryColor(category)}`}>
                                      {getCategoryIcon(category)}
                                      <span className="ml-2 capitalize">{category}</span>
                                    </Badge>
                                  </div>
                                  <div className="text-right">
                                    <p className="text-lg font-bold text-cyan-600">RS.{service.basePrice}</p>
                                    <p className="text-base text-gray-600">{service.baseDurationMinutes} min</p>
                                  </div>
                                </div>
                                <p className="text-base text-gray-600 mt-2">{service.description}</p>
                              </div>
                            </div>
                          </div>
                        )
                      })}
                      
                      {/* Other Service Option */}
                      <div
                        className={`border rounded-lg p-4 cursor-pointer transition-colors ${
                          selectedServices.includes("other")
                            ? "border-cyan-600 bg-cyan-50"
                            : "border-gray-200 hover:border-cyan-300"
                        }`}
                        onClick={() => handleServiceToggle("other")}
                      >
                        <div className="flex items-start space-x-3">
                          <Checkbox
                            checked={selectedServices.includes("other")}
                            onChange={() => handleServiceToggle("other")}
                            className="text-cyan-600"
                          />
                          <div className="flex-1">
                            <div className="flex items-center justify-between mb-2">
                              <div className="flex items-center space-x-2">
                                <h3 className="text-lg font-medium text-gray-900">Other Service</h3>
                                <Badge variant="outline" className="text-base px-3 py-1 bg-gray-500/10 text-gray-600 border-gray-200">
                                  <Wrench className="h-4 w-4" />
                                  <span className="ml-2">Custom</span>
                                </Badge>
                              </div>
                              <div className="text-right">
                                <p className="text-lg font-bold text-cyan-600">Contact for Quote</p>
                              </div>
                            </div>
                            <p className="text-base text-gray-600 mt-2">Custom service not listed above</p>
                            
                            {selectedServices.includes("other") && (
                              <div className="mt-4" onClick={(e) => e.stopPropagation()}>
                                <Label htmlFor="customService" className="text-gray-700 mb-2 block">
                                  Describe the service you need:
                                </Label>
                                <Textarea
                                  id="customService"
                                  value={customServiceText}
                                  onChange={(e) => setCustomServiceText(e.target.value)}
                                  placeholder="Please describe the service you need..."
                                  className="border-gray-200 focus:border-cyan-500 focus:ring-cyan-500"
                                  rows={3}
                                />
                              </div>
                            )}
                          </div>
                        </div>
                      </div>
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
                    <div className="grid grid-cols-5 gap-3">
                      {timeSlots.map((time) => {
                        const availability = slotAvailability[time]
                        const isAvailable = availability?.available !== false
                        const isDisabled = !isAvailable
                        
                        return (
                          <div key={time} className="relative">
                            <Button
                              variant={selectedTime === time ? "default" : "outline"}
                              onClick={() => !isDisabled && setSelectedTime(time)}
                              disabled={isDisabled}
                              className={`h-16 text-base font-medium w-full flex flex-col items-center justify-center ${
                                selectedTime === time 
                                  ? "bg-cyan-600 hover:bg-cyan-700 text-white"
                                  : isDisabled
                                  ? "bg-gray-100 border-gray-200 text-gray-400 cursor-not-allowed"
                                  : "bg-white border-gray-200 text-gray-700 hover:bg-cyan-50 hover:text-cyan-700"
                              }`}
                            >
                              <span>{time}</span>
                              {availability && (
                                <span className={`text-xs mt-1 ${
                                  selectedTime === time ? "text-white" : 
                                  isAvailable ? "text-green-600" : "text-red-600"
                                }`}>
                                  {isAvailable ? `${availability.maxCapacity - availability.bookedCount} available` : 'Full'}
                                </span>
                              )}
                            </Button>
                          </div>
                        )
                      })}
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
                        <p className="text-base text-gray-600">
                          {vehicles.find((v) => String(v.vehicleId) === selectedVehicle)?.model || "Selected Vehicle"}
                        </p>
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
                          <div key={service.serviceTypeId} className="flex justify-between items-center">
                            <span>{service.name}</span>
                            <div className="text-right">
                              <span className="font-medium">RS.{service.basePrice}</span>
                              <span className="text-sm text-muted-foreground ml-2">({service.baseDurationMinutes} min)</span>
                            </div>
                          </div>
                        ))}
                        {selectedServices.includes("other") && (
                          <div className="flex justify-between items-center">
                            <span>Custom Service</span>
                            <div className="text-right">
                              <span className="font-medium">Contact for Quote</span>
                            </div>
                          </div>
                        )}
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
                  <Button variant="outline" onClick={() => setStep(2)} disabled={submitting}>
                    Back
                  </Button>
                  <Button onClick={handleBooking} disabled={submitting}>
                    {submitting ? "Booking..." : "Confirm Booking"}
                  </Button>
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
                    <p className="text-lg font-medium text-gray-900">
                      {vehicles.find((v) => String(v.vehicleId) === selectedVehicle)?.model || "Selected Vehicle"}
                    </p>
                  </div>
                )}

                {selectedServices.length > 0 && (
                  <div>
                    <h4 className="font-medium text-sm text-muted-foreground mb-2">Services</h4>
                    <div className="space-y-1">
                      {getSelectedServicesDetails().map((service) => (
                        <div key={service.serviceTypeId} className="flex justify-between text-sm">
                          <span>{service.name}</span>
                          <span>RS.{service.basePrice}</span>
                        </div>
                      ))}
                      {selectedServices.includes("other") && (
                        <div className="flex justify-between text-sm">
                          <span>Custom Service</span>
                          <span className="text-xs">Quote</span>
                        </div>
                      )}
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
                      <span className="font-bold text-primary text-lg">
                        {selectedServices.includes("other") ? "Contact for Quote" : `RS.${getTotalPrice()}`}
                      </span>
                    </div>
                  </div>
                )}
              </CardContent>
            </Card>
          </div>
        </div>
          </>
         )} 
      </div>
    </div>
  )
}
