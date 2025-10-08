import { LoginForm} from "@/components/LoginForm/LoginForm"
import { SignUpForm } from "@/components/LoginForm/SignUpForm"
import { Car, Wrench, Shield, Clock } from "lucide-react"
import { useState } from "react"

export function LoginPage() {
  const [isSignUp, setIsSignUp] = useState(false)

  return (
    <div className="min-h-screen bg-background flex">
      {/* Left side - Branding */}
      <div className="hidden lg:flex lg:w-1/2 bg-primary relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary via-primary to-accent opacity-90" />
        <div className="relative z-10 flex flex-col justify-center px-12 text-primary-foreground">
          <div className="mb-8">
            <div className="flex items-center gap-3 mb-6">
              <div className="w-12 h-12 bg-primary-foreground rounded-xl flex items-center justify-center">
                <Car className="w-7 h-7 text-primary" />
              </div>
              <h1 className="text-4xl font-bold">RevUp</h1>
            </div>
            <p className="text-xl text-primary-foreground/90 leading-relaxed mb-12">
              Professional Automobile Care Management Platform For Modern Service Centers
            </p>
          </div>

          <div className="space-y-8">
            <div className="flex items-start gap-4">
              <div className="w-10 h-10 bg-primary-foreground/20 rounded-lg flex items-center justify-center flex-shrink-0">
                <Wrench className="w-5 h-5" />
              </div>
              <div>
                <h3 className="font-semibold text-lg mb-2">Complete Service Management</h3>
                <p className="text-primary-foreground/80 leading-relaxed">
                  Track repairs, maintenance schedules, and customer communications in one unified platform
                </p>
              </div>
            </div>

            <div className="flex items-start gap-4">
              <div className="w-10 h-10 bg-primary-foreground/20 rounded-lg flex items-center justify-center flex-shrink-0">
                <Shield className="w-5 h-5" />
              </div>
              <div>
                <h3 className="font-semibold text-lg mb-2">Secure & Reliable</h3>
                <p className="text-primary-foreground/80 leading-relaxed">
                  Enterprise-grade security with automated backups and 99.9% uptime guarantee
                </p>
              </div>
            </div>

            <div className="flex items-start gap-4">
              <div className="w-10 h-10 bg-primary-foreground/20 rounded-lg flex items-center justify-center flex-shrink-0">
                <Clock className="w-5 h-5" />
              </div>
              <div>
                <h3 className="font-semibold text-lg mb-2">Real-time Updates</h3>
                <p className="text-primary-foreground/80 leading-relaxed">
                  Instant notifications and live status updates keep your team synchronized
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Right side - Login Form */}
      <div className="flex-1 flex items-center justify-center px-6 py-12">
        <div className="w-full max-w-md">
          <div className="lg:hidden mb-8 text-center">
            <div className="flex items-center justify-center gap-3 mb-4">
              <div className="w-10 h-10 bg-primary rounded-xl flex items-center justify-center">
                <Car className="w-6 h-6 text-primary-foreground" />
              </div>
              <h1 className="text-3xl font-bold text-foreground">RevUp</h1>
            </div>
            <p className="text-muted-foreground">Automobile care management</p>
          </div>

          {/* Tab Switcher */}
          <div className="flex gap-2 mb-6 p-1 bg-muted rounded-lg">
            <button
              onClick={() => setIsSignUp(false)}
              className={`flex-1 py-2.5 px-4 rounded-md text-sm font-medium transition-all ${
                !isSignUp
                  ? "bg-background text-foreground shadow-sm"
                  : "text-muted-foreground hover:text-foreground"
              }`}
            >
              Sign In
            </button>
            <button
              onClick={() => setIsSignUp(true)}
              className={`flex-1 py-2.5 px-4 rounded-md text-sm font-medium transition-all ${
                isSignUp
                  ? "bg-background text-foreground shadow-sm"
                  : "text-muted-foreground hover:text-foreground"
              }`}
            >
              Sign Up
            </button>
          </div>

          {isSignUp ? <SignUpForm onSwitchToLogin={() => setIsSignUp(false)} /> : <LoginForm />}
        </div>
      </div>
    </div>
  )
}
