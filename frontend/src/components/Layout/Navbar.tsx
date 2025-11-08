
import { useState } from "react"
import { Button } from "@/components/UI/Button"
import { Bell, User, Settings, Menu, X, Car, LogOut } from "lucide-react"

interface CommonNavbarProps {
  userName?: string;
  onNotificationClick?: () => void;
  onProfileClick?: () => void;
  onSettingsClick?: () => void;
  onLogoutClick?: () => void;
}

export function CommonNavbar({ 
  userName = "User", 
  onNotificationClick, 
  onProfileClick, 
  onSettingsClick,
  onLogoutClick 
}: CommonNavbarProps) {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  return (
    <nav className="sticky top-0 z-50 border-b border-cyan-200/80 bg-gradient-to-r from-blue-50 via-cyan-50 to-blue-100 shadow-sm backdrop-blur-sm supports-[backdrop-filter]:bg-white/80">
      <div className="max-w-full mx-auto px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="flex items-center space-x-3">
            <div className="bg-cyan-600 rounded-lg p-2">
              <Car className="h-5 w-5 text-white" />
            </div>
            <div>
              <span className="text-xl font-bold text-gray-900">
                RevUp
              </span>
            </div>
          </div>

          {/* Desktop Actions */}
          <div className="hidden md:flex items-center space-x-2">
            <Button 
              variant="ghost" 
              size="sm" 
              className="relative hover:bg-cyan-50 rounded-md h-10 px-3"
              onClick={onNotificationClick}
            >
              <Bell className="h-4 w-4 text-gray-600" />
              <span className="absolute top-2 right-2 h-2 w-2 bg-red-500 rounded-full ring-2 ring-white"></span>
            </Button>
            
            <div className="h-8 w-px bg-gray-200 mx-2"></div>
            
            <Button 
              variant="ghost" 
              size="sm" 
              className="hover:bg-cyan-50 rounded-md h-10 px-4"
              onClick={onProfileClick}
            >
              <div className="flex items-center space-x-2">
                <div className="h-7 w-7 rounded-full bg-cyan-600 flex items-center justify-center">
                  <span className="text-white text-xs font-semibold">{userName.charAt(0).toUpperCase()}</span>
                </div>
                <span className="text-sm font-medium text-gray-700">{userName}</span>
              </div>
            </Button>
            <Button 
              variant="ghost" 
              size="sm" 
              className="hover:bg-red-50 hover:text-red-800 text-red-600 rounded-md h-10 px-3"
              onClick={onLogoutClick}
            >
              <LogOut className="h-4 w-4" />
            </Button>
          </div>

          {/* Mobile Menu Button */}
          <button
            className="md:hidden p-2 rounded-lg hover:bg-cyan-50 text-gray-700"
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          >
            {mobileMenuOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
          </button>
        </div>
      </div>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <div className="md:hidden border-t border-gray-200 bg-white shadow-lg">
          <div className="px-4 py-3 space-y-1">
            <Button variant="ghost" className="w-full justify-start hover:bg-cyan-50 text-gray-600" onClick={onNotificationClick}>
              <Bell className="h-4 w-4 mr-3" />
              Notifications
              <span className="ml-auto h-2 w-2 bg-red-500 rounded-full"></span>
            </Button>
            <Button variant="ghost" className="w-full justify-start hover:bg-cyan-50 text-gray-600" onClick={onProfileClick}>
              <User className="h-4 w-4 mr-3" />
              Profile - {userName}
            </Button>
            <Button variant="ghost" className="w-full justify-start hover:bg-cyan-50 text-gray-600" onClick={onSettingsClick}>
              <Settings className="h-4 w-4 mr-3" />
              Settings
            </Button>
            <Button variant="ghost" className="w-full justify-start hover:bg-cyan-50 text-gray-600" onClick={onProfileClick}>
              <User className="h-4 w-4 mr-3" />
              Profile - {userName}
            </Button>
            <Button variant="ghost" className="w-full justify-start hover:bg-cyan-50 text-gray-600" onClick={onSettingsClick}>
              <Settings className="h-4 w-4 mr-3" />
              Settings
            </Button>
            <Button variant="ghost" className="w-full justify-start hover:bg-red-50 hover:text-red-800 text-red-600" onClick={onLogoutClick}>
              <LogOut className="h-4 w-4 mr-3" />
              Logout
            </Button>
          </div>
        </div>
      )}
    </nav>
  )
}