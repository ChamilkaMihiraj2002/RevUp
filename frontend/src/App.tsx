import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { LoginPage } from "@/pages/login/LoginPage"
import  Home  from "@/pages/Home/home"
import { Dashboard } from "@/pages/Dashboard"
import  Overview from "@/pages/Overview/overview"
import  Book from "@/pages/Overview/book"



import { useAuth } from "@/contexts/authContext/authContext"

function App() {
  const { userLoggedIn, loading } = useAuth()

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
        <Route path="/login" element={userLoggedIn ? <Navigate to="/dashboard" replace /> : <LoginPage />} />
        <Route
          path="/dashboard"
          element={userLoggedIn ? <Dashboard /> : <Navigate to="/login" replace />}
        />
        <Route
          path="/overview"
          element={userLoggedIn ? <Overview /> : <Navigate to="/login" replace />}
        />
        <Route
          path="/book"
          element={userLoggedIn ? <Book /> : <Navigate to="/login" replace />}
        />
      </Routes>
    </Router>
  )
}

export default App
