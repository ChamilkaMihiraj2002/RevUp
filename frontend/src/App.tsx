import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { LoginPage } from "@/pages/login/LoginPage"
import  Home  from "@/pages/Home/home"
import { Dashboard } from "@/pages/Dashboard"
import  Overview from "@/pages/Overview/overview"
import  Book from "@/pages/Overview/book"
import { useAuth } from "@/contexts/authContext/authContext"
import AdminDashboard from "@/pages/admin/AdminDashboard.tsx"
import TechnicianDashboard from "@/pages/technician/TechnicianDashboard.tsx";

function App() {
  const { userLoggedIn, loading, role } = useAuth()

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

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route 
          path="/login" 
          element={
            userLoggedIn 
              ? role === 'TECHNICIAN' 
                ? <Navigate to="/technician-dashboard" replace /> 
                : <Navigate to="/overview" replace />
              : <LoginPage />
          } 
        />
        <Route
          path="/dashboard"
          element={userLoggedIn ? <Dashboard /> : <Navigate to="/login" replace />}
        />
        <Route
          path="/overview"
          element={
            userLoggedIn 
              ? role === 'CUSTOMER' 
                ? <Overview /> 
                : <Navigate to="/technician-dashboard" replace />
              : <Navigate to="/login" replace />
          }
        />
        <Route
          path="/book"
          element={
            userLoggedIn 
              ? role === 'CUSTOMER' 
                ? <Book /> 
                : <Navigate to="/technician-dashboard" replace />
              : <Navigate to="/login" replace />
          }
        />
        <Route
          path="/admin-dashboard"
          element={userLoggedIn ? <AdminDashboard /> : <Navigate to="/" replace />}
        />
        <Route
          path="/technician-dashboard"
          element={
            userLoggedIn 
              ? role === 'TECHNICIAN' 
                ? <TechnicianDashboard /> 
                : <Navigate to="/overview" replace />
              : <Navigate to="/login" replace />
          }
        />
      </Routes>
    </Router>
  )
}

export default App
