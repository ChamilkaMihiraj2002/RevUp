"use client"

import type React from "react"
import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { Button } from "../UI/Button"
import { Input } from "../UI/Input"
import { Label } from "../UI/Label"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../UI/Card"
import { Eye, EyeOff, Loader2 } from "lucide-react"
import { logIn, logInWithGoogle, passwordReset } from "../../firebase/auth"
import { getFirebaseErrorMessage } from "../../firebase/errorUtils"
import { useAuth } from "../../contexts/authContext/authContext"
import { getUserByFirebaseUID } from "../../services/userService"

export function LoginForm() {
  const [showPassword, setShowPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [isGoogleLoading, setIsGoogleLoading] = useState(false)
  const [error, setError] = useState("")
  const [successMessage, setSuccessMessage] = useState("")
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  })
  const { userLoggedIn, setUserData, setAccessToken } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    setError("")
    setSuccessMessage("")

    try {
      const userCredential = await logIn(formData.email, formData.password)
      const token = await userCredential.user.getIdToken()
      
      const dbUser = await getUserByFirebaseUID(userCredential.user.uid, token)
      setUserData(dbUser)
      setAccessToken(token)
      
      console.log("Login successful!")
      
      if (dbUser.role === 'TECHNICIAN') {
        navigate('/technician-dashboard')
      } else {
        navigate('/overview')
      }
    } catch (err: any) {
      setError(getFirebaseErrorMessage(err))
      console.error("Login error:", err)
    } finally {
      setIsLoading(false)
    }
  }

  const handleGoogleSignIn = async () => {
    setIsGoogleLoading(true)
    setError("")
    setSuccessMessage("")

    try {
      const result = await logInWithGoogle()
      const token = await result.user.getIdToken()
      
      const dbUser = await getUserByFirebaseUID(result.user.uid, token)
      setUserData(dbUser)
      setAccessToken(token)
      
      console.log("Google sign-in successful!")
      
      if (dbUser.role === 'TECHNICIAN') {
        navigate('/technician-dashboard')
      } else {
        navigate('/overview')
      }
    } catch (err: any) {
      setError(getFirebaseErrorMessage(err))
      console.error("Google sign-in error:", err)
    } finally {
      setIsGoogleLoading(false)
    }
  }

  const handleForgotPassword = async () => {
    if (!formData.email) {
      setError("Please enter your email address first.")
      return
    }

    setError("")
    setSuccessMessage("")

    try {
      await passwordReset(formData.email)
      setSuccessMessage("Password reset email sent! Check your inbox.")
    } catch (err: any) {
      setError(getFirebaseErrorMessage(err))
      console.error("Password reset error:", err)
    }
  }

  if (userLoggedIn) {
    return (
      <Card className="border-0 shadow-2xl">
        <CardContent className="pt-6">
          <p className="text-center text-green-600 font-medium">You are already logged in!</p>
        </CardContent>
      </Card>
    )
  }

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
  }

  return (
    <Card className="border-0 shadow-2xl">
      <CardHeader className="space-y-2 text-center pb-8">
        <CardTitle className="text-2xl font-bold">Welcome Back</CardTitle>
        <CardDescription className="text-muted-foreground">Sign In To Your RevUp Account To Continue</CardDescription>
      </CardHeader>

      <CardContent>
        {error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 rounded-md text-sm">
            {error}
          </div>
        )}
        {successMessage && (
          <div className="mb-4 p-3 bg-green-50 border border-green-200 text-green-700 rounded-md text-sm">
            {successMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="email" className="text-sm font-medium">
              Email Address
            </Label>
            <Input
              id="email"
              type="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={(e) => handleInputChange("email", e.target.value)}
              required
              className="h-11"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="password" className="text-sm font-medium">
              Password
            </Label>
            <div className="relative">
              <Input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="Enter your password"
                value={formData.password}
                onChange={(e) => handleInputChange("password", e.target.value)}
                required
                className="h-11 pr-10"
              />
              <Button
                type="button"
                variant="ghost"
                size="sm"
                className="absolute right-0 top-0 h-11 px-3 hover:bg-transparent"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? (
                  <EyeOff className="h-4 w-4 text-muted-foreground" />
                ) : (
                  <Eye className="h-4 w-4 text-muted-foreground" />
                )}
              </Button>
            </div>
          </div>

          <div className="flex items-center justify-between text-sm">
            <label className="flex items-center gap-2 cursor-pointer">
              <input type="checkbox" className="rounded border-border" />
              <span className="text-muted-foreground">Remember me</span>
            </label>
            <Button 
              type="button"
              variant="link" 
              className="p-0 h-auto text-primary hover:text-primary/80"
              onClick={handleForgotPassword}
            >
              Forgot Password?
            </Button>
          </div>

          <Button type="submit" className="w-full h-11 text-base font-medium" disabled={isLoading}>
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Signing in...
              </>
            ) : (
              "Sign in"
            )}
          </Button>

          <div className="relative">
            <div className="absolute inset-0 flex items-center">
              <span className="w-full border-t" />
            </div>
            <div className="relative flex justify-center text-xs uppercase">
              <span className="bg-background px-2 text-muted-foreground">Or continue with</span>
            </div>
          </div>

          <Button
            type="button"
            variant="outline"
            className="w-full h-11 text-base font-medium"
            onClick={handleGoogleSignIn}
            disabled={isGoogleLoading}
          >
            {isGoogleLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Signing in with Google...
              </>
            ) : (
              <>
                <svg className="mr-2 h-4 w-4" viewBox="0 0 24 24">
                  <path
                    d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                    fill="#4285F4"
                  />
                  <path
                    d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                    fill="#34A853"
                  />
                  <path
                    d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                    fill="#FBBC05"
                  />
                  <path
                    d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                    fill="#EA4335"
                  />
                </svg>
                Sign in with Google
              </>
            )}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
