
import { Car, Clock, Shield, Users, Wrench, Calendar, BarChart3, Bell } from "lucide-react"
import { Link } from "react-router-dom"

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navigation */}
      <nav className="border-b border-cyan-200/80 bg-gradient-to-r from-blue-50 via-cyan-50 to-blue-100 shadow-sm">
        <div className="mx-auto px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-3">
              <div className="bg-cyan-600 rounded-lg p-2">
                <Car className="h-5 w-5 text-white" />
              </div>
              <span className="text-xl font-bold text-gray-900">RevUp</span>
            </div>
            <div className="flex items-center space-x-8">
              <Link to="/about">
                <button className="text-gray-700 hover:text-cyan-700 transition-colors text-sm font-medium">About Us</button>
              </Link>
              <Link to="/contact">
                <button className="text-gray-700 hover:text-cyan-700 transition-colors text-sm font-medium">Contact Us</button>
              </Link>
              <Link to="/login">
                <button className="text-gray-700 hover:text-cyan-700 transition-colors text-sm font-medium">Sign In</button>
              </Link>
              <Link to="/login">
                <button className="bg-cyan-600 hover:bg-cyan-700 text-white px-6 py-2 rounded-md text-sm font-medium shadow-sm">
                  Get Started
                </button>
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="bg-gradient-to-br from-blue-50 via-cyan-50 to-blue-100 py-20 px-8">
        <div className="mx-auto">
          <div className="grid lg:grid-cols-2 gap-16 items-start">
            <div className="pt-8">
              <h1 className="text-5xl font-bold leading-tight text-gray-900">
                The complete platform to <span className="text-cyan-600">revolutionize</span> auto service.
              </h1>
              <p className="text-lg text-gray-700 mt-6 leading-relaxed">
                Streamline your service center operations with real-time tracking, automated scheduling, and seamless
                customer communication. Join the automotive service revolution.
              </p>
              <div className="flex items-center space-x-4 mt-8">
                <Link to="/login">
                  <button className="bg-cyan-600 hover:bg-cyan-700 text-white px-8 py-3 rounded-md text-base font-semibold shadow-md">
                    Get Started
                  </button>
                </Link>
                <button className="bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 px-8 py-3 rounded-md text-base font-medium shadow-sm">
                  Watch Demo
                </button>
              </div>
            </div>

            {/* Hero Visual */}
            <div className="relative">
              <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-lg">
                <div className="space-y-4">
                  <div className="flex items-center justify-between">
                    <h3 className="font-semibold text-gray-900">Service Dashboard</h3>
                    <div className="flex items-center space-x-2">
                      <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse" />
                      <span className="text-sm text-gray-600">Live</span>
                    </div>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div className="bg-gradient-to-br from-blue-100 to-cyan-100 border border-blue-200 rounded-lg p-4">
                      <div className="flex items-center space-x-2 mb-3">
                        <Wrench className="h-4 w-4 text-cyan-700" />
                        <span className="text-sm text-gray-800">Oil Change</span>
                      </div>
                      <p className="text-2xl font-bold text-gray-900">45 min</p>
                    </div>
                    <div className="bg-gradient-to-br from-green-100 to-cyan-100 border border-green-200 rounded-lg p-4">
                      <div className="flex items-center space-x-2 mb-3">
                        <Clock className="h-4 w-4 text-cyan-700" />
                        <span className="text-sm text-gray-800">Tire Rotation</span>
                      </div>
                      <p className="text-2xl font-bold text-gray-900">30 min</p>
                    </div>
                  </div>
                  <div className="bg-gradient-to-r from-cyan-50 to-blue-50 border border-cyan-200 rounded-lg p-4">
                    <p className="text-sm text-cyan-700 font-medium">Service completed! Customer notified.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="bg-white py-16 px-8 shadow-inner">
        <div className="mx-auto">
          <div className="grid grid-cols-4 gap-8">
            <div className="text-center">
              <div className="text-4xl font-bold text-cyan-600">100+</div>
              <div className="text-sm text-gray-600 mt-2">Vehicles per week</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-cyan-600">50%</div>
              <div className="text-sm text-gray-600 mt-2">Faster service</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-cyan-600">95%</div>
              <div className="text-sm text-gray-600 mt-2">Customer satisfaction</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-cyan-600">24/7</div>
              <div className="text-sm text-gray-600 mt-2">Real-time tracking</div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="bg-gradient-to-b from-gray-50 to-white py-20 px-8">
        <div className="mx-auto">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900">Everything you need to manage your service center</h2>
            <p className="text-gray-600 mt-4 text-lg max-w-3xl mx-auto">
              From appointment booking to service completion, RevUp provides all the tools your team needs to deliver
              exceptional automotive service.
            </p>
          </div>

          <div className="grid grid-cols-3 gap-6">
            <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
              <div className="bg-gradient-to-br from-blue-100 to-cyan-100 rounded-lg p-3 w-fit mb-4">
                <Calendar className="h-6 w-6 text-cyan-700" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Smart Scheduling</h3>
              <p className="text-gray-600 text-sm leading-relaxed">
                Automated appointment booking with real-time availability and customer notifications.
              </p>
            </div>

            <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
              <div className="bg-gradient-to-br from-blue-100 to-cyan-100 rounded-lg p-3 w-fit mb-4">
                <Clock className="h-6 w-6 text-cyan-700" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Time Tracking</h3>
              <p className="text-gray-600 text-sm leading-relaxed">
                Precise service time logging with progress updates and completion notifications.
              </p>
            </div>

            <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
              <div className="bg-gradient-to-br from-blue-100 to-cyan-100 rounded-lg p-3 w-fit mb-4">
                <Bell className="h-6 w-6 text-cyan-700" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Real-time Updates</h3>
              <p className="text-gray-600 text-sm leading-relaxed">
                Instant notifications keep customers informed throughout the service process.
              </p>
            </div>

            <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
              <div className="bg-gradient-to-br from-blue-100 to-cyan-100 rounded-lg p-3 w-fit mb-4">
                <Users className="h-6 w-6 text-cyan-700" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Team Management</h3>
              <p className="text-gray-600 text-sm leading-relaxed">
                Assign technicians, track workloads, and monitor service performance.
              </p>
            </div>

            <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
              <div className="bg-gradient-to-br from-blue-100 to-cyan-100 rounded-lg p-3 w-fit mb-4">
                <BarChart3 className="h-6 w-6 text-cyan-700" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Analytics Dashboard</h3>
              <p className="text-gray-600 text-sm leading-relaxed">
                Comprehensive insights into service metrics, efficiency, and customer satisfaction.
              </p>
            </div>

            <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
              <div className="bg-gradient-to-br from-blue-100 to-cyan-100 rounded-lg p-3 w-fit mb-4">
                <Shield className="h-6 w-6 text-cyan-700" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Secure & Reliable</h3>
              <p className="text-gray-600 text-sm leading-relaxed">
                Enterprise-grade security with reliable uptime and data protection.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="bg-gradient-to-br from-cyan-50 via-blue-50 to-cyan-100 py-20 px-8">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-4xl font-bold text-gray-900">Ready to revolutionize your auto service?</h2>
          <p className="text-gray-700 mt-4 text-lg">
            Join hundreds of service centers already using RevUp to streamline operations and delight customers.
          </p>
          
          
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gradient-to-r from-cyan-600 to-cyan-700 text-white py-10">
  <div className="mx-auto max-w-7xl px-6 sm:px-8 lg:px-12">
          <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
            <div className="flex items-center space-x-3">
              <div className="bg-white/10 rounded-lg p-2">
                <Car className="h-5 w-5 text-white" />
              </div>
              <div>
                <span className="font-bold text-white">RevUp</span>
                <p className="text-sm text-cyan-100 mt-1">Simplifying automotive service operations</p>
              </div>
            </div>

              <div className="flex items-center space-x-6">
                <p className="text-sm text-cyan-100">© 2025 RevUp. All rights reserved.</p>
              </div>
          </div>
        </div>
      </footer>
    </div>
  )
}