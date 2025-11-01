import { Button } from "@/components/UI/Button"
import { Card, CardDescription, CardHeader, CardTitle } from "@/components/UI/Card"
import { Car, Clock, Shield, Users, Wrench, Calendar, BarChart3, Bell } from "lucide-react"
import { Link } from "react-router-dom"

export default function HomePage() {
  return (
    <div className="min-h-screen bg-background">
      {/* Navigation */}
      <nav className="border-b border-border/50 backdrop-blur-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-3">
              <div className="bg-primary rounded-lg p-2">
                <Car className="h-6 w-6 text-primary-foreground" />
              </div>
              <span className="text-xl font-bold">RevUp</span>
            </div>
            <div className="flex items-center space-x-4">
              <Link to="/dashboard">
                <Button variant="ghost">Customer Dashboard</Button>
              </Link>
              <Link to="/technician">
                <Button variant="ghost">Technician Portal</Button>
              </Link>
              <Link to="/admin">
                <Button variant="ghost">Admin Panel</Button>
              </Link>
              <Link to="/login">
                <Button variant="ghost">Sign In</Button>
              </Link>
              <Link to="/login">
                <Button>Get Started</Button>
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="relative py-20 px-4 sm:px-6 lg:px-8">
        {/* Background Pattern */}
        <div className="absolute inset-0 overflow-hidden">
          <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-transparent to-accent/10" />
          <div className="absolute top-1/4 left-1/4 w-96 h-96 border border-primary/20 rounded-full animate-pulse" />
          <div className="absolute bottom-1/4 right-1/4 w-64 h-64 border border-accent/20 rounded-full animate-pulse delay-1000" />
        </div>

        <div className="relative z-10 max-w-7xl mx-auto">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div>
              <h1 className="text-5xl font-bold text-balance leading-tight">
                The complete platform to <span className="text-primary">revolutionize</span> auto service.
              </h1>
              <p className="text-xl text-muted-foreground mt-6 text-pretty">
                Streamline your service center operations with real-time tracking, automated scheduling, and seamless
                customer communication. Join the automotive service revolution.
              </p>
              <div className="flex items-center space-x-4 mt-8">
                <Link to="/login">
                  <Button size="lg" className="px-8">
                    Get Started
                  </Button>
                </Link>
                <Button variant="outline" size="lg">
                  Watch Demo
                </Button>
              </div>
            </div>

            {/* Hero Visual */}
            <div className="relative">
              <div className="bg-card border border-border/50 rounded-2xl p-6 backdrop-blur-sm">
                <div className="space-y-4">
                  <div className="flex items-center justify-between">
                    <h3 className="font-semibold">Service Dashboard</h3>
                    <div className="flex items-center space-x-2">
                      <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse" />
                      <span className="text-sm text-muted-foreground">Live</span>
                    </div>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div className="bg-secondary rounded-lg p-4">
                      <div className="flex items-center space-x-2">
                        <Wrench className="h-5 w-5 text-primary" />
                        <span className="text-sm">Oil Change</span>
                      </div>
                      <p className="text-2xl font-bold mt-2">45 min</p>
                    </div>
                    <div className="bg-secondary rounded-lg p-4">
                      <div className="flex items-center space-x-2">
                        <Clock className="h-5 w-5 text-primary" />
                        <span className="text-sm">Tire Rotation</span>
                      </div>
                      <p className="text-2xl font-bold mt-2">30 min</p>
                    </div>
                  </div>
                  <div className="bg-primary/10 border border-primary/20 rounded-lg p-4">
                    <p className="text-sm text-primary font-medium">Service completed! Customer notified.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="py-16 px-4 sm:px-6 lg:px-8 border-t border-border/50">
        <div className="max-w-7xl mx-auto">
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-8">
            <div className="text-center">
              <div className="text-3xl font-bold text-primary">100+</div>
              <div className="text-sm text-muted-foreground mt-1">Vehicles per week</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-primary">50%</div>
              <div className="text-sm text-muted-foreground mt-1">Faster service</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-primary">95%</div>
              <div className="text-sm text-muted-foreground mt-1">Customer satisfaction</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-primary">24/7</div>
              <div className="text-sm text-muted-foreground mt-1">Real-time tracking</div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-16">
            <h2 className="text-3xl font-bold text-balance">Everything you need to manage your service center</h2>
            <p className="text-muted-foreground mt-4 text-pretty max-w-2xl mx-auto">
              From appointment booking to service completion, RevUp provides all the tools your team needs to deliver
              exceptional automotive service.
            </p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            <Card className="border-border/50">
              <CardHeader>
                <div className="bg-primary/10 rounded-lg p-3 w-fit">
                  <Calendar className="h-6 w-6 text-primary" />
                </div>
                <CardTitle>Smart Scheduling</CardTitle>
                <CardDescription>
                  Automated appointment booking with real-time availability and customer notifications.
                </CardDescription>
              </CardHeader>
            </Card>

            <Card className="border-border/50">
              <CardHeader>
                <div className="bg-primary/10 rounded-lg p-3 w-fit">
                  <Clock className="h-6 w-6 text-primary" />
                </div>
                <CardTitle>Time Tracking</CardTitle>
                <CardDescription>
                  Precise service time logging with progress updates and completion notifications.
                </CardDescription>
              </CardHeader>
            </Card>

            <Card className="border-border/50">
              <CardHeader>
                <div className="bg-primary/10 rounded-lg p-3 w-fit">
                  <Bell className="h-6 w-6 text-primary" />
                </div>
                <CardTitle>Real-time Updates</CardTitle>
                <CardDescription>
                  Instant notifications keep customers informed throughout the service process.
                </CardDescription>
              </CardHeader>
            </Card>

            <Card className="border-border/50">
              <CardHeader>
                <div className="bg-primary/10 rounded-lg p-3 w-fit">
                  <Users className="h-6 w-6 text-primary" />
                </div>
                <CardTitle>Team Management</CardTitle>
                <CardDescription>Assign technicians, track workloads, and monitor service performance.</CardDescription>
              </CardHeader>
            </Card>

            <Card className="border-border/50">
              <CardHeader>
                <div className="bg-primary/10 rounded-lg p-3 w-fit">
                  <BarChart3 className="h-6 w-6 text-primary" />
                </div>
                <CardTitle>Analytics Dashboard</CardTitle>
                <CardDescription>
                  Comprehensive insights into service metrics, efficiency, and customer satisfaction.
                </CardDescription>
              </CardHeader>
            </Card>

            <Card className="border-border/50">
              <CardHeader>
                <div className="bg-primary/10 rounded-lg p-3 w-fit">
                  <Shield className="h-6 w-6 text-primary" />
                </div>
                <CardTitle>Secure & Reliable</CardTitle>
                <CardDescription>Enterprise-grade security with reliable uptime and data protection.</CardDescription>
              </CardHeader>
            </Card>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 px-4 sm:px-6 lg:px-8 bg-card/50">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-3xl font-bold text-balance">Ready to revolutionize your auto service?</h2>
          <p className="text-muted-foreground mt-4 text-pretty">
            Join hundreds of service centers already using RevUp to streamline operations and delight customers.
          </p>
          
          <div className="flex items-center justify-center space-x-4 mt-6">
            <Link to="/login">
              <Button size="lg" className="px-8">
                Start Free Trial
              </Button>
            </Link>
            <Button variant="outline" size="lg">
              Schedule Demo
            </Button>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-border/50 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <div className="bg-primary rounded-lg p-2">
                <Car className="h-5 w-5 text-primary-foreground" />
              </div>
              <span className="font-bold">RevUp</span>
            </div>
            <p className="text-sm text-muted-foreground">© 2025 RevUp. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  )
}
