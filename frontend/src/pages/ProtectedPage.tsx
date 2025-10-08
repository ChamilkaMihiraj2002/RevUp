import { useAuth } from "../contexts/authContext/authContext"
import { logOut } from "../firebase/auth"
import { Button } from "../components/UI/Button"

/**
 * Example component showing how to use authentication in your app
 * This can be your home/dashboard page after login
 */
export function ProtectedPage() {
  const { currentUser, userLoggedIn, loading } = useAuth()

  const handleLogout = async () => {
    try {
      await logOut()
      console.log("Logged out successfully")
      // Optionally redirect to login page
    } catch (error) {
      console.error("Logout error:", error)
    }
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Loading...</p>
        </div>
      </div>
    )
  }

  if (!userLoggedIn) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold mb-4">Access Denied</h2>
          <p className="text-muted-foreground mb-6">Please log in to view this page.</p>
          <Button onClick={() => window.location.href = "/login"}>
            Go to Login
          </Button>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-background p-8">
      <div className="max-w-4xl mx-auto">
        <div className="bg-card rounded-lg shadow-lg p-8 mb-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h1 className="text-3xl font-bold mb-2">Welcome to RevUp Dashboard</h1>
              <p className="text-muted-foreground">
                Logged in as: <span className="font-medium text-foreground">{currentUser?.email}</span>
              </p>
            </div>
            <Button variant="outline" onClick={handleLogout}>
              Log Out
            </Button>
          </div>

          <div className="border-t pt-6">
            <h2 className="text-xl font-semibold mb-4">User Information</h2>
            <div className="space-y-2 text-sm">
              <div className="flex">
                <span className="font-medium w-32">User ID:</span>
                <span className="text-muted-foreground">{currentUser?.uid}</span>
              </div>
              <div className="flex">
                <span className="font-medium w-32">Email:</span>
                <span className="text-muted-foreground">{currentUser?.email}</span>
              </div>
              <div className="flex">
                <span className="font-medium w-32">Email Verified:</span>
                <span className={currentUser?.emailVerified ? "text-green-600" : "text-orange-600"}>
                  {currentUser?.emailVerified ? "Yes" : "No"}
                </span>
              </div>
              <div className="flex">
                <span className="font-medium w-32">Provider:</span>
                <span className="text-muted-foreground">
                  {currentUser?.providerData?.[0]?.providerId || "N/A"}
                </span>
              </div>
              <div className="flex">
                <span className="font-medium w-32">Created:</span>
                <span className="text-muted-foreground">
                  {currentUser?.metadata?.creationTime || "N/A"}
                </span>
              </div>
              <div className="flex">
                <span className="font-medium w-32">Last Sign In:</span>
                <span className="text-muted-foreground">
                  {currentUser?.metadata?.lastSignInTime || "N/A"}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-card rounded-lg shadow p-6">
            <h3 className="font-semibold mb-2">Total Services</h3>
            <p className="text-3xl font-bold text-primary">0</p>
          </div>
          <div className="bg-card rounded-lg shadow p-6">
            <h3 className="font-semibold mb-2">Active Jobs</h3>
            <p className="text-3xl font-bold text-primary">0</p>
          </div>
          <div className="bg-card rounded-lg shadow p-6">
            <h3 className="font-semibold mb-2">Pending Tasks</h3>
            <p className="text-3xl font-bold text-primary">0</p>
          </div>
        </div>
      </div>
    </div>
  )
}
