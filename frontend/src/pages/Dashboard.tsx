import { useAuth } from "@/contexts/authContext/authContext"
import { logOut } from "@/firebase/auth"
import { getFirebaseErrorMessage } from "@/firebase/errorUtils"
import { Button } from "@/components/UI/Button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/UI/Card"
import { useNavigate } from "react-router-dom"
import { useState } from "react"

export function Dashboard() {
  const { currentUser, userLoggedIn, loading } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState("")

  const handleLogout = async () => {
    try {
      setError("")
      await logOut()
      console.log("Logged out successfully")
      // The auth context will handle the state change
    } catch (error) {
      console.error("Logout error:", error)
      setError(getFirebaseErrorMessage(error))
    }
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Loading...</p>
        </div>
      </div>
    )
  }

  if (!userLoggedIn) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="text-center">
          <h2 className="text-2xl font-bold mb-4">Access Denied</h2>
          <p className="text-muted-foreground mb-6">Please log in to view this page.</p>
          <Button onClick={() => navigate("/")}>
            Go to Login
          </Button>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-background p-8">
      <div className="max-w-2xl mx-auto">
        <Card className="shadow-lg">
          <CardHeader className="text-center">
            <CardTitle className="text-3xl font-bold">Welcome to RevUp Dashboard</CardTitle>
            <p className="text-muted-foreground mt-2">You are successfully logged in!</p>
          </CardHeader>

          <CardContent className="space-y-6">
            {error && (
              <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 rounded-md text-sm">
                {error}
              </div>
            )}

            <div className="bg-muted/50 rounded-lg p-6">
              <h3 className="text-xl font-semibold mb-4">Your Account Details</h3>
              <div className="space-y-3 text-sm">
                <div className="flex justify-between">
                  <span className="font-medium">Email:</span>
                  <span className="text-muted-foreground">{currentUser?.email}</span>
                </div>
                <div className="flex justify-between">
                  <span className="font-medium">User ID:</span>
                  <span className="text-muted-foreground font-mono text-xs">{currentUser?.uid}</span>
                </div>
                <div className="flex justify-between">
                  <span className="font-medium">Email Verified:</span>
                  <span className={currentUser?.emailVerified ? "text-green-600" : "text-orange-600"}>
                    {currentUser?.emailVerified ? "Yes" : "No"}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="font-medium">Provider:</span>
                  <span className="text-muted-foreground">
                    {currentUser?.providerData?.[0]?.providerId?.replace('.com', '') || 'Email'}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="font-medium">Account Created:</span>
                  <span className="text-muted-foreground">
                    {currentUser?.metadata?.creationTime ?
                      new Date(currentUser.metadata.creationTime).toLocaleDateString() :
                      'N/A'
                    }
                  </span>
                </div>
              </div>
            </div>

            <div className="flex gap-4">
              <Button
                onClick={handleLogout}
                variant="outline"
                className="flex-1"
              >
                Sign Out
              </Button>
              <Button
                onClick={() => navigate("/")}
                className="flex-1"
              >
                Back to Login
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}