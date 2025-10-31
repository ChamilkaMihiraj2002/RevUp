import { type ReactNode, type ReactElement } from 'react';
import { NavLink } from "react-router-dom";
import styles from "./Layout.module.css";
import revupIcon from "../../assets/revup-icon.jpg";


import {
  Bell,
  User,
  Settings,
  Grid,
  Calendar,
  Car,
  CheckSquare,
  BarChart2,
  Sliders,
} from "lucide-react";

type UserRole = 'customer' | 'technician' | 'admin';

interface MenuItem {
  name: string;
  icon: ReactElement;
  path: string;
}

interface LayoutProps {
  children: ReactNode;
  role: UserRole;
  username: string;
  subtitle?: string;
}

const Layout = ({ children, role, username, subtitle }: LayoutProps) => {
  // Sidebar menus based on role
  const menus: Record<UserRole, MenuItem[]> = {
    customer: [
      { name: "Overview", icon: <Grid size={18} />, path: "/" },
      { name: "My Vehicles", icon: <Car size={18} />, path: "/vehicles" },
      { name: "Appointments", icon: <Calendar size={18} />, path: "/appointments" },
      { name: "Service History", icon: <CheckSquare size={18} />, path: "/service-history" },
    ],
    technician: [
      { name: "My Assignments", icon: <Grid size={18} />, path: "/assignments" },
      { name: "Completed Today", icon: <CheckSquare size={18} />, path: "/completed" },
      { name: "Schedule", icon: <Calendar size={18} />, path: "/schedule" },
    ],
    admin: [
      { name: "Appointments", icon: <Calendar size={18} />, path: "/appointments" },
      { name: "Technicians", icon: <User size={18} />, path: "/technicians" },
      { name: "Analytics", icon: <BarChart2 size={18} />, path: "/analytics" },
      { name: "Settings", icon: <Sliders size={18} />, path: "/settings" },
    ],
  };

  const selectedMenu = menus[role] || [];

  return (
    <div className={styles.container}>
      {/* Sidebar */}
      <aside className={styles.sidebar}>
        <div className={styles.sidebarHeader}>
          <div className={styles.logoIcon}>
            <img
              src={revupIcon}
              alt="RevUp Logo"
              className={styles.logoImage}
            />
          </div>
            <span className={styles.brandName}>RevUp</span>
          
        </div>

        <nav className={styles.navMenu}>
          {selectedMenu.map((item) => (
            <NavLink
              key={item.name}
              to={item.path}
              className={({ isActive }) =>
                isActive ? `${styles.navItem} ${styles.active}` : styles.navItem
              }
            >
              {item.icon}
              <span>{item.name}</span>
            </NavLink>
          ))}
        </nav>
      </aside>

      {/* Main content */}
      <div className={styles.mainContent}>
        {/* Topbar */}
        <header className={styles.topbar}>
          <div className={styles.topbarLeft}>
            <div className={styles.menuToggle}>
              <Grid size={20} />
            </div>
          </div>
          <div className={styles.topbarCenter}>
             <h2 className={styles.dashboardTitle}>
            {role === "admin"
              ? "Admin Dashboard"
              : role === "technician"
              ? "Technician Dashboard"
              : "Customer Dashboard"}
          </h2>
          </div>
          <div className={styles.topbarRight}>
            <Bell className={styles.icon} />
            <div className={styles.userInfo}>
              <span className={styles.username}>{username}</span>
              {subtitle && <span className={styles.subtitle}>{subtitle}</span>}
            </div>
            <User className={styles.icon} />
            <Settings className={styles.icon} />
          </div>
        </header>

        {/* Page content */}
        <main className={styles.pageContent}>{children}</main>
      </div>
    </div>
  );
};

export default Layout;
