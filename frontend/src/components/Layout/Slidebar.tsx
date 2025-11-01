import { Button } from "@/components/UI/Button"

interface NavItem {
  id: string;
  label: string;
  icon: React.ComponentType<{ className?: string }>;
}

interface CommonSidebarProps {
  navItems: NavItem[];
  activeTab: string;
  onTabChange: (id: string) => void;
  dashboardName?: string;
}

export function CommonSidebar({ navItems, activeTab, onTabChange, dashboardName = "Customer" }: CommonSidebarProps) {
  return (
    <aside className="w-80 border-r border-cyan-200 bg-cyan-800/10 min-h-[calc(100vh-64px)] hidden md:block backdrop-blur-sm">
      <div className="p-6">
        {/* Dashboard Header */}
        <div className="mb-6 pb-4 border-b border-cyan-200">
          <h2 className="text-lg font-bold text-gray-900">{dashboardName} DashBoard</h2>
          <p className="text-sm text-cyan-700 mt-0.5">Manage your services</p>
        </div>

        {/* Navigation Menu */}
        <div className="space-y-1.5">
         
          {navItems.map((item) => {
            const Icon = item.icon
            const isActive = activeTab === item.id
            return (
              <button
                key={item.id}
                onClick={() => onTabChange(item.id)}
                className={`w-full flex items-center space-x-3 px-4 py-3.5 rounded-lg transition-all duration-200 group ${
                  isActive
                    ? "bg-cyan-600 text-white"
                    : "text-gray-700 hover:bg-cyan-50"
                }`}
              >
                <div className={`${isActive ? "bg-white/20" : "bg-white"} p-2 rounded-lg transition-colors`}>
                  <Icon className={`h-4 w-4 ${isActive ? "text-white" : "text-cyan-600"}`} />
                </div>
                <span className="font-medium text-sm">{item.label}</span>
              </button>
            )
          })}
        </div>

        {/* Quick Actions */}
        <div className="mt-8 p-4 bg-white rounded-lg border border-cyan-200">
          <h4 className="text-xs font-semibold text-gray-900 uppercase tracking-wider mb-2">Quick Actions</h4>
          <p className="text-xs text-cyan-600 mb-3">Need help with your services?</p>
          <Button className="w-full bg-cyan-600 hover:bg-cyan-700 text-white text-sm h-9">
            Contact Support
          </Button>
        </div>
      </div>
    </aside>
  )
}


